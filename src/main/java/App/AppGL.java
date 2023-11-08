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
import Texture.TextureNearest;

/**
 *
 * @author matej uzel
 */
public class AppGL extends AbstractAppGL {
    
    int vbaCube0, vbaCube1; // handler vertex buffer array
    
    int hTexture0, hTexture1;
    
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
        gLibrary.getDepthBuffer().setClearValue(1.0f);
        gLibrary.getDepthBuffer().setDepthFunction(DepthBufferAbstract.DepthFunction.LESS);
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        gLibrary.setFaceCullingMode(GLibrary.FaceCullingMode.BACK);
        
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(45), gLibrary.getWidth()/gLibrary.getHeight(), -0.5d, -5.0d));
        gLibrary.setMatrixModelView(Mtx4.getIdentity());
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(1, 0, 0);
        mtxCube1.loadIdentity().translate(-0.5, 0.4, 0).multiply(Mtx4.getRotationZ(2.8));
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -3);
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.9);

        vbaCube1 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube1).addCube(0.9);
        
        hTexture0 = gLibrary.getTextureUnit().addTexture(new TextureNearest("file0.png"));
        hTexture1 = gLibrary.getTextureUnit().addTexture(new TextureNearest("file1.png"));
        
        System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear();
        
        // objekt 1 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture0);
        gLibrary.vertexBufferRender(vbaCube0);
        
        // objekt 2 - krychle
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.getTextureUnit().setCurrentTexture(hTexture1);
        gLibrary.vertexBufferRender(vbaCube1);
        
        // do object transformations
        mtxCube0.multiply(Mtx4.getRotationX(0.005d));
        mtxCube0.multiply(Mtx4.getRotationY(0.005d));
        mtxCube0.translate(0, 0, -0.0000);
        
        mtxCube1.multiply(Mtx4.getRotationX(-0.004d));
        //mtxCube1.multiply(Mtx4.getRotationY(0.003d));
        mtxCube1.translate(0, 0, -0.0000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    public static void main(String[] args) {
 /*
        float[] arr = {
            0.0f,
            0.01f,
            0.001f,
            0.0001f,
            0.00001f,
            0.123456f,
            0.1f,
            0.2f,
            0.3f,
            0.4f,
            0.5f,
            0.6f,
            0.7f,
            0.8f,
            0.9f,
            1.0f
        };
        
        long samples = 100000000000L;
        
        int rawInt;
        float floatval = 0.34553f;
        
        float out1;
        int out2;
        
        long time0 = System.currentTimeMillis();
        
        for (long i=0; i<samples; i++) {
            
            //float fla =  1.0f / ( 1.0f/34.4224f + 1.0f/4.43233f );
            
            //out1 = floatval * 8388608;
        }
        
        long time1 = System.currentTimeMillis();
        
        for (long i=0; i<samples; i++) {
            
            //double fla =  1.0d / ( 1.0d/34.4224d + 1.0d/4.43233d );
            
            //rawInt = Float.floatToRawIntBits(floatval);
            //out2 = ((rawInt & 0x007fffff) | 0x00800000) >>> (-(((rawInt & 0x7f800000) >>> 23) - 127));
        }
        
        long time2 = System.currentTimeMillis();
        
        System.out.println((time1 - time0)+" vs. "+(time2 - time1));
        
        
        System.out.println("done");
        
        if (true) return;
        */
        AbstractAppGL app = new AppGL(100, 80);
        app.initCallback();
        app.runLoop(5);
        //app.run();
        
    }
}
