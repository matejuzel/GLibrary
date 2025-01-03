/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package GL;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import Math.Mtx4;
import Math.Vec4;
import Rasterizer.RasterizerAbstract;
import Rasterizer.RasterizerPoints;
import Rasterizer.RasterizerLines;
import Rasterizer.RasterizerSolid;
import Rasterizer.RasterizerTextures;
import Texture.TextureAbstract;
import Texture.TextureNearest;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author matej uzel
 */
public class GLibrary {

    public enum FaceCullingMode {
        NONE,
        FRONT,
        BACK,
        FRONT_AND_BACK
    }
    
     public enum PrimitiveMode {
        POINTS,
        LINES,
        SOLID,
        TEXTURES
    }
    
    public boolean debug = false;
     
    private final FrameBuffer frameBuffer;
    private final DepthBufferAbstract depthBuffer;
    
    private final RasterizerAbstract rasterizerPoints;
    private final RasterizerAbstract rasterizerLines;
    private final RasterizerAbstract rasterizerSolid;
    private final RasterizerAbstract rasterizerTextures;
    private RasterizerAbstract rasterizer;
    
    private FaceCullingMode faceCullingMode = FaceCullingMode.NONE;
    private PrimitiveMode primitiveMode = PrimitiveMode.SOLID;
    
    private Mtx4 matrixModelView = new Mtx4();
    private Mtx4 matrixProjection = new Mtx4();
    private Mtx4 matrixViewPort = new Mtx4();
    
    private ArrayList<VertexBuffer> vertexBufferArray = new ArrayList<>();
    
    private final TextureUnit textureUnit = new TextureUnit();
    
    
    private Mtx4 matrixFinal = new Mtx4(); // pracovni matice - zde bude soucit modelview a projection
    private TextureAbstract textureWork;
    
    //TriangleMode triangleMode = TriangleMode.SIMPLE;
    
    public GLibrary(int width, int height, boolean debug) {
        
        this.debug = debug;
        
        depthBuffer = new DepthBufferDouble(width, height);
        frameBuffer = new FrameBuffer(width, height);
        
        matrixViewPort.loadViewport(width, height, 0, 0);
        
        TextureAbstract texture = textureUnit.getCurrentTexture();
        
        rasterizerPoints = new RasterizerPoints(frameBuffer, depthBuffer, texture);
        rasterizerLines = new RasterizerLines(frameBuffer, depthBuffer, texture);
        rasterizerSolid = new RasterizerSolid(frameBuffer, depthBuffer, texture);
        rasterizerTextures = new RasterizerTextures(frameBuffer, depthBuffer, texture, 5);
        
    }
    
    public GLibrary render(int handler) {
        
        textureWork = getTextureUnit().getCurrentTexture();
        
        VertexBuffer vb = this.vertexBufferArray.get(handler);
        
        matrixFinal.loadIdentity();
        matrixFinal.multiply(matrixProjection);
        matrixFinal.multiply(matrixModelView);
        
        
        switch (primitiveMode) {
            case POINTS:
                rasterizer = rasterizerPoints;
                break;
            case LINES:
                rasterizer = rasterizerLines;
                break;
            case SOLID:
                rasterizer = rasterizerSolid;
                break;
            case TEXTURES:
                rasterizer = rasterizerTextures;
                break;
            default:
                throw new UnsupportedOperationException("primitiveMode neni nastaven na validni hodnotu.");
        }
        
        rasterizer.debug = this.debug;
        
        switch (vb.triangleMode) {
            case SIMPLE:
                
                for (int i=0; i<vb.vertexArray.size(); i+=3) {
                    
                    Vec4 vertA = new Vec4(vb.vertexArray.get(i));
                    Vec4 vertB = new Vec4(vb.vertexArray.get(i+1));
                    Vec4 vertC = new Vec4(vb.vertexArray.get(i+2));
                    
                    
                    Vec4 vec_a = new Vec4(vertB.getX() - vertA.getX(), vertB.getY() - vertA.getY(), vertB.getZ() - vertA.getZ(), 1.0d);
                    Vec4 vec_b = new Vec4(vertC.getX() - vertB.getX(), vertC.getY() - vertB.getY(), vertC.getZ() - vertB.getZ(), 1.0d);
                    
                    Vec4 norm = new Vec4(
                            vec_a.getX()*vec_b.getY() - vec_b.getX()*vec_a.getY(), // ax*by - bx*ay
                            vec_a.getY()*vec_b.getZ() - vec_b.getY()*vec_a.getZ(), // ay*bz - by*az
                            vec_a.getZ()*vec_b.getX() - vec_b.getZ()*vec_a.getX(), // az*bx - bz*ax
                            1
                    );
                    
                    Vec4 normalA = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 1.0d);
                    Vec4 normalB = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 1.0d);
                    Vec4 normalC = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 1.0d);
                    
