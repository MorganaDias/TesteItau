package com.api.transfer;

import static org.mockito.ArgumentMatchers.anyLong;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.api.transfer.dto.TransferDTO;
import com.api.transfer.model.Client;
import com.api.transfer.model.OperationType;
import com.api.transfer.model.Transfer;
import com.api.transfer.repo.ClientRepository;
import com.api.transfer.service.ClientService;
import com.api.transfer.service.TransferService;
import com.api.transfer.util.AppMessage;

public abstract class MockTests extends AppTests {

	@Autowired
	protected AppMessage appMessage;

	@InjectMocks
	protected ClientService clienteService;

	@Mock
	protected AppMessage appMessageMock;

	@Mock
	protected ClientRepository clientRepository;

	@Mock
	private TransferService transferService;

	@BeforeAll
	public void init() throws Exception {
		BDDMockito.when(appMessageMock.getNenhumRegistroLocalizado())
				.thenReturn(appMessage.getNenhumRegistroLocalizado());
		BDDMockito.when(appMessageMock.getNenhumClientePelaConta()).thenReturn(appMessage.getNenhumClientePelaConta());
		BDDMockito.when(appMessageMock.getSaldoInsuficiente()).thenReturn(appMessage.getSaldoInsuficiente());
		BDDMockito.when(appMessageMock.getContaIgual()).thenReturn(appMessage.getContaIgual());
		BDDMockito.when(appMessageMock.getTransferenciaSucesso()).thenReturn(appMessage.getTransferenciaSucesso());
		BDDMockito.when(appMessageMock.getValorMaximoTransferencia())
				.thenReturn(appMessage.getValorMaximoTransferencia());
		BDDMockito.when(appMessageMock.getValorInvalido()).thenReturn(appMessage.getValorInvalido());
	}

	protected Client getClient(Long id, String nome, Long numeroConta) {
		Client c = new Client();
		c.setId(id);
		c.setSaldo(new BigDecimal(800l));
		c.setNumeroConta(numeroConta);
		c.setNome(nome);
		return c;
	}

	protected void configFindAllMock(List<Client> clients) {
		BDDMockito.when(clientRepository.findAll()).thenReturn(clients);
	}

	protected void configFindAllMock() {
		configFindAllMock(Arrays.asList(getClient(1l, nomeC1, 1000l), getClient(2l, nomeC2, 1001l)));
	}

	protected void configFindMock(Optional<Client> client, Long conta) {
		BDDMockito.when(clientRepository.findByNumeroConta(BDDMockito.eq(conta))).thenReturn(client);
		BDDMockito.when(clientRepository.findByNumeroContaOrderByIdDesc(BDDMockito.eq(conta))).thenReturn(client);
	}

	@SuppressWarnings("unchecked")
	protected void configTrasferenciaMock(Client clienteOrigem, Client clienteDestino, BigDecimal valor,
			Boolean sucesso) {

		Transfer t = Transfer.builder().contaDestino(clienteDestino.getNumeroConta())
				.contaOrigem(clienteOrigem.getNumeroConta()).valor(valor).data(LocalDateTime.now()).sucesso(sucesso)
				.operacaoType(OperationType.DEBIT).build();

		BDDMockito.when(clientRepository.findByNumeroContaOrderByIdDesc(anyLong()))
				.thenReturn(Optional.of(clienteOrigem), Optional.of(clienteDestino));
		final TransferDTO dto = new TransferDTO(valor, clienteOrigem, clienteDestino);

		BDDMockito.when(transferService.registraTransferencia(dto, sucesso, appMessage.getTransferenciaSucesso()))
				.thenReturn(t);

	}

}

