/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL.DepthBuffer;

/**
 *
 * @author Matej
 */
public abstract class DepthBufferAbstract {
    
    protected int width;
    protected int height;
    protected DepthFunction depthFunction;
    
    public enum DepthFunction {
        LESS,
        GREATER
    };
    
    public DepthBufferAbstract(int width, int height) {
        this.width = width;
        this.height = height;
        this.depthFunction = DepthFunction.LESS;
    }
    
    public int mapFunction(int x, int y) {
        return y * width + x;
    }
    
    public void setDepthFunction(DepthFunction depthFunction) {
        this.depthFunction = depthFunction;
    }
    
    public abstract void setClearValue(double value);
    public abstract void clear();
    public abstract boolean write(int x, int y, double newValue);
}
