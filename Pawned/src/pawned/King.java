package pawned;

import javax.swing.ImageIcon;

public class King extends Piece{
    
    public King(char type){
        this.color = type;
        this.type  = Type.KING;
        this.png   = new ImageIcon(this.getClass().getResource("icons/King" + type + ".png")).getImage();
    }
}
