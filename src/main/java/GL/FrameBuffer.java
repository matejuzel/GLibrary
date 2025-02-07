/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Math.Vec4;
import java.awt.image.BufferedImage;

/**
 *
 * @author matej uzel
 */
public class FrameBuffer extends BufferedImage {
    
    private int[] clearColor = null;
    private int[] color = {0, 0, 0};
    
    
    
    public FrameBuffer(int width, int height) {
        super(width, height, BufferedImage.TYPE_3BYTE_BGR);
        this.clearColor = new int[3 * this.getWidth() * this.getHeight()];
        
    }
    
    private int mapFunction(int x, int y) {
        
        return 3*(y*this.getWidth() + x);
    }
    
    public void putPixel(int x, int y, int r, int g, int b) {
        
        if (x < 0 || x >= this.getWidth()) return;
        if (y < 0 || y >= this.getHeight()) return;
        
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        
        this.getRaster().setPixel(x, y, this.color);
    }
    
    public void putPixel(int[] pos, int[] color) {
        
        if (pos[0] < 0 || pos[0] >= this.getWidth()) return;
        if (pos[1] < 0 || pos[1] >= this.getHeight()) return;
        
        this.getRaster().setPixel(pos[0], pos[1], color);
    }
    
    public void putPixel(int x, int y, int r, int g, int b, int size) {
        
        int x0 = x - size / 2;
        int y0 = y - size / 2;
        
        for (int i=0; i<size; i++) {
            
            for (int j=0; j<size; j++) {
                
                this.putPixel(x0+i, y0+j, r, g, b);
            }
        }    
    }
    
    public void putLine(int x1, int y1, int x2, int y2, int r, int g, int b) {
        
        float dx = x2 - x1;
        float dy = y2 - y1;
        
        float step, x,y;
        int i;
        
        if (Math.abs(dx) >= Math.abs(dy)) {
            step = Math.abs(dx);
        } else {
            step = Math.abs(dy);
        }
        
        dx = dx / step;
        dy = dy / step;
        
        x = x1;
        y = y1;
        i = 0;
        while (i <= step) {
          this.putPixel((int)x, (int)y, r, g, b);
          x = x + dx;
          y = y + dy;
          i = i + 1;
        }
    }
    
    public void clear() {
        
        this.getRaster().setPixels(0, 0, this.getWidth(), this.getHeight(), this.clearColor);
    }
    
    public final void setClearColor(int r, int g, int b) {
        
        for (int i=0; i<this.getWidth()*this.getHeight(); i++) {
            
            this.clearColor[3*i] = r;
            this.clearColor[3*(i)+1] = g;
            this.clearColor[3*(i)+2] = b;
        }
    }

    void putTriangleLines(double ax, double ay, double bx, double by, double cx, double cy, int r, int g, int b) {
        
        putLine((int)ax, (int)ay, (int)bx, (int)by, r,g,b);
        putLine((int)bx, (int)by, (int)cx, (int)cy, r,g,b);
        putLine((int)cx, (int)cy, (int)ax, (int)ay, r,g,b);
    }
    
    
    void putTriangleFilled(Vec4 vertexA, Vec4 vertexB, Vec4 vertexC, int r, int g, int b) {
        
        //vertexA.stdOutPrintln("A");
        //vertexB.stdOutPrintln("B");
        //vertexC.stdOutPrintln("C");
        
        Vec4 pom;
        if (vertexB.getY() < vertexA.getY()) {
            
            pom = vertexB;
            vertexB = vertexA;
            vertexA = pom;
        }
        if (vertexC.getY() < vertexB.getY()) {
        
            pom = vertexC;
            vertexC = vertexB;
            vertexB = pom;
            
            if (vertexB.getY() < vertexA.getY()) {
                
                pom = vertexB;
                vertexB = vertexA;
                vertexA = pom;
            }
        }
        
        int dxAC = (int) (vertexC.getX() - vertexA.getX());
        int dxAB = (int) (vertexB.getX() - vertexA.getX());
        int dxBC = (int)(vertexC.getX() - vertexB.getX());
        
        int dy0 = (int)(vertexC.getY() - vertexA.getY());
        int dy1 = (int)(vertexB.getY() - vertexA.getY());
        int dy2 = (int)(vertexC.getY() - vertexB.getY());
        
        float dyAC_inv = 1.0f / dy0;
        float dyAB_inv = 1.0f / dy1;
        float dyBC_inv = 1.0f / dy2;
        
        int crossAC = (int)(vertexC.getY()*vertexA.getX() - vertexA.getY()*vertexC.getX());
        int crossAB = (int)(vertexB.getY()*vertexA.getX() - vertexA.getY()*vertexB.getX());
        int crossBC = (int)(vertexC.getY()*vertexB.getX() - vertexB.getY()*vertexC.getX());

        // horni cast trojuhelniku
        for (int line = (int) vertexA.getY(); line < (int) vertexB.getY(); line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = Math.round((crossAC + line * dxAC) * dyAC_inv);
            int xAB = Math.round((crossAB + line * dxAB) * dyAB_inv);
            
            scanLine(xAC, xAB, line);
        }
        
        // dolni cast trojuhelniku
        for (int line = (int) vertexB.getY(); line < (int) vertexC.getY(); line++) {
            
            // pruseciky svislych usecek - krajni body pro scanline
            int xAC = Math.round((crossAC + line * dxAC) * dyAC_inv);
            int xBC = Math.round((crossBC + line * dxBC) * dyBC_inv);
            
            scanLine(xAC, xBC, line);
        }
    }
    
    void putTriangle(Vec4 vertexA, Vec4 vertexB, Vec4 vertexC, int r, int g, int b) {
        
        //putTriangleLines(ax, ay, bx, by, cx, cy, r,g,b);
        putTriangleFilled(vertexA, vertexB, vertexC, r,g,b);
    }
    
    public void scanLine(int x0, int x1, int y) {
        
        for (int x = x0; x < x1; x++) {
            
            putPixel(x,y, 255,0,0);
        }
    }
    
}
