/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Geometry;

import Math.Mtx4;
import Shader.FragmentShader;
import Shader.FragmentShaderFlat;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author matej uzel
 */
public class Mesh {
    
    Mtx4 transformationMatrix;
    FragmentShader fragmentShader;
    ArrayList<Face> faces;
    
    public Mesh(FragmentShader fragmentShader) {
        
        this.transformationMatrix = new Mtx4(
            1,0,0,0, 
            0,1,0,0, 
            0,0,1,0, 
            0,0,0,1
        );
        this.fragmentShader = fragmentShader;
        faces = new ArrayList();
    }
    
    public Mesh() {
        this(new FragmentShaderFlat(255, 255, 255));
    }
    
    public void addFace(Face face) {
        this.faces.add(face);
    }

    public Mtx4 getTransformationMatrix() {
        return transformationMatrix;
    }
    
    public void transform(Mtx4 transformationMatrix) {
        this.transformationMatrix.multiply(transformationMatrix);
    }

    public Mesh setFragmentShader(FragmentShader fragmentShader) {
        this.fragmentShader = fragmentShader;
        return this;
    }
    
    public FragmentShader getFragmentShader() {
        return fragmentShader;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }
    
}
