/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import FileFormat.Obj.Obj;
import GL.DepthBuffer.DepthBufferAbstract;
import GL.GLibrary;
import Math.Mtx4;
import Math.Vec4;
import Shader.FragmentShader;
import Shader.FragmentShaderFlat;
import Shader.FragmentShaderTextureSimple;
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
    
    int frame = 0;
    
    
    FragmentShaderTextureSimple fragmentShader01 = new FragmentShaderTextureSimple(new TextureNearest("data/textures/rock128.jpg"));
    //FragmentShader fragmentShader02 = new FragmentShaderTextureSimple(new TextureNearest("data/sach8.png"));
    FragmentShader fragmentShader02 = new FragmentShaderTextureSimple(new TextureNearest("data/textures/uhl128.png"));
    
    public AppGL(int width, int height, int sleepMillis, int frames, int frameLimit, int frameOffset, boolean debug) {
        super(width, height, sleepMillis, frames, frameLimit, frameOffset, debug);
        
    }
    
    @Override
    public void initCallback() {
        
        // nastaveni vychozi barvy pozadi - clearColor
        gLibrary.getFrameBuffer().setClearColor(0, 0, 0);
        gLibrary.getDepthBuffer().setClearValue(-2.0f);
        gLibrary.getDepthBuffer().setDepthFunction(DepthBufferAbstract.DepthFunction.GREATER);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        gLibrary.setFaceCullingMode(GLibrary.FaceCullingMode.NONE);
        
        gLibrary.setMatrixModelView(Mtx4.getIdentity());
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(0, 0, -0.6);
        mtxCube1.loadIdentity().translate(0, 1, -0.6);
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -2.8);
        //mtxLookAt.loadLookAt(new Vec4(8,1,3,1), new Vec4(0,0,0,1), new Vec4(0,1,0,0));
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        vbaCube1 = gLibrary.addVertexBuffer();
        
        gLibrary.getVertexBuffer(vbaCube1).addCube(0.4);
        
        
        try {
            Obj model = new Obj("data/models/land2.obj");
            gLibrary.getVertexBuffer(vbaCube0).addMesh(model.getFaces());
            
        } catch (IOException ex) {
            Logger.getLogger(AppGL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear();
        
        int w0, h0;
        w0 = width;
        h0 = height;
        
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(20), w0/(double)h0, -0.5, -10.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(w0, h0, 0, 0));
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        
        mtxCube0.multiply(Mtx4.getRotationY(0.002d));
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        mtxCube1.multiply(Mtx4.getRotationX(-0.005d));
        mtxCube1.multiply(Mtx4.getRotationY( 0.001d));
        mtxCube1.multiply(Mtx4.getRotationZ( 0.02d));
        mtxCube1.translate(0.0001, 0, 0.000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    @Override
    public void render() {
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.TEXTURES);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));        
        gLibrary.render(vbaCube0, fragmentShader01);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.render(vbaCube1, fragmentShader02);
        
        fragmentShader01.frameInc();
        frame++;
    }
    
    public static void scene01(double ratio, int height) {
        
        int width = (int) Math.round(ratio * height);
        int sleepMillis = 5;
        int frames = 1000;
        int frameLimit = 1000;
        int frameOffset = 0;
        boolean debug = false;
        
        AbstractAppGL app = new AppGL(width, height, sleepMillis, frames, frameLimit, frameOffset, debug);
        app.initCallback();
        app.runLoop();
    }
    
    public static void main(String[] args) {
        
        scene01(24/9.0, 950);
    }
}
