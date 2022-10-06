package com.api.transfer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OperationType {
	CREDIT("Crédito") {
		@Override
		public Transfer buildTransferencia(Long origem, Long destino, BigDecimal valor, Boolean sucesso,String observacao) {
			return  Transfer.builder()
					.contaOrigem(origem)
					.contaDestino(destino)
					.data(LocalDateTime.now())
					.valor(valor)
					.sucesso(sucesso)
					.observacao(observacao)
					.operacaoType(this).build();
			
		}
	},
	DEBIT("Débito") {
		@Override
		public Transfer buildTransferencia(Long origem, Long destino, BigDecimal valor, Boolean sucesso,String observacao) {
			return  Transfer.builder()
					.contaOrigem(origem)
					.contaDestino(destino)
					.data(LocalDateTime.now())
					.valor(valor)
					.sucesso(sucesso)
					.observacao(observacao)
					.operacaoType(this).build();
			
		}
	};

	private String displayName;

	OperationType(String displayName) {
		this.displayName = displayName;
	}

	public abstract Transfer buildTransferencia(Long origem, Long destino, BigDecimal valor, Boolean sucesso,String observacao);

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}