package com.pucmm.eict.mockupapi.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case 400:
                    return "error/400";
                case 401:
                    return "error/401";
                case 403:
                    return "error/403";
                case 404:
                    return "error/404";
                case 500:
                    return "error/500";
                default:
                    return "error/error";
            }
        }
        return "error/error";
    }
}
