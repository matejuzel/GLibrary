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
import Texture.AbstractTexture;
import Texture.TextureNearest;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author matej uzel
 */
public class GLibrary {

    public enum MatrixType {
        MODELVIEW,
        PROJECTION
    }
    
    public enum FaceCullingMode {
        FRONT,
        BACK,
        FRONT_AND_BACK
    }
    
    public enum PrimitiveMode {
        POINTS,
        LINES,
        SOLID,
        TEXTURED
    }
    
    FrameBuffer frameBuffer;
    DepthBufferAbstract depthBuffer;
    RasterizerAbstract rasterizer;
    
    Mtx4 mtxViewPort = new Mtx4();
    
    Stack<Mtx4> stackMatrixModelView = new Stack<>();
    Stack<Mtx4> stackMatrixProjection = new Stack<>();
    
    ArrayList<VertexBuffer> vertexBufferArray = new ArrayList<>();
    
    
    // odkladiste pro transformovane vertexy
    Vec4[] vertexBufferWork = new Vec4[1024];
    
    //TriangleMode triangleMode = TriangleMode.SIMPLE;
    
    public GLibrary(int width, int height) {
        
        depthBuffer = new DepthBufferDouble(width, height);
        frameBuffer = new FrameBuffer(width, height);
        
        stackMatrixModelView.push(new Mtx4());
        stackMatrixProjection.push(new Mtx4());
        mtxViewPort.loadViewport(width, height);
        
        AbstractTexture texture = new TextureNearest(256, 256);
        //rasterizer = new RasterizerLines(frameBuffer, 0, texture);
        rasterizer = new RasterizerSolid(frameBuffer, depthBuffer, texture);
        
        for (int i=0; i<vertexBufferWork.length; i++) {
            vertexBufferWork[i] = new Vec4(0.0d, 0.0d, 0.0d, 0.0d);
        }
    }
    
    public GLibrary vertexBufferRender(int handler) {
        /*
        rasterizer.setVertexCoordinates(new Vec4(34, 50, 0, 1), new Vec4(254, 70, 0, 1), new Vec4(100, 400, 0, 1));
        rasterizer.setColorA(255, 0, 0);
        rasterizer.setColorB(0, 255, 0);
        rasterizer.setColorC(0, 0, 255);
        rasterizer.drawTriangle();
        */
        
        VertexBuffer vb = this.vertexBufferArray.get(handler);
        
        Mtx4 mtx = Mtx4.getIdentity();
        mtx.multiply(this.getMatrix(MatrixType.PROJECTION));
        mtx.multiply(this.getMatrix(MatrixType.MODELVIEW));
        
        
        
        
        for (int i=0; i<vb.vertexArray.size(); i++) {
            
            
            
            this.vertexBufferWork[i].setData(vb.vertexArray.get(i));
            this.vertexBufferWork[i].transform(mtx).divideByW();
        }
        
        
        int[] colors = {
            255,0,0,
            255,0,0,
            0,255,0,
            0,255,0,
            0,0,255,
            0,0,255,
            
            255,255,0,
            255,255,0,
            255,0,255,
            255,0,255,
            255,255,255,
            255,255,255,
        };
        
        
        switch (vb.triangleMode) {
            case SIMPLE:
                
                for (int i=0; i<vb.vertexArray.size(); i+=3) {
                    
                    int colorR = colors[i%36];
                    int colorG = colors[i%36+1];
                    int colorB = colors[i%36+2];
                    
                    Vec4 vertA = this.vertexBufferWork[i];
                    Vec4 vertB = this.vertexBufferWork[i+1];
                    Vec4 vertC = this.vertexBufferWork[i+2];
                    
                    vertA.transform(mtxViewPort);
                    vertB.transform(mtxViewPort);
                    vertC.transform(mtxViewPort);
                    
                    rasterizer.setVertexCoordinates(vertA, vertB, vertC);
                    rasterizer.setColorA(colorR, colorG, colorB);
                    rasterizer.setColorB(colorR, colorG, colorB);
                    rasterizer.setColorC(colorR, colorG, colorB);
                    
                    rasterizer.setTextureCoordinates(new Vec4(0.0d, 0.0d, 0.0d), new Vec4(1.0d, 0.0d, 0.0d), new Vec4(0.5d, 1.0d, 0.0d));
                    rasterizer.drawTriangle();
                }
                
                break;
            default:
                throw new RuntimeException("Zatim je podporovan pouze trangleMode = SIMPLE");
        }
        
        return this;
    }
    
    public GLibrary matrixSet(Mtx4 matrix, MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                stackMatrixModelView.peek().setData(matrix);
                break;
            case PROJECTION:
                stackMatrixProjection.peek().setData(matrix);
                break;
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
        return this;
    }
    
    public GLibrary matrixLoadIdentity(MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                stackMatrixModelView.peek().loadIdentity();
                break;
            case PROJECTION:
                stackMatrixProjection.peek().loadIdentity();
                break;
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
        return this;
    }
    
    public GLibrary matrixMultiply(Mtx4 matrix, MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                stackMatrixModelView.peek().multiply(matrix);
                break;
            case PROJECTION:
                stackMatrixProjection.peek().multiply(matrix);
                break;
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
        return this;
    }
    
    public GLibrary matrixPush(Mtx4 matrix, MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                stackMatrixModelView.push(matrix);
                break;
            case PROJECTION:
                stackMatrixProjection.push(matrix);
                break;
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
        return this;
    }
    
    public GLibrary matrixPop(Mtx4 matrix, MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                stackMatrixModelView.pop();
                break;
            case PROJECTION:
                stackMatrixProjection.pop();
                break;
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
        return this;
    }
    public Mtx4 getMatrix(MatrixType matrixType) {
        
        switch (matrixType) {
            case MODELVIEW:
                return stackMatrixModelView.peek();
            case PROJECTION:
                return stackMatrixProjection.peek();
            default:
                throw new UnsupportedOperationException("setMatrix() nespravny typ matice");
        }
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
    
    
    @Override
    public String toString() {
        
        int sum = 0;
        for (int i=0; i<vertexBufferArray.size(); i++) {
            sum += vertexBufferArray.get(i).vertexArray.size();
        }
        
        return "pocet vertexu: "+sum;
    }
}
