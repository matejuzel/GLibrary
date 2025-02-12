package Shader;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author matej uzel
 */
abstract public class Shader {
    
    protected int frame = 0;
    
    public Shader() {
    
    }
    
    public void frameInc() {
        frame++;
    }
    
    abstract public void run();
}
