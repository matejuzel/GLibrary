/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Viewer;

import GL.FrameBuffer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
/**
 *
 * @author matej uzel
 */
public class Viewer extends JPanel {
    
    //private BufferedImage buffer;
    private FrameBuffer buffer;
    
    public Viewer(int width, int height, FrameBuffer image) {
        
        this.buffer = image;

        JFrame frame = new JFrame("Graphic Library test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(width, height);
        frame.setVisible(true);
        
        //super.setDoubleBuffered(true);

    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.buffer != null) {
            // Draw the loaded image on the panel
            g.drawImage(this.buffer, 0, 0, this);
        }
    }
    
    @Override
    public void repaint() {
        
        super.repaint();
    }
    

}
