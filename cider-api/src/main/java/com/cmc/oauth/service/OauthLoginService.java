package com.cmc.oauth.service;

import com.cmc.certify.Certify;
import com.cmc.challengeLike.ChallengeLike;
import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.CiderException;
import com.cmc.common.exception.MemberTokenNotFoundException;
import com.cmc.domains.certify.repository.CertifyRepository;
import com.cmc.domains.certifyLike.repository.CertifyLikeRepository;
import com.cmc.domains.challengeLike.repository.ChallengeLikeRepository;
import com.cmc.domains.image.certify.repository.CertifyImageRepository;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.memberToken.MemberTokenRepository;
import com.cmc.domains.participate.repository.ParticipateRepository;
import com.cmc.member.Member;
import com.cmc.member.constant.MemberType;
import com.cmc.memberToken.MemberToken;
import com.cmc.oauth.apple.AppleToken;
import com.cmc.oauth.constant.SocialType;
import com.cmc.oauth.dto.OAuthAttributes;
import com.cmc.oauth.dto.TokenDto;
import com.cmc.oauth.dto.response.KakaoAccount;
import com.cmc.oauth.dto.response.ResponseJwtTokenDto;
import com.cmc.participate.Participate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberRepository memberRepository;
    private final MemberTokenRepository memberTokenRepository;
    private final ChallengeLikeRepository challengeLikeRepository;
    private final CertifyRepository certifyRepository;
    private final ParticipateRepository participateRepository;
    private final CertifyLikeRepository certifyLikeRepository;
    private final CertifyImageRepository certifyImageRepository;
    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;

    // member 생성
    public ResponseJwtTokenDto createMemberAndJwt(KakaoAccount memberInfo, SocialType socialType) {

        // 회원 가입 or 로그인
        Member requestMember;
        final Optional<Member> foundMember = memberRepository.findByEmail(memberInfo.getEmail());

        ResponseJwtTokenDto responseJwtTokenDto = null;
        if (foundMember.isEmpty()) { // 기존 회원 아닐 때
            log.info("기존 회원 아닐 때 ");
            Member newMember = Member.create(memberInfo.getProfile().getNickname(),
                    memberInfo.getEmail(), memberInfo.getBirthday(), memberInfo.getGender(), socialType);
            newMember.updateProfileImage();
            requestMember = memberRepository.save(newMember);

            // JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.createTokenDtoKakao(requestMember.getMemberId());
            log.info("tokenDto: {}", tokenDto);

            responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);

           // final boolean isNewMember = StringUtils.isEmpty(requestMember.getMemberName());

            responseJwtTokenDto.setIsNewMember(true);
//            if (!isNewMember) {
//                if(requestMember.getMemberName() != null) {
//                    responseJwtTokenDto.setMemberName(requestMember.getMemberName());
//                }else{
//                    responseJwtTokenDto.setMemberName("");
//                }
//            }
            responseJwtTokenDto.setMemberName("");
            responseJwtTokenDto.setMemberId(requestMember.getMemberId());
            responseJwtTokenDto.setBirthday("");
            responseJwtTokenDto.setGender("");

        } else {
            log.info("기존 회원일 때 :: " + foundMember.get().getMemberId() + foundMember.get().getMemberName());
            requestMember = foundMember.get(); // 기존 회원일 때
            log.info("foundMember email ::::: " + foundMember.get().getEmail());

            // JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.createTokenDtoKakao(requestMember.getMemberId());
            log.info("tokenDto: {}", tokenDto);

            responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);

            final boolean isNewMember = StringUtils.isEmpty(requestMember.getMemberName());
            responseJwtTokenDto.setIsNewMember(false);
//            if (!isNewMember) {
//                responseJwtTokenDto.setMemberName(requestMember.getMemberName());
//            }
            responseJwtTokenDto.setMemberId(requestMember.getMemberId());
            responseJwtTokenDto.setBirthday(requestMember.getMemberBirth());
            responseJwtTokenDto.setGender(requestMember.getMemberGender());
            responseJwtTokenDto.setIsUpdatedMember(requestMember.getIsUpdatedMember());
        }

        return responseJwtTokenDto;
    }

    public ResponseJwtTokenDto loginAppleIos(String tokenString) throws JsonProcessingException {
        Member requestMember = null;

        String[] decodeArray = tokenString.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");

        PublicKey publicKey = this.getPublicKey(kid, alg);

        // Claims !!
        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(tokenString).getBody();

        // json 파싱 다시
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userInfo);
        log.info("json: :::::::::" + jsonString);

        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(jsonString);

        JsonElement appleAlg = userInfoObject.get("email");
        String email = appleAlg.getAsString();
        log.info("email ::::: " + email);

        OAuthAttributes socialUserInfo = OAuthAttributes
                .builder()
                .email(email)
                .name("")
                .socialType(SocialType.APPLE)
                .build();

        log.info("oauthAttributes: {}", socialUserInfo.toString());

        // 첫 가입인 경우
        Optional<Member> findMember = memberRepository.findByEmailAndSocialType(socialUserInfo.getEmail(), SocialType.APPLE);

        if(findMember.isEmpty()){
            String socialId = (String) userInfo.get("sub");
            log.info("처음 가입한 유저 socialId ::::::::::::::::: " + socialId);
            log.info("처음 가입한 유저 email ::::::::::::::::: " + email);

            Member newMember = Member.createApple(email, SocialType.APPLE, socialId);

            newMember.updateProfileImage();
            requestMember = memberRepository.save(newMember);

            return generateTokenApple(requestMember);
        }

        // 이미 가입한 경우
        String socialId = (String) userInfo.get("sub");
        log.info("이미 가입한 유저 socialId ::::::::::::::::: " + socialId);
        log.info("이미 가입한 유저 email ::::::::::::::::: " + email);

