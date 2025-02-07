/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.FrameBuffer;
import Math.Vec4;
import Shader.FragmentShader;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public abstract class RasterizerAbstract {
    
    public boolean debug = false;
    
    protected FrameBuffer frameBuffer;
    protected DepthBufferAbstract depthBuffer; // prizpusobit datovy typ
    
    FragmentShader fragmentShader;
    
    // vertex coordinates
    int xA, yA;
    int xB, yB;
    int xC, yC;
    
    int idxA = 0, idxB = 1, idxC = 2;
    
    double[][] atrs;
    /*
    double[][] atrs = {
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d},
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d},
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d}
    };*/
    
    // z coordinates
    double aZ, bZ, cZ;
    
    // colors
    int aR, aG, aB;
    int bR, bG, bB;
    int cR, cG, cB;
    
    double z_faktor, z_offset;
    
    //double lightX, lightY, lightZ;
    
    protected int atrsCount = 5;
    
    protected boolean linesFlag = true;
    
    public RasterizerAbstract(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, FragmentShader fragmentShader, int atrsCount, boolean linesFlag) {
        this.frameBuffer = frameBuffer;
        this.depthBuffer = depthBuffer;
        this.fragmentShader = fragmentShader;
        this.atrsCount = atrsCount;
        this.linesFlag = linesFlag;
        
        this.atrs = new double[3][atrsCount];
        
    }
    
    public abstract void drawTriangle();
    
    public void setParams(
                    double ax, double ay, double az,
                    double bx, double by, double bz,
                    double cx, double cy, double cz,
                    
                    double lightX, double lightY, double lightZ,
                    
                    double z_faktor, double z_offset,
                    
                    double[] atrsA, double[] atrsB, double[] atrsC
                    ) {
        
        if (debug) {
            
            int rndDigits = 8;
            int rndPrecision = 2;
            String fF = "%+"+rndDigits+"."+rndPrecision+"f";
            String fF0 = "%."+rndDigits+"f";
            String fTriple = fF + "; " + fF + "; " + fF;

            System.out.println(
                    String.format(
                            "RASTERIZER\n A  ["+fTriple+"]\n B  ["+fTriple+"]\n C  ["+fTriple+"]\n lig["+fTriple+"]\n zFaktor: "+fF0+"\n zOffset: "+fF0, 
                            ax, ay, az, 
                            bx, by, bz, 
                            cx, cy, cz, 
                            lightX, lightY, lightZ,
                            z_faktor, z_offset
                    )
            );
            
            System.out.println(" Attributes:");
            for (int i=0; i<atrsCount; i++) {
                System.out.print(" (");
                System.out.print(String.format(fF + "; ", atrsA[i]));
                System.out.print(String.format(fF + "; ", atrsB[i]));
                System.out.print(String.format(fF + "", atrsC[i]));
                System.out.println(")");
            }
            System.out.println("");
        }
        
        
        idxA = 0; idxB = 1; idxC = 2;
        
        this.xA = (int) ax;
        this.yA = (int) ay;
        this.aZ = az;
        
        this.xB = (int) bx;
        this.yB = (int) by;
        this.bZ = bz;
        
        this.xC = (int) cx;
        this.yC = (int) cy;
        this.cZ = cz;
        
        fragmentShader.setLight(new Vec4(lightX, lightY, -lightZ, 0));
        /*
        this.lightX = lightX;
        this.lightY = lightY;
        this.lightZ = lightZ;
        */
        
        this.z_faktor = z_faktor;
        this.z_offset = z_offset;
        
        this.atrs[idxA][0] = atrsA[0];
        this.atrs[idxA][1] = atrsA[1];
        this.atrs[idxA][2] = atrsA[2];
        this.atrs[idxA][3] = atrsA[3];
        this.atrs[idxA][4] = atrsA[4];
        
        this.atrs[idxB][0] = atrsB[0];
        this.atrs[idxB][1] = atrsB[1];
        this.atrs[idxB][2] = atrsB[2];
        this.atrs[idxB][3] = atrsB[3];
        this.atrs[idxB][4] = atrsB[4];
        
        this.atrs[idxC][0] = atrsC[0];
        this.atrs[idxC][1] = atrsC[1];
        this.atrs[idxC][2] = atrsC[2];
        this.atrs[idxC][3] = atrsC[3];
        this.atrs[idxC][4] = atrsC[4];
    }
    
    /**
     * seradi vertexy shora dolu v poradi A,B,C
     */
    public void sortVertices() {
        
        if (yB < yA) {
            swapAB();
        }
        if (yC < yB) {
            swapBC();
            if (yB < yA) {
                swapAB();
            }
        }
    }
    
    public void swapAB() {
        int tmp; double tmp2;
        tmp  = xA; xA = xB; xB = tmp;
        tmp  = yA; yA = yB; yB = tmp;
        tmp2 = aZ; aZ = bZ; bZ = tmp2;
        
        tmp = idxA; idxA = idxB; idxB = tmp;
    }
    
    public void swapBC() {
        int tmp; double tmp2;
        tmp  = xB; xB = xC; xC = tmp;
        tmp  = yB; yB = yC; yC = tmp;
        tmp2 = bZ; bZ = cZ; cZ = tmp2;
        
        tmp = idxB; idxB = idxC; idxC = tmp;
    }
    
    public void setVertexCoordinates(Vec4 a, Vec4 b, Vec4 c) {
        xA = (int) a.getX();
        yA = (int) a.getY();
        xB = (int) b.getX();
        yB = (int) b.getY();
        xC = (int) c.getX();
        yC = (int) c.getY();
        
        aZ = a.getZ();
        bZ = b.getZ();
        cZ = c.getZ();
    }
    
    public void setTextureCoordinates(Vec4 a, Vec4 b, Vec4 c) {
        
        atrs[idxA][0] = (int) a.getX();
        atrs[idxA][1] = (int) a.getY();
        
        atrs[idxB][0] = (int) b.getX();
        atrs[idxB][1] = (int) b.getY();
        
        atrs[idxC][0] = (int) c.getX();
        atrs[idxC][1] = (int) c.getY();
    }
    
    public void setDepthValues(double a, double b, double c) {
        aZ = a;
        bZ = b;
        cZ = c;
    }
    
    public void setColorA(int r, int g, int b) {
        aR = r;
        aG = g;
        aB = b;
    }
    public void setColorB(int r, int g, int b) {
        bR = r;
        bG = g;
        bB = b;
    }
    public void setColorC(int r, int g, int b) {
        cR = r;
        cG = g;
        cB = b;
    }
    public void setAtrsA(int idx, double value) {
        atrs[idxA][idx] = value;
    }
    public void setAtrsB(int idx, double value) {
        atrs[idxB][idx] = value;
    }
    public void setAtrsC(int idx, double value) {
        atrs[idxC][idx] = value;
    }
    

    public FragmentShader getFragmentShader() {
        return fragmentShader;
    }
    
    public void setFragmentShader(FragmentShader fragmentShader) {
        this.fragmentShader = fragmentShader;
    }
    
}
