package com.neetika.challenge.autobound.configuration;

import com.neetika.challenge.autobound.exception.CompanyAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CompanyAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(CompanyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.OK)
    String companyAlreadyExistsHandler(CompanyAlreadyExistsException e) {
        return e.getMessage();
    }
}
