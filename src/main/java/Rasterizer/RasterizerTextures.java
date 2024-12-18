/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.Color;
import GL.DepthBuffer.DepthBufferAbstract;
import GL.FrameBuffer;
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
    
    double uzAInv, uzBInv, uzCInv;
    double vzAInv, vzBInv, vzCInv;
    double duzACInv, duzABInv, duzBCInv;
    double dvzACInv, dvzABInv, dvzBCInv;
    double zAC_k, zAB_k, zBC_k, uAC_k, uAB_k, uBC_k, vAC_k, vAB_k, vBC_k;
    
    
    public RasterizerTextures(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, TextureAbstract texture) {
        super(frameBuffer, depthBuffer, texture);
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
        tmp2 = aU; aU = bU; bU = tmp2;
        tmp2 = aV; aV = bV; bV = tmp2;
    }
    
    public void swapBC() {
        int tmp; double tmp2;
        tmp  = xB; xB = xC; xC = tmp;
        tmp  = yB; yB = yC; yC = tmp;
        tmp2 = bZ; bZ = cZ; cZ = tmp2;
        tmp2 = bU; bU = cU; cU = tmp2;
        tmp2 = bV; bV = cV; cV = tmp2;
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
        
        uzAInv = aU * zAInv;
        uzBInv = bU * zBInv;
        uzCInv = cU * zCInv;
        
        vzAInv = aV * zAInv;
        vzBInv = bV * zBInv;
        vzCInv = cV * zCInv;
        
        duzACInv = uzCInv - uzAInv;
        duzABInv = uzBInv - uzAInv;
        duzBCInv = uzCInv - uzBInv;
        
        dvzACInv = vzCInv - vzAInv;
        dvzABInv = vzBInv - vzAInv;
        dvzBCInv = vzCInv - vzBInv;
        
        kAC = scanEdge(
            yA, yB, 
            crossAC, crossAB, dxAC, dxAB, dyACInv, dyABInv,
            zAInv, dzACInv, zAInv, dzABInv,
            uzAInv, duzACInv, uzAInv, duzABInv, 
            vzAInv, dvzACInv, vzAInv, dvzABInv,
            0
        );
        
        scanEdge(
            yB, yC, 
            crossAC, crossBC, dxAC, dxBC, dyACInv, dyBCInv,
            zAInv, dzACInv, zBInv, dzBCInv,
            uzAInv, duzACInv, uzBInv, duzBCInv, 
            vzAInv, dvzACInv, vzBInv, dvzBCInv, 
            kAC
        );
    }
    
    public double scanEdge(
                    int lineL, int lineR, 
                    double crossL, double crossR, double dxL, double dxR, double dyInvL, double dyInvR,
                    double zL0, double dzL, double zR0, double dzR,
                    double uL0, double duL, double uR0, double duR, 
                    double vL0, double dvL, double vR0, double dvR,
                    double kL) {
    
        double kR = 0;
        
        for (int line = lineL; line < lineR; line++) {
            
            int xL = (int) Math.round((crossL + line * dxL) * dyInvL);
            int xR = (int) Math.round((crossR + line * dxR) * dyInvR);
            
            double zL_k = 1.0d / (zL0 + kL * dzL);
            double zR_k = 1.0d / (zR0 + kR * dzR);
            
            double uL_k = (uL0 + kL * duL) * zL_k;
            double uR_k = (uR0 + kR * duR) * zR_k;
            double vL_k = (vL0 + kL * dvL) * zL_k;
            double vR_k = (vR0 + kR * dvR) * zR_k;
            
            scanLine(xL, xR, line, zL_k, zR_k, uL_k, uR_k, vL_k, vR_k);
            
            kL += dyInvL;
            kR += dyInvR;
        }
        return kL;
    }
    
    public void scanLine(int x0, int x1, int y, double z0, double z1, double u0, double u1, double v0, double v1) {
        
        if (x0 > x1) {
            scanLine(x1, x0, y, z1, z0, u1, u0, v1, v0);
            return;
        }
        
        int dx = x1 - x0;
        double dxInv = 1.0d / (double) dx;
        double z0Inv = 1.0d / z0;
        double z1Inv = 1.0d / z1;
        double dzInv = z1Inv - z0Inv;
        double k = 0;
        double z_k;
        
        double u0z0Inv = u0*z0Inv;
        double u1z1Inv = u1*z1Inv;
        double v0z0Inv = v0*z0Inv;
        double v1z1Inv = v1*z1Inv;
        double duzInv = u1z1Inv - u0z0Inv;
        double dvzInv = v1z1Inv - v0z0Inv;
        
        int x = x0;
        for (int i=0; i<dx + 1; i++) {
            
            z_k = 1.0d / (z0Inv + k * dzInv);
            
            if (depthBuffer.write(x, y, z_k)) {
                
                double u_k = ( u0z0Inv + k * duzInv) * z_k;
                double v_k = ( v0z0Inv + k * dvzInv) * z_k;
                
                Color color = this.texture.getColor(u_k, v_k);
                frameBuffer.putPixel(x, y, color.getR(),color.getG(),color.getB());
            }
            x++;
            k += dxInv;
        }
    }
    
}
