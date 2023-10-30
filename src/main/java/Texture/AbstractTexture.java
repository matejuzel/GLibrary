/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Texture;

/**
 *
 * @author matej uzel
 */
public abstract class AbstractTexture {
    
    protected int width;
    protected int height;
    protected int textels[];
    
    public AbstractTexture(int width, int height) {
        
        this.width = width;
        this.height = height;
    }
    
    public abstract int mapFunction(int x, int y);
    
    
}
