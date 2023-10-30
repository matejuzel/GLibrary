/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

import GL.GLibrary;
import Viewer.Viewer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matej uzel
 */
public abstract class AbstractAppGL {
    
    GLibrary gLibrary = null;
    Viewer viewer = null;
    
    public abstract void initCallback();
    
    public abstract void loopCallback();
    
    public AbstractAppGL(int width, int height) {
        gLibrary = new GLibrary(width, height);
        viewer = new Viewer(width+20, height+44, gLibrary.getFrameBuffer());
    }
    
    public void runLoop(int sleepMillis) {
        
        for (int i=0; i<10000; i++) {
            
            loopCallback();
            viewer.repaint();
            
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbstractAppGL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void run() {
        
        this.loopCallback();
        viewer.repaint();
    }
}
