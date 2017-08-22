package pawned;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;

public abstract class Game {
    protected Board   board;  // game board
    protected boolean turn;   // true for white turn
    protected boolean freeze; // set when game ends
    
    public Game(boolean inverted){
        turn    = true;
        freeze  = false;
        board   = new Board(inverted);
    }
    public void draw(Graphics g){
        board.draw(g);
    }
    
    // Move
    public boolean move(location from, location to){
        if(freeze)
            return false;
        
        // Validate move
        if(validateMove(from, to)){
            // Change piece position
            if(!promote(from, to))
                board.update(from, to);
            
            // Change turn
            turn = !turn;
            if(isCheckmated())
                freeze = true;
            return true;
        }
        return false;
    }
    protected boolean validateMove(location from, location to){
        Vector<location> moves = GetRealMoves(from);
        
        return moves.contains(to);
    }
    protected Vector<location> GetRealMoves(location from){
        Vector <location> moves = new Vector();
        Piece p = board.getPiece(from);
        
        switch(p.getType()){
            case PAWN:
                getPawnMoves(from, moves);
                break;
            case ROOK:
                getOrthogonalMoves(from, moves);
                break;
            case KNIGHT:
                getKnightMoves(from, moves);
                break;
            case BISHOP:
                getDiagonalMoves(from, moves);
                break;
            case QUEEN:
                getDiagonalMoves(from, moves);
                getOrthogonalMoves(from, moves);
                break;
            case KING:
                getKingMoves(from, moves);
                break;
        }
        
        Piece[][] clone = board.getClone();
        for(int i = 0; i < moves.size(); ){
            board.update(from, moves.get(i));
            if(isChecked())
                moves.remove(i);
            else
                i++;
            board.setClone(clone);
        }
        return moves;
    }
    
