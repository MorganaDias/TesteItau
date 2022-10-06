package com.api.transfer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.api.transfer.config.ErrorResponse;
import com.api.transfer.model.OperationType;
import com.api.transfer.model.Transfer;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ParallelTransfer extends EndPointsTests {

	@Test
	@Order(1)
	public void testaTransferenciaEntreContasParalelo() {
		postUsuario(nomeC1, new BigDecimal(1000), HttpStatus.CREATED);
		postUsuario(nomeC2, new BigDecimal(1000), HttpStatus.CREATED);

		IntStream.range(0, 10).parallel().forEach(nbr -> {
			JsonPath resposta = postTransferencia(numC1, new BigDecimal(100), numC2);
			String s = resposta.getString("");
			if (s.contains("operationType")) {
				Transfer t = resposta.getObject("", Transfer.class);
				assertEquals(OperationType.DEBIT, t.getOperacaoType());
				assertEquals("Débito", t.getOperacaoType().toString());
				assertEquals(numC1, t.getContaOrigem());
				assertEquals(numC2, t.getContaDestino());
			
			} else {
				ErrorResponse errorResponse = resposta.getObject("", ErrorResponse.class);
				assertEquals(appMessage.getConflict(), errorResponse.getMessage());
				assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getCode());
				assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), errorResponse.getStatus());
				assertEquals(1l, verficaCampoComErro(errorResponse, appMessage.getRecursoAtualizado(), ""));
			}
		});
	}

	protected JsonPath postTransferencia(Long conta, BigDecimal valor, Long contaDestino) {
		return given().pathParam("numeroConta", conta).queryParam("numeroContaDestino", contaDestino)
				.queryParam("valor", valor).contentType(ContentType.JSON).when()
				.post(getURL("/clientes/{numeroConta}/transferencia")).andReturn().jsonPath();
	}

}