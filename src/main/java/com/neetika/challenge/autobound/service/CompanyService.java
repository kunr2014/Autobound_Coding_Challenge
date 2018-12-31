package com.neetika.challenge.autobound.service;

import com.neetika.challenge.autobound.entity.Company;
import com.neetika.challenge.autobound.exception.CompanyAlreadyExistsException;
import com.neetika.challenge.autobound.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllConfiguredCompanies() {
        return companyRepository.findAll();
    }

    public void save(String name) {
        Long existingCount = companyRepository.findAllByName(name).stream().count();
        if (existingCount == 0) {
            companyRepository.save(Company.builder().name(name).build());
        } else {
            throw new CompanyAlreadyExistsException(name);
        }

    }

    public void deleteByName(String name) {
        companyRepository.findAllByName(name).stream().forEach(company -> {
            companyRepository.deleteById(company.getId());
        });
    }
}
