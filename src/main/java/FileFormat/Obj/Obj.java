/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileFormat.Obj;

import GL.Face;
import Math.Vec4;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Matej
 */
public class Obj {
    
    public static boolean debug = true;
    
    protected ArrayList verts = new ArrayList<Vec4>();
    protected ArrayList coords = new ArrayList<Vec4>();
    protected ArrayList normals = new ArrayList<Vec4>();
    protected ArrayList faces = new ArrayList<Face>();
    
    public Obj(String FileName) throws FileNotFoundException, IOException {
        
        try (BufferedReader br = new BufferedReader(new FileReader(FileName))) {
            String line;
            
            int vertexIndex = 0;
            int coordIndex = 0;
            int normalIndex = 0;
            
            while ((line = br.readLine()) != null) {
               
                String[] vals = line.split(" ");
                if (vals.length < 1) continue;
                
                switch (vals[0]) {
                    case "v":
                        // vertex
                        if (vals.length < 4) continue;
                        double vertexX = Double.parseDouble(vals[1]);
                        double vertexY = Double.parseDouble(vals[2]);
                        double vertexZ = Double.parseDouble(vals[3]);
                        if (Obj.debug) System.out.println(String.format("vertex: %f ; %f ; %f", vertexX, vertexY, vertexZ));
                        verts.add(new Vec4(vertexX, vertexY, vertexZ, 1));
                        break;
                    case "vt":
                        // texturovaci souradnice
                        if (vals.length < 3) continue;
                        double coordU = Double.parseDouble(vals[1]);
                        double coordV = Double.parseDouble(vals[2]);
                        if (Obj.debug) System.out.println(String.format("vertex coord: %f ; %f", coordU, coordV));
                        coords.add(new Vec4(coordU, coordV, 0, 1));
                        break;
                    case "vn":
                        // vertex normal
                        if (vals.length < 4) continue;
                        double normalX = Double.parseDouble(vals[1]);
                        double normalY = Double.parseDouble(vals[2]);
                        double normalZ = Double.parseDouble(vals[3]);
                        if (Obj.debug) System.out.println(String.format("vertex normal: %f ; %f ; %f", normalX, normalY, normalZ));
                        normals.add(new Vec4(normalX, normalY, normalZ, 1));
                        break;
                    case "f":
                        // face definition
                        if (vals.length < 4) continue;
                        String[] vals2;
                        
                        String vertexA = vals[1];
                        vals2 = vertexA.split("/");
                        if (vals2.length != 3) continue;
                        int vertexIndexA = Integer.parseInt(vals2[0]) - 1;
                        int coordIndexA = Integer.parseInt(vals2[1]) - 1;
                        int normalIndexA = Integer.parseInt(vals2[2]) - 1;
                        
                        String vertexB = vals[2];
                        vals2 = vertexB.split("/");
                        if (vals2.length != 3) continue;
                        int vertexIndexB = Integer.parseInt(vals2[0]) - 1;
                        int coordIndexB = Integer.parseInt(vals2[1]) - 1;
                        int normalIndexB = Integer.parseInt(vals2[2]) - 1;
                        
                        String vertexC = vals[3];
                        vals2 = vertexC.split("/");
                        if (vals2.length != 3) continue;
                        int vertexIndexC = Integer.parseInt(vals2[0]) - 1;
                        int coordIndexC = Integer.parseInt(vals2[1]) - 1;
                        int normalIndexC = Integer.parseInt(vals2[2]) - 1;
                        
                        String vertexD = vals[4];
                        vals2 = vertexD.split("/");
                        if (vals2.length != 3) continue;
                        int vertexIndexD = Integer.parseInt(vals2[0]) - 1;
                        int coordIndexD = Integer.parseInt(vals2[1]) - 1;
                        int normalIndexD = Integer.parseInt(vals2[2]) - 1;
                        
                        /*
                        if (Obj.debug) System.out.println(String.format(
                                "%d/%d/%d ; %d/%d/%d ; %d/%d/%d ; %d/%d/%d", 
                                vertexIndexA, coordIndexA, normalIndexA, 
                                vertexIndexB, coordIndexB, normalIndexB, 
                                vertexIndexC, coordIndexC, normalIndexC,
                                vertexIndexD, coordIndexD, normalIndexD
                        ));
                        */
                        
                        //ACB
                        faces.add(new Face(
                                (Vec4)verts.get(vertexIndexA), (Vec4)verts.get(vertexIndexC), (Vec4)verts.get(vertexIndexB),
                                (Vec4)coords.get(coordIndexA), (Vec4)coords.get(coordIndexC), (Vec4)coords.get(coordIndexB),
                                (Vec4)normals.get(normalIndexA), (Vec4)normals.get(normalIndexC), (Vec4)normals.get(normalIndexB)
                        ));
                        
                        //ADC
                        faces.add(new Face(
                                (Vec4)verts.get(vertexIndexA), (Vec4)verts.get(vertexIndexD), (Vec4)verts.get(vertexIndexC),
                                (Vec4)coords.get(coordIndexA), (Vec4)coords.get(coordIndexD), (Vec4)coords.get(coordIndexC),
                                (Vec4)normals.get(normalIndexA), (Vec4)normals.get(normalIndexD), (Vec4)normals.get(normalIndexC)
                        ));
                        
                        break;
                }
            }
        }
        
    }
    
    public ArrayList<Face> getFaces() {
        return faces;
    }
    
}
