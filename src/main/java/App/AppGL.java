/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.DepthBuffer.DepthBufferAbstract;
import GL.GLibrary;
import GL.Vertex;
import Math.MFloat;
import Math.Mtx4;
import Math.Utils;
import Math.Vec4;
import Texture.TextureNearest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public AppGL(int width, int height, int sleepMillis, int frames, int frameLimit, int frameOffset, boolean debug) {
        super(width, height, sleepMillis, frames, frameLimit, frameOffset, debug);
    }
    
    @Override
    public void initCallback() {
        
        // nastaveni vychozi barvy pozadi - clearColor
        gLibrary.getFrameBuffer().setClearColor(0, 0, 0);
        gLibrary.getDepthBuffer().setClearValue(-1000.0f);
        gLibrary.getDepthBuffer().setDepthFunction(DepthBufferAbstract.DepthFunction.GREATER);
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        gLibrary.setFaceCullingMode(GLibrary.FaceCullingMode.BACK);
        
        gLibrary.setMatrixModelView(Mtx4.getIdentity());
        
        //gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(10), gLibrary.getWidth()/gLibrary.getHeight(), -0.5d, -5.0d));
        //gLibrary.setMatrixViewPort(Mtx4.getViewport(width/2-100, height, 0, 0));
        
        hTexture0 = gLibrary.getTextureUnit().addTexture(new TextureNearest("data/tex32.png"));
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(1, 0, 0);
        mtxCube1.loadIdentity().translate(-0.5, 0.0, 0).multiply(Mtx4.getRotationZ(1));
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -2.3);
        mtxLookAt.loadLookAt(new Vec4(8,9,3,1), new Vec4(0,0,0,1), new Vec4(0,1,0,0));
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        //vbaCube1 = gLibrary.addVertexBuffer();
        
        gLibrary.getVertexBuffer(vbaCube0).addCube(1);
        
        /*
        gLibrary.getVertexBuffer(vbaCube0).addQuad(
            new Vertex(new Vec4(-2,-1,-1,1), new Vec4(0,0,0,1)),
            new Vertex(new Vec4(-2,-1, 1,1), new Vec4(0,1,0,1)),
            new Vertex(new Vec4(-2, 1, 1,1), new Vec4(1,1,0,1)),
            new Vertex(new Vec4(-2, 1,-1,1), new Vec4(1,0,0,1))        
        );
        */
        //gLibrary.getVertexBuffer(vbaCube1).addCube(0.9);
        
        //System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear();
        
        int w0, h0, w1, h1;
        w0 = width;
        h0 = height;
        //gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(20), w0/(double)h0, -0.01d, -8.0d));
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(20), w0/(double)h0, -0.5, -5.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(w0, h0, 0, 0));
        // objekt 1 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.TEXTURES);
        //gLibrary.render(vbaCube0);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.TEXTURES);
        //gLibrary.render(vbaCube1);
        
        // do object transformations
        mtxCube0.multiply(Mtx4.getRotationX(0.005d));
        mtxCube0.multiply(Mtx4.getRotationY(0.002d));
        mtxCube0.multiply(Mtx4.getRotationZ(-0.0002d));
        mtxCube0.translate(0, 0, 0.0000);
        
        
        mtxCube1.multiply(Mtx4.getRotationX(-0.005d));
        mtxCube1.multiply(Mtx4.getRotationY( 0.001d));
        mtxCube1.multiply(Mtx4.getRotationZ( 0.0002d));
        mtxCube1.translate(0.0001, 0, 0.000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
        
        //gLibrary.render2DTexture(new TextureNearest("data/tex256.png"));
    }
    
    @Override
    public void render() {
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.TEXTURES);
        gLibrary.render(vbaCube0);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.TEXTURES);
        gLibrary.render(vbaCube1);
    }
    
    public static void main(String[] args) {
        
        int scale = 1;
        
        int[] dim = new int[] {
            380 * scale,
            200 * scale
        };
        
        int sleepMillis = 5;
        int frames = 1000;
        int frameLimit = 1;
        int frameOffset = 204;
        boolean debug = true;
        
        AbstractAppGL app = new AppGL(dim[0], dim[1], sleepMillis, frames, frameLimit, frameOffset, debug);
        app.initCallback();
        app.runLoop();
    }
}
