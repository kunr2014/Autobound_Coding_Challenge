package com.neetika.challenge.autobound.repository;

import com.neetika.challenge.autobound.entity.SpreadSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SpreadSheetRepository extends JpaRepository<SpreadSheet, String> {

}
