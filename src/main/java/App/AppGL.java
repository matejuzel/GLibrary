/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import FileFormat.Obj.Obj;
import GL.DepthBuffer.DepthBufferAbstract;
import GL.GLibrary;
import Geometry.Vertex;
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
    
    int vbaMesh0, vbaMesh1; // handler vertex buffer array
    
    Mtx4 mtxCamera = new Mtx4();
    Mtx4 mtxCube0 = new Mtx4();
    Mtx4 mtxCube1 = new Mtx4();
    
    int frame = 0;
    
    FragmentShader fragmentShader01, fragmentShader02;
    
    public AppGL(int width, int height, int sleepMillis, int frames, int frameLimit, int frameOffset, boolean debug) {
        super(width, height, sleepMillis, frames, frameLimit, frameOffset, debug);
    }
    
    @Override
    public void initCallback() {
        
        // nastaveni vychozi barvy pozadi - clearColor
        gLibrary.getFrameBuffer().setClearColor(22, 65, 124);
        gLibrary.getDepthBuffer().setClearValue(-6.0f);
        gLibrary.getDepthBuffer().setDepthFunction(DepthBufferAbstract.DepthFunction.GREATER);
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        gLibrary.setFaceCullingMode(GLibrary.FaceCullingMode.BACK);
        
        gLibrary.setMatrixModelView(Mtx4.getIdentity());
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(0, 0, 0);
        mtxCube1.loadIdentity().translate(-1.8, 0, -1);
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, 0);
        //mtxCamera.loadLookAt(new Vec4(5.6,2,-2,1), new Vec4(0,0,0,1), new Vec4(0,-1,0,0));
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaMesh0 = gLibrary.addVertexBuffer();
        vbaMesh1 = gLibrary.addVertexBuffer();
        
        fragmentShader01 = new FragmentShaderTextureSimple(new TextureNearest("data/textures/rock128.jpg"));
        fragmentShader02 = new FragmentShaderTextureSimple(new TextureNearest("data/textures/stone.png"));
        
        try {
            gLibrary.getVertexBuffer(vbaMesh0).addMesh(new Obj("data/models/obj/land2.obj").getFaces());
            //gLibrary.getVertexBuffer(vbaMesh1).addMesh(new Obj("data/models/obj/stone.obj").getFaces());
            gLibrary.getVertexBuffer(vbaMesh1).addQuad(
                    new Vertex(new Vec4(-1,-1,0,1), new Vec4(0,0,0,1)), 
                    new Vertex(new Vec4( 1,-1,0,1), new Vec4(1,0,0,1)), 
                    new Vertex(new Vec4( 1, 1,0,1), new Vec4(1,1,0,1)),
                    new Vertex(new Vec4(-1, 1,0,1), new Vec4(0,1,0,1))
                    );
            
        } catch (IOException ex) {
            Logger.getLogger(AppGL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        gLibrary.setMatrixProjection(Mtx4.getProjectionPerspective(Math.toRadians(20), width/(double)height, -0.05, -10.0d));
        gLibrary.setMatrixViewPort(Mtx4.getViewport(width, height, 0, 0));
        
        //System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear();
        
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));
        
        mtxCube0.multiply(Mtx4.getRotationY(0.002d));
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        //mtxCube1.multiply(Mtx4.getRotationX(-0.005d));
        //mtxCube1.multiply(Mtx4.getRotationY( 0.001d));
        mtxCube1.multiply(Mtx4.getRotationZ( 0.02d));
        //mtxCube1.translate(-0.00, 0, 0.000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    @Override
    public void render() {
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube0));        
        gLibrary.render(vbaMesh0, fragmentShader01);
        
        gLibrary.setPrimitiveMode(GLibrary.PrimitiveMode.SOLID);
        
        gLibrary.setMatrixModelView(mtxCamera.getOrthonormalInverted().multiply(mtxCube1));
        gLibrary.render(vbaMesh1, fragmentShader02);
        
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
        
        scene01(16/9.0, 250);
    }
}
