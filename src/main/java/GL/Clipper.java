/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Geometry.Vertex;
import Math.Utils;
import Math.Vec4;

/**
 *
 * @author matej uzel
 * 
 * Priklad orezani dle roviny Right Frustum v Clip Space
 * usecka AB: 
 *  x = Ax + k*(Bx - Ax)
 *  y = Ay + k*(By - Ay)
 *  z = Az + k*(Bz - Az)
 *  w = Aw + k*(Bw - Aw)
 * 
 * rovina x = w
 *           Ax + k*(Bx - Ax) = Aw + k*(Bw - Aw)
 *  k*(Bx - Ax) - k*(Bw - Aw) = Aw - Ax
 *  k*((Bx - Ax) - (Bw - Aw)) = Aw - Ax
 *                          k = (Aw - Ax) / ( (Bx - Ax) - (Bw - Aw) )
 * 
 * rovina x = -w
 *           Ax + k*(Bx - Ax)  = - Aw - k*(Bw - Aw)
 *  k*(bx - Ax) + k*(Bw - Aw)  = - Aw - Ax
 *  k*( bx - Ax) + (Bw - Aw) ) = - (Aw + Ax)
 *                           k = - (Aw + Ax) / ( (bx - Ax) + (Bw - Aw) )
 */
public class Clipper {
    
    int cnt = 3;
    
    Vertex a = new Vertex();
    Vertex b = new Vertex();
    Vertex c = new Vertex();
    
    Vertex[] work = {
        new Vertex(),
        new Vertex(),
        new Vertex(),
        new Vertex(),
        new Vertex(),
        new Vertex(),
    };
    
    public void set(Vertex a, Vertex b, Vertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.cnt = 3;
    }
    
    public static double rightK(Vec4 a, Vec4 b) {
        
        return (a.getW() - a.getX()) / ( (b.getX() - a.getX()) - (b.getW() - a.getW()) );
    }
    public static double leftK(Vec4 a, Vec4 b) {
        
        return - (a.getW() + a.getX()) / ( (b.getX() - a.getX()) + (b.getW() - a.getW()) );
    }
    
    public static void clip(int mask, Vertex a, Vertex b, Vertex c) {
        
        switch (mask) {
            case 3:
                // in:  C
                // out: A,B
                a = Utils.cutVertexRight(c, a);
                b = Utils.cutVertexRight(c, b);
            case 4:
                // in: A,B
                // out: C
                c = Utils.cutVertexRight(a, c);
                break;
            case 5:
                // in:  B
                // out: A,C
                a = Utils.cutVertexRight(b, a);
                c = Utils.cutVertexRight(b, c);
            case 2:
                // in: A,C
                // out: B
                b = Utils.cutVertexRight(b, a);
                break;
            case 6:
                // in:  A
                // out: B,C
                b = Utils.cutVertexRight(a, b);
                c = Utils.cutVertexRight(a, c);
                break;
            case 1:
                // in: B,C
                // out: A
                a = Utils.cutVertexRight(a, b);
                break;
        }
    }
    
    
}
