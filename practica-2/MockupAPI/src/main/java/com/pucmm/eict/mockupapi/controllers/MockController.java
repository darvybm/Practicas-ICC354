package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.services.MockService;
import com.pucmm.eict.mockupapi.services.ProjectService;
import com.pucmm.eict.mockupapi.utils.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequestMapping("/mocks")
public class MockController {

    private final MockService mockService;
    private final ProjectService projectService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public MockController(MockService mockService, ProjectService projectService) {
        this.mockService = mockService;
        this.projectService = projectService;
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
    public String showCreateForm(Model model, Locale locale) {
        model.addAttribute("mock", new Mock());
        model.addAttribute("title", messageSource.getMessage("title", null, locale));
        return "mock/create";
    }

    @PostMapping("/create")
    public String createMock(@ModelAttribute Mock mock, @RequestParam("projectId") UUID projectId) {
        mock.setHash(HashGenerator.generarHash());
        mock.setProject(projectService.getProjectById(projectId));
        mockService.createMock(mock);
        return "redirect:/mocks";
    }

}
