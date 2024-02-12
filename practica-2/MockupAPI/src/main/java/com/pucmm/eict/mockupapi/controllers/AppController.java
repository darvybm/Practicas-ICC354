package com.pucmm.eict.mockupapi.controllers;

import ch.qos.logback.core.model.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucmm.eict.mockupapi.enums.UserRole;
import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.models.Project;
import com.pucmm.eict.mockupapi.models.User;
import com.pucmm.eict.mockupapi.services.MockService;
import com.pucmm.eict.mockupapi.services.ProjectService;
import com.pucmm.eict.mockupapi.services.UserService;
import com.pucmm.eict.mockupapi.utils.HashGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AppController {

    private final MockService mockService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public AppController(MockService mockService, ProjectService projectService, UserService userService) {
        this.mockService = mockService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) throws JsonProcessingException {
        // Crear un nuevo proyecto
        Project project = new Project();
        project.setName("School");
        project.setDescription("Students School API Administration");
        projectService.createProject(project);

        User user = new User();
        user.setUsername("darvybm");
        user.setName("Darvy Betances");
        user.setRole(UserRole.ADMINISTRADOR);
        user.setPassword("sifbisf.ffd");
        userService.createUser(user);

        User user2 = new User();
        user2.setUsername("anthonyb");
        user2.setName("Anthony Beato");
        user2.setRole(UserRole.ADMINISTRADOR);
        user2.setPassword("123456");
        userService.createUser(user2);


        ObjectMapper objectMapper = new ObjectMapper();

        // Create a Map to represent headers
        Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("Authorization", "Bearer yourAccessToken");
        headersMap.put("Header1", "Value1");
        headersMap.put("Header2", "Value2");
        headersMap.put("user", user);

        // Convert Map to JSON string
        String headersJson = objectMapper.writeValueAsString(headersMap);


        // Crear un nuevo mock y asociarlo al proyecto
        Mock mock = new Mock();
        mock.setName("Obtener estudiantes");
        mock.setDescription("Mock para obtener la lista de estudiantes de la escuela");
        mock.setEndpoint("/students"); // Ejemplo de endpoint
        mock.setMethod(HttpMethod.GET.name());
        mock.setStatusCode(200);
        mock.setContentType("application/json");
        mock.setBody("[{\"name\":\"John\",\"age\":20},{\"name\":\"Jane\",\"age\":22}]");
        mock.setHeaders(headersJson); // Almacenar el JSON de los headers como un String
        mock.setHash(HashGenerator.generarHash()); // Ejemplo de generación de hash
        mock.setExpirationDate(LocalDateTime.now().plusMonths(1)); // Ejemplo de fecha de expiración en 1 mes
        mock.setDelay(0);
        mock.setValidateJWT(true);

        // Asignar el mock al usuario y al proyecto
        mock.setUser(user); // Asegúrate de tener el objeto User creado y disponible
        mock.setProject(project);

        System.out.println(mock.getHash());

        // Guardar el mock en la base de datos usando el servicio correspondiente
        mockService.createMock(mock);

        return "index";
    }


}
