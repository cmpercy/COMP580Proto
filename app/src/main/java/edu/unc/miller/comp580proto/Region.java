package edu.unc.miller.comp580proto;

//Class that represents the active region of a button within an imageview
public class Region {

    private float x0,x1,y0,y1;
    private String label;

    public Region(float startx, float endx, float starty, float endy, String lbl){
        x0 = startx;
        x1 = endx;
        y0 = starty;
        y1 = endy;
        label = lbl;
    }

    public boolean checkBounds(float x,float y){
        boolean checkX = (x>=x0)&&(x<=x1);
        boolean checkY = (y>=y0)&&(y<=y1);
        return (checkX&&checkY);
    }

    public String getLabel(){
        return label;
    }

    public String toString(){
        return "X0: "+x0+" X1: "+x1+" Y0: "+y0+" Y1: "+y1+" Label: "+label;
    }

}
