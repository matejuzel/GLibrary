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
        
        // horni cast trojuhelniku
        for (int line = aY; line < bY; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            int xAB = (int) Math.round((crossAB + line * dxAB) * dyInvAB);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zAB_k = 1.0d / (zInvA + kAB * dzInvAB);
            
            // interpolace tex coord u
            double uAC_k = ( this.aU*zInvA + kAC * (this.cU*zInvC - this.aU*zInvA)) * zAC_k;
            double uAB_k = ( this.aU*zInvA + kAB * (this.bU*zInvB - this.aU*zInvA)) * zAB_k;
            // interpolace tex coord v
            double vAC_k = ( this.aV*zInvA + kAC * (this.cV*zInvC - this.aV*zInvA)) * zAC_k;
            double vAB_k = ( this.aV*zInvA + kAB * (this.bV*zInvB - this.aV*zInvA)) * zAB_k;
            
            scanLine(xAC, xAB, line, zAC_k, zAB_k, uAC_k, uAB_k, vAC_k, vAB_k, aR, aG, aB);
            
            kAC += dyInvAC;
            kAB += dyInvAB;
        }
        
        if (this.debug) System.out.println("drawTriangle() - part 2");
        
        // dolni cast trojuhelniku
        for (int line = bY; line < cY; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            int xBC = (int) Math.round((crossBC + line * dxBC) * dyInvBC);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zBC_k = 1.0d / (zInvB + kBC * dzInvBC);
            
            // interpolace tex coord u
            double uAC_k = ( this.aU*zInvA + kAC * (this.cU*zInvC - this.aU*zInvA)) * zAC_k;
            double uBC_k = ( this.bU*zInvB + kBC * (this.cU*zInvC - this.bU*zInvB)) * zBC_k;
            
            // interpolace tex coord v
            double vAC_k = ( this.aV*zInvA + kAC * (this.cV*zInvC - this.aV*zInvA)) * zAC_k;
            double vBC_k = ( this.bV*zInvB + kBC * (this.cV*zInvC - this.bV*zInvB)) * zBC_k;
            
            scanLine(xAC, xBC, line, zAC_k, zBC_k, uAC_k, uBC_k, vAC_k, vBC_k, aR, aG, aB);
            
            kAC += dyInvAC;
            kBC += dyInvBC;
        }
        
        if (this.debug) System.out.println("");
        
        
        if (this.debug) {
            
            this.frameBuffer.putPixel(this.aX, this.aY, 255, 0, 0, 6);
            this.frameBuffer.putPixel(this.bX, this.bY, 0, 255, 0, 6);
            this.frameBuffer.putPixel(this.cX, this.cY, 0, 0, 255, 6);
        }
        
        
    }
    
    public void scanLine(int x0, int x1, int y, double z0, double z1, double u0, double u1, double v0, double v1, int r, int g, int b) {
        
        if (x0 > x1) {
            scanLine(x1, x0, y, z1, z0, u1, u0, v1, v0, r, g, b);
            return;
        }
        
        int dx = x1 - x0;
        double dxInv = 1.0d / (double) dx;
        double z0Inv = 1.0d / z0;
        double z1Inv = 1.0d / z1;
        double dzInv = z1Inv - z0Inv;
        double k = 0;
        
        double z_k;
        
        
        int x = x0;
        for (int i=0; i<dx + 1; i++) {
            
            z_k = 1.0d / (z0Inv + k * dzInv);
            
            double u_k = ( u0*z0Inv + k * (u1*z1Inv - u0*z0Inv)) * z_k;
            double v_k = ( v0*z0Inv + k * (v1*z1Inv - v0*z0Inv)) * z_k;
            
            if (depthBuffer.write(x, y, z_k)) {
                
                Color color = this.texture.getColor(u_k, v_k);
                frameBuffer.putPixel(x, y, color.getR(),color.getG(),color.getB());
            }
            x++;
            k += dxInv;
        }
    }
    
}
