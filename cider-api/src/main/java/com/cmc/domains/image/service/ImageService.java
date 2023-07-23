package com.cmc.domains.image.service;

import com.cmc.challenge.Challenge;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.image.certifyExample.repository.CertifyExampleImageRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.global.s3.S3Uploader;
import com.cmc.image.certifyExample.CertifyExampleImage;
import com.cmc.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3Uploader s3Uploader;
    private final ChallengeRepository challengeRepository;
    private final CertifyExampleImageRepository certifyExampleImageRepository;


    // 챌린지 인증 성공 예시 사진 업로드
    public List<CertifyExampleImage> uploadCertifyExampleImages(List<MultipartFile> certifyExampleImages, Long challengeId, Long memberId, String exampleType) throws IOException {

        if(certifyExampleImages.size() >= 3){
            throw new BadRequestException("인증 예시 성공/실패 이미지는 2장까지 등록 가능합니다.");
        }

        Challenge challenge = findChallengeOrThrow(challengeId);    // TODO : 본인이 작성한 챌린지 예외처리 다시
//        if(!challenge.isCreator(memberId)){
//            throw new BadRequestException("본인이 작성한 챌린지가 아닙니다.");
//        }

        List<String> imageUrlList = s3Uploader.s3UploadOfCertifyExampleImages(challenge, certifyExampleImages);

        List<CertifyExampleImage> certifyExampleImageList = new ArrayList<>();
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                CertifyExampleImage exampleImage = certifyExampleImageRepository.save(new CertifyExampleImage(challenge, imageUrl, exampleType));
                certifyExampleImageList.add(exampleImage);
            }
        }

        return certifyExampleImageList;
    }


    private Challenge findChallengeOrThrow(Long challengeId){
        return challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지는 존재하지 않습니다.");
        });
    }

}