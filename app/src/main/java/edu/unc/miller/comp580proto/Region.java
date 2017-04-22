package edu.unc.miller.comp580proto;

//Class that represents the active region of a button within an imageview
public class Region {

    private float x0,x1,y0,y1;
    private String label;

    Region(float startx, float endx, float starty, float endy, String lbl){
        x0 = startx;
        x1 = endx;
        y0 = starty;
        y1 = endy;
        label = lbl;
    }

    boolean checkBounds(float x,float y){
        boolean checkX = (x>=x0)&&(x<=x1);
        boolean checkY = (y>=y0)&&(y<=y1);
        return (checkX&&checkY);
    }

    float getX0(){return x0;}
    float getX1(){return x1;}
    float getY0(){return y0;}
    float getY1(){return y1;}
    void setX0(int a){this.x0=a;}
    void setX1(int a){this.x1=a;}
    void setY0(int a){this.y0=a;}
    void setY1(int a){this.y1=a;}

    public String getLabel(){
        return label;
    }

    public String toString(){
        return "X0: "+x0+" X1: "+x1+" Y0: "+y0+" Y1: "+y1+" Label: "+label;
    }

}
