package com.pucmm.eict.mockupapi.services;

import com.pucmm.eict.mockupapi.models.Project;
import com.pucmm.eict.mockupapi.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getAllProjectsByUser(UUID id) {
        return projectRepository.findAllByUserId(id);
    }

    public Project getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
}
