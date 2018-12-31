package com.neetika.challenge.autobound.repository;

import com.neetika.challenge.autobound.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query ("select c from Company c where name=:name")
    List<Company> findAllByName (@Param("name") String name);

}
