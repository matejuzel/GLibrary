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
public class RasterizerSolid extends RasterizerAbstract {

    public RasterizerSolid(FrameBuffer frameBuffer, int depthBuffer, AbstractTexture texture) {
        super(frameBuffer, depthBuffer, texture);
    }

    @Override
    public void drawTriangle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
