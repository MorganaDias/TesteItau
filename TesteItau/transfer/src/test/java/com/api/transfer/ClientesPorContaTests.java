package com.api.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.api.transfer.config.ErrorResponse;
import com.api.transfer.model.Client;
import io.restassured.path.json.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
@DirtiesContext
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ClientePorContaTests extends EndPointsTests {
	
	@Test
	@Order(1)
	public void testaBuscarClienteNaoCadastrado() {
		JsonPath resposta = getClientePelaConta(1000L, HttpStatus.NOT_FOUND);
		ErrorResponse errorResponse = resposta.getObject("", ErrorResponse.class);
		assertEquals(appMessage.getNotFound(), errorResponse.getMessage());
		assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
		assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getStatus());
	}

	@Test
	@Order(2)
	public void testaBuscarClienteCadastrado() {
		postUsuario(nomeC1, new BigDecimal(1000), HttpStatus.CREATED);
		JsonPath resposta = getClientePelaConta(numC1, HttpStatus.OK);
		Client cliente = resposta.getObject("", Client.class);
		assertEquals(1, cliente.getId());
		assertEquals(nomeC1, cliente.getNome());
		assertEquals(1000, cliente.getNumeroConta());
	}

}

