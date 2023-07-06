package com.cmc.oauth.service;

import com.cmc.common.exception.BadRequestException;
import com.cmc.common.exception.CiderException;
import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.member.Member;
import com.cmc.oauth.constant.SocialType;
import com.cmc.oauth.dto.OAuthAttributes;
import com.cmc.oauth.dto.OauthLoginDto;
import com.cmc.oauth.dto.TokenDto;
import com.cmc.oauth.dto.response.KakaoUserInfoResDto;
import com.cmc.oauth.dto.response.ResponseJwtTokenDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;

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
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;

    // member 생성
    public ResponseJwtTokenDto createMemberAndJwt(KakaoUserInfoResDto kakaoUserInfoResDto, SocialType socialType) {

        // 회원 가입 or 로그인
        Member requestMember;
        final Optional<Member> foundMember = memberRepository.findByEmail(kakaoUserInfoResDto.getEmail());
        if (foundMember.isEmpty()) { // 기존 회원 아닐 때
            Member newMember = Member.create(kakaoUserInfoResDto.getEmail(), socialType);
            requestMember = memberRepository.save(newMember);
        } else {
            requestMember = foundMember.get(); // 기존 회원일 때
        }

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(requestMember.getMemberId());
        log.info("tokenDto: {}", tokenDto);

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        final boolean isNewMember = StringUtils.isEmpty(requestMember.getMemberName());
        responseJwtTokenDto.setIsNewMember(isNewMember);
        if (!isNewMember) {
            responseJwtTokenDto.setMemberName(requestMember.getMemberName());
        }
        responseJwtTokenDto.setMemberId(requestMember.getMemberId());

        return responseJwtTokenDto;
    }

    public ResponseJwtTokenDto loginAppleIos(String tokenString) throws JsonProcessingException {
        Member requestMember;

        String[] decodeArray = tokenString.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) JsonParser.parseString(header)).get("kid");
        JsonElement alg = ((JsonObject) JsonParser.parseString(header)).get("alg");

        PublicKey publicKey = this.getPublicKey(kid, alg);

        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(tokenString).getBody();

        // json 파싱 다시
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(userInfo);
        log.info("json: :::::::::" + jsonString);

        JsonObject userInfoObject = (JsonObject) JsonParser.parseString(jsonString);

        JsonElement appleAlg = userInfoObject.get("email");
        String email = appleAlg.getAsString();

        OAuthAttributes socialUserInfo = OAuthAttributes
                .builder()
                .email(email) // 이메일 동의 x 경우
                .name("")
                .socialType(SocialType.APPLE)
                .build();

        log.info("oauthAttributes: {}", socialUserInfo.toString());


        return null;
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


    private final KakaoLoginService kakaoLoginService;

    public String getAccessToken(String authorizationCode) {

        //return kakaoLoginService.getAccess(authorizationCode);
        return authorizationCode;
    }

    public void validateLoginParams(SocialType socialType, String accessToken) {

        validateAccessToken(accessToken);
    }

    private void validateAccessToken(String accessToken) {

        if (StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("Access 토큰값을 입력해주세요.");
        }
    }



}
