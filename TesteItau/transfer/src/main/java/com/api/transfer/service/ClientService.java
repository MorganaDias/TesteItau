package com.api.transfer.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.transfer.config.ClientException;
import com.api.transfer.config.ClientNotFoundException;
import com.api.transfer.dto.TransferDTO;
import com.api.transfer.model.Client;
import com.api.transfer.model.Transfer;
import com.api.transfer.repo.ClientRepository;
import com.api.transfer.util.AppMessage;

@Service
public class ClientService {

	private static final Long FATOR_CONTA = 1000L;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private TransferService transferService;

	@Autowired
	private AppMessage appMessage;

	public List<Client> findAll() {
		List<Client> list = clientRepository.findAll();
		if (list.isEmpty()) {
			throw new ClientNotFoundException(appMessage.getNenhumRegistroLocalizado());
		}
		return list;
	}

	public Client findByConta(Long numeroConta) {
		return clientRepository.findByNumeroConta(numeroConta).map(s -> s)
				.orElseThrow(() -> new ClientNotFoundException(appMessage.getNenhumRegistroLocalizado()));
	}

	public Client findByContaParaTransferencia(Long numeroConta) {
		return clientRepository.findByNumeroContaOrderByIdDesc(numeroConta).map(s -> s)
				.orElseThrow(() -> new ClientNotFoundException(appMessage.getNenhumClientePelaConta(), numeroConta,
						"numeroContaDestino"));
	}

	@Transactional
	public Client save(Client client) {
		client.setNumeroConta(
				clientRepository.findTop1ByOrderByIdDesc().map(s -> s.getId() + FATOR_CONTA).orElse(FATOR_CONTA));
		return clientRepository.save(client);
	}

	@Transactional
	public Transfer transfere(BigDecimal valor, Long numeroContaOrigem, Long numeroContaDestino) {
		TransferDTO dto = geraTransferencia(valor, numeroContaOrigem, numeroContaDestino);
		try {
			dto.validaConta(appMessage);
			dto.transfer();
			clientRepository.save(dto.getContaOrigem());
			clientRepository.save(dto.getContaDestino());
			return transferService.registraTransferencia(dto, Boolean.TRUE, appMessage.getTransferenciaSucesso());
		} catch (ClientException e) {
			transferService.registraTransferencia(dto, Boolean.FALSE, e.getMessage());
			throw e;
		}
	}

	@Transactional
	private TransferDTO geraTransferencia(BigDecimal valor, Long numeroContaOrigem, Long numeroContaDestino) {
		Client contaOrigem = findByContaParaTransferencia(numeroContaOrigem);
		Client contaDestino = findByContaParaTransferencia(numeroContaDestino);
		return new TransferDTO(valor, contaOrigem, contaDestino);

	}

}
