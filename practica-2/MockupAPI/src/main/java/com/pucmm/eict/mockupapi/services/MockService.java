package com.pucmm.eict.mockupapi.services;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.repositories.MockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MockService {

    private final MockRepository mockRepository;

    @Autowired
    public MockService(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public List<Mock> getAllMocks() {
        return mockRepository.findAll();
    }

    public Mock getMockById(UUID id) {
        return mockRepository.findById(id);
    }

    public Mock createMock(Mock mock) {
        return mockRepository.save(mock);
    }
}