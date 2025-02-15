/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Math;

import Geometry.Vertex;

/**
 *
 * @author matej uzel
 */




public class Utils {
    
    public static int MASK_MANTISSA_FLOAT = 0x007fffff; // 31
    public static int MASK_EXPONENT_FLOAT = 0x7f800000; // 30-23
    public static int MASK_SIGN_FLOAT = 0x80000000; // 22-0
    
    
    public static double clamp(double value, double valueMin, double valueMax, double clampMin, double clampMax) {
        return (clampMax - clampMin) * (value - valueMin) / (valueMax - valueMin) + clampMin;
    }
    
    public static double dotProduct(Vec4 a, Vec4 b) {
        return a.getX()*b.getX() + a.getY()*b.getY() + a.getZ()*b.getZ() + a.getW()*b.getW();
    }
    
    public static double interpolate(double a0, double a1, double k) {
        return a0 + k * (a1 - a0);
    }
    
    public static Vec4 interpolate(Vec4 a0, Vec4 a1, double k) {
        Vec4 ak = new Vec4(
            a0.getX() + k * (a1.getX() - a0.getX()),
            a0.getY() + k * (a1.getY() - a0.getY()),
            a0.getZ() + k * (a1.getZ() - a0.getZ()),
            a0.getW() + k * (a1.getW() - a0.getW())
        );
        ak.normal();
        return ak;
    }
    
    
    
    
    
    
    
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
    
    
    public static Vec4 cutRight(Vec4 A, Vec4 B) {
        
        double k = (A.getW() - A.getX()) / ((B.getX() - A.getX()) - (B.getW() - A.getW()));
        
        return new Vec4(
                A.getX() + k*(B.getX() - A.getX()),
                A.getY() + k*(B.getY() - A.getY()),
                A.getZ() + k*(B.getZ() - A.getZ()),
                A.getW() + k*(B.getW() - A.getW())
        );
    }
    
    public static Vertex cutVertexRight(Vertex a, Vertex b) {
        
        double k = (a.getVertex().getW() - a.getVertex().getX()) / ((b.getVertex().getX() - a.getVertex().getX()) - (b.getVertex().getW() - a.getVertex().getW()));
        
        return new Vertex(
            new Vec4(
                a.getVertex().getX() + k * (b.getVertex().getX() - a.getVertex().getX()),
                a.getVertex().getY() + k * (b.getVertex().getY() - a.getVertex().getY()),
                a.getVertex().getZ() + k * (b.getVertex().getZ() - a.getVertex().getZ()),
                a.getVertex().getW() + k * (b.getVertex().getW() - a.getVertex().getW())
            )   
        );
    }
    
}
