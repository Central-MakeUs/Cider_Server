package com.cmc.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Image {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false, length = 300)
    private String imageUrl;

    public Image(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
