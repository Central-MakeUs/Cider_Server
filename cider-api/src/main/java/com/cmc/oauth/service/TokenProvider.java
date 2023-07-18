package com.cmc.oauth.service;

import com.cmc.domains.member.repository.MemberRepository;
import com.cmc.domains.memberToken.MemberTokenRepository;
import com.cmc.global.util.DateTimeUtils;
import com.cmc.memberToken.MemberToken;
import com.cmc.memberToken.constant.JwtTokenType;
import com.cmc.oauth.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberTokenRepository memberTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${token.access-token-expiration-time}")
    private String accessTokenExpirationTime;

    @Value("${token.refresh-token-expiration-time}")
    private String refreshTokenExpirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    private static final String BEARER_TYPE = "bearer";

    public static Long getMemberIdKakao(String token) {
        try {
            Key key = Keys.hmacShaKeyFor("cidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkey".getBytes());

            final Claims claims = Jwts.parser().setSigningKey(key)
                    .parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getAudience());
        } catch (Exception e) {
            log.warn("토큰 변환 중 에러 발생: {}", token);
            e.printStackTrace();
            throw e;
        }
//        try {
//            final Claims claims = Jwts.parser().setSigningKey("cidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkey") //jwt 만들 때 사용했던 키. static 메서드 사용하기 위해서 String으로 하드 코딩.
//                    .parseClaimsJws(token).getBody();
//            return Long.parseLong(claims.getAudience());
//        } catch (Exception e) {
//            log.warn("토큰 변환 중 에러 발생: {}", token);
//            e.printStackTrace();
//            throw e;
//        }
    }

    public static Long getMemberId(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey("cidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkey") //jwt 만들 때 사용했던 키. static 메서드 사용하기 위해서 String으로 하드 코딩.
                    .parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getAudience());
        } catch (Exception e) {
            log.warn("토큰 변환 중 에러 발생: {}", token);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 엑세스/리프레시 토큰 dto 생성
     * refresh token 저장 로직 포함
     * @param memberId
     * @return
     */
    public TokenDto createTokenDtoApple(Long memberId) {

        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessToken(memberId, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);

        final MemberToken memberToken = MemberToken.create(refreshToken, DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime), memberId);
        memberTokenRepository.save(memberToken);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /**
     * 엑세스/리프레시 토큰 dto 생성
     * refresh token 저장 로직 포함
     * @param memberId
     * @return
     */
    public TokenDto createTokenDtoKakao(Long memberId) {

        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String accessToken = createAccessTokenV2(memberId, accessTokenExpireTime);
        String refreshToken = createRefreshTokenV2(memberId, refreshTokenExpireTime);

        final MemberToken memberToken = MemberToken.create(refreshToken, DateTimeUtils.convertToLocalDateTime(refreshTokenExpireTime), memberId);
        memberTokenRepository.save(memberToken);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    private String createRefreshToken(Long memberId, Date expirationTime) {
        return Jwts.builder()
                .setSubject(JwtTokenType.REFRESH.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String createAccessToken(Long memberId, Date expirationTime) {
        return Jwts.builder()
                .setSubject(JwtTokenType.ACCESS.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public String createAccessTokenV2(Long memberId, Date expirationTime) {
        Key key = Keys.hmacShaKeyFor("cidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkey".getBytes()); // 안전한 키 생성

        // JWT 생성
        String token = Jwts.builder()
                .setSubject(JwtTokenType.ACCESS.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS512) // 생성한 키와 알고리즘을 명시적으로 지정하여 서명
                .compact();

        return token;
    }

    public String createRefreshTokenV2(Long memberId, Date expirationTime) {
        Key key = Keys.hmacShaKeyFor("cidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkeycidersecretkey".getBytes()); // 안전한 키 생성

        // JWT 생성
        String token = Jwts.builder()
                .setSubject(JwtTokenType.REFRESH.name())
                .setAudience(Long.toString(memberId))
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS512) // 생성한 키와 알고리즘을 명시적으로 지정하여 서명
                .compact();

        return token;
    }

    /**
     * access token 만료 시간 생성
     * @return
     */
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * refresh token 만료 시간 생성
     * @return
     */
    private Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    public boolean isTokenExpired(Date now, Date tokenExpiredTime) {
        if (now.after(tokenExpiredTime)) { // 토큰 만료된 경우
            return true;
        }
        return false;
    }

    public boolean isTokenExpired(LocalDateTime now, LocalDateTime tokenExpiredTime) {
        if (now.isAfter(tokenExpiredTime)) { // 토큰 만료된 경우
            return true;
        }
        return false;
    }

    /**
     * 토큰 유효 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {  // 토큰 변조
            log.warn("토큰 변조 감지: {}", token);
            e.printStackTrace();
        } catch (Exception e) {
            log.warn("토큰 검증 에러 발생: {}", token);
            e.printStackTrace();
            throw e;
        }

        return false;
    }

    /**
     * 토큰 파싱 후 property 변환
     * @param token
     * @return
     */
    public Claims getTokenClaims(String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw e;
        }
        return claims;
    }

}
