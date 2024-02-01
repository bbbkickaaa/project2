package com.API.User;

import java.security.SecureRandom;

public class RandomNicknameGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NICKNAME_LENGTH = 8; // 원하는 닉네임의 길이를 조정하세요.

    public static String generateRandomNickname() {
        StringBuilder nickname = new StringBuilder(NICKNAME_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < NICKNAME_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            nickname.append(randomChar);
        }

        return nickname.toString();
    }
}
