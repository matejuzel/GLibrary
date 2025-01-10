/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Other;

import Math.Mtx4;
import Math.Vec4;
import java.util.stream.IntStream;

/**
 *
 * @author matej uzel
 */
public class ParallelTasks {
    
    public static void sceneParallelStreamTest() {
        
        int size = 1000000;
        
        Vec4[] array1 = new Vec4[size];
        Vec4[] array2 = new Vec4[size];
        Vec4[] array3 = new Vec4[size];
        
        Mtx4 mtx, mtx2;
        
        mtx = new Mtx4();
        mtx.loadIdentity();
        mtx
                .multiply(new Mtx4().loadIdentity().translate(12, -2, 8))
                .multiply(Mtx4.getRotationX(0.002))
                .multiply(Mtx4.getRotationY(-0.0034))
                ;
        
        mtx2 = new Mtx4().loadIdentity().multiply(Mtx4.getProjectionPerspective(-3,2,-1,2,0.1, 1300));
        
        //System.out.println(mtx2);
        
        //System.exit(0);
        
        long time0 = System.currentTimeMillis();
        
        for (int i=0; i<size; i++) {
            array1[i] = new Vec4(Math.random(), Math.random(), Math.random(), 0);
            array2[i] = new Vec4(Math.random(), Math.random(), Math.random(), 0);
            array3[i] = new Vec4(0,0,0,0);
        }
        
        long time1 = System.currentTimeMillis();
        
        for (int j=0; j<100; j++) {
            evaulateArray(array1, array2, array3, mtx, mtx2, size);
        }
        
        long time2 = System.currentTimeMillis();
        
        
        mtx = new Mtx4();
        mtx.loadIdentity();
        mtx
                .multiply(new Mtx4().loadIdentity().translate(14, -3, 9))
                .multiply(Mtx4.getRotationX(0.0024))
                .multiply(Mtx4.getRotationY(-0.004))
                ;
        
        mtx2 = new Mtx4().loadIdentity().multiply(Mtx4.getProjectionPerspective(-1,1,-2,3,0.2, 1200));
        
        for (int i=0; i<size; i++) {
            array1[i] = new Vec4(Math.random(), Math.random(), Math.random(), 0);
            array2[i] = new Vec4(Math.random(), Math.random(), Math.random(), 0);
            array3[i] = new Vec4(0,0,0,0);
        }
        
        evaulateArrayParallel(array1, array2, array3, mtx, mtx2, size);
        
        
        
        
        //System.out.println(String.format("normal: %d\nparallel: %d\n", time2-time1, time4-time3));
    }
    
    public static void evaulateArray(Vec4[] array1, Vec4[] array2, Vec4[] array3, Mtx4 mtx, Mtx4 mtx2, int length) {
        for (int i=0; i<length; i++) {
            evaluateFunction(array1, array2, array3, mtx, mtx2, i);
        }
    }
    
    public static void evaulateArrayParallel(Vec4[] array1, Vec4[] array2, Vec4[] array3, Mtx4 mtx, Mtx4 mtx2, int length) {
        
        long time0 = System.currentTimeMillis();
        
        IntStream bbb = IntStream.range(0, length);
        
        long time1 = System.currentTimeMillis();
        bbb.parallel().forEach(i -> {
            evaluateFunction(array1, array2, array3, mtx, mtx2, i);
        });
        long time2 = System.currentTimeMillis();
        
        System.out.println(String.format("parallel: %d ... %d", time1-time0, time2-time1));
    }
    
    public static void evaluateFunction(Vec4[] a, Vec4[] b, Vec4[] c, Mtx4 mtx, Mtx4 mtx2, int index) {
        
        mtx.multiply(mtx2);
        
        Vec4 a0 = a[index];
        Vec4 b0 = b[index];
        
        a0.transform(mtx).divideByW();
        b0.transform(mtx).divideByW();
        
        a0.normal();
        b0.normal();
        
        c[index] = a0;
        c[index].subtract(b0);
        c[index].normal();
    }
    
    
}
