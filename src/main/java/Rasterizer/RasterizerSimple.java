/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.FrameBuffer;
import Texture.AbstractTexture;

/**
 *
 * @author matej uzel
 */
public class RasterizerSimple extends RasterizerAbstract {
    
    public RasterizerSimple(FrameBuffer frameBuffer, int depthBuffer, AbstractTexture texture) {
        super(frameBuffer, depthBuffer, texture);
    }

    @Override
    public void drawTriangle() {
        
        frameBuffer.putLine(aX, aY, bX, bY, aR, aG, aB);
        frameBuffer.putLine(bX, bY, cX, cY, bR, bG, bB);
        frameBuffer.putLine(cX, cY, aX, aY, cR, cG, cB);
    }
}
