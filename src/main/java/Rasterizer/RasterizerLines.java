/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rasterizer;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import GL.FrameBuffer;
import Shader.FragmentShader;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public class RasterizerLines extends RasterizerAbstract {
    
    public RasterizerLines(FrameBuffer frameBuffer, DepthBufferAbstract depthBuffer, FragmentShader fragmentShader) {
        super(frameBuffer, depthBuffer, fragmentShader, 1, false);
    }

    @Override
    public void drawTriangle() {
        
        frameBuffer.putLine(xA, yA, xB, yB, aR, aG, aB);
        frameBuffer.putLine(xB, yB, xC, yC, bR, bG, bB);
        frameBuffer.putLine(xC, yC, xA, yA, cR, cG, cB);
    }
}
