/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferDouble;
import GL.FrameBuffer;
import Texture.AbstractTexture;

/**
 *
 * @author matej uzel
 */
public class RasterizerPoints extends RasterizerAbstract {

    public RasterizerPoints(FrameBuffer frameBuffer, DepthBufferDouble depthBuffer, AbstractTexture texture) {
        super(frameBuffer, depthBuffer, texture);
    }

    @Override
    public void drawTriangle() {
        
        frameBuffer.putPixel(aX, aY, aR, aG, bB, 4);
        frameBuffer.putPixel(bX, bY, bR, bG, bB, 4);
        frameBuffer.putPixel(cX, cY, cR, cG, bB, 4);
    }
    
}
