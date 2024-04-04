package com.API.User.Oauth2;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.API.User.Entity.User;
import com.API.User.Entity.UserLevel;
import com.API.User.Entity.UserRole;
import com.API.User.Etc.PasswordGenerator;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String userid;
    private String accountType;
 
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
 
        return ofGoogle(userNameAttributeName, attributes);
    }
 
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .userid((String) attributes.get("sub"))
                .accountType("Google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
 
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .userid((String) response.get("id"))
                .accountType("Naver")
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
 
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    	Long longId = (Long)attributes.get("id");
    	String id = longId.toString();
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> account = (Map<String, Object>)response.get("profile");
        return OAuthAttributes.builder()
                .name((String) account.get("nickname"))
                .email((String) response.get("email"))
                .userid(id)
                .accountType("Kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
 
    public User toEntity() {
        return User.builder()
                .name(userid)
                .email(email)
                .role(UserRole.USER)
                .userlevel(new UserLevel(1, 0))
                .password(PasswordGenerator.generateRandomPassword(15))
                .nickname(name)
                .accountType(accountType)
                .build();
    }
    public OAuthAttributes withNewUserId(String newUserId) {
        this.userid = newUserId;
        return this;
    }
}