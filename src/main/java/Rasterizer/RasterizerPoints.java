/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import GL.FrameBuffer;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public class RasterizerPoints extends RasterizerAbstract {

    public RasterizerPoints(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, TextureAbstract texture) {
        super(frameBuffer, depthBuffer, texture);
    }

    @Override
    public void drawTriangle() {
        
        frameBuffer.putPixel(xA, yA, aR, aG, bB, 4);
        frameBuffer.putPixel(xB, yB, bR, bG, bB, 4);
        frameBuffer.putPixel(xC, yC, cR, cG, bB, 4);
    }
    
}
