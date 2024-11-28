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
    ArrayList<Vec4> texCoordArray = new ArrayList<>();
    TriangleMode triangleMode = TriangleMode.SIMPLE;
    
    public VertexBuffer(TriangleMode triangleMode) {
    
        this.triangleMode = triangleMode;
    }
    
    public VertexBuffer() {
    
        this(TriangleMode.SIMPLE);
    }
    
    public VertexBuffer add(Vec4 vertex, Vec4 texCoord) {
        
        this.vertexArray.add(vertex);
        return this;
    }
    
    public VertexBuffer addTriangle(Vertex vertex1, Vertex vertex2, Vertex vertex3) {
        switch (this.triangleMode) {
            case SIMPLE:
                this.vertexArray.add(vertex1.getVertex());
                this.vertexArray.add(vertex2.getVertex());
                this.vertexArray.add(vertex3.getVertex());
                
                this.texCoordArray.add(vertex1.getTextureCoord());
                this.texCoordArray.add(vertex2.getTextureCoord());
                this.texCoordArray.add(vertex3.getTextureCoord());
                
                break;
            case FAN:
                throw new RuntimeException("Traingle mode FAN neni zatim implementovan.");
            default:
                throw new RuntimeException("Neznamy triangle mode.");
        }
        return this;
    }
    
    public VertexBuffer addQuad(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex vertex4) {
        this.addTriangle(vertex1, vertex2, vertex3);
        this.addTriangle(vertex1, vertex3, vertex4);
        return this;
    }
    
    
    public VertexBuffer addCube(double size) {
        
        // front
        this.addQuad(
            new Vertex(new Vec4(-size, -size, -size, 1), new Vec4(0,0,0,1)),
            new Vertex(new Vec4( size, -size, -size, 1), new Vec4(0,1,0,1)),
            new Vertex(new Vec4( size,  size, -size, 1), new Vec4(1,1,0,1)),
            new Vertex(new Vec4(-size,  size, -size, 1), new Vec4(1,0,0,1))
        );
        
        // back
        this.addQuad(
            new Vertex(new Vec4(-size, -size,  size, 1), new Vec4(0,0,0,1)),
            new Vertex(new Vec4( size, -size,  size, 1), new Vec4(0,1,0,1)),
            new Vertex(new Vec4( size,  size,  size, 1), new Vec4(1,1,0,1)),
            new Vertex(new Vec4(-size,  size,  size, 1), new Vec4(1,0,0,1))
        );
        
        // left
        this.addQuad(
            new Vertex(new Vec4(-size, -size, -size, 1), new Vec4(0,0,0,1)),
            new Vertex(new Vec4(-size,  size, -size, 1), new Vec4(0,1,0,1)),
            new Vertex(new Vec4(-size,  size,  size, 1), new Vec4(1,1,0,1)),
            new Vertex(new Vec4(-size, -size,  size, 1), new Vec4(1,0,0,1))
        );
        
        // right
        this.addQuad(
            new Vertex(new Vec4( size, -size, -size, 1), new Vec4(0,0,0,1)),
            new Vertex(new Vec4( size,  size, -size, 1), new Vec4(0,1,0,1)),
            new Vertex(new Vec4( size,  size,  size, 1), new Vec4(1,1,0,1)),
            new Vertex(new Vec4( size, -size,  size, 1), new Vec4(1,0,0,1))
        );
        
        return this;
    }
    
    
}
