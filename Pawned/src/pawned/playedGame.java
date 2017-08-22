package pawned;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class playedGame extends Game{
    private boolean  selected;  // If a piece is selected
    private location from;      // Selected piece location
    private char     color;     // Choosen color
    
    

    public playedGame(boolean inverted, char c) {
        super(inverted);
        color = c;
        if(c == 'B')
            AI();
    }
    
    // Select piece or command move
    public void select(location l){
        if(freeze)
            return;
        if(selected){
            selected = false;
            if(move(from, l) && !freeze)
                AI();
        } else {
            // Ensure selection of own pieces
            try{
                if (board.getPiece(l).getColor() == color){
                    from = l;
                    selected = true;
                }
            }catch(NullPointerException e){}
        }
    }
    public void draw(Graphics g){
        super.draw(g);
        if(selected){
            g.setColor(Color.ORANGE);
            for(location l : GetRealMoves(from)){
                g.fillRect(l.getScreenX() * 80, l.getScreenY() * 80, 80, 5);
                g.fillRect(l.getScreenX() * 80, l.getScreenY() * 80 + 75, 80, 5);
                
                g.fillRect(l.getScreenX() * 80, l.getScreenY() * 80, 5, 80);
                g.fillRect(l.getScreenX() * 80 + 75, l.getScreenY() * 80, 5, 80);
            }
            g.setColor(Color.BLUE);
            g.drawRect(from.getScreenX() * 80, from.getScreenY() * 80, 80, 80);
        }
    }
    public void AI(){
        Piece [][] Clone=board.getClone();
        Random rnd = new Random();
        location start;
        location end;
        Vector<location> locations = new Vector();
        Vector<location> RealMoves;
        
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                if(     Clone[i][j]!=null   &&
                        Clone[i][j].color != color &&
                        GetRealMoves(new location(i,j)).size()>0    
                        )
                    
                        locations.add(new location(i,j));
        
        start=locations.elementAt(rnd.nextInt(locations.size()));
        RealMoves=GetRealMoves(start);
        end = RealMoves.elementAt(rnd.nextInt(RealMoves.size()));
        
        move(start,end);
    }
    
    protected Type choosePromotion(){
        char c = (turn) ? 'W' : 'B';
        if(c != color)
            return null;
        Object[] options = {"ROOK", "KNIGHT", "BISHOP", "QUEEN"};
        Type t;
        switch(JOptionPane.showOptionDialog(null,
                "Choose your new Piece",
                "Promotion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                "Queen")){
            case -1:
                return null;
            case 0:
                t = Type.ROOK;
                break;
            case 1:
                t = Type.KNIGHT;
                break;
            case 2:
                t = Type.BISHOP;
                break;
            case 3:
            default:
                t = Type.QUEEN;
                break;
        }
        return t;
    }
    protected boolean isCheckmated(){
        boolean x = super.isCheckmated();
        if(x == true && !turn){
            JOptionPane.showMessageDialog(new JFrame(),"You Win!");
        }
        else if(x==true){
            JOptionPane.showMessageDialog(new JFrame(),"You Lose!");
        }
        return x;
    }
}