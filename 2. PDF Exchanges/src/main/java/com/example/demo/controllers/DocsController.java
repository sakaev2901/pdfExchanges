package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Generated;

@Controller
public class DocsController {

    @GetMapping("/")
    public ModelAndView getPage() {
        return new ModelAndView("docs");
    }
}
