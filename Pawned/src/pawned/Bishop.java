package pawned;

import javax.swing.ImageIcon;

public class Bishop extends Piece{
    
    public Bishop(char type){
        this.color = type;
        this.type  = Type.BISHOP;
        this.png   = new ImageIcon(this.getClass().getResource("icons/Bishop" + type + ".png")).getImage();
    }
}
