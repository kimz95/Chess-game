package pawned;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Piece {
    protected Image png;
    protected char color;
    protected Type type;
    
    public Type getType(){
        return type;
    }
    public char getColor(){
        return color;
    }
    public void draw(Graphics g, int x, int y){
        g.drawImage(png, x, y, null);
    }
}
