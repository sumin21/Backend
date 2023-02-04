package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    @Value("${auth.kakao.key}")
    private String authKakaoKey;

    @Value("${auth.kakao.redirecturl}")
    private String authKakaoRedirectUrl;

    public String getAccessTokenByCode(String code) {
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
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.toString());
        }
        return accessToken;
    }

    public Optional<User> saveUserInfoByToken(String accessToken) {
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
            System.out.println("elem = " + elem);
            Long id = (Long) elem.get("id");
            String email = (String) ((JSONObject) elem.get("kakao_account")).get("email");
            String nickname = (String) ((JSONObject) elem.get("properties")).get("nickname");
            String img = (String) ((JSONObject) elem.get("properties")).get("profile_image");

            return Optional.ofNullable(User.createKakaoUser(id, email, img, nickname));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