                    Vec4 texCoordA = new Vec4(vb.texCoordArray.get(i));
                    Vec4 texCoordB = new Vec4(vb.texCoordArray.get(i+1));
                    Vec4 texCoordC = new Vec4(vb.texCoordArray.get(i+2));
                    
                    Vec4 lightDirection = new Vec4(0,0,-1,0);
                    lightDirection = lightDirection.transform(matrixModelView.getOrthonormalInverted());
                    
                    vertA.transform(matrixFinal);
                    vertB.transform(matrixFinal);
                    vertC.transform(matrixFinal);
                    
                    double wA = vertA.getW();
                    double wB = vertB.getW();
                    double wC = vertC.getW();
                    
                    vertA.divideByW();
                    vertB.divideByW();
                    vertC.divideByW();
                    
                    // pro cross product
                    double vABx = vertB.getX() - vertA.getX();
                    double vABy = vertB.getY() - vertA.getY();
                    double vACx = vertC.getX() - vertA.getX();
                    double vACy = vertC.getY() - vertA.getY();
                    
                    switch (faceCullingMode) {
                        case NONE:
                            // nic
                            break;
                        case BACK:
                            // corss product: (ax, ay, 0) x (bx, by, 0) = (ax*by-ay*bx)
                            if (vABx*vACy - vABy*vACx < 0) continue;
                            break;
                        case FRONT:
                            // corss product: (ax, ay, 0) x (bx, by, 0) = (ax*by-ay*bx)
                            if (vABx*vACy - vABy*vACx > 0) continue;
                            break;
                        case FRONT_AND_BACK:
                            throw new UnsupportedOperationException("faceCullingMode=FRONT_AND_BACK is not supported yet");
                        default:
                            throw new UnsupportedOperationException("faceCullingMode option is not set");
                    }
                    
                    vertA.transform(matrixViewPort);
                    vertB.transform(matrixViewPort);
                    vertC.transform(matrixViewPort);
                    
                    // RASTERIZACE:
                    
                    rasterizer.setParams(
                            vertA.getX(), vertA.getY(), wA,
                            vertB.getX(), vertB.getY(), wB,
                            vertC.getX(), vertC.getY(), wC,
                            
                            lightDirection.getX(),
                            lightDirection.getY(),
                            lightDirection.getZ(),
                            
                            this.getMatrixProjection().getData(11), // normalizacni z-faktor: -2*n*f + (n+f)/(f-n)
                            this.getMatrixProjection().getData(10), // normalizacni z-offset: (n+f)/(f-n)
                            
                            new double[] {
                                texCoordA.getX(),
                                texCoordA.getY(),
                                normalA.getX(), 
                                normalA.getY(), 
                                normalA.getZ()
                            },
                            new double[] {
                                texCoordB.getX(), 
                                texCoordB.getY(), 
                                normalB.getX(), 
                                normalB.getY(), 
                                normalB.getZ()
                            },
                            new double[] {
                                texCoordC.getX(), 
                                texCoordC.getY(), 
                                normalC.getX(), 
                                normalC.getY(), 
                                normalC.getZ()
                            }
                    );
                    
