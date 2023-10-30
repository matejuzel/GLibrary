/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Math.Mtx4;
import Math.Vec4;

/**
 *
 * @author matej uzel
 */
public class Vertex {
    
    Vec4 vertex = null;
    Vec4 textureCoord = null;
    Vec4 textureCoordExt1 = null;
    Vec4 textureCoordExt2 = null;
    Vec4 textureCoordExt3 = null;
    
    public Vertex(Vec4 vertex, Vec4 textureCoord) {
    
        this.vertex = vertex;
        this.textureCoord = textureCoord;
    }
    
    public Vertex transform(Mtx4 transformation) {
        
        this.vertex.transform(transformation);
        return this;
    }
    
    public Vertex transformTextureCoord(Mtx4 transformation) {
        
        this.textureCoord.transform(transformation);
        return this;
    }
    
    public Vec4 getVertex() {
        
        return this.vertex;
    }
    
    public Vec4 getTextureCoord() {
        
        return this.textureCoord;
    }
    
    public Vec4 getTextureCoordExt1() {
        
        return this.textureCoordExt1;
    }
    public Vec4 getTextureCoordExt2() {
        
        return this.textureCoordExt2;
    }
    public Vec4 getTextureCoordExt3() {
        
        return this.textureCoordExt3;
    }
    
}
