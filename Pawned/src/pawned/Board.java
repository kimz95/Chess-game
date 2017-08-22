package pawned;

import java.awt.Graphics;
import java.util.Vector;
// Draws the pieces (inverted if black is down)
// Contains one piece of each type

public class Board {
    private Piece[][] map;
    private boolean inverted;
    
    // Pawns
    private Pawn whitePawn;
    private Pawn blackPawn;
    // Rooks
    private Rook whiteRook;
    private Rook blackRook;
    // Knights
    private Knight whiteKnight;
    private Knight blackKnight;
    // Bishops
    private Bishop whiteBishop;
    private Bishop blackBishop;
    // Queens
    private Queen whiteQueen;
    private Queen blackQueen;
    // Kings
    private King whiteKing;
    private King blackKing;
    
    
    public Board(boolean inverted){
        this.inverted = inverted;
        location.setInverted(inverted);
        map = new Piece[8][8];
        initialize();
    }
    private void initialize(){
        whitePawn = new Pawn('W');
        blackPawn = new Pawn('B');
        
        whiteRook = new Rook('W');
        blackRook = new Rook('B');
        
        whiteKnight = new Knight('W');
        blackKnight = new Knight('B');
        
        whiteBishop = new Bishop('W');
        blackBishop = new Bishop('B');
        
        whiteQueen = new Queen('W');
        blackQueen = new Queen('B');
        
        whiteKing = new King('W');
        blackKing = new King('B');
        
        for(int i = 0 ; i < 8 ; i++){
            map[i][1] = whitePawn;
            map[i][6] = blackPawn;
        }
        
        map[0][0] = whiteRook;
        map[7][0] = whiteRook;
        map[0][7] = blackRook;
        map[7][7] = blackRook;
        
        map[1][0] = whiteKnight;
        map[6][0] = whiteKnight;
        map[1][7] = blackKnight;
        map[6][7] = blackKnight;
        
        map[2][0] = whiteBishop;
        map[5][0] = whiteBishop;
        map[2][7] = blackBishop;
        map[5][7] = blackBishop;
        
        map[3][0] = whiteQueen;
        map[3][7] = blackQueen;
        
        map[4][0] = whiteKing;
        map[4][7] = blackKing;
    }
    
    public void draw(Graphics g){
        if(inverted){
            for(int i = 0 ; i < map.length ; i++)
                for(int j = 0 ; j < map[i].length ; j++)
                    try{map[i][j].draw(g, (7 - i) * 80, j * 80);}
                    catch(NullPointerException e){}
        } else {
            for(int i = 0 ; i < map.length ; i++)
                for(int j = 0 ; j < map[i].length ; j++)
                    try{map[i][j].draw(g, i * 80, (7 - j) * 80);}
                    catch(NullPointerException e){}
        }
        
    }
    public void update(location from, location to){
        map[to.getX()]
           [to.getY()]
      = map[from.getX()]
           [from.getY()];
        
        // Remove old piece
        map[from.getX()]
           [from.getY()] = null;
    }
    
    // Invert the board
    public void invert(){
        inverted = !inverted;
        location.setInverted(inverted);
    }
    public Piece getPiece(location l){
        return map[l.getX()][l.getY()];
    }
    public Piece getPiece(int x, int y){
        return map[x][y];
    }
    
    public location getBlackKing(){
        for (int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                if(map[i][j]== blackKing)
                    return new location(i,j);
        return null;
    }
    public location getWhiteKing(){
        for (int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                if(map[i][j]== whiteKing)
                    return new location(i,j);
        return null;
    }
    public void setClone(Piece[][] clone){
        for(int i = 0 ; i < map.length ; i++)
            for(int j = 0; j < map[i].length ; j++)
                map[i][j] = clone[i][j];
    }
    public Piece[][] getClone(){
        Piece[][] clone = new Piece[8][8];
        for(int i = 0 ; i < map.length ; i++)
            for(int j = 0; j < map[i].length ; j++)
                clone[i][j] = map[i][j];
        return clone;
    }
    public Vector<location> getColorPieces(char c){
        Vector <location> P = new Vector();
        for(int i=0; i<8 ;i++)
            for(int j=0;j<8;j++){
                if(map[i][j] == null)
                    continue;
                if(map[i][j].color == c)
                    P.add(new location(i,j));}
        return P;
    }

    
    public void castleKingSide(boolean turn){
        if(turn){
            map[4][0] = null;
            map[6][0] = whiteKing;
            map[5][0] = whiteRook;
            map[7][0] = null;
        }else{
            map[4][7] = null;
            map[6][7] = blackKing;
            map[5][7] = blackRook;
            map[7][7] = null;
        }
    }
    public void castleQueenSide(boolean turn){
        if(turn){
            map[4][0] = null;
            map[2][0] = whiteKing;
            map[3][0] = whiteRook;
            map[0][0] = null;
        }else{
            map[4][7] = null;
            map[2][7] = blackKing;
            map[3][7] = blackRook;
            map[0][7] = null;
        }
    }
    
    public Vector<location> getFile(String str){
        Vector<location> v = new Vector();
        char c = str.charAt(0);
        if(c >= 'a' && c <= 'h'){
            for(int i = 0 ; i < 8 ; i++){
                /*Piece p = map[i][c - 'a'];
                if(p != null)
                    v.add(new location(i, c - 'a'));*/
                Piece p = map[c - 'a'][i];
                if(p != null)
                    v.add(new location(c - 'a', i));
            }
        } else if (c >= '1' && c <= '8'){
            for(int i = 0 ; i < 8 ; i++){
                /*Piece p = map[c - '1'][i];
                if(p != null)
                    v.add(new location(c - '1', i));*/
                Piece p = map[i][c - '1'];
                if(p != null)
                    v.add(new location(i, c - '1'));
            }
        }
        return v;
    }
    
    public void promote(boolean turn, Type type, location from, location to){
        Piece p = null;
        switch(type){
            case ROOK:
                p = (turn) ? whiteRook : blackRook;
                break;
            case KNIGHT:
                p = (turn) ? whiteKnight : blackKnight;
                break;
            case BISHOP:
                p = (turn) ? whiteBishop : blackBishop;
                break;
            case QUEEN:
                p = (turn) ? whiteQueen : blackQueen;
                break;
        }
        if(p == null)
            return;
        
        map[to.getX()]
           [to.getY()] = p;
        map[from.getX()]
           [from.getY()] = null;
    }
}