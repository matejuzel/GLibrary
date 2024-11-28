/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

/**
 *
 * @author matej uzel
 */
public class Color {
    
    int r,g,b;
    
    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public int getR() {
        return this.r;
    }
    public int getG() {
        return this.g;
    }
    public int getB() {
        return this.b;
    }
    
    public void setR(int value) {
        this.r = value;
    }
    public void setG(int value) {
        this.g = value;
    }
    public void setB(int value) {
        this.b = value;
    }
    
}
