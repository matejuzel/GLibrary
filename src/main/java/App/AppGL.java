/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.GLibrary;
import Math.Mtx4;

/**
 *
 * @author matej uzel
 */
public class AppGL extends AbstractAppGL {
    
    int vbaCube0 = 0, vbaCube1 = 0; // handler vertex buffer array
    
    Mtx4 mtxCamera = new Mtx4();
    
    Mtx4 mtxCube0 = new Mtx4();
    Mtx4 mtxCube1 = new Mtx4();
    
    
    public AppGL(int width, int height) {
        super(width, height);
    }
    
    @Override
    public void initCallback() {
        
        // nastaveni vychozi barvy pozadi - clearColor
        gLibrary.getFrameBuffer().setClearColor(0, 0, 0);
        
        // nastaveni projekcni a model-view matice
        //gLibrary.matrixPush(Mtx4.getProjectionPerspective(-0.1d, 0.1d, -0.1d, 0.1d, -0.1d, -10.0d), GLibrary.MatrixType.PROJECTION);
        gLibrary.matrixPush(Mtx4.getProjectionPerspective(Math.toRadians(20), this.gLibrary.getWidth()/this.gLibrary.getHeight(), -0.1d, -10.0d), GLibrary.MatrixType.PROJECTION);
        gLibrary.matrixPush(Mtx4.getIdentity(), GLibrary.MatrixType.MODELVIEW);
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(1, 0,0);
        mtxCube1.loadIdentity().translate(-2, 0, 0);
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -3);
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);

        vbaCube1 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube1).addCube(0.3);
        
        System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        
        // set modelview matrix
        gLibrary.matrixSet(mtxCamera.getOrthonormalInverted().multiply(mtxCube0), GLibrary.MatrixType.MODELVIEW);
        gLibrary.vertexBufferRender(vbaCube0);
        
        gLibrary.matrixSet(mtxCamera.getOrthonormalInverted().multiply(mtxCube1), GLibrary.MatrixType.MODELVIEW);
        gLibrary.vertexBufferRender(vbaCube1);
        
        // do object transformations
        mtxCube0.multiply(Mtx4.getRotationX(0.005d));
        mtxCube0.multiply(Mtx4.getRotationY(0.005d));
        mtxCube0.translate(0, 0, -0.0000);
        
        mtxCube1.multiply(Mtx4.getRotationX(-0.004d));
        mtxCube1.multiply(Mtx4.getRotationY(0.003d));
        mtxCube1.translate(0, 0, -0.0000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    public static void main(String[] args) {
        
        AbstractAppGL app = new AppGL(800, 600);
        app.initCallback();
        app.runLoop(5);
        //app.run();
        
    }
}
