package com.api.transfer.repo;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.api.transfer.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	@Lock(LockModeType.OPTIMISTIC)
	Optional<Client> findByNumeroContaOrderByIdDesc(Long numeroConta);

	Optional<Client> findByNumeroConta(Long numeroConta);

	Optional<Client> findTop1ByOrderByIdDesc();

}
