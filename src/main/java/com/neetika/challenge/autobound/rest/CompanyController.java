package com.neetika.challenge.autobound.rest;

import com.neetika.challenge.autobound.entity.Company;
import com.neetika.challenge.autobound.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/companies")
    public List<Company> all() {
        return companyService.getAllConfiguredCompanies();
    }

    @PostMapping("/companies/{name}")
    public List<Company> add(@PathVariable String name) {
         companyService.save(name);
         return all();
    }

    @DeleteMapping("/companies/{name}")
    void delete(@PathVariable String name) {
        companyService.deleteByName(name);
    }

}