//        requestMember = memberRepository.findByMemberBySocialTypeAndSocialId(SocialType.APPLE, socialId).orElseThrow(() -> {
//            throw new CiderException("유저를 찾을 수 없습니다.");
//        });

        requestMember = memberRepository.findByEmailAndSocialType(email, SocialType.APPLE).orElseThrow(() -> {
            throw new CiderException("유저를 찾을 수 없습니다.");
        });

        return generateTokenNotNew(requestMember);
    }

//    public ResponseJwtTokenDto generateTokenApple(Member member) {
//
//        //String socialId = (String) claims.get("sub");
//
//        // JWT 토큰 생성
//        TokenDto tokenDto = tokenProvider.createTokenDtoApple(member.getMemberId());
//        log.info("tokenDto: {}", tokenDto);
//
//        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
//        final boolean isNewMember = StringUtils.isEmpty(member.getMemberName());
//        responseJwtTokenDto.setIsNewMember(isNewMember);
//        if (!isNewMember) {
//            responseJwtTokenDto.setMemberName(member.getMemberName());
//        }
//        responseJwtTokenDto.setMemberId(member.getMemberId());
//        responseJwtTokenDto.setMemberName("");
//        responseJwtTokenDto.setBirthday("");
//        responseJwtTokenDto.setGender("");
//
//        return responseJwtTokenDto;
//    }


    public ResponseJwtTokenDto generateTokenApple(Member member) {

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDtoKakao(member.getMemberId());
        log.info("tokenDto: {}", tokenDto);

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        final boolean isNewMember = StringUtils.isEmpty(member.getMemberName());
        responseJwtTokenDto.setIsNewMember(isNewMember);

        if (!isNewMember) {
            responseJwtTokenDto.setMemberName(member.getMemberName());
        }
        responseJwtTokenDto.setMemberId(member.getMemberId());
        responseJwtTokenDto.setMemberName("");
        responseJwtTokenDto.setBirthday("");
        responseJwtTokenDto.setGender("");

        return responseJwtTokenDto;
    }

    public ResponseJwtTokenDto generateTokenNotNew(Member member) {

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDtoKakao(member.getMemberId());
        log.info("tokenDto: {}", tokenDto);

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        responseJwtTokenDto.setIsNewMember(false);

        responseJwtTokenDto.setMemberId(member.getMemberId());
        responseJwtTokenDto.setMemberName(member.getMemberName());
        responseJwtTokenDto.setBirthday(member.getMemberBirth());
        responseJwtTokenDto.setGender(member.getMemberGender());

        return responseJwtTokenDto;
    }

    // 로그인
//    public ResponseJwtTokenDto login(SocialType socialType, String accessToken) {
//        final OauthLoginDto oauthLoginDto = OauthLoginDto.builder().accessToken(accessToken).socialType(socialType).build();
//        return createMemberAndJwt(oauthLoginDto);
//    }

    public PublicKey getPublicKey(JsonElement kid, JsonElement alg) {

        JsonArray keys = this.getApplePublicKeys();

        JsonObject avaliableObject = null;

        for (int i = 0; i < keys.size(); i++) {
            JsonObject appleObject = (JsonObject) keys.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                avaliableObject = appleObject;
                break;
            }
        }

        //일치하는 공개키 없음
        if (ObjectUtils.isEmpty(avaliableObject)) {
            throw new BadRequestException("유호하지 않은 토큰입니다.");
        }

        String nStr = avaliableObject.get("n").toString();
        String eStr = avaliableObject.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new CiderException("애플 로그인 퍼블릭 키를 불러오는데 실패했습니다.");
        }
    }

    // 로그아웃
    public void logout(String refreshToken, LocalDateTime now) {

       // List<MemberToken> memberTokenList = memberTokenRepository.findByMember

        final MemberToken memberToken = memberTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new MemberTokenNotFoundException("해당 리프레시 토큰이 존재하지 않습니다."));
        memberToken.expire(now);
    }

    // 탈퇴
    public void signOut(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CiderException("해당 멤버가 존재하지 않습니다."));

        // TODO : 아예 삭제 or isDeleted 처리 논의

        // 챌린지 좋아요 삭제
        challengeLikeRepository.deleteAll(member.getChallengeLikes());

        // 게시글 좋아요 삭제
        certifyLikeRepository.deleteAll(member.getCertifyLikes());

        // 피드 삭제
//        for(Participate participate : member.getParticipates()){
//
//            for(Certify certify : participate.getCertifies()){
//                log.info("certifyID  :::::: "+ certify.getCertifyId());
//                certifyImageRepository.deleteAll(certify.getCertifyImageList());
//            }
//            certifyRepository.deleteAll(participate.getCertifies());
//        }

        // 참여 챌린지 기록 삭제
        //participateRepository.deleteAll(member.getParticipates());

        // member isDeleted 업데이트
        member.updateIsDeleted();
        member.updateEmail();

        // refresh token 만료처리
        memberTokenRepository.updateExpirationTimeByMemberId(memberId, LocalDateTime.now());
    }

    private JsonArray getApplePublicKeys() {
        StringBuilder apiKey = new StringBuilder();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                apiKey.append(line);
            }

            JsonObject keys = (JsonObject) JsonParser.parseString(apiKey.toString());

            return (JsonArray) keys.get("keys");
        } catch (IOException e) {
            throw new CiderException("URL 파싱 실패");
        }
    }

    public String getAccessToken(String authorizationCode) {

        //return kakaoLoginService.getAccess(authorizationCode);
        return authorizationCode;
    }

    private void validateAccessToken(String accessToken) {

        if (StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("Access 토큰값을 입력해주세요.");
        }
    }

}