                    rasterizer.drawTriangle();
                }
                
                break;
            default:
                throw new RuntimeException("Zatim je podporovan pouze trangleMode = SIMPLE");
        }
        
        return this;
    }
    
    /* PRUSECIK ROVINY S PRIMKOU
rovina dana body A, B, C
primka data body D, E
prusecny bod P.

A + k1*u +k2*v = D + k3*p
k1*u + k2*v - k3*p = A - D

u1*k1 + v1*k2 - p1*k3 = A1 - D1
u2*k1 + v2*k2 - p2*k3 = A2 - D2
u3*k1 + v3*k2 - p3*k3 = A3 - D3

| u1 v1 -p1 |   | k1 |   | A1 - D1 |
| u2 v2 -p2 | x | k2 | = | A2 - D2 |
| u3 v3 -p3 |   | k3 |   | A3 - D3 |

M x k = d
M^-1 * M * k = M^-1 * d
k = M^-1 * d

| k1 |          | A1 - D1 |
| k2 | = M^-1 * | A2 - D2 |
| k3 |          | A3 - D3 |

M^-1 = inverse{{u1,v1,-p1},{u2,v2,-p2},{u3,v3,-p3}} 
     = 1/(p1*u2*v3 - p1*u3*v2 - p2*u1*v3 + p2*u3*v1 + p3*u1*v2 - p3*u2*v1) *
         | p3*v2-p2*v3  p1*v3-p3*v1  p2*v1-p1*v2 |
         | p2*u3-p3*u2  p3*u1-p1*u3  p1*u2-p2*u1 |
         | u3*v2-u2*v3  u1*v3-u3_v1  u2*v1-u1*v2 |





    */
    
    public int addVertexBuffer() {
        VertexBuffer vertexBuffer = new VertexBuffer();
        this.vertexBufferArray.add(vertexBuffer);
        return this.vertexBufferArray.size()-1;
    }
    
    public VertexBuffer getVertexBuffer(int handler) {
        return this.vertexBufferArray.get(handler);
    }
    
    
    
    public GLibrary drawRectangle(int x1, int y1, int x2, int y2, int r, int g, int b) {
        
        for (int x=x1; x<=x2; x++) {
            
            this.frameBuffer.putPixel(x, y1, r, g, b);
            this.frameBuffer.putPixel(x, y2, r, g, b);
        }
        
        for (int y=y1; y<=y2; y++) {
            
            this.frameBuffer.putPixel(x1, y, r, g, b);
            this.frameBuffer.putPixel(x2, y, r, g, b);
        }
        
        return this;
    }
    
    public FrameBuffer getFrameBuffer() {
        return this.frameBuffer;
    }
    
    public DepthBufferAbstract getDepthBuffer() {
        return this.depthBuffer;
    }
    
    public int getWidth() {
        return this.frameBuffer.getWidth();
    }
    
    public int getHeight() {
        return this.frameBuffer.getHeight();
    }
    
    public void setFaceCullingMode(FaceCullingMode faceCullingMode) {
        this.faceCullingMode = faceCullingMode;
    }

    public void setPrimitiveMode(PrimitiveMode primitiveMode) {
        this.primitiveMode = primitiveMode;
    }

    public void setMatrixModelView(Mtx4 matrixModelView) {
        this.matrixModelView = matrixModelView;
    }

    public void setMatrixProjection(Mtx4 matrixProjection) {
        this.matrixProjection = matrixProjection;
    }

    public void setMatrixViewPort(Mtx4 matrixViewPort) {
        this.matrixViewPort = matrixViewPort;
    }

    public FaceCullingMode getFaceCullingMode() {
        return faceCullingMode;
    }

    public PrimitiveMode getPrimitiveMode() {
        return primitiveMode;
    }

    public Mtx4 getMatrixModelView() {
        return matrixModelView;
    }

    public Mtx4 getMatrixProjection() {
        return matrixProjection;
    }

    public Mtx4 getMatrixViewPort() {
        return matrixViewPort;
    }
    
    public TextureUnit getTextureUnit() {
        return textureUnit;
    }
    
    public void render2DTexture(TextureAbstract texture) {
        
        var color = new Color(0, 0, 0);
        
        for (int y = 0; y < texture.getHeight(); y++) {
            
            for (int x = 0; x < texture.getWidth(); x++) {
                
                color.setR(texture.getColor(x, y).getR());
                color.setG(texture.getColor(x, y).getG());
                color.setB(texture.getColor(x, y).getB());
                
                this.frameBuffer.putPixel(x, y, color.getR(), color.getG(), color.getB());
            }
        }
        
    }
    
    
    @Override
    public String toString() {
        
        int sum = 0;
        for (int i=0; i<vertexBufferArray.size(); i++) {
            sum += vertexBufferArray.get(i).vertexArray.size() / 3;
        }
        
        return "pocet vertexu: "+sum;
    }
}
