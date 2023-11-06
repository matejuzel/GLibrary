/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL.DepthBuffer;

/**
 *
 * @author matej uzel
 */
public class DepthBufferFixed24Bit extends DepthBufferAbstract {
    
    private int[] data;
    private int[] clearData;
    
    public DepthBufferFixed24Bit(int width, int height) {
        super(width, height);
        data = new int[width*height];
        clearData = new int[width*height];
    }
    
    
    @Override
    public void setClearValue(double value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean write(int x, int y, double newValue) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
