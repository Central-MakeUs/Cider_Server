package com.cmc.member.nickname;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zz_name_noun")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameNoun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noun_id", nullable = false, unique = true)
    private Integer nounId;

    private String nounContent;

}
