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
    ArrayList<Vec4> normalArray = new ArrayList<>();
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
                
                this.normalArray.add(vertex1.getNormal());
                this.normalArray.add(vertex2.getNormal());
                this.normalArray.add(vertex3.getNormal());
                
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
    
    
    public VertexBuffer addMesh(ArrayList<Face> mesh) {
        
        for (int i=0; i<mesh.size(); i++) {
            
            Face face = mesh.get(i);
            
            this.addTriangle(
                    new Vertex(face.getVertexA(), face.getCoordA(), face.getNormalA()),
                    new Vertex(face.getVertexB(), face.getCoordB(), face.getNormalB()),
                    new Vertex(face.getVertexC(), face.getCoordC(), face.getNormalC())
            );
        }
        
        return this;
    }
    
    public VertexBuffer addCube(double size) {
        
        Vec4 a = new Vec4(-size, -size, -size, 1.0d);
        Vec4 b = new Vec4( size, -size, -size, 1.0d);
        Vec4 c = new Vec4( size,  size, -size, 1.0d);
        Vec4 d = new Vec4(-size,  size, -size, 1.0d);
        
        Vec4 e = new Vec4( size, -size,  size, 1.0d);
        Vec4 f = new Vec4( size,  size,  size, 1.0d);
        Vec4 g = new Vec4(-size,  size,  size, 1.0d);
        Vec4 h = new Vec4(-size, -size,  size, 1.0d);
        
        // front
        this.addQuad(
            new Vertex(a, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(b, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(c, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(d, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        // right
        this.addQuad(
            new Vertex(b, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(e, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(f, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(c, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        // right
        this.addQuad(
            new Vertex(e, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(h, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(g, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(f, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        // right
        this.addQuad(
            new Vertex(h, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(a, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(d, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(g, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        // right
        this.addQuad(
            new Vertex(d, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(c, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(f, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(g, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        // right
        this.addQuad(
            new Vertex(b, new Vec4(0,0,0,1), new Vec4(0,0,1,0)),
            new Vertex(a, new Vec4(0,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(h, new Vec4(1,1,0,1), new Vec4(0,0,1,0)),
            new Vertex(e, new Vec4(1,0,0,1), new Vec4(0,0,1,0))
        );
        
        return this;
    }
    
    
}
