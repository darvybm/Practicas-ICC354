package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Project;
import com.pucmm.eict.mockupapi.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String getAllProjects(Model model) {
        Project project1 = new Project(UUID.randomUUID(), "Proyecto 1", "Prueba ");
        Project project2 = new Project(UUID.randomUUID(), "Proyecto 2", "Prueba ");
        Project project3 = new Project(UUID.randomUUID(), "Proyecto 3", "Prueba ");
        projectService.createProject(project1);
        projectService.createProject(project2);
        projectService.createProject(project3);

        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);
        model.addAttribute("colors", Arrays.asList("#0C4E3A", "#12946D", "#10BE89", "#10BE89", "#12946D", "#0C4E3A"));

        return "project/list"; // Puedes ajustar la vista según tu estructura
    }

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable UUID id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        return "project/create";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project) {
        projectService.createProject(project);
        return "redirect:/projects";
    }
}
