package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.services.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/mocks")
public class MockController {

    private final MockService mockService;

    @Autowired
    public MockController(MockService mockService) {
        this.mockService = mockService;
    }

    @GetMapping
    public String getAllMocks(Model model) {
        List<Mock> mocks = mockService.getAllMocks();
        model.addAttribute("mocks", mocks);
        return "mock/list";
    }

    @GetMapping("/{id}")
    public String getMockById(@PathVariable UUID id, Model model) {
        Mock mock = mockService.getMockById(id);
        model.addAttribute("mock", mock);
        return "mock/details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("mock", new Mock());
        return "mock/create";
    }

    @PostMapping("/create")
    public String createMock(@ModelAttribute Mock mock) {
        mockService.createMock(mock);
        return "redirect:/mocks";
    }
}
