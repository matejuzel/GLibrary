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
        
        
        double[] atrs_zAInv = {
            atrs[idxA][0] * zAInv,
            atrs[idxA][1] * zAInv,
            atrs[idxA][2] * zAInv,
            atrs[idxA][3] * zAInv,
            atrs[idxA][4] * zAInv,
        };
        
        double[] atrs_zBInv = {
            atrs[idxB][0] * zBInv,
            atrs[idxB][1] * zBInv,
            atrs[idxB][2] * zBInv,
            atrs[idxB][3] * zBInv,
            atrs[idxB][4] * zBInv
        };
        double[] atrs_zCInv = {
            atrs[idxC][0] * zCInv,
            atrs[idxC][1] * zCInv,
            atrs[idxC][2] * zCInv,
            atrs[idxC][3] * zCInv,
            atrs[idxC][4] * zCInv,
        };
        double[] atrs_dzACInv = {
            atrs_zCInv[0] - atrs_zAInv[0],
            atrs_zCInv[1] - atrs_zAInv[1],
            atrs_zCInv[2] - atrs_zAInv[2],
            atrs_zCInv[3] - atrs_zAInv[3],
            atrs_zCInv[4] - atrs_zAInv[4],
        };
        double[] atrs_dzABInv = {
            atrs_zBInv[0] - atrs_zAInv[0],
            atrs_zBInv[1] - atrs_zAInv[1],
            atrs_zBInv[2] - atrs_zAInv[2],
            atrs_zBInv[3] - atrs_zAInv[3],
            atrs_zBInv[4] - atrs_zAInv[4]
        };
        double[] atrs_dzBCInv = {
            atrs_zCInv[0] - atrs_zBInv[0],
            atrs_zCInv[1] - atrs_zBInv[1],
            atrs_zCInv[2] - atrs_zBInv[2],
            atrs_zCInv[3] - atrs_zBInv[3],
            atrs_zCInv[4] - atrs_zBInv[4]
        };
        
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
        double[] atrs_L_k = {0,0,0,0,0};
        double[] atrs_R_k = {0,0,0,0,0};
        
        kR = 0;
        
        for (int line = lineL; line < lineR; line++) {
            
            xL = (int) Math.round((crossL + line * dxL) * dyInvL);
            xR = (int) Math.round((crossR + line * dxR) * dyInvR);
            
            zL_k = 1.0d / (zL0 + kL * dzL);
            zR_k = 1.0d / (zR0 + kR * dzR);
            
            atrs_L_k[0] = (atrs_L0[0] + kL * atrs_dL[0]) * zL_k;
            atrs_L_k[1] = (atrs_L0[1] + kL * atrs_dL[1]) * zL_k;
            atrs_L_k[2] = (atrs_L0[2] + kL * atrs_dL[2]) * zL_k;
            atrs_L_k[3] = (atrs_L0[3] + kL * atrs_dL[3]) * zL_k;
            atrs_L_k[4] = (atrs_L0[4] + kL * atrs_dL[4]) * zL_k;
            
            atrs_R_k[0] = (atrs_R0[0] + kR * atrs_dR[0]) * zR_k;
            atrs_R_k[1] = (atrs_R0[1] + kR * atrs_dR[1]) * zR_k;
            atrs_R_k[2] = (atrs_R0[2] + kR * atrs_dR[2]) * zR_k;
            atrs_R_k[3] = (atrs_R0[3] + kR * atrs_dR[3]) * zR_k;
            atrs_R_k[4] = (atrs_R0[4] + kR * atrs_dR[4]) * zR_k;
            
            scanLine(xL, xR, line, zL_k, zR_k, atrs_L_k, atrs_R_k);
            
            kL += dyInvL;
            kR += dyInvR;
        }
        return kL;
    }
    
    public void scanLine(int x0, int x1, int y, double z0, double z1, double[] atrs_0, double[] atrs_1) {
        
        if (x0 > x1) {
            scanLine(x1, x0, y, z1, z0, atrs_1, atrs_0);
            return;
        }
        
        int dx = x1 - x0;
        double dxInv = 1.0d / (double) dx;
        double z0Inv = 1.0d / z0;
        double z1Inv = 1.0d / z1;
        double dzInv = z1Inv - z0Inv;
        double k = 0;
        double z_k;
        
        double[] atrs_0inv = {0,0,0,0,0};
        double[] atrs_k = {0,0,0,0,0};
        
        atrs_0inv[0] = atrs_0[0] * z0Inv;
        atrs_0inv[1] = atrs_0[1] * z0Inv;
        atrs_0inv[2] = atrs_0[2] * z0Inv;
        atrs_0inv[3] = atrs_0[3] * z0Inv;
        atrs_0inv[4] = atrs_0[4] * z0Inv;
        
        //int x = x0;
        //for (int i=0; i<dx + 1; i++) {
        for (int x = x0; x <= x1; x++) {
            z_k = 1.0d / (z0Inv + k * dzInv);
            
            if (depthBuffer.write(x, y, z_k)) {
                
                atrs_k[0] = (atrs_0inv[0] + k * (atrs_1[0] * z1Inv - atrs_0inv[0])) * z_k;
                atrs_k[1] = (atrs_0inv[1] + k * (atrs_1[1] * z1Inv - atrs_0inv[1])) * z_k;
                atrs_k[2] = (atrs_0inv[2] + k * (atrs_1[2] * z1Inv - atrs_0inv[2])) * z_k;
                atrs_k[3] = (atrs_0inv[3] + k * (atrs_1[3] * z1Inv - atrs_0inv[3])) * z_k;
                atrs_k[4] = (atrs_0inv[4] + k * (atrs_1[4] * z1Inv - atrs_0inv[4])) * z_k;
                
                this.fragmentShader(x, y, z_k, atrs_k);
                
            }
            
            k += dxInv;
        }
    }
    
    
    public void fragmentShader(int x, int y, double z, double[] atrs) {
    
        int r,g,b;
        
        Color color = this.texture.getColor(atrs[0], atrs[1]);
            
        r = color.getR();
        g = color.getG();
        b = color.getB();
        
        /*
        r = (int)atrs[2];
        g = (int)atrs[3];
        b = (int)atrs[4];
        */
        
        /*
        x += Math.round((Math.random()-0.5d) * 5);
        y += Math.round((Math.random()-0.5d) * 5);
        */
        
        frameBuffer.putPixel(x, y, r, g, b);
    }
    
}
