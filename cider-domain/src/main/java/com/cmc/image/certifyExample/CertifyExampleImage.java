package com.cmc.image.certifyExample;

import com.cmc.challenge.Challenge;
import com.cmc.image.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = {"challenge"})
@DiscriminatorValue("challenge")
@Entity
public class CertifyExampleImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    protected CertifyExampleImage(){}

    public CertifyExampleImage(Challenge challenge, String imagerUrl){
        super(imagerUrl);
        this.challenge = challenge;
        challenge.getCertifyExampleImageList().add(this);
    }
}
