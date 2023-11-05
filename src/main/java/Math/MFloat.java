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
    
    private float num;
    private int rawInt;
    private String rawString;
    
    public MFloat(float num) {
        this.num = num;
        this.rawInt = Float.floatToRawIntBits(this.num);
    }
    
    public static String intToBinnary(int number) {
    
        String res = Integer.toBinaryString(number);
        
        int rest = 32 - res.length();
        for (int i=0; i<rest; i++) {
            
            res = "0" + res;
        }
        return res;
    }
    
    public static String BinaryFormatString(String binary, int length) {
        String res = "";
        for (int i=0; i<binary.length(); i++) {
            
            res += binary.charAt(i);
            if (i%4==3) res += " ";
        }
        return res;
    }
    
    public int remapToInt(MFloat mfloat) {
        
        String binaryStr = MFloat.intToBinnary(rawInt);
        
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
        
        
        int sign = (rawInt & MASK_SIGN_FLOAT) >>> OFFSET_SIGN;
        int exponent = (rawInt & MASK_EXPONENT_FLOAT) >>> OFFSET_EXPONENT;
        int exponentReal = exponent - 127;
        int mantissa = rawInt & MASK_MANTISSA_FLOAT;
        
        int remapped = (mantissa | MASK_HIDDEN_BIT) >>> (23 - exponentReal);
        /*
        System.out.println("number: "+num);
        System.out.println("raw: "+binaryStr);
        System.out.println(signString+" ### "+expString+" ### "+mantissaString);
        
        System.out.println("signum  : "+BinaryFormatString(MFloat.intToBinnary(sign), 1));
        System.out.println("exponent: "+BinaryFormatString(MFloat.intToBinnary(exponent), 8));
        System.out.println("expReal : "+BinaryFormatString(MFloat.intToBinnary(exponentReal), 23)+ " ("+exponentReal+")");
        System.out.println("mantissa: "+BinaryFormatString(MFloat.intToBinnary(mantissa), 8));
        
        
        System.out.println("man posu: "+BinaryFormatString(MFloat.intToBinnary((mantissa | MASK_HIDDEN_BIT)) , 0));
        System.out.println("man posu: "+BinaryFormatString(MFloat.intToBinnary(remapped), 0));
        */
        return remapped;
    }
    
    @Override
    public String toString() {
        
        String res = "";
        
        
        
        return res;
    }
    
    
}
