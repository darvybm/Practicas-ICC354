package com.pucmm.eict.mockupapi.repositories;

import com.pucmm.eict.mockupapi.models.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MockRepository extends JpaRepository<Mock, Integer> {
    Mock findById(UUID id);

    Mock findByHash(String hash);
}
