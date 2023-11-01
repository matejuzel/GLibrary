/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

/**
 *
 * @author Matej
 */
public class DepthBuffer {
    
    private double[] data;
    int width;
    int height;
    
    public DepthBuffer(int width, int height) {
        
        this.width = width;
        this.height = height;
        
        data = new double[width*height];
    }
    
    public int mapFunction(int x, int y) {
        return y*width + x;
    }
    
    public void clear(double value) {
        
        for (int i=0; i<data.length; i++) {
            data[i] = value;
        }
    }
    
    public boolean write(int x, int y, double newValue) {
    
        int idx = mapFunction(x,y);
        
        if (newValue < data[idx]) {
            data[idx] = newValue;
            return true;
        }
        return false;
    }
    
}
