/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Math.Vec4;
import java.util.ArrayList;

/**
 *
 * @author matej uzel
 */
public class VertexBuffer {
    
    public enum TriangleMode {
        SIMPLE,
        FAN,
        STRIP
    }
    
    ArrayList<Vec4> vertexArray = new ArrayList<>();
    TriangleMode triangleMode = TriangleMode.SIMPLE;
    
    public VertexBuffer(TriangleMode triangleMode) {
    
        this.triangleMode = triangleMode;
    }
    
    public VertexBuffer() {
    
        this(TriangleMode.SIMPLE);
    }
    
    public VertexBuffer add(Vec4 vertex) {
        
        this.vertexArray.add(vertex);
        
        return this;
    }
    
    public VertexBuffer addTriangle(Vec4 vertex1, Vec4 vertex2, Vec4 vertex3) {
        switch (this.triangleMode) {
            case SIMPLE:
                this.vertexArray.add(vertex1);
                this.vertexArray.add(vertex2);
                this.vertexArray.add(vertex3);
                break;
            case FAN:
                throw new RuntimeException("Traingle mode FAN neni zatim implementovan.");
            default:
                throw new RuntimeException("Neznamy triangle mode.");
        }
        return this;
    }
    
    public VertexBuffer addQuad(Vec4 vertex1, Vec4 vertex2, Vec4 vertex3, Vec4 vertex4) {
        this.addTriangle(vertex1, vertex2, vertex3);
        this.addTriangle(vertex1, vertex3, vertex4);
        return this;
    }
    
    public VertexBuffer addCube(double size) {
        
        Vec4 a = new Vec4(-size,-size,-size,1);
        Vec4 b = new Vec4( size,-size,-size,1);
        Vec4 c = new Vec4( size, size,-size,1);
        Vec4 d = new Vec4(-size, size,-size,1);
        
        Vec4 e = new Vec4(-size,-size, size,1);
        Vec4 f = new Vec4( size,-size, size,1);
        Vec4 g = new Vec4( size, size, size,1);
        Vec4 h = new Vec4(-size, size, size,1);
        
        this.addQuad(a,b,c,d); // front
        this.addQuad(f,e,h,g); // back
        
        this.addQuad(d,c,g,h); // top
        this.addQuad(e,f,b,a); // bottom
        
        this.addQuad(e,a,d,h); // left
        this.addQuad(b,f,g,c); // right
        return this;
    }
    
    
}
