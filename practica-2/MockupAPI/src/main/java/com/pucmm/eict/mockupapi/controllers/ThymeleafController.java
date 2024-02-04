package com.pucmm.eict.mockupapi.controllers;

import com.pucmm.eict.mockupapi.models.Project;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller()
@RequestMapping(path = "/")
public class ThymeleafController {

//    private final EstudianteServices estudianteServices;
//
//    @Autowired
//    public ThymeleafController(EstudianteServices estudianteServices) {
//        this.estudianteServices = estudianteServices;
//    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("titulo", "Pagina desde thymeleaf");

        return "auth/login";
    }

//    @GetMapping("/login")
//    public String loginPage(Model model){
//
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String registerPage(Model model){
//
//        return "register";
//    }


}
