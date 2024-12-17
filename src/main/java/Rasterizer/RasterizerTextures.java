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
    protected double dyInvAC, dyInvAB, dyInvBC;
    
    // pomocne hodnoty - interpolace hloubky
    protected double zInvA, zInvB, zInvC;
    protected double dzInvAC, dzInvAB, dzInvBC;
    protected double kAC, kAB, kBC;
    
    
    public RasterizerTextures(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, TextureAbstract texture) {
        super(frameBuffer, depthBuffer, texture);
    }

    /**
     * seradi vertexy shora dolu v poradi A,B,C
     */
    public void sortVertices() {
        
        if (bY < aY) {
            swapAB();
        }
        if (cY < bY) {
            swapBC();
            if (bY < aY) {
                swapAB();
            }
        }
    }
    
    public void swapAB() {
        int tmp; double tmp2;
        tmp  = aX; aX = bX; bX = tmp;
        tmp  = aY; aY = bY; bY = tmp;
        tmp2 = aZ; aZ = bZ; bZ = tmp2;
        tmp2 = aU; aU = bU; bU = tmp2;
        tmp2 = aV; aV = bV; bV = tmp2;
    }
    
    public void swapBC() {
        int tmp; double tmp2;
        tmp  = bX; bX = cX; cX = tmp;
        tmp  = bY; bY = cY; cY = tmp;
        tmp2 = bZ; bZ = cZ; cZ = tmp2;
        tmp2 = bU; bU = cU; cU = tmp2;
        tmp2 = bV; bV = cV; cV = tmp2;
    }
    
    public void swapCA() {
        int tmp; double tmp2;
        tmp  = cX; cX = aX; aX = tmp;
        tmp  = cY; cY = aY; aY = tmp;
        tmp2 = cZ; cZ = aZ; aZ = tmp2;
        tmp2 = cU; cU = aU; aU = tmp2;
        tmp2 = cV; cV = aV; aV = tmp2;
    }
    
    public void preprocessTriangle() {
        
        dxAC = cX - aX;
        dxAB = bX - aX;
        dxBC = cX - bX;
        
        dyAC = cY - aY;
        dyAB = bY - aY;
        dyBC = cY - bY;
        
        dyInvAC = 1.0d / dyAC;
        dyInvAB = 1.0d / dyAB;
        dyInvBC = 1.0d / dyBC;
        
        crossAC = cY * aX - aY * cX;
        crossAB = bY * aX - aY * bX;
        crossBC = cY * bX - bY * cX;
    }
    
    public void preprocessDepthInterpolation() {
        
        zInvA = 1.0d / aZ;
        zInvB = 1.0d / bZ;
        zInvC = 1.0d / cZ;
        
        dzInvAC = zInvC - zInvA;
        dzInvAB = zInvB - zInvA;
        dzInvBC = zInvC - zInvB;
        
        kAC = 0;
        kAB = 0;
        kBC = 0;
    }
    
    @Override
    public void drawTriangle() {
        
        sortVertices();
        preprocessTriangle();
        preprocessDepthInterpolation();
        
        int xAC = 0, xAB = 0, xBC = 0;
        
        double aUzInvA = this.aU*zInvA;
        double bUzInvB = this.bU*zInvB;
        double cUzInvC = this.cU*zInvC;
        
        double aVzInvA = this.aV*zInvA;
        double bVzInvB = this.bV*zInvB;
        double cVzInvC = this.cV*zInvC;
        
        double dACuzInv = cUzInvC - aUzInvA;
        double dABuzInv = bUzInvB - aUzInvA;
        double dBCuzInv = cUzInvC - bUzInvB;
        
        double dACvzInv = cVzInvC - aVzInvA;
        double dABvzInv = bVzInvB - aVzInvA;
        double dBCvzInv = cVzInvC - bVzInvB;
        
        // horni cast trojuhelniku
        for (int line = aY; line < bY; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            xAB = (int) Math.round((crossAB + line * dxAB) * dyInvAB);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zAB_k = 1.0d / (zInvA + kAB * dzInvAB);
            
            // interpolace tex coord
            double uAC_k = (aUzInvA + kAC * dACuzInv) * zAC_k;
            double uAB_k = (aUzInvA + kAB * dABuzInv) * zAB_k;
            double vAC_k = (aVzInvA + kAC * dACvzInv) * zAC_k;
            double vAB_k = (aVzInvA + kAB * dABvzInv) * zAB_k;
            
            scanLine(xAC, xAB, line, zAC_k, zAB_k, uAC_k, uAB_k, vAC_k, vAB_k);
            
            kAC += dyInvAC;
            kAB += dyInvAB;
        }
        
        // dolni cast trojuhelniku
        for (int line = bY; line < cY; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            xBC = (int) Math.round((crossBC + line * dxBC) * dyInvBC);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zBC_k = 1.0d / (zInvB + kBC * dzInvBC);
            
            // interpolace tex coord
            double uAC_k = (aUzInvA + kAC * dACuzInv) * zAC_k;
            double uBC_k = (bUzInvB + kBC * dBCuzInv) * zBC_k;
            double vAC_k = (aVzInvA + kAC * dACvzInv) * zAC_k;
            double vBC_k = (bVzInvB + kBC * dBCvzInv) * zBC_k;
            
            scanLine(xAC, xBC, line, zAC_k, zBC_k, uAC_k, uBC_k, vAC_k, vBC_k);
            
            kAC += dyInvAC;
            kBC += dyInvBC;
        }
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
            
            double u_k = ( u0z0Inv + k * duzInv) * z_k;
            double v_k = ( v0z0Inv + k * dvzInv) * z_k;
            
            if (depthBuffer.write(x, y, z_k)) {
                
                Color color = this.texture.getColor(u_k, v_k);
                frameBuffer.putPixel(x, y, color.getR(),color.getG(),color.getB());
            }
            x++;
            k += dxInv;
        }
    }
    
}
