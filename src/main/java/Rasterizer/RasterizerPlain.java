/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import GL.FrameBuffer;
import Math.Vec4;
import Shader.FragmentShader;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public class RasterizerPlain extends RasterizerAbstract {
    
    // pomocne hodnoty - rasterizace
    protected int dxAC, dxAB, dxBC;
    protected int dyAC, dyAB, dyBC;
    protected int crossAC, crossAB, crossBC;
    protected double dyInvAC, dyInvAB, dyInvBC;
    
    // pomocne hodnoty - interpolace hloubky
    protected double zInvA, zInvB, zInvC;
    protected double dzInvAC, dzInvAB, dzInvBC;
    protected double kAC, kAB, kBC;
    
    
    public RasterizerPlain(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, FragmentShader fragmentShader) {
        super(frameBuffer, depthBuffer, fragmentShader, 1, false);
    }

    
    public void preprocessTriangle() {
        
        dxAC = xC - xA;
        dxAB = xB - xA;
        dxBC = xC - xB;
        
        dyAC = yC - yA;
        dyAB = yB - yA;
        dyBC = yC - yB;
        
        dyInvAC = 1.0d / dyAC;
        dyInvAB = 1.0d / dyAB;
        dyInvBC = 1.0d / dyBC;
        
        crossAC = yC * xA - yA * xC;
        crossAB = yB * xA - yA * xB;
        crossBC = yC * xB - yB * xC;
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
        
        if (this.debug) System.out.println("drawTriangle() - part 1");
        
        sortVertices();
        preprocessTriangle();
        preprocessDepthInterpolation();
        
        double uA=123, uB=123, uC=123;
        double vA=123, vB=123, vC=123;
        
        // horni cast trojuhelniku
        for (int line = yA; line < yB; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            int xAB = (int) Math.round((crossAB + line * dxAB) * dyInvAB);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zAB_k = 1.0d / (zInvA + kAB * dzInvAB);
            
            // interpolace tex coord u
            double uAC_k = ( uA*zInvA + kAC * (uC*zInvC - uA*zInvA)) * zAC_k;
            double uAB_k = ( uA*zInvA + kAB * (uB*zInvB - uA*zInvA)) * zAB_k;
            // interpolace tex coord v
            double vAC_k = ( vA*zInvA + kAC * (vC*zInvC - vA*zInvA)) * zAC_k;
            double vAB_k = ( vA*zInvA + kAB * (vB*zInvB - vA*zInvA)) * zAB_k;
            
            scanLine(xAC, xAB, line, zAC_k, zAB_k, aR, aG, aB);
            
            kAC += dyInvAC;
            kAB += dyInvAB;
        }
        
        if (this.debug) System.out.println("drawTriangle() - part 2");
        
        // dolni cast trojuhelniku
        for (int line = yB; line < yC; line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = (int) Math.round((crossAC + line * dxAC) * dyInvAC);
            int xBC = (int) Math.round((crossBC + line * dxBC) * dyInvBC);
            
            // interpolace z
            double zAC_k = 1.0d / (zInvA + kAC * dzInvAC);
            double zBC_k = 1.0d / (zInvB + kBC * dzInvBC);
            
            // interpolace tex coord u
            double uAC_k = ( uA*zInvA + kAC * (uC*zInvC - uA*zInvA)) * zAC_k;
            double uBC_k = ( uB*zInvB + kBC * (uC*zInvC - uB*zInvB)) * zBC_k;
            // interpolace tex coord v
            double vAC_k = ( vA*zInvA + kAC * (vC*zInvC - vA*zInvA)) * zAC_k;
            double vBC_k = ( vB*zInvB + kBC * (vC*zInvC - vB*zInvB)) * zBC_k;
            
            scanLine(xAC, xBC, line, zAC_k, zBC_k, aR, aG, aB);
            
            kAC += dyInvAC;
            kBC += dyInvBC;
        }
        
        if (this.debug) System.out.println("");
    }
    
    public void scanLine(int x0, int x1, int y, double z0, double z1, int r, int g, int b) {
        
        if (x0 > x1) {
            scanLine(x1, x0, y, z1, z0, r, g, b);
            return;
        }
        
        if (this.debug) System.out.println("scanLine(" + x0 + "," + x1 + "," + y + "," + z0 + "," + z1 + "); " + "line_length: " + (Math.abs(x1-x0)+1) + " lr_flag: "+(x0 < x1));
        
        int dx = x1 - x0;
        double dxInv = 1.0d / dx;
        double k = 0;
        double z0Inv = 1.0d / z0;
        double z1Inv = 1.0d / z1;
        double dzInv = z1Inv - z0Inv;
        
        double z_k;
        
        int x = x0;
        for (int i=0; i<dx+1; i++) {
            
            z_k = 1.0d / (z0Inv + k * dzInv);
            if (depthBuffer.write(x, y, z_k)) {
                
                frameBuffer.putPixel(x, y, r,g,b);
            }
            x++;
            k += dxInv;
        }
    }
    
    /*
    public void triangleSolid(Vertex vertexA, Vertex vertexB, Vertex vertexC) {
        
        //System.out.println("A: "+vertexA.getU()+" ; "+vertexA.getV()+"; B: "+vertexB.getU()+" ; "+vertexB.getV()+"; C: "+vertexC.getU()+" ; "+vertexC.getV());
        
        Vertex pom;
        if (vertexB.getOut().getY() < vertexA.getOut().getY()) {
            
            pom = vertexB;
            vertexB = vertexA;
            vertexA = pom;
        }
        if (vertexC.getOut().getY() < vertexB.getOut().getY()) {
        
            pom = vertexC;
            vertexC = vertexB;
            vertexB = pom;
            
            if (vertexB.getOut().getY() < vertexA.getOut().getY()) {
                
                pom = vertexB;
                vertexB = vertexA;
                vertexA = pom;
            }
        }
        
        
        Vector4 outA = vertexA.getOut();
        Vector4 outB = vertexB.getOut();
        Vector4 outC = vertexC.getOut();
        
        // z Normalized Device Coordinates prevedeme souradnice do Screen Space [-1 ; 1] -> [0 ; widht resp. height]
        Vector2 screenA = new Vector2(Math.round((context.getWidth() / 2.0f * (outA.getX() + 1))), Math.round(context.getHeight() / 2.0f * (outA.getY() + 1)));
        Vector2 screenB = new Vector2(Math.round((context.getWidth() / 2.0f * (outB.getX() + 1))), Math.round(context.getHeight() / 2.0f * (outB.getY() + 1)));
        Vector2 screenC = new Vector2(Math.round((context.getWidth() / 2.0f * (outC.getX() + 1))), Math.round(context.getHeight() / 2.0f * (outC.getY() + 1)));
        
        //System.out.println(scrA.toString()+" , "+scrB.toString()+" , "+scrC.toString());
        
        int dxAC = screenC.getX() - screenA.getX();
        int dxAB = screenB.getX() - screenA.getX();
        int dxBC = screenC.getX() - screenB.getX();
        
        int dy0 = screenC.getY() - screenA.getY();
        int dy1 = screenB.getY() - screenA.getY();
        int dy2 = screenC.getY() - screenB.getY();
        
        float dyAC_inv = 1.0f / dy0;
        float dyAB_inv = 1.0f / dy1;
        float dyBC_inv = 1.0f / dy2;
        
        int crossAC = screenC.getY()*screenA.getX() - screenA.getY()*screenC.getX();
        int crossAB = screenB.getY()*screenA.getX() - screenA.getY()*screenB.getX();
        int crossBC = screenC.getY()*screenB.getX() - screenB.getY()*screenC.getX();
        
        float kAC = 0;//dyAC_inv / 2; 
        float kAB = 0;//dyAB_inv / 2;
        float kBC = 0;//dyBC_inv / 2;
        
        float zAC_0 = vertexA.getRealZ();
        float zAC_1 = vertexC.getRealZ();
        float zAB_0 = vertexA.getRealZ();
        float zAB_1 = vertexB.getRealZ();
        float zBC_0 = vertexB.getRealZ();
        float zBC_1 = vertexC.getRealZ();
        
        float uAC_0 = vertexA.getU();
        float uAC_1 = vertexC.getU();
        float uAB_0 = vertexA.getU();
        float uAB_1 = vertexB.getU();
        float uBC_0 = vertexB.getU();
        float uBC_1 = vertexC.getU();
        
        float vAC_0 = vertexA.getV();
        float vAC_1 = vertexC.getV();
        float vAB_0 = vertexA.getV();
        float vAB_1 = vertexB.getV();
        float vBC_0 = vertexB.getV();
        float vBC_1 = vertexC.getV();
        
        // horni cast trojuhelniku
        for (int line = screenA.getY(); line < screenB.getY(); line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = Math.round((crossAC + line * dxAC) * dyAC_inv);
            int xAB = Math.round((crossAB + line * dxAB) * dyAB_inv);
            
            // interpolace z
            float zAC_k = 1.0f / ( 1.0f/zAC_0 + kAC * (1.0f/zAC_1 - 1.0f/zAC_0) );
            float zAB_k = 1.0f / ( 1.0f/zAB_0 + kAB * (1.0f/zAB_1 - 1.0f/zAB_0) );
            // interpolace texturovacich souradnic u, v
            float uAC_k = ( uAC_0/zAC_0 + kAC * (uAC_1/zAC_1 - uAC_0/zAC_0) ) * zAC_k;
            float uAB_k = ( uAB_0/zAB_0 + kAB * (uAB_1/zAB_1 - uAB_0/zAB_0) ) * zAB_k;
            float vAC_k = ( vAC_0/zAC_0 + kAC * (vAC_1/zAC_1 - vAC_0/zAC_0) ) * zAC_k;
            float vAB_k = ( vAB_0/zAB_0 + kAB * (vAB_1/zAB_1 - vAB_0/zAB_0) ) * zAB_k;
            
            scanLine(xAC, xAB, line, zAC_k, zAB_k, uAC_k, uAB_k, vAC_k, vAB_k);
            
            kAC += dyAC_inv;
            kAB += dyAB_inv;
        }
        
        // dolni cast trojuhelniku
        for (int line = screenB.getY(); line < screenC.getY(); line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = Math.round((crossAC + line * dxAC) * dyAC_inv);
            int xBC = Math.round((crossBC + line * dxBC) * dyBC_inv);
            
            // interpolace z
            float zAC_k = 1.0f / ( 1.0f/zAC_0 + kAC * (1.0f/zAC_1 - 1.0f/zAC_0) );
            float zBC_k = 1.0f / ( 1.0f/zBC_0 + kBC * (1.0f/zBC_1 - 1.0f/zBC_0) );
            // interpolace texturovacich souradnic u, v
            float uAC_k = ( uAC_0/zAC_0 + kAC * (uAC_1/zAC_1 - uAC_0/zAC_0) ) * zAC_k;
            float uBC_k = ( uBC_0/zBC_0 + kBC * (uBC_1/zBC_1 - uBC_0/zBC_0) ) * zBC_k;
            float vAC_k = ( vAC_0/zAC_0 + kAC * (vAC_1/zAC_1 - vAC_0/zAC_0) ) * zAC_k;
            float vBC_k = ( vBC_0/zBC_0 + kBC * (vBC_1/zBC_1 - vBC_0/zBC_0) ) * zBC_k;

            scanLine(xAC, xBC, line, zAC_k, zBC_k, uAC_k, uBC_k, vAC_k, vBC_k);
            
            kAC += dyAC_inv;
            kBC += dyBC_inv;
        }
    }
    
    public void scanLine(int x0, int x1, int y, float z0, float z1, float u0, float u1, float v0, float v1) {
        
        if (x0 > x1) {
            
            int pomI; float pomF;
            pomI = x0; x0 = x1; x1 = pomI;
            pomF = z0; z0 = z1; z1 = pomF;
            pomF = u0; u0 = u1; u1 = pomF;
            pomF = v0; v0 = v1; v1 = pomF;
        }
        
        float dx_inv = 1.0f / (x1 - x0);
        float k = dx_inv / 2; // zaciname vlastne v pulce pixelu proto ten posun o polovinu kroku
        
        
        float u_k0 = 0, v_k0 = 0; // texturovaci souradnice z predchoziho kroku pro filtrovani textury
        
        for (int x = x0; x < x1; x++) {
            
            float z_k = 1.0f / ( 1.0f/z0 + k * (1.0f/z1 - 1.0f/z0));
            
            float u_k = ( u0/z0 + k * (u1/z1 - u0/z0) ) * z_k;
            float v_k = ( v0/z0 + k * (v1/z1 - v0/z0) ) * z_k;
            
            dot(new Vector2(x, y), texture.getTextel(u_k, v_k, u_k0, v_k0));
            
            k += dx_inv;
            
            u_k0 = u_k;
            v_k0 = v_k;
        }
    }
    */
}
