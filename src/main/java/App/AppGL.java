/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.GLibrary;
import Math.MFloat;
import Math.Mtx4;
import Math.Utils;
import Math.Vec4;
import Texture.TextureNearest;

/**
 *
 * @author matej uzel
 */
public class AppGL extends AbstractAppGL {
    
    int vbaCube0, vbaCube1; // handler vertex buffer array
    
    int hTexture0, hTexture1;
    
    Mtx4 mtxCamera = new Mtx4();
    Mtx4 mtxLookAt = new Mtx4();
    Mtx4 mtxCube0 = new Mtx4();
    Mtx4 mtxCube1 = new Mtx4();
    
    public AppGL(int width, int height) {
        super(width, height);
    }
    
    @Override
    public void initCallback() {
        
        // nastaveni vychozi barvy pozadi - clearColor
        gLibrary.getFrameBuffer().setClearColor(0, 0, 0);
        gLibrary.getDepthBuffer().setClearValue(1.0f);
        gLibrary.getDepthBuffer().setDepthFunction(DepthBufferAbstract.DepthFunction.LESS);
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.LINES);
        gLibrary.setFaceCullingMode(GLibrary.FaceCullingMode.BACK);
        
        gLibrary.setMatrixModelView(Mtx4.getIdentity());
        
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(45), gLibrary.getWidth()/gLibrary.getHeight(), -0.5d, -5.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(width/2-10, height, 0, 0));
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(1, 0, 0);
        mtxCube1.loadIdentity().translate(-0.5, 0.0, 0).multiply(Mtx4.getRotationZ(0));
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -3);
        
        mtxLookAt.loadLookAt(new Vec4(8,9,3,1), new Vec4(0,0,0,1), new Vec4(0,1,0,0));
        
        System.out.println(mtxLookAt);
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.9);

        vbaCube1 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube1).addCube(0.9);
        
        hTexture0 = gLibrary.getTextureUnit().addTexture(new TextureNearest("data/tex0.png"));
        hTexture1 = gLibrary.getTextureUnit().addTexture(new TextureNearest("data/tex0.png"));
        
        System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear();
        
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(45), gLibrary.getWidth()/gLibrary.getHeight(), -0.5d, -20.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(width/2-10, height, 0, 0));
        // objekt 1 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.render(vbaCube0);
        
        // objekt 2 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture1);
        gLibrary.render(vbaCube1);
        
        // obraz 2
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(48), gLibrary.getWidth()/gLibrary.getHeight(), -0.5d, -20.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(width/2-10, height, width/2, 0));
        // objekt 1 - krychle
        gLibrary.setMatrixModelView(new Mtx4(mtxLookAt).multiply(mtxCube0));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.render(vbaCube0);
        
        // objekt 2 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture1);
        gLibrary.render(vbaCube1);
        
        
        
        // do object transformations
        mtxCube0.multiply(Mtx4.getRotationX(0.005d));
        mtxCube0.multiply(Mtx4.getRotationY(0.005d));
        mtxCube0.translate(0, 0, -0.0000);
        
        mtxCube1.multiply(Mtx4.getRotationX(-0.004d));
        //mtxCube1.multiply(Mtx4.getRotationY(0.003d));
        mtxCube1.translate(0, 0, -0.0000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    public static void runAnimation() {
        AbstractAppGL app = new AppGL(400, 150);
        app.initCallback();
        app.runLoop(5);
    }
    
    public static void runWork() {
        
        Mtx4 m = new Mtx4();
        m.loadLookAt(new Vec4(5,0,-10,1), new Vec4(0,0,0,1), new Vec4(0,1,0,0));
        m = m.getOrthonormalInverted();
        
        System.out.println(m);
    }
    
    public static void main(String[] args) {
        //runAnimation();
        runWork();
    }
}
