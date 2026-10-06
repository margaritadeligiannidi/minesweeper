
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;


public class mineTile extends JButton{
    private int r,c;
    private  boolean isMine=false;
    private boolean isFlag=false;
    private boolean isClickable=true;
    
    
    
    //constractor
    public mineTile(int r,int c){
        this.r=r;
        this.c=c;
    }

    //setters and getters
    public int getR() {
        return r;
    }

    public int getC() {
        return c;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setC(int c) {
        this.c = c;
    }
    

    public boolean getIsMine(){
        return isMine;
    }
    
    public void setIsMine(boolean isMine){
        this.isMine=isMine;
    }
    
    public boolean getIsFlag(){
        return isFlag;
    }
    
    public void setIsFlag(boolean isFlag){
        this.isFlag=isFlag;
    }
    
    public void setIsClickable(boolean isClickable){
        this.isClickable=isClickable;
    }
    
    public boolean getIsClickable(){
        return isClickable;
    }
    
}