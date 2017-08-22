package pawned;

import javax.swing.ImageIcon;

public class Rook extends Piece{
    
    public Rook(char type){
        this.color = type;
        this.type  = Type.ROOK;
        this.png   = new ImageIcon(this.getClass().getResource("icons/Rook" + type + ".png")).getImage();
    }
}
