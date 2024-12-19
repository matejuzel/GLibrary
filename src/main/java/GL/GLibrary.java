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

    // odkladiste pro transformovane vertexy
    Vec4[] vertexBufferWork = new Vec4[1024];
    
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
        rasterizerTextures = new RasterizerTextures(frameBuffer, depthBuffer, texture);
        
        for (int i=0; i<vertexBufferWork.length; i++) {
            vertexBufferWork[i] = new Vec4(0.0d, 0.0d, 0.0d, 0.0d);
        }
    }
    
    public GLibrary render(int handler) {
        
        textureWork = getTextureUnit().getCurrentTexture();
        
        int[] colors = {
            255,0,0,255,0,0,
            0,255,0,0,255,0,
            0,0,255,0,0,255,
            255,255,0,255,255,0,
            255,0,255,255,0,255,
            255,255,255,255,255,255,
        };
        
        VertexBuffer vb = this.vertexBufferArray.get(handler);
        
        matrixFinal.loadIdentity();
        matrixFinal.multiply(matrixProjection);
        matrixFinal.multiply(matrixModelView);
        
        for (int i=0; i<vb.vertexArray.size(); i++) {
            this.vertexBufferWork[i].setData(vb.vertexArray.get(i));
            //this.vertexBufferWork[i].divideByW();
        }
        
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
                    
                    int colorR = colors[i%36];
                    int colorG = colors[i%36+1];
                    int colorB = colors[i%36+2];
                    
                    Vec4 vertA = new Vec4(this.vertexBufferWork[i]);
                    Vec4 vertB = new Vec4(this.vertexBufferWork[i+1]);
                    Vec4 vertC = new Vec4(this.vertexBufferWork[i+2]);
                    
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
                    
                    rasterizer.setVertexCoordinates(
                            new Vec4(vertA.getX(), vertA.getY(), wA, 1), 
                            new Vec4(vertB.getX(), vertB.getY(), wB, 1), 
                            new Vec4(vertC.getX(), vertC.getY(), wC, 1)
                    );
                    rasterizer.setColorA(colorR, colorG, colorB);
                    rasterizer.setColorB(colorR, colorG, colorB);
                    rasterizer.setColorC(colorR, colorG, colorB);
                    
                    //rasterizer.setTextureCoordinates(new Vec4(0.0d, 0.0d, 0.0d), new Vec4(1.0d, 0.0d, 0.0d), new Vec4(0.5d, 1.0d, 0.0d));
                    
                    rasterizer.setTextureCoordinates(vb.texCoordArray.get(i), vb.texCoordArray.get(i+1), vb.texCoordArray.get(i+2));
                    
                    rasterizer.setAtrsA(2, 255);
                    rasterizer.setAtrsA(3, 0);
                    rasterizer.setAtrsA(4, 0);
                    
                    rasterizer.setAtrsB(2, 0);
                    rasterizer.setAtrsB(3, 255);
                    rasterizer.setAtrsB(4, 0);
                    
                    rasterizer.setAtrsC(2, 0);
                    rasterizer.setAtrsC(3, 0);
                    rasterizer.setAtrsC(4, 255);
                    
                    rasterizer.drawTriangle();
                }
                
                break;
            default:
                throw new RuntimeException("Zatim je podporovan pouze trangleMode = SIMPLE");
        }
        
        return this;
    }
    
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
