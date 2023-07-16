package com.cmc.domains.member.repository.nickname;

import com.cmc.member.nickname.NameAnimal;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NameAnimalRepository extends JpaRepository<NameAnimal, Integer> {

    @Query("select n from NameAnimal n where n.animalId =:animalId")
    Optional<NameAnimal> findById(@Param(value = "animalId") Integer animalId);
}
