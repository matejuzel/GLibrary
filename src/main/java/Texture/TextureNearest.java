/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
        
        try {
            // Load the PNG image from a file
            File imageFile = new File("your_image.png");
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    @Override
    public int mapFunction(int x, int y) {
        return y*this.height + x;
    }
}
