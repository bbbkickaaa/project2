package com.API.User.Oauth2;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.API.User.UserRepository;
import com.API.User.Entity.User;
import com.API.User.Entity.UserLevel;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
 
    private final UserRepository userRepository;
 
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
 
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
 
        User user = saveOrUpdate(attributes);
        
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
 
    private User saveOrUpdate(OAuthAttributes attributes) {
    	 Optional<User> existingUserOpt = userRepository.findByEmail(attributes.getEmail());

    	    if (existingUserOpt.isPresent()) {
    	        User existingUser = existingUserOpt.get();
    	        if (existingUser.isDeleted()) {
    	            throw new IllegalStateException("삭제된 회원입니다.");
    	        }
    	        existingUser.update(attributes.getUserid());
    	        return userRepository.save(existingUser);
    	    } else {
    	        return userRepository.save(attributes.toEntity());
    	    }
    }
}

