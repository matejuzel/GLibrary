/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Math.Vec4;

/**
 *
 * @author Matej
 */
public class Face {
    
    protected Vec4 vertexA;
    protected Vec4 vertexB;
    protected Vec4 vertexC;
    
    protected Vec4 coordA;
    protected Vec4 coordB;
    protected Vec4 coordC;
    
    protected Vec4 normalA;
    protected Vec4 normalB;
    protected Vec4 normalC;
    
    public Face(
                Vec4 vertexA, Vec4 vertexB, Vec4 vertexC,
                Vec4 coordA, Vec4 coordB, Vec4 coordC,
                Vec4 normalA, Vec4 normalB, Vec4 normalC) {
        
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.vertexC = vertexC;
        
        this.coordA = coordA;
        this.coordB = coordB;
        this.coordC = coordC;
        
        this.normalA = normalA;
        this.normalB = normalB;
        this.normalC = normalC;
    }

    public Vec4 getVertexA() {
        return vertexA;
    }

    public Vec4 getVertexB() {
        return vertexB;
    }

    public Vec4 getVertexC() {
        return vertexC;
    }

    public Vec4 getCoordA() {
        return coordA;
    }

    public Vec4 getCoordB() {
        return coordB;
    }

    public Vec4 getCoordC() {
        return coordC;
    }

    public Vec4 getNormalA() {
        return normalA;
    }

    public Vec4 getNormalB() {
        return normalB;
    }

    public Vec4 getNormalC() {
        return normalC;
    }
    
    @Override
    public String toString() {
        
        String str = "Face {\n";
        str += "  Geometry: " + coordA.toString() + " ; " + coordB.toString() + " ; " + coordC.toString() + "\n";
        str += "  Coord: " + coordA.toString() + " ; " + coordB.toString() + " ; " + coordC.toString() + "\n";
        str += "  Normal: " + normalA.toString() + " ; " + normalB.toString() + " ; " + normalC.toString() + "\n";
        str += "}";
        
        return str;
    }
    
}
