/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.Color;
import GL.DepthBuffer.DepthBufferAbstract;
import GL.FrameBuffer;
import Math.Utils;
import Math.Vec4;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public class RasterizerTextures extends RasterizerAbstract {
    
    // pomocne hodnoty - rasterizace
    protected int dxAC, dxAB, dxBC;
    protected int dyAC, dyAB, dyBC;
    protected int crossAC, crossAB, crossBC;
    protected double dyACInv, dyABInv, dyBCInv;
    
    // pomocne hodnoty - interpolace hloubky
    protected double zAInv, zBInv, zCInv;
    protected double dzACInv, dzABInv, dzBCInv;
    protected double kAC;
    
    protected double[] atrs_zAInv, atrs_zBInv, atrs_zCInv;
    protected double[] atrs_dzACInv, atrs_dzABInv, atrs_dzBCInv;
    protected double[] atrs_L_k, atrs_R_k;
    protected double[] atrs_0inv, atrs_k;
    
    protected int step;
    
    public RasterizerTextures(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, TextureAbstract texture, int atrsCount, boolean linesFlag) {
        super(frameBuffer, depthBuffer, texture, atrsCount, linesFlag);
        
        this.atrs_zAInv = new double[this.atrsCount];
        this.atrs_zBInv = new double[this.atrsCount];
        this.atrs_zCInv = new double[this.atrsCount];
        
        this.atrs_dzABInv = new double[this.atrsCount];
        this.atrs_dzBCInv = new double[this.atrsCount];
        this.atrs_dzACInv = new double[this.atrsCount];
        
        this.atrs_L_k = new double[this.atrsCount];
        this.atrs_R_k = new double[this.atrsCount];
        
        this.atrs_0inv = new double[this.atrsCount];
        this.atrs_k = new double[this.atrsCount];
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
    
    @Override
    public void drawTriangle() {
        
        sortVertices();
        
        dxAC = xC - xA;
        dxAB = xB - xA;
        dxBC = xC - xB;
        
        dyAC = yC - yA;
        dyAB = yB - yA;
        dyBC = yC - yB;
        
        dyACInv = 1.0d / dyAC;
        dyABInv = 1.0d / dyAB;
        dyBCInv = 1.0d / dyBC;
        
        crossAC = yC * xA - yA * xC;
        crossAB = yB * xA - yA * xB;
        crossBC = yC * xB - yB * xC;
        
        zAInv = 1.0d / aZ;
        zBInv = 1.0d / bZ;
        zCInv = 1.0d / cZ;
        
        dzACInv = zCInv - zAInv;
        dzABInv = zBInv - zAInv;
        dzBCInv = zCInv - zBInv;
        
        // podle levotocivosti/pravotocivosti trojuhelniku rozhodneme, jestli Scan Line bude probihat zleva doprava nebo zprava doleva
        step = ((dxAB * dyAC - dyAB * dxAC) > 0) ? 1 : -1;
        
        for (int i=0; i<this.atrsCount; i++) {
            atrs_zAInv[i] = atrs[idxA][i] * zAInv;
            atrs_zBInv[i] = atrs[idxB][i] * zBInv;
            atrs_zCInv[i] = atrs[idxC][i] * zCInv;
            
            atrs_dzACInv[i] = atrs_zCInv[i] - atrs_zAInv[i];
            atrs_dzABInv[i] = atrs_zBInv[i] - atrs_zAInv[i];
            atrs_dzBCInv[i] = atrs_zCInv[i] - atrs_zBInv[i];
        }
        
        kAC = scanEdge(
            yA, yB, 
            crossAC, crossAB, dxAC, dxAB, dyACInv, dyABInv,
            zAInv, dzACInv, zAInv, dzABInv,
            atrs_zAInv, atrs_dzACInv, atrs_zAInv, atrs_dzABInv,
            0
        );
        
        scanEdge(
            yB, yC, 
            crossAC, crossBC, dxAC, dxBC, dyACInv, dyBCInv,
            zAInv, dzACInv, zBInv, dzBCInv,
            atrs_zAInv, atrs_dzACInv, atrs_zBInv, atrs_dzBCInv,
            kAC
        );
    }
    
    public double scanEdge(
                    int lineL, int lineR, 
                    double crossL, double crossR, double dxL, double dxR, double dyInvL, double dyInvR,
                    double zL0, double dzL, double zR0, double dzR,
                    double[] atrs_L0, double[] atrs_dL, double[] atrs_R0, double[] atrs_dR,
                    double kL) {
        
        
        int xL, xR;
        double kR, zL_k, zR_k;
        
        kR = 0;
        
        for (int line = lineL; line < lineR; line++) {
            
            xL = (int) Math.round((crossL + line * dxL) * dyInvL);
            xR = (int) Math.round((crossR + line * dxR) * dyInvR);
            
            zL_k = 1.0d / (zL0 + kL * dzL);
            zR_k = 1.0d / (zR0 + kR * dzR);
            
            for (int i=0; i<this.atrsCount; i++) {
                atrs_L_k[i] = (atrs_L0[i] + kL * atrs_dL[i]) * zL_k;
                atrs_R_k[i] = (atrs_R0[i] + kR * atrs_dR[i]) * zR_k;
            }
            
            scanLine(xL, xR, line, zL_k, zR_k, atrs_L_k, atrs_R_k);
            
            kL += dyInvL;
            kR += dyInvR;
        }
        return kL;
    }
    
    public void scanLine(int x0, int x1, int y, double z0, double z1, double[] atrs_0, double[] atrs_1) {
       
        if (linesFlag) {
            this.fragmentShader(x0, y, z0, atrs_0);
            this.fragmentShader(x1, y, z1, atrs_1);
            return;
        }
        
        int dx = step * (x1 - x0);
        double dxInv = 1.0d / (double) dx;
        double z0Inv = 1.0d / z0;
        double z1Inv = 1.0d / z1;
        double dzInv = z1Inv - z0Inv;
        double k = 0;
        double z_k;
        int x = x0;
        
        for (int i=0; i<this.atrsCount; i++) {
            atrs_0inv[i] = atrs_0[i] * z0Inv;
        }
        
        
        for (int j=0; j<=dx; j++) {
        
            double z_k_inv = z0Inv + k * dzInv;
            double z_k_norm = z_k_inv * z_faktor + z_offset;
            z_k = 1.0d / z_k_inv;
            
            if (depthBuffer.write(x, y, z_k)) {
                
                for (int i=0; i<this.atrsCount; i++) {
                    atrs_k[i] = (atrs_0inv[i] + k * (atrs_1[i] * z1Inv - atrs_0inv[i])) * z_k;
                }
                
                this.fragmentShader(x, y, z_k_norm, atrs_k);
            }
            
            x += step;
            k += dxInv;
        }
    }
    
    
    public void fragmentShader(int x, int y, double zNorm, double[] atrs) {
    
        int r,g,b;
        
        Color color = this.texture.getColor(atrs[0], atrs[1]);
            
        r = color.getR();
        g = color.getG();
        b = color.getB();
        
        Vec4 light = new Vec4(lightX, lightY, lightZ, 0.0d);
        Vec4 normal = new Vec4(atrs[2], atrs[3], atrs[4], 0.0d).normal();
        
        double dot = Utils.dotProduct(light, normal);
        
        double dotFaktor = Utils.clamp(dot, -1.0d, 1.0d, 0.2d, 1.0d);
        r *= dotFaktor;
        g *= dotFaktor;
        b *= dotFaktor;
        
        /*
        x += Math.round((2*Math.random()-1.0d) * dotFaktor * 5);
        y += Math.round((2*Math.random()-1.0d) * dotFaktor * 5);
        //*/
        
        frameBuffer.putPixel(x, y, r, g, b);
    }
    
}
