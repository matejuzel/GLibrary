/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shader;

import GL.Color;
import Math.Utils;
import Math.Vec4;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
public class FragmentShaderFlat extends FragmentShader {
    
    private final int r,g,b;
    
    public FragmentShaderFlat(int r, int g, int b) {
        super();
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    @Override
    public void run() {
        outPos[0] = inPos[0];
        outPos[1] = inPos[1];
        
        outColor[0] = r;
        outColor[1] = g;
        outColor[2] = b;
    }
    
}
