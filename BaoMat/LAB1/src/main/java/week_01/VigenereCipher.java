package week_01;

import java.util.Locale;

/**
 * Vigenere: lặp khóa A-Z; dấu cách và dấu câu không làm tiến khóa.
 */
public final class VigenereCipher {

    private VigenereCipher() {
    }

    public static String encrypt(String text, String key) {
        return process(text, key, true);
    }

    public static String decrypt(String text, String key) {
        return process(text, key, false);
    }

    private static String process(String text, String key, boolean encrypt) {
        String k = key.trim().toUpperCase(Locale.ROOT);
        if (!k.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Khóa Vigenere chỉ gồm A-Z.");
        }
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            char base;
            if (c >= 'A' && c <= 'Z') {
                base = 'A';
            } else if (c >= 'a' && c <= 'z') {
                base = 'a';
            } else {
                result.append(c);
                continue;
            }
            int shift = k.charAt(keyIndex % k.length()) - 'A';
            keyIndex++;
            int delta = encrypt ? shift : -shift;
            result.append((char) (base + Math.floorMod(c - base + delta, 26)));
        }
        return result.toString();
    }
}
