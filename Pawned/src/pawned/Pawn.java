package pawned;

import javax.swing.ImageIcon;

public class Pawn extends Piece{
    
    public Pawn(char type){
        this.color = type;
        this.type  = Type.PAWN;
        this.png   = new ImageIcon(this.getClass().getResource("icons/Pawn" + type + ".png")).getImage();
    }
}
