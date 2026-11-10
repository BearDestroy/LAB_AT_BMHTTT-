package week_01; 
  
import java.util.Locale; 
  
/** Bài nâng cao 02: ba kỹ thuật phân tích mật mã cổ điển (chỉ dùng cho dữ liệu học tập). */ 
public final class AttackDemos { 
  
    /** Tần suất (%) chữ A..Z trong văn bản tiếng Anh, dùng để chấm điểm các bản rõ thử. */ 
    private static final double[] ENGLISH = {8.2, 1.5, 2.8, 4.3, 12.7, 2.2, 2.0, 6.1, 7.0, 0.15, 0.77, 4.0, 2.4, 
        6.7, 7.5, 1.9, 0.095, 6.0, 6.3, 9.1, 2.8, 0.98, 2.4, 0.15, 2.0, 0.074}; 
  
    private AttackDemos() { 
    } 
  
    /** 1) Vét cạn: thử đủ 26 khóa Caesar, đánh dấu khóa cho bản rõ "giống tiếng Anh" nhất. */ 
    public static String caesarBruteForce(String cipher) { 
        int best = 0; 
        double bestScore = -Double.MAX_VALUE; 
        for (int k = 0; k < 26; k++) { 
            double score = englishScore(CaesarCipher.decrypt(cipher, k)); 
            if (score > bestScore) { 
                bestScore = score; 
                best = k; 
            } 
        } 
        StringBuilder sb = new StringBuilder("k  | Bản rõ thử\n"); 
        for (int k = 0; k < 26; k++) { 
            sb.append(String.format("%2d | %s%s%n", k, CaesarCipher.decrypt(cipher, k), k == best ? "   <== có nghĩa nhất" : 
"")); 
        } 
        sb.append("Kết luận: khóa k = ").append(best).append(", bản rõ = ").append(CaesarCipher.decrypt(cipher, best)); 
        return sb.toString(); 
    } 
  
    /** 2) Thống kê tần suất các chữ cái A-Z có trong bản mã (sắp theo bảng chữ cái). */ 
    public static String frequency(String cipher) {  int[] count = new int[26]; 
        int total = 0; 
        for (char c : cipher.toUpperCase(Locale.ROOT).toCharArray()) { 
            if (c >= 'A' && c <= 'Z') { 
                count[c - 'A']++; 
                total++; 
            } 
        } 
        if (total == 0) { 
            return "Bản mã không có chữ cái A-Z."; 
        } 
        StringBuilder sb = new StringBuilder("Chữ | Số lần | Tần suất\n"); 
        for (int i = 0; i < 26; i++) { 
            if (count[i] > 0) { 
                sb.append(String.format(Locale.ROOT, " %c  |   %2d   | %5.1f %%%n", (char) ('A' + i), count[i], 100.0 * count[i] / 
total)); 
            } 
        } 
        sb.append("Tổng số chữ: ").append(total); 
        return sb.toString(); 
    } 
  
    /** 3) Biết một cặp bản rõ/bản mã Vigenère: khóa_i = (C_i - P_i) mod 26. */ 
    public static String knownPlaintext(String plain, String cipher) { 
        String p = plain.trim().toUpperCase(Locale.ROOT); 
        String c = cipher.trim().toUpperCase(Locale.ROOT); 
        if (p.isEmpty() || p.length() != c.length()) { 
            throw new IllegalArgumentException("Bản rõ và bản mã phải cùng độ dài."); 
        } 
        if (!p.matches("[A-Z]+") || !c.matches("[A-Z]+")) { 
            throw new IllegalArgumentException("Chỉ nhập chữ A-Z, không dấu, không khoảng trắng."); 
        } 
        StringBuilder key = new StringBuilder(); 
        for (int i = 0; i < p.length(); i++) { 
            key.append((char) ('A' + Math.floorMod(c.charAt(i) - p.charAt(i), 26))); 
        } 
        return key.toString(); 
    } 
  
    /** Điểm "giống tiếng Anh": cộng log tần suất của từng chữ; điểm càng LỚN càng giống tiếng Anh. */ 
    private static double englishScore(String text) { 
        double score = 0; 
        for (char ch : text.toUpperCase(Locale.ROOT).toCharArray()) { 
            if (ch >= 'A' && ch <= 'Z') { 
                score += Math.log(ENGLISH[ch - 'A']); 
            } 
        } 
        return score; 
    } 

} 