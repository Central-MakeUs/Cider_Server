package com.cmc.certify;

import com.cmc.participate.Participate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "certify")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certify_id", columnDefinition = "BIGINT", nullable = false, unique = true)
    private Long certifyId;

    @ManyToOne
    @JoinColumn(name = "participate_id")
    private Participate participate;

    private String certifyName;

    private String certifyContent;

    public static Certify create(Participate participate, String certifyName, String certifyContent){

        final Certify certify = new CertifyBuilder()
                .participate(participate)
                .certifyName(certifyName)
                .certifyContent(certifyContent)
                .build();
        participate.getCertifies().add(certify);
        return certify;
    }

}
