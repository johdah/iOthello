package org.iothello.logic;

/**
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Vector2D {
	private int x;
    private int y;
    
    public Vector2D(){
        x = 0;
        y = 0;
    }
    
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(Vector2D ref){
        this.x = ref.x;
        this.y = ref.y;
    }
    
    public Boolean equals(Vector2D v){
        return this.x == v.x && this.y == v.y;
    }
    
    public void add(Vector2D v){
        this.x += v.x;
        this.y += v.y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public String toString(){
        String s = + x + " " + y;
        return s;
    }
}
