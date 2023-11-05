/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL.DepthBuffer;

/**
 *
 * @author Matej
 */
public class DepthBufferDouble extends DepthBufferAbstract {
    
    private double[] data;
    private double[] clearData;
    
    public DepthBufferDouble(int width, int height) {
        super(width, height);
        data = new double[width*height];
        clearData = new double[width*height];
    }
    
    @Override
    public void setClearValue(double value) {
        for (int i=0; i<clearData.length; i++) {
            clearData[i] = value;
        }
    }
    
    
    @Override
    public void clear() {
        System.arraycopy(clearData, 0, data, 0, data.length);
    }
    
    @Override
    public boolean write(int x, int y, double newValue) {
    
        int idx = mapFunction(x,y);
        
        if (depthFunction == DepthFunction.LESS) {
            
            if (newValue < data[idx]) {
                data[idx] = newValue;
                return true;
            }
            return false;
        } else if (depthFunction == DepthFunction.GREATER) {
            if (newValue > data[idx]) {
                data[idx] = newValue;
                return true;
            }
            return false;
        }
        
        return false;
    }
}
