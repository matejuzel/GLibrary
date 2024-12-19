/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import GL.FrameBuffer;
import Math.Vec4;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public abstract class RasterizerAbstract {
    
    public boolean debug = false;
    
    protected FrameBuffer frameBuffer;
    protected DepthBufferAbstract depthBuffer; // prizpusobit datovy typ
    protected TextureAbstract texture;
    
    // vertex coordinates
    int xA, yA;
    int xB, yB;
    int xC, yC;
    
    int idxA = 0, idxB = 1, idxC = 2;
    
    double[][] atrs = {
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d},
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d},
        {0.0d, 0.0d, 0.0d, 0.0d, 0.0d}
    };
    
    
    // z coordinates
    double aZ, bZ, cZ;
    
    // colors
    int aR, aG, aB;
    int bR, bG, bB;
    int cR, cG, cB;
    
    public RasterizerAbstract(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, TextureAbstract texture) {
        this.frameBuffer = frameBuffer;
        this.depthBuffer = depthBuffer;
        this.texture = texture;
    }
    
    public abstract void drawTriangle();
    
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
    

    
}
