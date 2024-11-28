/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Texture;

import GL.Color;

/**
 *
 * @author matej uzel
 */
public class TextureNearest extends TextureAbstract {
    
    public TextureNearest(String path) {
        super(path);
    }

    @Override
    public Color getColor(double x, double y) {
        
        int x_mapped = (int) Math.round(x * this.width);
        int y_mapped = (int) Math.round(y * this.height);
        
        if (x_mapped < 0) x_mapped = 0;
        if (y_mapped < 0) y_mapped = 0;
        
        if (x_mapped >= this.width) x_mapped = this.width-1;
        if (y_mapped >= this.height) y_mapped = this.height-1;
        
        return this.textels[this.mapFunction(x_mapped,y_mapped)];
    }
    
    @Override
    public int mapFunction(int x, int y) {
        return y*this.height + x;
    }
}
