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
public class FragmentShaderTextureSimple extends FragmentShader {
    
    public int frame = 0;
    
    
    public FragmentShaderTextureSimple(TextureAbstract texture) {
        super();
        this.setTexture(texture);
    }
    
    public void frameInc() {
        frame++;
    }
    
    @Override
    public void run() {
        outPos[0] = inPos[0];
        outPos[1] = inPos[1];
        
        // moznost simulace vlniciho povrchu
        double offsetX = 0;//0.05*Math.sin(10 * inAtrs[0] + 0.05 *frame);
        double offsetY = 0;//0.05*Math.cos(10 * inAtrs[1] + 0.05 *frame);
        
        double[] texCoord = {inAtrs[0], inAtrs[1]};
        Color color = inTexture.getColor(texCoord[0] + offsetX, texCoord[1] + offsetY);
        outColor[0] = color.getR();
        outColor[1] = color.getG();
        outColor[2] = color.getB();
        
        Vec4 normal = new Vec4(inAtrs[2], inAtrs[3], inAtrs[4], 0.0d).normal();
        
        double dot = Utils.dotProduct(inLight, normal);
        
        double dotFaktor = Utils.clamp(dot, -1.0d, 1.0d, 0.2d, 1.0d);
        outColor[0] *= dotFaktor;
        outColor[1] *= dotFaktor;
        outColor[2] *= dotFaktor;
    }
    
}
