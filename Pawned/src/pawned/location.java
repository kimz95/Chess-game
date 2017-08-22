package pawned;
// Contains the x,y coordinates of a square

public class location {
    private int x;      // square c  eg. 0
    private int y;      // square r  eg. 7
    private static boolean inverted;
    public static void setInverted(boolean inv){
        inverted = inv;
    }
    public location(int nx, int ny){
        x = nx;
        y = ny;
    }
    public location(String str){
        x = str.charAt(0) - 'a';
        y = str.charAt(1) - '1';
    }
    
    
    // Create location from mouse input
    public static location locationize(int x, int y){
        if(inverted)
            return new location(7 - x/80, y/80);
        return new location(x/80, 7 - y/80);
    }
    
    // Return game x,y coordinates (0..7)
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    // Return real x,y coordinates (0..7)
    public int getScreenX(){
        if(inverted)
            return 7 - x;
        return x;
    }
    public int getScreenY(){
        if(inverted)
            return y;
        return 7 - y;
    }
    
    public boolean equals(Object o){
        if(super.equals(o))
            return true;
        location l = (location)o;
        
        if(x == l.x
        && y == l.y)
            return true;
        return false;
    }
    
    public String toString(){
        return String.valueOf((char)(x + 'a')) + String.valueOf(y + 1);
    }
}
