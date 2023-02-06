package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.exception.*;
import com.backend.doyouhave.jwt.JwtTokenProvider;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:env.properties")
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.kakao.key}")
    private String authKakaoKey;

    @Value("${auth.kakao.redirecturl}")
    private String authKakaoRedirectUrl;

    @Value("${auth.naver.clientId}")
    private String authNaverClientId;

    @Value("${auth.naver.key}")
    private String authNaverKey;

    @Value("${auth.naver.redirecturl}")
    private String authNaverRedirectUrl;


    public String getKakaoAccessTokenByCode(String code) {
        String accessToken = "";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", authKakaoKey);
            params.add("redirect_uri", authKakaoRedirectUrl);
            params.add("code", code);

            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                    new HttpEntity<>(params, headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(response.getBody());
            System.out.println("elem = " + elem);
            accessToken = (String) elem.get("access_token");
        } catch (Exception e) {
            throw new SocialLoginException();
        }
        return accessToken;
    }

    public Optional<User> saveUserInfoByKakaoToken(String accessToken) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );

            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(response.getBody());

            Long id = (Long) elem.get("id");
            String email = (String) ((JSONObject) elem.get("kakao_account")).get("email");
            String nickname = (String) ((JSONObject) elem.get("properties")).get("nickname");
            String img = (String) ((JSONObject) elem.get("properties")).get("profile_image");

            return Optional.ofNullable(User.createKakaoUser(id, email, img, nickname));
        } catch (Exception e) {
            throw new SocialLoginException();
        }
    }

    public String getNaverAccessTokenByCode(String code, String state) {
        String accessToken = "";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", authNaverClientId);
            params.add("client_secret", authNaverKey);
            params.add("code", code);
            params.add("state", state);

            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                    new HttpEntity<>(params, headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://nid.naver.com/oauth2.0/token",
                    HttpMethod.POST,
                    naverTokenRequest,
                    String.class
            );

            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(response.getBody());
            System.out.println("elem = " + elem);
            accessToken = (String) elem.get("access_token");
        } catch (Exception e) {
            throw new SocialLoginException();
        }
        return accessToken;
    }

    public Optional<User> saveUserInfoByNaverToken(String accessToken) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            RestTemplate rt = new RestTemplate();
            rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);

            ResponseEntity<String> response = rt.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET,
                    naverProfileRequest,
                    String.class
            );

            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) ((JSONObject) parser.parse(response.getBody())).get("response");
            System.out.println("elem = " + elem);
            Long id = (Long) elem.get("id");
            String email = (String) elem.get("email");
            String nickname = (String) elem.get("name");
            String img = (String) elem.get("profile_image");

            return Optional.ofNullable(User.createNaverUser(id, email, img, nickname));
        } catch (Exception e) {
            throw new SocialLoginException();
        }
    }

    public LoginResponseDto tokenRefresh(String refreshToken){
        boolean isValid = jwtTokenProvider.validateToken(refreshToken) == null;

        if (refreshToken == null || !isValid) {
            throw new BusinessException(ExceptionCode.FAIL_AUTHENTICATION);
        }

        Long userId = jwtTokenProvider.getJwtTokenPayload(refreshToken);
        String usersRefreshToken = userRepository.findRefreshTokenById(userId);

        if (!refreshToken.equals(usersRefreshToken)) {
            throw new BusinessException(ExceptionCode.FAIL_AUTHENTICATION);
        }

        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        updateRefreshToken(userId, newRefreshToken);

        return LoginResponseDto.from(
                jwtTokenProvider.createAccessToken(userId),
                newRefreshToken);
    }

    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }
}
