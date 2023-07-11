package com.cmc.member.nickname;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zz_name_adjective")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameAdjective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjective_id", nullable = false, unique = true)
    private Integer adjectiveId;

    private String adjectiveContent;

}
