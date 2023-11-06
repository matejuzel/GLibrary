/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Math;

/**
 *
 * @author matej uzel
 */
public class MFloat {
    
    public static int MASK_SIGN_FLOAT = 0x80000000; // 22-0
    public static int MASK_EXPONENT_FLOAT = 0x7f800000; // 30-23
    public static int MASK_MANTISSA_FLOAT = 0x007fffff; // 31
    
    public static int MASK_HIDDEN_BIT = 0x00800000; // pozice bitu mantisy, ktery se nezapisuje
    
    public static int OFFSET_SIGN = 31;
    public static int OFFSET_EXPONENT = 23;
    
    private final float num;
    private final int rawInt;
    private String rawString;
    
    int sign;
    int exponent;
    int exponentReal;
    int mantissa;
    int mantissaReal;
    
    public MFloat(float num) {
        this.num = num;
        rawInt = Float.floatToRawIntBits(this.num);
        rawString = MFloat.intToBinnaryString32(rawInt);
        
        sign = (rawInt & MASK_SIGN_FLOAT) >>> OFFSET_SIGN;
        exponent = (rawInt & MASK_EXPONENT_FLOAT) >>> OFFSET_EXPONENT;
        exponentReal = exponent - 127;
        mantissa = rawInt & MASK_MANTISSA_FLOAT;
        
        if (exponentReal > 0) {
            mantissaReal = (mantissa | MASK_HIDDEN_BIT) << (exponentReal);
        } else {
            mantissaReal = (mantissa | MASK_HIDDEN_BIT) >>> (-exponentReal);
        }
    }
    
    public static int floatNormPositiveToInt24Bit(float in) {
        // prevede float z rozmezi [0.0f-1.0f] na integer v rozmezi [0-8388608] ; 8388608 = 2^23
        int res = Float.floatToRawIntBits(in);
        return ((res & 0x007fffff) | 0x00800000) >>> (-(((res & 0x7f800000) >>> 23) - 127));
    }
    
    public static String intToBinnaryString32(int number) {
        String res = Integer.toBinaryString(number);
        int rest = 32 - res.length();
        for (int i=0; i<rest; i++) {
            res = "0" + res;
        }
        return res;
    }
    
    public static String BinaryFormatString(String binary, int length) {
        String res = "";
        int j = (32 - length) % 8;
        for (int i=binary.length() - length; i<binary.length(); i++) {
            if (j % 8 == 0) res += " ";
            res += binary.charAt(i);
            j++;
        }
        return res;
    }
    
    public int remapToInt(MFloat mfloat) {
        
        String binaryStr = MFloat.intToBinnaryString32(rawInt);
        
        String signString = "";
        String expString = "";
        String mantissaString = "";
        
        for (int i=0; i<binaryStr.length(); i++) {
            if (i==0) {
                signString = "" + binaryStr.charAt(i);
            } else if (i<9) {
                expString = expString + binaryStr.charAt(i);
            } else {
                mantissaString = mantissaString + binaryStr.charAt(i);
            }
        }
        
        int remapped = (mantissa | MASK_HIDDEN_BIT) >>> (23 - exponentReal);
        return remapped;
    }
    
    public int getIntval() {
        return mantissaReal;
    }
    
    @Override
    public String toString() {
        
        String patternFormat = "%+10.2f;";
        
        String strSign = BinaryFormatString(MFloat.intToBinnaryString32(sign), 1);
        String strExponent = BinaryFormatString(MFloat.intToBinnaryString32(exponent), 8);
        String strExponentReal = BinaryFormatString(MFloat.intToBinnaryString32(exponentReal), 23);
        String strMantissa = BinaryFormatString(MFloat.intToBinnaryString32(mantissa), 23);
        String strMantissaReal = BinaryFormatString(MFloat.intToBinnaryString32(mantissaReal), 32);
        
        String res = String.format("Num: %+12.6f [%s] ;;; [%s || %s || %s] ;;; [expR=%s] [manR=%s]", num, rawString, strSign, strExponent, strMantissa, exponentReal, strMantissaReal);
        
        return res;
    }
    
    
}
