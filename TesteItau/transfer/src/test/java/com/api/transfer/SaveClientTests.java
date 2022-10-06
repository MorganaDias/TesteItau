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
class SalveClientTests extends EndPointsTests {

	@Test
	@Order(1)
	public void testaCadastrarNovoCliente() {
		JsonPath postCarlos = postUsuario("Carlos Henrique Garcia", new BigDecimal(1000), HttpStatus.CREATED);
		JsonPath postJoaquim = postUsuario("Joaquim Silva", new BigDecimal(1000), HttpStatus.CREATED);

		Client respostaCarlos = postCarlos.getObject("", Client.class);
		assertEquals(1, respostaCarlos.getId());
		assertEquals(1000L, respostaCarlos.getNumeroConta());

		Client respostaJoaquim = postJoaquim.getObject("", Client.class);
		assertEquals(2, respostaJoaquim.getId());
		assertEquals(1001L, respostaJoaquim.getNumeroConta());
	}

	@Test
	@Order(2)
	public void testaErroAoCadastrarNovoCliente() {
		JsonPath user1 = postUsuario(null, new BigDecimal(1000), HttpStatus.BAD_REQUEST);
		JsonPath user2 = postUsuario("Joaquim Silva", null, HttpStatus.BAD_REQUEST);
		ErrorResponse errorResponse1 = user1.getObject("", ErrorResponse.class);
		ErrorResponse errorResponse2 = user2.getObject("", ErrorResponse.class);
		assertEquals(1l, verficaCampoComErro(errorResponse1, "não pode estar em branco", "nome"));
		assertEquals(1l, verficaCampoComErro(errorResponse2, "não pode ser nulo", "saldo"));
	}
}
