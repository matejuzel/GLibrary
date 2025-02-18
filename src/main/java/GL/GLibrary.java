/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package GL;

import Geometry.VertexBuffer;
import GL.DepthBuffer.DepthBufferAbstract;
import GL.DepthBuffer.DepthBufferDouble;
import Geometry.Face;
import Geometry.Mesh;
import Geometry.Vertex;
import Math.Mtx4;
import Math.Utils;
import Math.Vec4;
import Rasterizer.RasterizerAbstract;
import Rasterizer.RasterizerTextures;
import Shader.FragmentShader;
import Shader.FragmentShaderFlat;
import Shader.FragmentShaderTextureSimple;
import Texture.TextureAbstract;
import java.util.ArrayList;

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
    private final RasterizerAbstract rasterizer;
    private FragmentShader fragmentShader;
    
    private FaceCullingMode faceCullingMode = FaceCullingMode.NONE;
    private PrimitiveMode primitiveMode = PrimitiveMode.SOLID;
    
    private Mtx4 matrixModelView = new Mtx4();
    private Mtx4 matrixProjection = new Mtx4();
    private Mtx4 matrixViewPort = new Mtx4();
    
    private ArrayList<VertexBuffer> vertexBufferArray = new ArrayList<>();
    
    private final TextureUnit textureUnit = new TextureUnit();
    private Mtx4 matrixFinal = new Mtx4(); // pracovni matice - zde bude soucit modelview a projection
    
    public GLibrary(int width, int height, boolean debug) {
        
        TextureAbstract texture = textureUnit.getCurrentTexture();
        
        matrixViewPort.loadViewport(width, height, 0, 0);
        depthBuffer = new DepthBufferDouble(width, height);
        frameBuffer = new FrameBuffer(width, height);
        rasterizer = new RasterizerTextures(frameBuffer, depthBuffer, fragmentShader, 5, false); //rasterizer = new RasterizerPlain(frameBuffer, depthBuffer, fragmentShader);
        
        this.debug = debug;
    }
    
    public void setFragmentShader(FragmentShader fragmentShader) {
        this.fragmentShader = fragmentShader;
    }
    
    public GLibrary render(Mesh mesh, Mtx4 modelViewMatrix) {
        
        matrixFinal.loadIdentity();
        matrixFinal.multiply(matrixProjection);
        matrixFinal.multiply(matrixModelView);
        matrixFinal.multiply(mesh.getTransformationMatrix());
        
        //System.out.println(matrixFinal);
        
        rasterizer.setFragmentShader(mesh.getFragmentShader());
        
        switch (primitiveMode) {
            case LINES:
                rasterizer.setLinesFlag(true);
                break;
            case SOLID:
                rasterizer.setLinesFlag(false);
                break;
            case TEXTURES:
                rasterizer.setLinesFlag(false);
                break;
            case POINTS:
                throw new UnsupportedOperationException("primitiveMode=POINTS neni podporovan.");
            default:
                throw new UnsupportedOperationException("primitiveMode neni nastaven na validni hodnotu.");
        }
        
        for (Face face : mesh.getFaces()) {
            
            Vertex vertexA = face.getVertexA();
            Vertex vertexB = face.getVertexB();
            Vertex vertexC = face.getVertexC();
            
            Vec4 a = new Vec4(vertexA.getVertex()).transform(matrixFinal);
            Vec4 b = new Vec4(vertexB.getVertex()).transform(matrixFinal);
            Vec4 c = new Vec4(vertexC.getVertex()).transform(matrixFinal);
            
            Vec4 lightDirection = new Vec4(0,0,-1,0);
            lightDirection = lightDirection.transform(matrixModelView.getOrthonormalInverted());
                    
            // CLIP SPACE



            if (a.getX() >= a.getW()) continue;
            if (b.getX() >= b.getW()) continue;
            if (c.getX() >= c.getW()) continue;

            int mask = 0;
            if (a.getX() >= a.getW()) mask |= 1;
            if (b.getX() >= b.getW()) mask |= 2;
            if (c.getX() >= c.getW()) mask |= 4;



            if (a.getX() <= -a.getW()) continue;
            if (b.getX() <= -b.getW()) continue;
            if (c.getX() <= -c.getW()) continue;

            if (a.getY() >= a.getW()) continue;
            if (b.getY() >= b.getW()) continue;
            if (c.getY() >= c.getW()) continue;

            if (a.getY() <= -a.getW()) continue;
            if (b.getY() <= -b.getW()) continue;
            if (c.getY() <= -c.getW()) continue;

            if (a.getZ() >= a.getW()) continue;
            if (b.getZ() >= b.getW()) continue;
            if (c.getZ() >= c.getW()) continue;

            if (a.getZ() <= -a.getW()) continue;
            if (b.getZ() <= -b.getW()) continue;
            if (c.getZ() <= -c.getW()) continue;

            a.divideByW();
            b.divideByW();
            c.divideByW();

            // pro cross product
            double vABx = b.getX() - a.getX();
            double vABy = b.getY() - a.getY();
            double vACx = c.getX() - a.getX();
            double vACy = c.getY() - a.getY();

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

            a.transform(matrixViewPort);
            b.transform(matrixViewPort);
            c.transform(matrixViewPort);

            // RASTERIZACE:
            rasterizer.setParams(
                    a.getX(), a.getY(), vertexA.getVertex().getW(),
                    b.getX(), b.getY(), vertexB.getVertex().getW(),
                    c.getX(), c.getY(), vertexC.getVertex().getW(),

                    lightDirection.getX(),
                    lightDirection.getY(),
                    lightDirection.getZ(),

                    this.getMatrixProjection().getData(11), // normalizacni z-faktor: -2*n*f + (n+f)/(f-n)
                    this.getMatrixProjection().getData(10), // normalizacni z-offset: (n+f)/(f-n)

                    new double[] {
                        vertexA.getTextureCoord().getX(),
                        vertexA.getTextureCoord().getY(),
                        vertexA.getNormal().getX(), 
                        vertexA.getNormal().getY(), 
                        vertexA.getNormal().getZ()
                    },
                    new double[] {
                        vertexB.getTextureCoord().getX(),
                        vertexB.getTextureCoord().getY(),
                        vertexB.getNormal().getX(), 
                        vertexB.getNormal().getY(), 
                        vertexB.getNormal().getZ()
                    },
                    new double[] {
                        vertexC.getTextureCoord().getX(),
                        vertexC.getTextureCoord().getY(),
                        vertexC.getNormal().getX(), 
                        vertexC.getNormal().getY(), 
                        vertexC.getNormal().getZ()
                    }
            );

            rasterizer.drawTriangle();
            
            
        }
        
        
        
        return this;
    }
    
    
    public GLibrary render(int handler, FragmentShader fragmentShader) {
        
        VertexBuffer vb = this.vertexBufferArray.get(handler);
        
        rasterizer.setFragmentShader(fragmentShader);
        
        matrixFinal.loadIdentity();
        matrixFinal.multiply(matrixProjection);
        matrixFinal.multiply(matrixModelView);
        
        switch (primitiveMode) {
            case LINES:
                rasterizer.setLinesFlag(true);
                break;
            case SOLID:
                rasterizer.setLinesFlag(false);
                break;
            case TEXTURES:
                rasterizer.setLinesFlag(false);
                break;
            case POINTS:
                throw new UnsupportedOperationException("primitiveMode=POINTS neni podporovan.");
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
                    
                    /*
                    Vec4 vec_a = new Vec4(vertB.getX() - vertA.getX(), vertB.getY() - vertA.getY(), vertB.getZ() - vertA.getZ(), 1.0d);
                    Vec4 vec_b = new Vec4(vertC.getX() - vertB.getX(), vertC.getY() - vertB.getY(), vertC.getZ() - vertB.getZ(), 1.0d);
                    
                    Vec4 norm = new Vec4(
                            vec_a.getX()*vec_b.getY() - vec_b.getX()*vec_a.getY(), // ax*by - bx*ay
                            vec_a.getY()*vec_b.getZ() - vec_b.getY()*vec_a.getZ(), // ay*bz - by*az
                            vec_a.getZ()*vec_b.getX() - vec_b.getZ()*vec_a.getX(), // az*bx - bz*ax
                            1
                    );
                    
                    Vec4 normalA = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 0.0d);
                    Vec4 normalB = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 0.0d);
                    Vec4 normalC = new Vec4(norm.getX(), norm.getY(), norm.getZ(), 0.0d);
                    */
                    
                    Vec4 normalA = vb.normalArray.get(i);
                    Vec4 normalB = vb.normalArray.get(i+1);
                    Vec4 normalC = vb.normalArray.get(i+2);
                    
                    Vec4 texCoordA = new Vec4(vb.texCoordArray.get(i));
                    Vec4 texCoordB = new Vec4(vb.texCoordArray.get(i+1));
                    Vec4 texCoordC = new Vec4(vb.texCoordArray.get(i+2));
                    
                    Vec4 lightDirection = new Vec4(0,0,-1,0);
                    lightDirection = lightDirection.transform(matrixModelView.getOrthonormalInverted());
                    
                    vertA.transform(matrixFinal);
                    vertB.transform(matrixFinal);
                    vertC.transform(matrixFinal);
                    
                    // CLIP SPACE
                    
                    double wA = vertA.getW();
                    double wB = vertB.getW();
                    double wC = vertC.getW();
                    
                    
                    /*
                    if (vertA.getX() >= vertA.getW()) continue;
                    if (vertB.getX() >= vertB.getW()) continue;
                    if (vertC.getX() >= vertC.getW()) continue;
                    //*/
                    
                    int mask = 0;
                    if (vertA.getX() >= vertA.getW()) mask |= 1;
                    if (vertB.getX() >= vertB.getW()) mask |= 2;
                    if (vertC.getX() >= vertC.getW()) mask |= 4;
                    
                    
                    //*
                    switch (mask) {
                        case 7: // out: A,B,C
                            continue;
                        case 3:
                            // in:  C
                            // out: A,B
                            vertA = Utils.cutRight(vertC, vertA);
                            vertB = Utils.cutRight(vertC, vertB);
                            
                        case 4:
                            // in: A,B
                            // out: C
                            vertC = Utils.cutRight(vertA, vertC);
                            break;
                        case 5:
                            // in:  B
                            // out: A,C
                            vertA = Utils.cutRight(vertB, vertA);
                            vertC = Utils.cutRight(vertB, vertC);
                        case 2:
                            // in: A,C
                            // out: B
                            vertB = Utils.cutRight(vertB, vertA);
                            break;
                        case 6:
                            // in:  A
                            // out: B,C
                            vertB = Utils.cutRight(vertA, vertB);
                            vertC = Utils.cutRight(vertA, vertC);
                        case 1:
                            // in: B,C
                            // out: A
                            vertA = Utils.cutRight(vertA, vertB);
                            break;
                    }
                    //*/
                    
                    if (vertA.getX() <= -vertA.getW()) continue;
                    if (vertB.getX() <= -vertB.getW()) continue;
                    if (vertC.getX() <= -vertC.getW()) continue;
                    
                    if (vertA.getY() >= vertA.getW()) continue;
                    if (vertB.getY() >= vertB.getW()) continue;
                    if (vertC.getY() >= vertC.getW()) continue;
                    
                    if (vertA.getY() <= -vertA.getW()) continue;
                    if (vertB.getY() <= -vertB.getW()) continue;
                    if (vertC.getY() <= -vertC.getW()) continue;
                    
                    if (vertA.getZ() >= vertA.getW()) continue;
                    if (vertB.getZ() >= vertB.getW()) continue;
                    if (vertC.getZ() >= vertC.getW()) continue;
                    
                    if (vertA.getZ() <= -vertA.getW()) continue;
                    if (vertB.getZ() <= -vertB.getW()) continue;
                    if (vertC.getZ() <= -vertC.getW()) continue;
                    
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


    (a,b) => (0,1)
    f(x) = x/(b-a) - a/(b-a)

    (0,1) => (a,b)
    f(x) = (b-a)*x + a

    (p,q) => (m,n)
    f(x) = (n-m)/(q-p)*x - p*(n-m)/(q-p) + m

    (a,b) => (-1,1)
    f(x) = 2/(b-a)*x - (a+b)/(b-a)
    
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
