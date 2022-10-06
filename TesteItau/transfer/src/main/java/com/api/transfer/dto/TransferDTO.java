package com.api.transfer.dto;

import java.math.BigDecimal;

import com.api.transfer.config.ClientException;
import com.api.transfer.model.Client;
import com.api.transfer.util.AppMessage;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class TransferDTO {
	private static final BigDecimal LIMITE = new BigDecimal(1000);
	private final BigDecimal valor;
	private final Client contaOrigem;
	private final Client contaDestino;

	public Long numeroContaOrigem() {
		return contaOrigem.getNumeroConta();
	}

	public Long numeroContaDestino() {
		return contaDestino.getNumeroConta();
	}

	public void transfer() {
		contaOrigem.transferePara(valor, contaDestino);
	}

	public void validaConta(AppMessage appMessage) {
		if (contaOrigem.equals(contaDestino)) {
			throw new ClientException(appMessage.getContaIgual(), contaOrigem.getNumeroConta(), "Numero da Conta");
		}

		if (valor == null || BigDecimal.ZERO.compareTo(valor) >= 0) {
			throw new ClientException(appMessage.getValorInvalido(), valor, "valor");

		}
		if (LIMITE.compareTo(valor) < 0) {
			throw new ClientException(appMessage.getValorMaximoTransferencia(), valor, "valor");
		}

		if (valor.compareTo(contaOrigem.getSaldo()) > 0) {
			throw new ClientException(appMessage.getSaldoInsuficiente(), valor, "valor");
		}

	}

}
