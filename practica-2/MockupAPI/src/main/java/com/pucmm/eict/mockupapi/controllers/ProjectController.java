package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.models.Project;
import com.pucmm.eict.mockupapi.models.User;
import com.pucmm.eict.mockupapi.payload.request.ProjectRequest;
import com.pucmm.eict.mockupapi.services.MockService;
import com.pucmm.eict.mockupapi.services.ProjectService;
import com.pucmm.eict.mockupapi.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final MockService mockService;

    @GetMapping
    public String getAllProjects(Model model) {
        User user = userService.getAuthenticatedUser();
        System.out.println(user);
        List<Project> projects = projectService.getAllProjectsByUser(userService.getAuthenticatedUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("colors", Arrays.asList("#0C4E3A", "#12946D", "#10BE89", "#10BE89", "#12946D", "#0C4E3A"));

        return "project/list";
    }

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable UUID id, Model model) {
        Project project = projectService.getProjectById(id);
        List<Mock> mocks = mockService.getAllMocksByProjectId(project.getId());
        model.addAttribute("project", project);
        model.addAttribute("mocks", mocks);
        return "project/details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        return "project/create";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        try {
            Project project = convertToProject(projectRequest);
            projectService.createProject(project);
            return ResponseEntity.ok("Proyecto creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el proyecto");
        }
    }

    private Project convertToProject(ProjectRequest projectRequest) {
        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());

        project.setUser(userService.getAuthenticatedUser());

        return project;
    }
}
