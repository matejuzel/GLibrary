/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GL;

import Texture.TextureAbstract;
import Texture.TextureNearest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matej uzel
 */
public final class TextureUnit {
    
    List<TextureAbstract> textures;
    int currentHandler;
    
    TextureAbstract emptyTexture;
    
    public TextureUnit() {
        // iniciace kontejneru
        textures = new ArrayList<>();
        
        // vlozeni prazdne textury
        emptyTexture = new TextureNearest("data/sach64.png");
        addTexture(emptyTexture);
    }
    
    private TextureAbstract getTexture(int handler) {
        return textures.get(handler);
    }
    
    public int addTexture(TextureAbstract texture) {
        textures.add(texture);
        currentHandler = textures.size()-1;
        return currentHandler;
    }
    
    public void setCurrentTexture(int handler) {
        currentHandler = handler;
    }
    
    public TextureAbstract getCurrentTexture() {
        return textures.get(currentHandler);
    }
    
}
