package week_01; 
  
/** 
 * Bài nâng cao 01: gọi đúng lớp mã hóa theo tên thuật toán chọn trên JComboBox. 
 * Khóa luôn nhập dạng chuỗi; lớp này tự đổi sang kiểu số khi thuật toán cần. 
 */ 
public final class CipherTongHop { 
  
    /** Tên hiển thị trên JComboBox - phải giống hệt model của cbo_algorithm. */ 
    public static final String[] ALGORITHMS = {"Caesar", "Vigenere", "Rail Fence", "Playfair", "Transposition", "Autokey"}; 
  
    private CipherTongHop() { 
    } 
  
    public static String encrypt(String algorithm, String text, String key) { 
        return run(algorithm, text, key.trim(), true); 
    } 
  
    public static String decrypt(String algorithm, String text, String key) { 
        return run(algorithm, text, key.trim(), false); 
    } 
  
    /** Gợi ý khóa mẫu cho từng thuật toán (hiện cạnh JComboBox). */ 
    public static String sampleKey(String algorithm) { 
        switch (algorithm) { 
            case "Caesar": return "3"; 
            case "Rail Fence": return "3"; 
            case "Playfair": return "KEYWORD"; 
            case "Transposition": return "2,3,1"; 
            default: return "KEY"; 
        } 
    } 
  
    private static String run(String algorithm, String text, String key, boolean encrypt) { 
        if (text.isEmpty()) { 
            throw new IllegalArgumentException("Chưa nhập dữ liệu."); 
        } 
        switch (algorithm) { 
            case "Caesar":  int k = parseInt(key, "Khóa Caesar phải là số nguyên, ví dụ 3."); 
                return encrypt ? CaesarCipher.encrypt(text, k) : CaesarCipher.decrypt(text, k); 
            case "Vigenere": 
                return encrypt ? VigenereCipher.encrypt(text, key) : VigenereCipher.decrypt(text, key); 
            case "Rail Fence": 
                int rails = parseInt(key, "Số hàng Rail Fence phải là số nguyên, ví dụ 3."); 
                return encrypt ? RailFenceCipher.encrypt(text, rails) : RailFenceCipher.decrypt(text, rails); 
            case "Playfair": 
                return encrypt ? PlayfairCipher.encrypt(text, key) : PlayfairCipher.decrypt(text, key); 
            case "Transposition": 
                int[] order = TranspositionCipher.parseKey(key); 
                return encrypt ? TranspositionCipher.encrypt(text, order) : TranspositionCipher.decrypt(text, order); 
            case "Autokey": 
                return encrypt ? AutokeyCipher.encrypt(text, key) : AutokeyCipher.decrypt(text, key); 
            default: 
                throw new IllegalArgumentException("Thuật toán không hỗ trợ: " + algorithm); 
        } 
    } 
  
    private static int parseInt(String key, String message) { 
        try { 
            return Integer.parseInt(key); 
        } catch (NumberFormatException ex) { 
            throw new IllegalArgumentException(message); 
        } 
    } 

} 