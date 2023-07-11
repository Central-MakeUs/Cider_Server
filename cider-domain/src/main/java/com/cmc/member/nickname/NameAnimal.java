package com.cmc.member.nickname;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zz_name_animal")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id", nullable = false, unique = true)
    private Integer animalId;

    private String animalContent;

}