    // Return Valid locations in moves Variable
    private void getPawnMoves       (location from, Vector moves){
        char c = board.getPiece(from).color;
        
        if(c == 'W'){
        if(from.getY() == 1 && board.getPiece(new location (from.getX(),from.getY()+2)) == null
                && board.getPiece(new location (from.getX(),from.getY()+1)) == null)
            moves.add(new location(from.getX(), from.getY() + 2));
        try{if(board.getPiece(new location (from.getX(),from.getY()+1)) == null)
            moves.add(new location(from.getX(), from.getY() + 1));}catch (Exception e){}
        try{if(board.getPiece(new location (from.getX()+1,from.getY()+1)) != null && 
                board.getPiece(new location (from.getX()+1,from.getY()+1)).color != c)
            moves.add(new location(from.getX()+1, from.getY() + 1));}catch (Exception e){}
        try{if(board.getPiece(new location (from.getX()-1,from.getY()+1)) != null&& 
                board.getPiece(new location (from.getX()-1,from.getY()+1)).color != c)
            moves.add(new location(from.getX() - 1, from.getY() + 1));}catch (Exception e){}
            }
        if(c == 'B'){
        try{if(from.getY() == 6 && board.getPiece(new location (from.getX(),from.getY()-2)) == null
                && board.getPiece(new location (from.getX(),from.getY()-1)) == null)
            moves.add(new location(from.getX(), from.getY() - 2));}catch (Exception e){}
        try{if(board.getPiece(new location (from.getX(),from.getY()-1)) == null)
            moves.add(new location(from.getX(), from.getY() - 1));}catch (Exception e){}
        try{if(board.getPiece(new location (from.getX()+1,from.getY()-1)) != null&& 
                board.getPiece(new location (from.getX()+1,from.getY()-1)).color != c)
            moves.add(new location(from.getX()+1, from.getY() - 1));}catch (Exception e){}
        try{if(board.getPiece(new location (from.getX()-1,from.getY()-1)) != null&& 
                board.getPiece(new location (from.getX()-1,from.getY()-1)).color != c)
            moves.add(new location(from.getX() - 1, from.getY() - 1));}catch (Exception e){}
        }}
    private void getKingMoves       (location from, Vector moves){
        for(location l : iterKing(from))
            CheckPieceMovement(from, l, moves);
    }
    private void getKnightMoves     (location from, Vector moves){
        for(location l : iterKnight(from))
            CheckPieceMovement(from, l, moves);
    }
    private void getDiagonalMoves   (location from, Vector moves){ 
        for(location l: downRight(from)){
            if(!CheckPieceMovement(from, l, moves))
                break;}
        for(location l: downLeft(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
        for(location l: upRight(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
        for(location l: upLeft(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
    }
    private void getOrthogonalMoves (location from, Vector moves){
        for(location l: right(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
        for(location l: up(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
        for(location l: down(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
        for(location l: left(from))
            if(!CheckPieceMovement(from, l, moves))
                break;
    }
    
    private boolean CheckPieceMovement(location from ,location to, Vector moves){
        if(board.getPiece(to) == null){
            moves.add(to);
            return true;
        }else if(board.getPiece(to).color !=  board.getPiece(from).color){
            moves.add(to);
            return false;
        }else
            return false;
     }
    
    // Brute-force to check if current king is checked
    private boolean isChecked(){
        location currentKing;
        char c;
        if(turn){
            currentKing = board.getWhiteKing();
            c = 'W';
        }else{
            currentKing = board.getBlackKing();
            c = 'B';
        }
        
        // 0-3 up/dwon/left/right   || 4-7 diagonals
        Piece[] direction = {   getOffendingPiece(up(currentKing)),
                                getOffendingPiece(down(currentKing)),
                                getOffendingPiece(left(currentKing)),
                                getOffendingPiece(right(currentKing)),
                                getOffendingPiece(upLeft(currentKing)),
                                getOffendingPiece(upRight(currentKing)),
                                getOffendingPiece(downLeft(currentKing)),
                                getOffendingPiece(downRight(currentKing))};
        
        for(int i = 0 ; i < 4 ; i++){
            Piece p = direction[i];
            if(p != null &&
               p.getColor() != c &&
              (p.type == Type.ROOK || p.type == Type.QUEEN))
                return true;
        }
        for(int i = 4 ; i < 8 ; i++){
            Piece p = direction[i];
            if(p != null &&
               p.getColor() != c &&
              (p.type == Type.BISHOP || p.type == Type.QUEEN))
                return true;
        }
        // Check offending knights
        for(location l : iterKnight(currentKing)){
            Piece p = board.getPiece(l);
            if(p != null &&
               p.getColor() != c &&
               p.type == Type.KNIGHT)
                return true;
        }
        
        // Kings touching logs
        for(location l : iterKing(currentKing)){
            Piece p = board.getPiece(l);
            if(p != null &&
               p.getColor() != c &&
               p.type == Type.KING)
                return true;
        }
        
        // Check offending pawns
        int x = currentKing.getX() - 1;
        int y = (c == 'W') ? currentKing.getY() + 1 : currentKing.getY() - 1;
        for(int i = 0 ; i < 2 ; i++){
            if(x >= 0 && x <= 7 && y >= 0 && y <= 7){
                Piece p = board.getPiece(x, y);
                if(p != null &&
                   p.getColor() != c &&
                   p.type == Type.PAWN)
                    return true;
            }
            x += 2;
        }
        return false;
    }
    // Helper function gets offending piece in a direction
    private Piece getOffendingPiece(Iterable<location> iter){
        for(location l : iter){
            Piece p = board.getPiece(l);
            if(board.getPiece(l) != null)
                return p;
        }
        return null;
    }
    
    // Brute-force to check if opposing king is checkmated
    protected boolean isCheckmated(){
        char c = (turn) ? 'W' : 'B';
        Vector <location> p = board.getColorPieces(c);
        for(int i=0;i<p.size();i++){
            if(!GetRealMoves(p.elementAt(i)).isEmpty())
                return false;
        }
        return true;
    }
    
    // Direction iterators
    private Iterable<location> up(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return y != 7;
                    }
                    public location next() {
                        y++;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> down(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return y != 0;
                    }
                    public location next() {
                        y--;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> left(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 0;
                    }
                    public location next() {
                        x--;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> right(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 7;
                    }
                    public location next() {
                        x++;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> upLeft(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 0 && y != 7;
                    }
                    public location next() {
                        x--;y++;
                        return new location(x, y);
                    }
                    
                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> upRight(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 7 && y != 7;
                    }
                    public location next() {
                        x++;y++;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> downLeft(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 0 && y != 0;
                    }
                    public location next() {
                        x--;y--;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> downRight(final location l){
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return new Iterator<location>() {
                    int x = l.getX();
                    int y = l.getY();

                    public boolean hasNext() {
                        return x != 7 && y != 0;
                    }
                    public location next() {
                        x++;y--;
                        return new location(x, y);
                    }

                    public void remove(){}
                };
            }
        };
    }
    private Iterable<location> iterKnight(final location l){
        final Vector<location> v = new Vector();
        for(int i = 0 ; i < 8 ; i++){
            int j,x,y,dx,dy;
            if(i < 4){
                j = i / 2;
                dx = 2;dy = 1;
                x = (j % 2 == 0) ? l.getX() + dx : l.getX() - dx;
                y = (i % 2 == 0) ? l.getY() + dy : l.getY() - dy;
            }else{
                j = (i - 4) / 2;
                dx = 1;dy = 2;
                x = (j % 2 == 0) ? l.getX() + dx : l.getX() - dx;
                y = (i % 2 == 0) ? l.getY() + dy : l.getY() - dy;
            }
            if(x < 0 || x > 7 || y < 0 || y > 7)
                continue;
            v.add(new location(x, y));
        }
        
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return v.iterator();
            }
        };
    }
    private Iterable<location> iterKing(final location l){
        final Vector<location> v = new Vector();
        int x1 = l.getX(), y1 = l.getY();
        int x2 = l.getX() + 1, y2 = l.getY() + 1;
        int x3 = l.getX() - 1, y3 = l.getY() - 1;
        if(x1 >= 0 && x1 <= 7){
            if(y2 >= 0 && y2 <= 7)
                v.add(new location(x1, y2));
            if(y3 >= 0 && y3 <= 7)
                v.add(new location(x1, y3));
        }
        if(x2 >= 0 && x2 <= 7){
            if(y1 >= 0 && y1 <= 7)
                v.add(new location(x2, y1));
            if(y2 >= 0 && y2 <= 7)
                v.add(new location(x2, y2));
            if(y3 >= 0 && y3 <= 7)
                v.add(new location(x2, y3));
        }
        if(x3 >= 0 && x3 <= 7){
            if(y1 >= 0 && y1 <= 7)
                v.add(new location(x3, y1));
            if(y2 >= 0 && y2 <= 7)
                v.add(new location(x3, y2));
            if(y3 >= 0 && y3 <= 7)
                v.add(new location(x3, y3));
        }
        return new Iterable<location>() {
            public Iterator<location> iterator() {
                return v.iterator();
            }
        };
    }
    
    public void invert(){
        board.invert();
    }
    protected boolean promote(location from, location to){
        if(board.getPiece(from).getType() == Type.PAWN && (to.getY() == 7 || to.getY() == 0)){
            Type type = choosePromotion();
            if(type == null)
                type = Type.QUEEN;
            board.promote(turn, type, from, to);
            return true;
        }
        return false;
    }
    protected abstract Type choosePromotion();
}