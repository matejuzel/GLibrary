/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.GLibrary;
import Math.MFloat;
import Math.Mtx4;
import Math.Utils;

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
        gLibrary.matrixPush(Mtx4.getProjectionPerspective(Math.toRadians(45), this.gLibrary.getWidth()/this.gLibrary.getHeight(), -0.5d, -5.0d), GLibrary.MatrixType.PROJECTION);
        gLibrary.matrixPush(Mtx4.getIdentity(), GLibrary.MatrixType.MODELVIEW);
        
        // umisteni objektu cube
        mtxCube0.loadIdentity().translate(1, 0,0);
        mtxCube1.loadIdentity().translate(-0.5, 0.4, 0).multiply(Mtx4.getRotationZ(2.8));
        
        // umisteni kamery
        mtxCamera.loadIdentity().translate(0, 0, -3);
        
        // vytvoreni vertex bufferu a vlozeni krychle do nej
        vbaCube0 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube0).addCube(0.9);
        
        /*gLibrary.getVertexBuffer(vbaCube0).addCube(0.8);
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
*/
        vbaCube1 = gLibrary.addVertexBuffer();
        gLibrary.getVertexBuffer(vbaCube1).addCube(0.9);
        
        System.out.println(gLibrary.toString());
    }

    @Override
    public void loopCallback() {
        // clear color buffer
        gLibrary.getFrameBuffer().clear();
        gLibrary.getDepthBuffer().clear(1.0d);
        
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
        //mtxCube1.multiply(Mtx4.getRotationY(0.003d));
        mtxCube1.translate(0, 0, -0.0000);
        
        //gLibrary.getFrameBuffer().getGraphics().drawString("test", 20, 20);
    }
    
    public static void main(String[] args) {

        
        //System.out.println(Integer.toBinaryString((0b10001101011 >> 2) & 0b11111111111));
        
        
        //Utils.test(1.0f/3.0f);
        //Utils.test(3.14159274101257324f);
        
        //ystem.out.println(Utils.multiplyByPowerOfTwo(87, 3));
        
        
        //System.out.println(Utils.remapFloatToByte(57f, -100.0f, 100.0f));
        
        
        /*
        System.out.println(Utils.floatToByte(-1.0f));
        System.out.println(Utils.floatToByte(1.0f));
        System.out.println(Utils.floatToByte(0.0f));
        System.out.println(Utils.floatToByte(0.1f));
        System.out.println(Utils.floatToByte(0.2f));
        System.out.println(Utils.floatToByte(0.3f));
        System.out.println(Utils.floatToByte(0.4f));
        System.out.println(Utils.floatToByte(0.5f));
        System.out.println(Utils.floatToByte(0.6f));
        */
        
        
        
        
        /*
        Utils.test(-1.0f);
        Utils.test(-0.9f);
        Utils.test(-0.8f);
        Utils.test(-0.7f);
        Utils.test(-0.6f);
        Utils.test(-0.5f);
        Utils.test(-0.4f);
        Utils.test(-0.3f);
        Utils.test(-0.2f);
        Utils.test(-0.1f);
        Utils.test(-0.0f);
        Utils.test(0.0f);
        Utils.test(0.1f);
        Utils.test(0.2f);
        Utils.test(0.3f);
        Utils.test(0.4f);
        Utils.test(0.5f);
        Utils.test(0.6f);
        Utils.test(0.7f);
        Utils.test(0.8f);
        Utils.test(0.9f);
        Utils.test(1.0f);*/
        
        
        
        float[] arr = {
            0.0f,
            1.0f,
            2.0f,
            3.0f,
            4.0f,
            5.0f,
            6.0f,
            7.0f,
            8.0f,
            9.0f,
            10.0f,
            11.0f,
            12.0f,
            13.0f,
            14.0f,
            15.0f,
            16.0f,
            32.0f,
            64.0f,
            
            79.0f,
            
            -128.0f,
            
            -37.49f,
            -37.51f,
            
            -72.012f,   
            -534.0f
        };
        for (int i=0; i<arr.length; i++) {
        
            MFloat mf = new MFloat(arr[i]); 
            System.out.println(arr[i] + " => " + mf.remapToInt(mf));
        }
        
        
        
        
        System.out.println("done");
        
        if (true) return;
        
        AbstractAppGL app = new AppGL(800, 600);
        app.initCallback();
        app.runLoop(5);
        //app.run();
        
    }
}
