package Test;

import Math.Mtx4;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author matej uzel
 */
public class TestGL {
    
    //@Test
    public void testMtx4() {
        
        Mtx4 a, b;
        
        a = new Mtx4(
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
        );
        b = new Mtx4();
        b.loadIdentity();
        
        //assertEquals(1,1);
        
    }
    
}
