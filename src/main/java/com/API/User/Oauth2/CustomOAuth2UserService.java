package com.API.User.Oauth2;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    	Optional<Set<User>> existingUsersOpt = userRepository.findByEmail(attributes.getEmail());

        if (existingUsersOpt.isPresent()) {
        	Set<User> existingUsers = existingUsersOpt.get();
            User mostRecentNonDeletedUser = existingUsers.stream()
                .filter(user -> user.getAccountType().equals(attributes.getAccountType()) && !user.isDeleted())
                .max(Comparator.comparing(User::getId))
                .orElse(null);
            if (mostRecentNonDeletedUser != null) {
                return userRepository.save(mostRecentNonDeletedUser);
            }
        } 
        return userRepository.save(attributes.toEntity());
    }
}

