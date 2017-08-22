package pawned;

import javax.swing.ImageIcon;

public class Knight extends Piece{
    
    public Knight(char type){
        this.color = type;
        this.type  = Type.KNIGHT;
        this.png   = new ImageIcon(this.getClass().getResource("icons/Knight" + type + ".png")).getImage();
    }
}
