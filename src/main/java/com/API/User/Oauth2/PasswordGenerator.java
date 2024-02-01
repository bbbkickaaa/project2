package com.API.User.Oauth2;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
	
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL_CHAR = "!@#$%&*()_+-=[]|,./?><";

    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + DIGIT + SPECIAL_CHAR;
    private static final Random RANDOM = new SecureRandom();

    public static String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = 0;
			try {
				index = RANDOM.nextInt(PASSWORD_ALLOW.length());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            password.append(PASSWORD_ALLOW.charAt(index));
        }
        return password.toString();
    }
}