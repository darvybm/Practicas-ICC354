package com.pucmm.eict.mockupapi.repositories;

import com.pucmm.eict.mockupapi.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findById(UUID id);
}
