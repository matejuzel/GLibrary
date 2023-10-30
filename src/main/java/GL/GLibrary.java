/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package GL;

import Math.Mtx4;
import Math.Vec4;
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
    
    
    
    FrameBuffer frameBuffer;
    
    Mtx4 mtxViewPort = new Mtx4();
    
    Stack<Mtx4> stackMatrixModelView = new Stack<>();
    Stack<Mtx4> stackMatrixProjection = new Stack<>();
    
    ArrayList<VertexBuffer> vertexBufferArray = new ArrayList<>();
    
    
    // odkladiste pro transformovane vertexy
    Vec4[] vertexBufferWork = new Vec4[1024];
    
    //TriangleMode triangleMode = TriangleMode.SIMPLE;
    
    public GLibrary(int width, int height) {
        this.frameBuffer = new FrameBuffer(width, height);
        
        stackMatrixModelView.push(new Mtx4());
        stackMatrixProjection.push(new Mtx4());
        mtxViewPort.loadViewport(width, height);
        
        for (int i=0; i<vertexBufferWork.length; i++) {
            vertexBufferWork[i] = new Vec4(0.0d, 0.0d, 0.0d, 0.0d);
        }
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
    
    public GLibrary vertexBufferRender(int handler) {
        
        VertexBuffer vb = this.vertexBufferArray.get(handler);
        
        Mtx4 mtx = Mtx4.getIdentity();
        mtx.multiply(this.getMatrix(MatrixType.PROJECTION));
        mtx.multiply(this.getMatrix(MatrixType.MODELVIEW));
        
        
        
        
        for (int i=0; i<vb.vertexArray.size(); i++) {
            
            this.vertexBufferWork[i].setData(vb.vertexArray.get(i));
            this.vertexBufferWork[i].transform(mtx).divideByW();
        }
        
        switch (vb.triangleMode) {
            case SIMPLE:
                
                for (int i=0; i<vb.vertexArray.size(); i+=3) {
                    
                    Vec4 vertA = this.vertexBufferWork[i];
                    Vec4 vertB = this.vertexBufferWork[i+1];
                    Vec4 vertC = this.vertexBufferWork[i+2];
                    
                    vertA.transform(mtxViewPort);
                    vertB.transform(mtxViewPort);
                    vertC.transform(mtxViewPort);
                    
                    this.frameBuffer.putPixel((int)vertA.getX(), (int) vertA.getY(), 255, 200, 200, 4);
                    this.frameBuffer.putPixel((int)vertB.getX(), (int) vertB.getY(), 255, 200, 200, 4);
                    this.frameBuffer.putPixel((int)vertC.getX(), (int) vertC.getY(), 255, 200, 200, 4);
                    
                    this.frameBuffer.putTriangle(vertA, vertB, vertC, 255, 200, 200);
                    /*
                    this.frameBuffer.putLine(
                            (int) Math.round(vertA.getX()), 
                            (int) Math.round(vertA.getY()), 
                            (int) Math.round(vertB.getX()), 
                            (int) Math.round(vertB.getY()), 255, 200, 200
                    );
                    this.frameBuffer.putLine(
                            (int) Math.round(vertB.getX()), 
                            (int) Math.round(vertB.getY()), 
                            (int) Math.round(vertC.getX()), 
                            (int) Math.round(vertC.getY()), 255, 200, 200
                    );
                    this.frameBuffer.putLine(
                            (int) Math.round(vertC.getX()), 
                            (int) Math.round(vertC.getY()), 
                            (int) Math.round(vertA.getX()), 
                            (int) Math.round(vertA.getY()), 255, 200, 200
                    );*/
                }
                
                break;
            default:
                throw new RuntimeException("Zatim je podporovan pouze trangleMode = SIMPLE");
        }
        
        return this;
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
    public int getWidth() {
        return this.frameBuffer.getWidth();
    }
    public int getHeight() {
        return this.frameBuffer.getHeight();
    }
    
    
}
