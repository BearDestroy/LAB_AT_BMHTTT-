package week_01;

/**
 * Hoán vị cột theo khóa liệt kê thứ tự đọc các cột (đánh số từ 1).
 */
public final class TranspositionCipher {

    private TranspositionCipher() {
    }

    public static String encrypt(String text, int[] key) {
        return process(text, key, true);
    }

    public static String decrypt(String text, int[] key) {
        return process(text, key, false);
    }

    /**
     * Đổi chuỗi khóa "2,3,1" (nhập trên form) thành mảng {2, 3, 1}.
     */
    public static int[] parseKey(String keyText) {
        String[] parts = keyText.trim().split("\\s*,\\s*");
        int[] key = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                key[i] = Integer.parseInt(parts[i]);
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Khóa phải là các số cách nhau bởi dấu phẩy, ví dụ 2,3,1.");
        }
        validateKey(key);
        return key;
    }

    private static void validateKey(int[] key) {
        if (key == null || key.length < 2 || key.length > 50) {
            throw new IllegalArgumentException("Khóa phải có từ 2 đến 50 cột.");
        }
        boolean[] used = new boolean[key.length + 1];
        for (int col : key) {
            if (col < 1 || col > key.length || used[col]) {
                throw new IllegalArgumentException("Khóa phải là hoán vị 1..m.");
            }
            used[col] = true;
        }
    }

    private static String process(String text, int[] key, boolean encrypt) {
        validateKey(key);
        int[] chars = text.codePoints().toArray();
        int length = chars.length;
        int columns = key.length;
        int rows = (length + columns - 1) / columns;
        int[][] grid = new int[rows][columns];
        int offset = 0;
        if (encrypt) {
            for (int i = 0; i < length; i++) {
                grid[i / columns][i % columns] = chars[i];
            }
            int[] result = new int[length];
            for (int column : key) {
                for (int row = 0; row < rows; row++) {
                    if (row * columns + column - 1 < length) {
                        result[offset++] = grid[row][column - 1];
                    }
                }
            }
            return new String(result, 0, result.length);
        }
        for (int column : key) {
            for (int row = 0; row < rows; row++) {
                if (row * columns + column - 1 < length) {
                    grid[row][column - 1] = chars[offset++];
                }
            }
        }
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = grid[i / columns][i % columns];
        }
        return new String(result, 0, result.length);
    }
}
