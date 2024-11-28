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
    int aX, aY;
    int bX, bY;
    int cX, cY;
    
    // texture coordinates
    double aU, aV;
    double bU, bV;
    double cU, cV;
    
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
        aX = (int) a.getX();
        aY = (int) a.getY();
        bX = (int) b.getX();
        bY = (int) b.getY();
        cX = (int) c.getX();
        cY = (int) c.getY();
        
        aZ = a.getZ();
        bZ = b.getZ();
        cZ = c.getZ();
    }
    
    public void setTextureCoordinates(Vec4 a, Vec4 b, Vec4 c) {
        aU = (int) a.getX();
        aV = (int) a.getY();
        bU = (int) b.getX();
        bV = (int) b.getY();
        cU = (int) c.getX();
        cV = (int) c.getY();
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
    

    
}
