package pawned;

import javax.swing.ImageIcon;

public class Queen extends Piece{
    
    public Queen(char type){
        this.color = type;
        this.type  = Type.QUEEN;
        this.png   = new ImageIcon(this.getClass().getResource("icons/Queen" + type + ".png")).getImage();
    }
}
