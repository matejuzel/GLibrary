/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shader;

import Math.Mtx4;
import Math.Vec4;
import Texture.TextureAbstract;

/**
 *
 * @author matej uzel
 */
abstract public class FragmentShader extends Shader {
    
    protected int[] inPos = {0, 0};
    protected double inNormZ;
    protected double[] inAtrs;
    
    protected int[] outPos = {0, 0};
    protected int[] outColor = {255, 255, 255};
    
    protected TextureAbstract inTexture;
    protected Vec4 inLight = new Vec4(1,0,0,0);
    
    protected Mtx4 inModelview = new Mtx4();
    protected Mtx4 inProjection = new Mtx4();
    
    public FragmentShader(TextureAbstract texture) {
        super();
        this.inTexture = texture;
    }
    
    public void setIn(int inX, int inY, double inNormZ, double[] atrs) {
        this.inPos[0] = inX;
        this.inPos[1] = inY;
        this.inNormZ = inNormZ;
        this.inAtrs = atrs;
    }
    
    public void setLight(Vec4 light) {
        this.inLight = light;
    }
    
    public void setTexture(TextureAbstract texture) {
        this.inTexture = texture;
    }
    
    public void setModelview(Mtx4 mtx) {
        this.inModelview = mtx;
    }
    
    public void setProjection(Mtx4 mtx) {
        this.inProjection = mtx;
    }
    
    public int[] getOutPos() {
        return outPos;
    }
    
    public int[] getOutColor() {
        return outColor;
    }
    
}
