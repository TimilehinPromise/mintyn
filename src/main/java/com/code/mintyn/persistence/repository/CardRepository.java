package com.code.mintyn.persistence.repository;

import com.code.mintyn.persistence.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card>findByBin(String bin);

}
