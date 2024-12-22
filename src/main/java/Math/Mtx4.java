/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Math;

/**
 *
 * @author matej uzel
 */
public final class Mtx4 {
    
    private final double data[] = new double[16];
    
    public Mtx4(
        double x0, double y0, double z0, double w0, 
        double x1, double y1, double z1, double w1,
        double x2, double y2, double z2, double w2,
        double x3, double y3, double z3, double w3) {
        
        this.setData(
            x0, y0, z0, w0, 
            x1, y1, z1, w1, 
            x2, y2, z2, w2, 
            x3, y3, z3, w3
        );
    }
    
    public Mtx4() {
        this(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    }
    
    public Mtx4(Mtx4 m) {
        this.setData(m);
    }
    
    public Mtx4 setData(Mtx4 m) {
        
        for (int i=0; i<16; i++) {
            
            this.data[i] = m.getData(i);
        }
        return this;
    }
    
    public Mtx4 setData(
        double x0, double y0, double z0, double w0, 
        double x1, double y1, double z1, double w1,
        double x2, double y2, double z2, double w2,
        double x3, double y3, double z3, double w3) {
        
        this.data[0] = x0;
        this.data[1] = y0;
        this.data[2] = z0;
        this.data[3] = w0;
        
        this.data[4] = x1;
        this.data[5] = y1;
        this.data[6] = z1;
        this.data[7] = w1;
        
        this.data[8] = x2;
        this.data[9] = y2;
        this.data[10] = z2;
        this.data[11] = w2;
        
        this.data[12] = x3;
        this.data[13] = y3;
        this.data[14] = z3;
        this.data[15] = w3;
        
        return this;
    }
    
    
    public Mtx4 loadIdentity() {
        
        this.setData(
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
        );
        return this;
    }
    
    public Mtx4 loadProjectionPerspective(double left, double right, double bottom, double top, double near, double far) {
        
        double d_x = right - left;
        double d_y = top - bottom;
        double d_z = far - near;
        
        double sum_x = right + left;
        double sum_y = top + bottom;
        double sum_z = far + near;
        
        
        this.setData(
            2*near/d_x,       0.0d,  sum_x/d_x,            0.0d,
                  0.0d, 2*near/d_y,  sum_y/d_y,            0.0d,
                  0.0d,       0.0d, -sum_z/d_z, -2*far*near/d_z,
                  0.0d,       0.0d,       -1.0,            0.0d
        );
        return this;
    }
    
    public Mtx4 loadProjectionPerspective(double fovy, double aspect, double zNear, double zFar) {
        
        double f = Math.tan(2.0d/fovy);
        
        double dZ = zFar - zNear;
        double dSum = zNear + zFar;
        
        this.setData(
                f/aspect, 0,       0,                0,
                0,        f,       0,                0,
                0,        0, dSum/dZ, -2*zNear*zFar/dZ,
                0,        0,      -1,                0
        );
        
        /*
        this.setData(
                f/aspect, 0,0,0,
                0, f, 0,0,
                0, 0,-dZ/dSum,-2*zNear*zFar/dZ,
                0, 0,-1,0
        );*/
        
        return this;
    }
    
    public Mtx4 loadViewport(int width, int height, int offsetX, int offsetY) {
        
        double w_half = width / 2.0d;
        double h_half = height / 2.0d;
        
        this.setData(
            w_half,   0.0d, 0.0d, w_half + offsetX,
              0.0d, h_half, 0.0d, h_half + offsetY,
              0.0d,   0.0d, 1.0d,   0.0d,
              0.0d,   0.0d, 0.0d,   1.0d
        );
        return this;
    }
    
    public Mtx4 loadLookAt(Vec4 eye, Vec4 center, Vec4 up) {
        
        Vec4 zAxis = Vec4.subtract(center, eye).normal();
        Vec4 xAxis = Vec4.crossProduct(up, zAxis).normal();
        Vec4 yAxis = Vec4.crossProduct(zAxis, xAxis);
        
        //System.out.println("xAxis:"+xAxis);
        //System.out.println("yAxis"+yAxis);
        //System.out.println("zAxis: "+zAxis);
        
        this.setData(
                xAxis.getX(), xAxis.getY(), xAxis.getZ(), -Vec4.dotProduct(xAxis, eye),
                yAxis.getX(), yAxis.getY(), yAxis.getZ(), -Vec4.dotProduct(yAxis, eye),
                zAxis.getX(), zAxis.getY(), zAxis.getZ(), -Vec4.dotProduct(zAxis, eye),
                0.0d, 0.0d, 0.0d, 1.0d
        );
        
        return this;
    }
    
    public Mtx4 loadRotationX(double angle) {
        
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        
        this.setData(
            1.0d, 0.0d, 0.0d, 0.0d,
            0.0d, cosA, -sinA, 0.0d,
            0.0d, sinA, cosA, 0.0d,
            0.0d, 0.0d, 0.0d, 1.0d
        );
        return this;
    }
    
    public Mtx4 loadRotationY(double angle) {
        
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        
        this.setData(
            cosA, 0.0d, sinA, 0.0d,
            0.0d, 1.0d, 0.0d, 0.0d,
            -sinA, 0.0d, cosA, 0.0d,
            0.0d, 0.0d, 0.0d, 1.0d
        );
        return this;
    }
    
    public Mtx4 loadRotationZ(double angle) {
        
        double sinA = Math.sin(angle);
        double cosA = Math.cos(angle);
        
        this.setData(
            cosA, -sinA, 0.0d, 0.0d,
            sinA, cosA, 0.0d, 0.0d,
            0.0d, 0.0d, 1.0d, 0.0d,
            0.0d, 0.0d, 0.0d, 1.0d
        );
        return this;
    }
    
    public Mtx4 translate(double x, double y, double z) {
        
        this.data[3] += x;
        this.data[7] += y;
        this.data[11] += z;
        
        return this;
    }
    
    public static Mtx4 getProjectionPerspective(double left, double right, double bottom, double top, double near, double far) {
        Mtx4 mtx = new Mtx4();
        mtx.loadProjectionPerspective(left, right, bottom, top, near, far);
        return mtx;
    }
    
    public static Mtx4 getProjectionPerspective(double fovy, double aspect, double zNear, double zFar) {
        Mtx4 mtx = new Mtx4();
        mtx.loadProjectionPerspective(fovy, aspect, zNear, zFar);
        return mtx;
    }
    
    public static Mtx4 getViewport(int width, int height, int offsetX, int offsetY) {
        Mtx4 mtx = new Mtx4();
        mtx.loadViewport(width, height, offsetX, offsetY);
        return mtx;
    }
    
    public static Mtx4 getIdentity() {
        Mtx4 mtx = new Mtx4();
        mtx.loadIdentity();
        return mtx;
    }
    
    public static Mtx4 getRotationX(double angle) {
        Mtx4 mtx = new Mtx4();
        mtx.loadRotationX(angle);
        return mtx;
    }
    
    public static Mtx4 getRotationY(double angle) {
        Mtx4 mtx = new Mtx4();
        mtx.loadRotationY(angle);
        return mtx;
    }
    
    public static Mtx4 getRotationZ(double angle) {
        Mtx4 mtx = new Mtx4();
        mtx.loadRotationZ(angle);
        return mtx;
    }
    
    public Mtx4 orthonormalInvertion() {
        double offsetX = this.data[0]*this.data[3] + this.data[1]*this.data[7] + this.data[2]*this.data[11];
        double offsetY = this.data[4]*this.data[3] + this.data[5]*this.data[7] + this.data[6]*this.data[11];
        double offsetZ = this.data[8]*this.data[3] + this.data[9]*this.data[7] + this.data[10]*this.data[11];
        this.setData(
                this.data[0], this.data[4], this.data[8], -offsetX,
                this.data[1], this.data[5], this.data[9], -offsetY,
                this.data[2], this.data[6], this.data[10], -offsetZ,
                0.0d, 0.0d, 0.0d, 1.0d
        );
        return this;
    }
    
    public Mtx4 orthonormalInvertion2() {
        
        Vec4 offset = getColumn(3);
        this.setColumn(Vec4.ZERO_AFFINE_VERTEX, 3); // treti sloupec vynulujeme
        transpose();
        
        this.setData(
                this.data[0], this.data[4], this.data[8], -offset.getX(),
                this.data[1], this.data[5], this.data[9], -offset.getY(),
                this.data[2], this.data[6], this.data[10], -offset.getZ(),
                0.0d, 0.0d, 0.0d, 1.0d
        );
        
        throw new RuntimeException("Mtx4:orthonormalInvertion2() - neni dokonceno");
        
        //return this;
    }
    
    public Mtx4 transpose() {
        setData(
                data[0], data[4], data[8], data[12], 
                data[1], data[5], data[9], data[13], 
                data[2], data[6], data[10], data[14], 
                data[3], data[7], data[11], data[15]
        );
        return this;
    }
    
    public Mtx4 setColumn(Vec4 vec, int columnIndex) {
        if (columnIndex < 0 || columnIndex > 3) throw new UnsupportedOperationException("Mtx3:setColumn(columnIndex); columnIndex="+columnIndex+" mimo dovolene rozmezi.");
        data[columnIndex] = vec.getX();
        data[columnIndex+4] = vec.getY();
        data[columnIndex+8] = vec.getZ();
        data[columnIndex+12] = vec.getW();
        return this;
    }
    
    public Mtx4 setRow(Vec4 vec, int rowIndex) {
        if (rowIndex < 0 || rowIndex > 3) throw new UnsupportedOperationException("Mtx3:setColumn(rowIndex); rowIndex="+rowIndex+" mimo dovolene rozmezi.");
        int offset = rowIndex * 4;
        data[offset] = vec.getX();
        data[offset+1] = vec.getY();
        data[offset+2] = vec.getZ();
        data[offset+3] = vec.getW();
        return this;
    }
    
    public Vec4 getRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex > 3) throw new UnsupportedOperationException("Mtx3:getRow(rowIndex); rowIndex="+rowIndex+" mimo dovolene rozmezi.");
        int offset = rowIndex * 4;
        return new Vec4(
                data[offset], 
                data[offset+1], 
                data[offset+2], 
                data[offset+3]
        );
    }
    
    public Vec4 getColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex > 3) throw new UnsupportedOperationException("Mtx3:getColumn(columnIndex); columnIndex="+columnIndex+" mimo dovolene rozmezi.");
        return new Vec4(
                data[columnIndex], 
                data[columnIndex+4], 
                data[columnIndex+8], 
                data[columnIndex+12]
        );
    }
    
    public Mtx4 getOrthonormalInverted() {
        Mtx4 mtx = new Mtx4(this);
        mtx.orthonormalInvertion();
        return mtx;
    }
    
    public Mtx4 multiply(Mtx4 b) {
    
        Mtx4 a = new Mtx4(
            this.data[0],this.data[1],this.data[2],this.data[3],
            this.data[4],this.data[5],this.data[6],this.data[7],
            this.data[8],this.data[9],this.data[10],this.data[11],
            this.data[12],this.data[13],this.data[14],this.data[15]
        );
        
        for (int y=0; y<4; y++) {
            
            for (int x=0; x<4; x++) {
                
                int idxC = y*4+x;
                this.data[idxC] = 0.0d;
                
                for (int i=0; i<4; i++) {
                
                    // C[x,y] = sum (A[i,y] * B[x,i])
                    int idxA = y*4+i;
                    int idxB = i*4+x;
                    this.data[idxC] += a.data[idxA] * b.data[idxB];
                }
            }
        }
        return this;
    }
    
    public boolean isEqual(Mtx4 b, double epsilon) {
        for (int i=0; i<16; i++) {
            if (Math.abs(this.data[i] - b.data[i]) > epsilon) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEqual(Mtx4 b) {
        return this.isEqual(b, 10e-8);
    }
    
    public double getData(int idx) {
    
        return this.data[idx];
    }
    
    @Override
    public String toString() {
        
        String patternFormat = "%+10.2f;";
        String patternRow = patternFormat.repeat(4);
        
        return String.format("MATRIX 4x4 [\n"+patternRow+"\n"+patternRow+"\n"+patternRow+"\n"+patternRow+"\n]", 
                this.data[0], this.data[1], this.data[2], this.data[3],
                this.data[4], this.data[5], this.data[6], this.data[7],
                this.data[8], this.data[9], this.data[10], this.data[11],
                this.data[12], this.data[13], this.data[14], this.data[15]
        );
    }
    
    public Mtx4 stdOutPrintln(String name) {
        
        System.out.println(name+": "+this.toString());
        return this;
    }
    
    public Mtx4 stdOutPrintln() {
    
        System.out.println(this.toString());
        return this;
    }
}
