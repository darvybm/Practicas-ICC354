package com.pucmm.eict.mockupapi.controllers;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

@Controller
public class AppController {

    @GetMapping("/")
    public String home() {
        return "redirect:/projects";
    }

    @GetMapping("")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500";
            }
//            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
//                return "error-403";
//            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403";
            }
        }
        System.out.println("hay un error que no he manejado");
        return "error";
    }

}
