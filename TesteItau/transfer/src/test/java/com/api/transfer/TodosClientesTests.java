package com.api.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

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
class TodosClientesTests extends EndPointsTests {

	@Test
	@Order(1)
	public void testaBuscarTodosClientesNotFound() {
		JsonPath usuarios = getTodosCliente(HttpStatus.NOT_FOUND);
		ErrorResponse errorResponse = usuarios.getObject("", ErrorResponse.class);
		assertEquals(appMessage.getNotFound(), errorResponse.getMessage());
		assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getCode());
		assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getStatus());
	}

	@Test
	@Order(2)
	public void testaBuscarTodos() {
		postUsuario(nomeC1, new BigDecimal(1000), HttpStatus.CREATED);
		postUsuario(nomeC2, new BigDecimal(1000), HttpStatus.CREATED);

		JsonPath usuarios = getTodosCliente(HttpStatus.OK);
		List<Client> list = usuarios.getList("", Client.class);
		assertEquals(2, list.size());
		assertEquals(1, list.get(0).getId());
		assertEquals(new BigDecimal(2000.0).setScale(2),
				list.stream().map(Client::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2));
	}
}

