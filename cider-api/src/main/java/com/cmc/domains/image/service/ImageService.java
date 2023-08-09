package com.cmc.domains.image.service;

import com.cmc.certify.Certify;
import com.cmc.challenge.Challenge;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.NoSuchIdException;
import com.cmc.domains.certify.repository.CertifyRepository;
import com.cmc.domains.challenge.repository.ChallengeRepository;
import com.cmc.domains.image.certify.repository.CertifyImageRepository;
import com.cmc.domains.image.certifyExample.repository.CertifyExampleImageRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.global.s3.S3Uploader;
import com.cmc.image.certify.CertifyImage;
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
    private final CertifyRepository certifyRepository;
    private final CertifyExampleImageRepository certifyExampleImageRepository;
    private final CertifyImageRepository certifyImageRepository;
    private final MemberRepository memberRepository;


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

    // 챌린지 인증 사진 업로드
    public List<CertifyImage> uploadCertifyImages(List<MultipartFile> certifyImages, Long certifyId) throws IOException {

        Certify certify = findCertifyOrThrow(certifyId);

        List<String> imageUrlList = s3Uploader.s3UploadOfCertifyImages(certify, certifyImages);

        List<CertifyImage> certifyImageList = new ArrayList<>();
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                CertifyImage certifyImage = certifyImageRepository.save(new CertifyImage(certify, imageUrl));
                certifyImageList.add(certifyImage);
            }
        }

        return certifyImageList;
    }

    // 멤버 프로필 이미지 업데이트
    public void updateProfileImage(MultipartFile profileImage, Long memberId) throws IOException {

        Member member = findMemberOrThrow(memberId);
        String imageUrl = s3Uploader.s3UploadOfProfileImage(member, profileImage);
        member.updateProfileImage(imageUrl);
    }


    private Challenge findChallengeOrThrow(Long challengeId){
        return challengeRepository.findById(challengeId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지는 존재하지 않습니다.");
        });
    }

    private Certify findCertifyOrThrow(Long certifyId){
        return certifyRepository.findById(certifyId).orElseThrow(() -> {
            throw new NoSuchIdException("요청하신 챌린지 인증은 존재하지 않습니다.");
        });
    }

    // 인증 성공 예시 이미지 조회
    public List<CertifyExampleImage> getCertifyImage(Challenge challenge) {

        return certifyExampleImageRepository.findCertifyImage(challenge.getChallengeId());
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new BadRequestException("요청한 멤버는 존재하지 않습니다.");
        });
    }
}
