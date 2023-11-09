/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.GLibrary;
import Viewer.Viewer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matej uzel
 */
public abstract class AbstractAppGL {
    
    GLibrary gLibrary = null;
    Viewer viewer = null;
    
    protected int width, height;
    
    public abstract void initCallback();
    
    public abstract void loopCallback();
    
    public AbstractAppGL(int width, int height) {
        this.width = width;
        this.height = height;
        gLibrary = new GLibrary(width, height);
        viewer = new Viewer(width+20, height+44, gLibrary.getFrameBuffer());
    }
    
    public void runLoop(int sleepMillis) {
        
        long time0, time1, time2;
        
        long fpsMean = 0;
        long fps = 0;
        
        time0 = System.currentTimeMillis();
        
        
        
        for (int i=1; i<10000; i++) {
            
            time1 = System.currentTimeMillis();
            
            
            
            //gLibrary.getFrameBuffer().getGraphics();
            /*
            Graphics g2dScene = viewer.getGraphics();
            g2dScene.setColor(Color.black);
            g2dScene.clearRect(1, 1, 100, 50);
            g2dScene.drawString("fps: "+fps, 20, 20);
            g2dScene.drawString("mean: "+fpsMean, 20, 40);
            */
            loopCallback();
            viewer.repaint();
            
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbstractAppGL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            time2 = System.currentTimeMillis();
            
            fpsMean = Math.round((double)i / (double)(time1-time0) * 1000);
            fps = Math.round((double)1 / (double)(time2-time1) * 1000);
            
        }
    }
    
    public void run() {
        
        this.loopCallback();
        viewer.repaint();
    }
}
