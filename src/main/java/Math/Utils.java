/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Math;

/**
 *
 * @author matej uzel
 */




public class Utils {
    
    public static int MASK_MANTISSA_FLOAT = 0x007fffff; // 31
    public static int MASK_EXPONENT_FLOAT = 0x7f800000; // 30-23
    public static int MASK_SIGN_FLOAT = 0x80000000; // 22-0
    
    public static int remapFloatToInt8Bit(float value, float minVal, float maxVal) {
        // Step 1: Scale the float to the range [0.0, 1.0]
        float scaledValue = (value - minVal) / (maxVal - minVal);

        // Step 2: Multiply the scaled float by 255 using bitwise operations
        int floatBits = Float.floatToRawIntBits(scaledValue);
        int remappedValue = (floatBits & 0x7FFFFF) >> 23; // Extract the significand (mantissa) bits
        remappedValue -= 127; // Adjust for the exponent bias (127 in single-precision)

        // Step 3: Ensure the result is within the range [0, 255] using bitwise operations
        remappedValue = Math.max(0, Math.min(255, remappedValue));

        return remappedValue;
    }
    
    public static float multiplyByPowerOfTwo(float number, int exponent) {
        
        int raw = Float.floatToRawIntBits(number);
        raw += (exponent << 23);
        return Float.intBitsToFloat(raw);
    }
    
    public static int floatToByte(float number) {
        
        number = Utils.multiplyByPowerOfTwo(number, 7);
        
        int raw = Float.floatToRawIntBits(number);
        
        int res = (((raw | 0x00800000) & 0x00ffffff)  >> 16 );
        
        System.out.println(Integer.toBinaryString(raw));
        System.out.println(Integer.toBinaryString(res));
        
        return (res);
        
        
    }
    
    
    
    public static void test(float a) {
        
        
        
        
        
        
        
        int bits = Float.floatToRawIntBits(a);
        int mantissa = MASK_MANTISSA_FLOAT & bits;
        int exponent = (MASK_EXPONENT_FLOAT & bits) >>> 23;
        int sign = (MASK_SIGN_FLOAT & bits) >>> 31;
        
        int expDecoded = exponent - 127;
        int mantissaDecoded; 
        
        if (expDecoded > 0) {
            
            mantissaDecoded = mantissa >>> expDecoded;
        } else if (expDecoded < 0) {
        
            mantissaDecoded = (mantissa << -expDecoded) & MASK_MANTISSA_FLOAT;
        } else {
            mantissaDecoded = mantissa;
        }
        
        System.out.println("ori: "+a);
        System.out.println("man: "+Integer.toBinaryString(mantissa) + " ; man2: " + Integer.toBinaryString(mantissaDecoded) +"");
        System.out.println("exp: "+Integer.toBinaryString(exponent) + "("+exponent+") ; exp2: " + Integer.toBinaryString(expDecoded) +" ("+expDecoded+")");
        System.out.println("sig: "+Integer.toBinaryString(sign));
        
        System.out.println("===============");
    }
    
    
}
