package bih.nic.in.policesoft.security;

import java.security.SecureRandom;


public class RandomString {

    /**
     * Generate a random string.
     */
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHA_NUMERIC_STRING1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static String randomAlphaNumeric(int count) {
        SecureRandom secureRnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) sb.append(ALPHA_NUMERIC_STRING.charAt(secureRnd.nextInt(ALPHA_NUMERIC_STRING.length())));
        return sb.toString();
    }
}