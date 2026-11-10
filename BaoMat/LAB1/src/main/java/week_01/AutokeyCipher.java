package week_01; 
  
import java.util.ArrayList; 
import java.util.List; 
import java.util.Locale; 
  
/** Plaintext Autokey: sau khóa khởi tạo, bổ sung các chữ của bản rõ. */ 
public final class AutokeyCipher { 
    private AutokeyCipher() { } 
  
    public static String encrypt(String text, String key) { 
        return process(text, key, true); 
    } 
  
    public static String decrypt(String text, String key) { 
        return process(text, key, false); 
    } 
  
    private static String process(String text, String key, boolean encrypt) { 
        String k = key.trim().toUpperCase(Locale.ROOT); 
        if (!k.matches("[A-Z]+")) { 
            throw new IllegalArgumentException("Khóa Autokey chỉ gồm A-Z."); 
        } 
        List<Integer> plainOffsets = new ArrayList<>(); 
        StringBuilder result = new StringBuilder(); 
        int letterIndex = 0; 
        for (char c : text.toCharArray()) { 
            char base; 
            if (c >= 'A' && c <= 'Z') base = 'A'; 
            else if (c >= 'a' && c <= 'z') base = 'a'; 
            else { 
                result.append(c); 
                continue; 
            } 
            int shift;          if (letterIndex < k.length()) { 
                shift = k.charAt(letterIndex) - 'A'; 
            } else { 
                shift = plainOffsets.get(letterIndex - k.length()); 
            } 
            int delta = encrypt ? shift : -shift; 
            char transformed = (char) (base + Math.floorMod(c - base + delta, 26)); 
            result.append(transformed); 
            // Giải mã phải thêm chữ bản rõ vừa KHÔI PHỤC, không thêm bản mã. 
            char plaintextChar = encrypt ? c : transformed; 
            plainOffsets.add(plaintextChar - base); 
            letterIndex++; 
        } 
        return result.toString(); 
    } 

} 