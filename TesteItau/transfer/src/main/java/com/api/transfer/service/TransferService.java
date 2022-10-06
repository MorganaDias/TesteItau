package com.api.transfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.api.transfer.config.ClientNotFoundException;
import com.api.transfer.dto.TransferDTO;
import com.api.transfer.model.OperationType;
import com.api.transfer.model.Transfer;
import com.api.transfer.repo.TransferRepository;
import com.api.transfer.util.AppMessage;

@Service
public class TransferService {

	@Autowired
	TransferRepository transferRepository;
 
	@Autowired
	AppMessage appMessage;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Transfer registraTransferencia(TransferDTO dto, Boolean sucesso, String observacao) {
		Transfer transferenciaOrigem = OperationType.DEBIT.buildTransferencia(dto.numeroContaOrigem(),
				dto.numeroContaDestino(), dto.getValor(), sucesso, observacao);

		Transfer transferenciaDestino = OperationType.CREDIT.buildTransferencia(dto.numeroContaDestino(),
				dto.numeroContaOrigem(), dto.getValor(), sucesso, observacao);

		transferenciaOrigem = transferRepository.save(transferenciaOrigem);
		transferenciaDestino = transferRepository.save(transferenciaDestino);
		return transferenciaOrigem;
	}

	public List<Transfer> extrato(Long numeroConta) {
		List<Transfer> list = transferRepository.findByContaOrigemOrderByDataDesc(numeroConta);
		if (list.isEmpty()) {
			throw new ClientNotFoundException(appMessage.getNenhumRegistroLocalizado());
		}
		return list;
	}
}
