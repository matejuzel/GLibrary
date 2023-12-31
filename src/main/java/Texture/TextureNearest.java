/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Texture;

/**
 *
 * @author matej uzel
 */
public class TextureNearest extends TextureAbstract {
    
    public TextureNearest(int width, int height) {
        super(width, height);
        this.textels = new int[width * height * 3];
    }
    
    public TextureNearest(String filename) {
        super(filename);
        
    }

    @Override
    public int mapFunction(int x, int y) {
        return y*this.height + x;
    }
}
