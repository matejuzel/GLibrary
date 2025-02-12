/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Geometry;

import Math.Vec4;

/**
 *
 * @author Matej
 */
public class Face {
    
    protected Vertex a,b,c;
    
    public Face(
                Vec4 positionA, Vec4 positionB, Vec4 positionC,
                Vec4 texCoordA, Vec4 texCoordB, Vec4 texCoordC,
                Vec4 normalA, Vec4 normalB, Vec4 normalC) {
        
        a = new Vertex(positionA, texCoordA, normalA);
        b = new Vertex(positionB, texCoordB, normalB);
        c = new Vertex(positionC, texCoordC, normalC);

    }

    public Vertex getVertexA() {
        return a;
    }
    public Vertex getVertexB() {
        return b;
    }
    public Vertex getVertexC() {
        return c;
    }
    
    public Vec4 getPositionA() {
        return a.getVertex();
    }

    public Vec4 getPositionB() {
        return b.getVertex();
    }

    public Vec4 getPositionC() {
        return c.getVertex();
    }

    public Vec4 getCoordA() {
        return a.getTextureCoord();
    }

    public Vec4 getCoordB() {
        return b.getTextureCoord();
    }

    public Vec4 getCoordC() {
        return c.getTextureCoord();
    }

    public Vec4 getNormalA() {
        return a.getNormal();
    }

    public Vec4 getNormalB() {
        return b.getNormal();
    }

    public Vec4 getNormalC() {
        return c.getNormal();
    }
    
}
