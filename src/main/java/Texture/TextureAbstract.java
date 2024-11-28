/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Texture;

import GL.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.console;
import javax.imageio.ImageIO;

/**
 *
 * @author matej uzel
 */
public abstract class TextureAbstract {
    
    protected int width;
    protected int height;
    protected Color textels[];    
    //BufferedImage image;
    
    public TextureAbstract(String path) {
        
        int[] data = new int[] {0, 0, 0, 0};
        
        try {
            InputStream stream = new FileInputStream(path);
            BufferedImage image = ImageIO.read(stream);
            this.width = image.getRaster().getWidth();
            this.height = image.getRaster().getHeight();
            this.textels = new Color[this.width * this.height];
            for (int y = 0; y < this.height; y++) {
                for (int x = 0; x < this.width; x++) {
                    image.getRaster().getPixel(x, y, data);
                    this.textels[this.mapFunction(x, y)] = new Color(data[0], data[1], data[2]);
                }
            }
        } catch(IOException e) {
            
            System.out.println(e.getMessage());
        }
        
        //System.out.println(String.format("wh: %d, %d", this.width, this.height));
    }
    
    public abstract int mapFunction(int x, int y);
    
    public abstract Color getColor(double x, double y);
    
    public final void setColor(int x, int y, Color color) {
    
        if (x < 0 || y < 0) return;
        if (x >= this.width || y >= this.height) return;
        
        this.textels[this.mapFunction(x, y)].setR(color.getR());
        this.textels[this.mapFunction(x, y)].setG(color.getG());
        this.textels[this.mapFunction(x, y)].setB(color.getB());
    }
    
    
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    
}
