package com.api.transfer.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.transfer.model.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

	List<Transfer> findByContaOrigemOrderByDataDesc(Long numeroConta);

}
