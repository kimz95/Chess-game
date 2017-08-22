
package pawned;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class watchGame extends Game {
    private int current = 0;
    private Type promotion;
    private Vector <Piece[][]> maps = new Vector();

    Pattern parse = Pattern.compile(
                 "([BKNQR])?"
                +"([a-h]|[1-8])?"
                +"x?"
                +"([a-h][1-h])"
                +".*?"
        );
    Pattern promo = Pattern.compile(
             "([a-h])?"
            +"x?"
            +"([a-h][1-8])"
            +"="
            +"([RNBQ])"
        );
    
    public watchGame(boolean inverted, File file) {
        super(inverted);
        Start(file);
    }
    public void Start(File file){
        maps.add(board.getClone());
        try{
            Scanner x = new Scanner(file);
            while(x.hasNext()){
                Vector <location> WP = board.getColorPieces('W');
                Vector <location> BP = board.getColorPieces('B');
            
                String a = cutString(x.next());
                String b = x.next();
                
                Matcher normalWhite = parse.matcher(a);Matcher promoWhite = promo.matcher(a);
                Matcher normalBlack = parse.matcher(b);Matcher promoBlack = promo.matcher(b);
                
                if("O-O".equals(a)){
                    board.castleKingSide(true);
                    turn = !turn;
                    maps.add(board.getClone());
                }else if("O-O-O".equals(a)){
                    board.castleQueenSide(true);
                    turn = !turn;
                    maps.add(board.getClone());
                }else if(promotionMove(promoWhite, WP))
                    ;
                else
                    normalMove(normalWhite, WP);
                if("O-O".equals(b)){
                    board.castleKingSide(false);
                    turn = !turn;
                    maps.add(board.getClone());
                
                }else if("O-O-O".equals(b)){
                    board.castleQueenSide(false);
                    turn = !turn;
                    maps.add(board.getClone());
                }else if(promotionMove(promoBlack, BP))
                    ;
                else
                    normalMove(normalBlack, BP);
            }
        }
        catch(Exception e){}
        update();
    }
    
    private Type type(String s){
        if(s == null)
            return Type.PAWN;
        
        switch (s){
            case "K":
                return Type.KING;
                
            case "Q":
                return Type.QUEEN;
                
            case "R":
                return Type.ROOK;
                
            case "B":
                return Type.BISHOP;
                
            case "N":
                return Type.KNIGHT;
        }
        return null;
    }
    public String cutString(String s){        
        int i = s.indexOf(".");
        if(i > 0)
            return s.substring(i + 1);
        else
            return s;
    }
    private void normalMove(Matcher m, Vector<location> p){
        if(m.find()){
            if(m.group(2) == null){
                for (location p1 : p) {
                    if (board.getPiece(p1).getType() == type(m.group(1))) {
                        if(move(p1, new location(m.group(3)))) {
                            maps.add(board.getClone());
                            break;
                        }
                    }
                }
            }else{
                Vector <location> v = board.getFile(m.group(2));
                for(int j=0;j<v.size();j++){    
                    if(board.getPiece(v.get(j)).getType() == type(m.group(1)))
                        if(move(v.get(j),new location(m.group(3)))){
                            maps.add(board.getClone());
                            break;
                        }
                    }
            } 
        }
    }
    private boolean promotionMove(Matcher m, Vector<location> p){
        if(m.find()){
            promotion = type(m.group(3));
            if(m.group(1) == null){
                for (location p1 : p) {
                    if (board.getPiece(p1).getType() == Type.PAWN) {
                        if (move(p1, new location(m.group(2)))) {
                            maps.add(board.getClone());
                            break;
                        }
                    }
                }
            }else{
                Vector <location> v = board.getFile(m.group(1));
                for(int j=0;j<v.size();j++){    
                    if(board.getPiece(v.get(j)).getType() == Type.PAWN)
                        if(move(v.get(j),new location(m.group(2)))){
                            maps.add(board.getClone());
                            break;
                        }
                    }
            }
            return true;
        }
        return false;
    }
    protected Type choosePromotion(){
        return promotion;
    }
    
    // Control
    public boolean forward(){
        if(current + 1 < maps.size()){
            current++;
            update();
            return true;
        }
        return false;
    }
    public void rewind(){
        current = 0;
        if(maps.size() > 0)
            update();
    }
    public void backward(){
        if(current - 1 >= 0){
            current--;
            update();
        }
    }
    private void update(){
        board.setClone(maps.get(current));
    }
}