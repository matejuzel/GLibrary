/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Geometry;

import Math.Mtx4;
import Math.Vec4;

/**
 *
 * @author matej uzel
 */
public class Vertex {
    
    Vec4 vertex;
    Vec4 normal;
    Vec4 textureCoord;
    Vec4 color;
    
    Vec4 textureCoordExt1 = null;
    
    
    public Vertex(Vec4 pos, Vec4 texCoord, Vec4 normal) {
        vertex = new Vec4(pos.getX(), pos.getY(), pos.getZ(), pos.getW());
        textureCoord = new Vec4(texCoord.getX(), texCoord.getY(), texCoord.getZ(), texCoord.getW());
        this.normal = new Vec4(normal.getX(), normal.getY(), normal.getZ(), normal.getW());
    }
    
    public Vertex(Vec4 pos, Vec4 texCoord) {
        vertex = new Vec4(pos.getX(), pos.getY(), pos.getZ(), pos.getW());
        textureCoord = new Vec4(texCoord.getX(), texCoord.getY(), texCoord.getZ(), texCoord.getW());
        normal = new Vec4(0, 0, 1, 0);
    }
    
    public Vertex(Vec4 pos) {
        vertex = new Vec4(pos.getX(), pos.getY(), pos.getZ(), pos.getW());
        textureCoord = new Vec4(1, 0, 0, 0);
        normal = new Vec4(0, 0, 1, 0);
    }
    
    public Vertex() {
        vertex = new Vec4(0, 0, 0, 1);
        textureCoord = new Vec4(0, 0, 0, 1);
        normal = new Vec4(0, 1, 0, 0);
    }    
    
    public Vertex transform(Mtx4 transformation) {
        vertex.transform(transformation);
        return this;
    }
    
    public Vertex transformTextureCoord(Mtx4 transformation) {
        textureCoord.transform(transformation);
        return this;
    }

    public void setVertex(Vec4 vertex) {
        this.vertex = vertex;
    }

    public void setNormal(Vec4 normal) {
        this.normal = normal;
    }

    public void setTextureCoord(Vec4 textureCoord) {
        this.textureCoord = textureCoord;
    }

    public void setColor(Vec4 color) {
        this.color = color;
    }
    
    public void setTextureCoordExt1(Vec4 textureCoordExt1) {
        this.textureCoordExt1 = textureCoordExt1;
    }
    
    public Vec4 getVertex() {
        return vertex;
    }
    
    public Vec4 getNormal() {
        return normal;
    }
    
    public Vec4 getTextureCoord() {
        return this.textureCoord;
    }
    
    public Vec4 getColor() {
        return color;
    }
    
    public Vec4 getTextureCoordExt1() {
        return this.textureCoordExt1;
    }
    
    
    @Override
    public String toString() {
    
        String str = "";
        str += "Vertex:\n";
        str += this.vertex.toString() + "\n";
        str += "texture coord: " + this.textureCoord.toString() + "\n";
        str += "\n";
        
        return str;
    }
    
}
