package week_01;

import java.util.Locale;

/**
 * Playfair 5x5: I/J chung ô, dùng X/Q làm ký tự đệm.
 */
public final class PlayfairCipher {

    private final char[][] matrix;

    public PlayfairCipher(String key) {
        this.matrix = makeMatrix(key);
    }

    public String encrypt(String text) {
        return process(text, true);
    }

    public String decrypt(String text) {
        return process(text, false);
    }

    public static String encrypt(String text, String key) {
        return new PlayfairCipher(key).encrypt(text);
    }

    public static String decrypt(String text, String key) {
        return new PlayfairCipher(key).decrypt(text);
    }

    public static String getKeyMatrix(String key) {
        StringBuilder display = new StringBuilder();
        for (char[] row : makeMatrix(key)) {
            for (char c : row) {
                display.append(c).append(' ');
            }
            display.append('\n');
        }
        return display.toString();
    }

    private static char[][] makeMatrix(String key) {
        String upper = key.trim().toUpperCase(Locale.ROOT);
        if (!upper.matches("[A-Z]+")) {
            throw new IllegalArgumentException("Khóa Playfair chỉ gồm A-Z.");
        }
        boolean[] used = new boolean[26];
        StringBuilder alphabet = new StringBuilder();
        for (char c : (upper + "ABCDEFGHIKLMNOPQRSTUVWXYZ").toCharArray()) {
            if (c == 'J') {
                c = 'I';
            }
            if (!used[c - 'A']) {
                used[c - 'A'] = true;
                alphabet.append(c);
            }
        }
        char[][] output = new char[5][5];
        for (int i = 0; i < 25; i++) {
            output[i / 5][i % 5] = alphabet.charAt(i);
        }
        return output;
    }

    private static String normalize(String text) {
        StringBuilder clean = new StringBuilder();
        for (char c : text.toUpperCase(Locale.ROOT).toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                clean.append(c == 'J' ? 'I' : c);
            }
        }
        if (clean.length() == 0) {
            throw new IllegalArgumentException("Văn bản Playfair cần chữ A-Z.");
        }
        return clean.toString();
    }

    private static String makePairs(String normalized) {
        StringBuilder pairs = new StringBuilder();
        int i = 0;
        while (i < normalized.length()) {
            char first = normalized.charAt(i++);
            char second;
            if (i >= normalized.length() || first == normalized.charAt(i)) {
                second = first == 'X' ? 'Q' : 'X';
            } else {
                second = normalized.charAt(i++);
            }
            pairs.append(first).append(second);
        }
        return pairs.toString();
    }

    private int[] locate(char c) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (matrix[row][col] == c) {
                    return new int[]{row, col};
                }
            }
        }
        throw new IllegalArgumentException("Ký tự không có trong bảng khóa.");
    }

    private String process(String text, boolean encrypt) {
        String data = normalize(text);
        if (encrypt) {
            data = makePairs(data);
        } else if (data.length() % 2 != 0) {
            throw new IllegalArgumentException("Bản mã Playfair phải có độ dài chẵn.");
        }
        int step = encrypt ? 1 : 4;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < data.length(); i += 2) {
            int[] a = locate(data.charAt(i));
            int[] b = locate(data.charAt(i + 1));
            if (a[0] == b[0]) {
                output.append(matrix[a[0]][(a[1] + step) % 5]);
                output.append(matrix[b[0]][(b[1] + step) % 5]);
            } else if (a[1] == b[1]) {
                output.append(matrix[(a[0] + step) % 5][a[1]]);
                output.append(matrix[(b[0] + step) % 5][b[1]]);
            } else {
                output.append(matrix[a[0]][b[1]]);
                output.append(matrix[b[0]][a[1]]);
            }
        }
        return output.toString();
    }
}
