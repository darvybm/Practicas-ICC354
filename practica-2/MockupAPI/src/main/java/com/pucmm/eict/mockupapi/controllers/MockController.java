package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Mock;
import com.pucmm.eict.mockupapi.models.Project;
import com.pucmm.eict.mockupapi.models.User;
import com.pucmm.eict.mockupapi.payload.request.MockRequest;
import com.pucmm.eict.mockupapi.services.MockService;
import com.pucmm.eict.mockupapi.services.ProjectService;
import com.pucmm.eict.mockupapi.services.UserService;
import com.pucmm.eict.mockupapi.utils.HashGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping("/mocks")
public class MockController {

    private final MockService mockService;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public MockController(MockService mockService, ProjectService projectService, UserService userService) {
        this.mockService = mockService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllMocks(Model model) {
        List<Mock> mocks = mockService.getAllMocksByUserId(userService.getAuthenticatedUser().getId());
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
        model.addAttribute("title", messageSource.getMessage("title", null, locale));
        model.addAttribute("projects", projectService.getAllProjects());
        return "mock/create";
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMock(@Valid @RequestBody MockRequest mockRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        try {
            Mock mock = convertToMock(mockRequest);
            if (mock.getProject() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo encontrar el proyecto correspondiente");
            }
            mockService.createMock(mock);
            System.out.println("MOCK CREADO EXITOSAMENTE " + mock);
            System.out.println(ResponseEntity.ok("Mock creado exitosamente"));
            return ResponseEntity.status(HttpStatus.OK).body("Mock creado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el mock");
        }
    }

    public Mock convertToMock(MockRequest mockRequest) {
        Mock mock = new Mock();
        mock.setName(mockRequest.getName());
        mock.setHash(HashGenerator.generarHash());
        mock.setDescription(mockRequest.getDescription());
        mock.setEndpoint(mockRequest.getEndpoint());
        mock.setMethod(mockRequest.getMethod().toUpperCase());
        mock.setHeaders(mockRequest.getHeaders());
        mock.setStatusCode(mockRequest.getStatusCode());
        mock.setContentType(mockRequest.getContentType());
        mock.setBody(mockRequest.getBody());
        mock.setHash(HashGenerator.generarHash());

        //Calculando la fecha de expiración
        Map<String, ChronoUnit> unitMap = new HashMap<>();
        unitMap.put("year", ChronoUnit.YEARS);
        unitMap.put("month", ChronoUnit.MONTHS);
        unitMap.put("week", ChronoUnit.WEEKS);
        unitMap.put("day", ChronoUnit.DAYS);
        unitMap.put("hour", ChronoUnit.HOURS);

        LocalDateTime expirationDate = LocalDateTime.now().plus(1, unitMap.get(mockRequest.getExpirationDate()));
        mock.setExpirationDate(expirationDate);

        mock.setDelay(mockRequest.getDelay());
        mock.setValidateJWT(mockRequest.isValidateJWT());

        mock.setUser(userService.getAuthenticatedUser());

        mock.setProject(projectService.getProjectById(UUID.fromString(mockRequest.getProjectId())));

        return mock;
    }

}
