package com.pucmm.eict.jmssensordata.controller;

import com.pucmm.eict.jmssensordata.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    SensorDataService sensorDataService;

    @Autowired
    public AppController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("sensorDataList", sensorDataService.getAll());
        return "index";
    }

}
