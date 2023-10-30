/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Math;

/**
 *
 * @author matej uzel
 */
public class Vec4 {
    
    private final double data[] = new double[4];
    
    public Vec4(double x, double y, double z, double w) {
        
        data[0] = x;
        data[1] = y;
        data[2] = z;
        data[3] = w;
    }
    
    public Vec4(double x, double y, double z) {
    
        data[0] = x;
        data[1] = y;
        data[2] = z;
        data[3] = 1;
    }
    
    public Vec4(Vec4 v) {
        
        data[0] = v.data[0];
        data[1] = v.data[1];
        data[2] = v.data[2];
        data[3] = v.data[3];
    }
    
    public Vec4 setData(Vec4 vec) {
        data[0] = vec.data[0];
        data[1] = vec.data[1];
        data[2] = vec.data[2];
        data[3] = vec.data[3];
        return this;
    }
    
    public Vec4 divideByW() {
        this.data[0] /= this.data[3];
        this.data[1] /= this.data[3];
        this.data[2] /= this.data[3];
        this.data[3] = 1.0d;
        return this;
    }
    
    public Vec4 add(Vec4 vec) {
        this.data[0] += vec.data[0];
        this.data[1] += vec.data[1];
        this.data[2] += vec.data[2];
        this.data[3] += vec.data[3];
        return this;
    }
    
    public Vec4 scale(double k) {
        this.data[0] *= k;
        this.data[1] *= k;
        this.data[2] *= k;
        this.data[3] *= k;
        return this;
    }
    
    public Vec4 transform(Mtx4 m) {
        
        double x = m.getData(0)*this.data[0] + m.getData(1)*this.data[1] + m.getData(2)*this.data[2] + m.getData(3)*this.data[3];
        double y = m.getData(4)*this.data[0] + m.getData(5)*this.data[1] + m.getData(6)*this.data[2] + m.getData(7)*this.data[3];
        double z = m.getData(8)*this.data[0] + m.getData(9)*this.data[1] + m.getData(10)*this.data[2] + m.getData(11)*this.data[3];
        double w = m.getData(12)*this.data[0] + m.getData(13)*this.data[1] + m.getData(14)*this.data[2] + m.getData(15)*this.data[3];
        
        this.data[0] = x;
        this.data[1] = y;
        this.data[2] = z;
        this.data[3] = w;
        
        return this;
    }
    
    public double getX() {
        return this.data[0];
    }
    public double getY() {
        return this.data[1];
    }
    public double getZ() {
        return this.data[2];
    }
    public double getW() {
        return this.data[3];
    }
    
    @Override
    public String toString() {
        
        String patternFormat = "%+10.2f;\n";
        String patternRow = patternFormat.repeat(4);
        
        return String.format("VECTOR 4x4 [\n"+patternRow+"]", 
                this.data[0], this.data[1], this.data[2], this.data[3]
        );
        /*
        return "VECTOR 4 [\n"
            +this.data[0]+" \n"
            +this.data[1]+" \n"
            +this.data[2]+" \n"
            +this.data[3]+" \n"
            +"]";*/
    }
    
    public Vec4 stdOutPrintln(String name) {
        
        System.out.println(name+": "+this.toString());
        return this;
    }
    
    public Vec4 stdOutPrintln() {
    
        System.out.println(this.toString());
        return this;
    }
    
}
