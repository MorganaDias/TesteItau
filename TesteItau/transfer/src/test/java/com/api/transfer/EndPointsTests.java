package com.api.transfer;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.api.transfer.config.ErrorResponse;
import com.api.transfer.model.Client;
import com.api.transfer.util.AppMessage;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public abstract class EndPointsTests extends AppTests {

	@Autowired
	protected AppMessage appMessage;

	
	private int port;

	public String getURL(String recurso) {
		return "http://localhost:" + port + "/v1" + recurso;
	}

	protected JsonPath postTranasferencia(Long conta, BigDecimal valor, Long contaDestino, HttpStatus statusCode) {
		return given().pathParam("numeroConta", conta).queryParam("numeroContaDestino", contaDestino)
				.queryParam("valor", valor).contentType(ContentType.JSON).expect().statusCode(statusCode.value()).when()
				.post(getURL("/clientes/{numeroConta}/transferencia")).andReturn().jsonPath();
	}

	protected JsonPath getTransferencia(Long numeroConta, HttpStatus statusCode) {
		return given().pathParam("numeroConta", numeroConta).contentType(ContentType.JSON).expect()
				.statusCode(statusCode.value()).when().get(getURL("/clientes/{numeroConta}/transferencia")).andReturn()
				.jsonPath();
	}

	protected JsonPath getClientePelaConta(Long conta, HttpStatus statusCode) {
		return given().pathParam("numeroConta", conta).contentType(ContentType.JSON).expect()
				.statusCode(statusCode.value()).when().get(getURL("/clientes/{numeroConta}")).andReturn().jsonPath();
	}

	protected JsonPath getTodosCliente(HttpStatus statusCode) {
		return given().contentType(ContentType.JSON).expect().statusCode(statusCode.value()).when()
				.get(getURL("/clientes")).andReturn().jsonPath();
	}

	protected JsonPath postUsuario(String nome, BigDecimal saldo, HttpStatus statusCode) {
		Client cliente = new Client();
		cliente.setNome(nome);
		cliente.setSaldo(saldo);

		return given().body(cliente).header("Accept", "application/json").contentType(ContentType.JSON).expect()
				.statusCode(statusCode.value()).when().post(getURL("/clientes")).andReturn().jsonPath();

	}

	protected long verficaCampoComErro(ErrorResponse errorResponse, final String mensagem, final String field) {
		return errorResponse.getError().stream()
				.filter(f -> f.getMessage().contains(mensagem) && f.getField().contains(field)).count();
	}
}
