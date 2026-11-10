package week_01;

/**
 * Rail Fence: xếp các ký tự theo đường zigzag rồi đọc theo từng hàng.
 */
public final class RailFenceCipher {

    private RailFenceCipher() {
    }

    public static String encrypt(String text, int rails) {
        return process(text, rails, true);
    }

    public static String decrypt(String text, int rails) {
        return process(text, rails, false);
    }

    private static int[] railPattern(int size, int rails) {
        int[] pattern = new int[size];
        int row = 0;
        int direction = 1;
        for (int i = 0; i < size; i++) {
            pattern[i] = row;
            if (row == 0) {
                direction = 1;
            } else if (row == rails - 1) {
                direction = -1;
            }
            row += direction;
        }
        return pattern;
    }

    private static String process(String text, int rails, boolean encrypt) {
        if (rails < 2 || rails > 1000) {
            throw new IllegalArgumentException("Số hàng phải từ 2 đến 1000.");
        }
        int[] characters = text.codePoints().toArray();
        int length = characters.length;
        if (length <= 1 || rails >= length) {
            return text;
        }
        int[] pattern = railPattern(length, rails);
        int[] result = new int[length];
        int offset = 0;
        if (encrypt) {
            for (int rail = 0; rail < rails; rail++) {
                for (int i = 0; i < length; i++) {
                    if (pattern[i] == rail) {
                        result[offset++] = characters[i];
                    }
                }
            }
        } else {
            int[] index = new int[length];
            for (int rail = 0; rail < rails; rail++) {
                for (int i = 0; i < length; i++) {
                    if (pattern[i] == rail) {
                        index[i] = offset++;
                    }
                }
            }
            for (int i = 0; i < length; i++) {
                result[i] = characters[index[i]];
            }
        }
        return new String(result, 0, result.length);
    }
}
