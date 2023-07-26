package com.cmc.image.certify;

import com.cmc.certify.Certify;
import com.cmc.image.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = {"certify"})
@DiscriminatorValue("certify")
@Entity
public class CertifyImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certify_id", nullable = false)
    private Certify certify;

    protected CertifyImage(){}

    public CertifyImage(Certify certify, String imagerUrl){
        super(imagerUrl);
        this.certify = certify;
        certify.getCertifyImageList().add(this);
    }
}
