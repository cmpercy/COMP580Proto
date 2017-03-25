package edu.unc.miller.comp580proto;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class RadialActivity extends AppCompatActivity {

    Activity activity;
    int setter;
    ArrayList<Region> regionList;
    ArrayList<Region> extraRegionList;
    Region centralRegion;
    Stack<String> charStack = new Stack<>();
    boolean pushed = false;
    RelativeLayout rl;
    DisplayMetrics displaymetrics;
    int screenheight,screenwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
            super.onWindowFocusChanged(hasFocus);
            if(hasFocus){
                //Re-enter immersive mode
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                //Create the region for the central button that performs character selection confirmation
                Button butt = (Button)findViewById(R.id.selectbutton);
                float regionx = butt.getX(); float regiony = butt.getY();
                centralRegion = new Region(regionx,regionx+(float)butt.getWidth(),regiony,regiony+(float)butt.getHeight(),"Select");
                //drawRegions();    //makes the button regions visible
            }
    }

    //Need to check on this.  Sometimes when the app launches, it lags pretty bad.  Sometimes
    //it works perfectly fine though.  Might need to make a new thread/activity/or view?
    @Override
    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            case MotionEvent.ACTION_MOVE:
                //If the event is in a region, push the char onto the stack (only one at a time)
                //Also, only checks the circular regions if the stack is empty
                if(charStack.empty()){
                    //Check each region to see if the touch event is in one of the button regions
                    for(int i=0; i<regionList.size();i++){
                        Region tempregion = regionList.get(i);
                        if(tempregion.checkBounds(e.getX(),e.getY())){
                                    charStack.push(tempregion.getLabel());
                                    print("Pushed "+tempregion.getLabel());
                        }
                    }
                }else{
                    if(centralRegion.checkBounds(e.getX(),e.getY())){
                        //Update the userString
                        appendUserString(charStack.peek());
                        print("Popped: "+charStack.pop());
                    }
                }
                break;
            default:
                //Do nothing
                break;
        }
        return true;
    }

    protected void onResume(){super.onResume(); updateUserString();}

    protected void onPause(){super.onPause();}

    public void initialize(){
        updateUserString();
        setter = AToZActivity.indicator;
        //prepareExtraButtons();
        //Enter immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //onClick listener for the radial buttons
        View.OnClickListener buttonListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Stuff to be executed when any button is clicked
                Button registeredButton = (Button)findViewById(v.getId());
                if(registeredButton!=null){
                    //Update the userString
                    appendUserString(registeredButton.getText().toString());
                    print(registeredButton.getText());
                }
            }
        };
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;

        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        int radius = 325;
        int numbutt = 8;
        int buttonWidth = Vars.BUTTON_WIDTH; int buttonHeight = Vars.BUTTON_HEIGHT;
        double section = 360/numbutt;

        regionList = new ArrayList<>(numbutt);
        //Create a series of "circular" buttons in a circle
        for(int i=0;i<numbutt;i++){
            double degrees = i*section;
            double xpos = radius*Math.cos(Math.toRadians(degrees))+(screenwidth/2)-25;  //50 is the xsize of the circle image (I moved it over some though 1.5*50)
            double ypos = radius*Math.sin(Math.toRadians(degrees))+(screenheight/2)+10; //50 is the ysize of the circle image
            ImageView iv = new ImageView(this);
            imageResSetter(iv,setter);   //set the image for the circular button
            //Set coordinates of the circle imageview
            iv.setX((float)xpos);
            iv.setY((float)ypos);
            iv.setScaleX(2);
            iv.setScaleY(2);
            rl.addView(iv);
            //Set information and coordinates for the buttons
            Button butt = new Button(this);
            butt.setId(i);
            butt.setX((float)xpos-50);
            butt.setY((float)ypos-40);
            butt.setAlpha(0.0f);      //makes buttons transparent when uncommented
            butt.setWidth(buttonWidth);
            butt.setHeight(buttonHeight);
            buttonTextSetter(butt,setter);   //set the text for the circular button
            butt.setOnClickListener(buttonListener);
            //Create button regions (used for slide motion detection) and add them to a list
            float regionx = butt.getX(); float regiony = butt.getY();
            String label = butt.getText().toString();
            //Create the interactable region for the button
            Region buttonRegion = new Region(regionx,regionx+(float)buttonWidth,regiony,regiony+(float)buttonHeight,label);
            regionList.add(buttonRegion);
            rl.addView(butt);
            setter++;
        }
        AToZActivity.indicator = 0;
    }


    //Pick the text to use for the button
    public void buttonTextSetter(Button b, int in){
        if(in==0){b.setText("A");}if(in==1){b.setText("B");}if(in==2){b.setText("C");}
        if(in==3){b.setText("D");}if(in==4){b.setText("E");}if(in==5){b.setText("F");}
        if(in==6){b.setText("G");}if(in==7){b.setText("H");}if(in==8){b.setText("I");}
        if(in==9){b.setText("J");}if(in==10){b.setText("K");}if(in==11){b.setText("L");}
        if(in==12){b.setText("M");}if(in==13){b.setText("N");}if(in==14){b.setText("O");}
        if(in==15){b.setText("P");}if(in==16){b.setText("Q");}if(in==17){b.setText("R");}
        if(in==18){b.setText("S");}if(in==19){b.setText("T");}if(in==20){b.setText("U");}
        if(in==21){b.setText("V");}if(in==22){b.setText("W");}if(in==23){b.setText("X");}
        if(in==24){b.setText("Y");}if(in==25){b.setText("Z");}if(in==26){b.setText(".");}
        if(in==27){b.setText(",");}if(in==28){b.setText("!");}if(in==29){b.setText("?");}
        if(in==30){b.setText(";");}if(in==31){b.setText("@");}
    }

    //Pick the resource to load into an imageview
    //24point font for the letters
    public void imageResSetter(ImageView imv, int in){
        if(in==0){imv.setImageResource(R.drawable.a);}if(in==1){imv.setImageResource(R.drawable.b);}if(in==2){imv.setImageResource(R.drawable.c);}
        if(in==3){imv.setImageResource(R.drawable.d);}if(in==4){imv.setImageResource(R.drawable.e);}if(in==5){imv.setImageResource(R.drawable.f);}
        if(in==6){imv.setImageResource(R.drawable.g);}if(in==7){imv.setImageResource(R.drawable.h);}if(in==8){imv.setImageResource(R.drawable.i);}
        if(in==9){imv.setImageResource(R.drawable.j);}if(in==10){imv.setImageResource(R.drawable.k);}if(in==11){imv.setImageResource(R.drawable.l);}
        if(in==12){imv.setImageResource(R.drawable.m);}if(in==13){imv.setImageResource(R.drawable.n);}if(in==14){imv.setImageResource(R.drawable.o);}
        if(in==15){imv.setImageResource(R.drawable.p);}if(in==16){imv.setImageResource(R.drawable.q);}if(in==17){imv.setImageResource(R.drawable.r);}
        if(in==18){imv.setImageResource(R.drawable.s);}if(in==19){imv.setImageResource(R.drawable.t);}if(in==20){imv.setImageResource(R.drawable.u);}
        if(in==21){imv.setImageResource(R.drawable.v);}if(in==22){imv.setImageResource(R.drawable.w);}if(in==23){imv.setImageResource(R.drawable.x);}
        if(in==24){imv.setImageResource(R.drawable.y);}if(in==25){imv.setImageResource(R.drawable.z);}if(in==26){imv.setImageResource(R.drawable.period);}
        if(in==27){imv.setImageResource(R.drawable.comma);}if(in==28){imv.setImageResource(R.drawable.exclamation);}if(in==29){imv.setImageResource(R.drawable.question);}
        if(in==30){imv.setImageResource(R.drawable.semicolon);}if(in==31){imv.setImageResource(R.drawable.at);}
    }

    public void backspacePressed(View view){
        deleteLastCharUserString();
    }

    public void spacePressed(View view){
        appendUserString(" ");
        print(" ");
    }

    public void drawRegions(){
        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        for(int i=0; i<regionList.size();i++){
            Button butt = new Button(this);
            float x0 = regionList.get(i).getX0(); float y0 = regionList.get(i).getY0();
            float x1 = regionList.get(i).getX1(); float y1 = regionList.get(i).getY1();
            butt.setX(x0); butt.setY(y0);
            butt.setWidth(140); butt.setHeight(125);
            butt.setBackgroundColor(Color.YELLOW);
            rl.addView(butt);

        }
    }

    public void prepareExtraButtons(){
        //TODO Make and handle regions for the extra buttons
    }

    //Delete the last character of the userString
    public void deleteLastCharUserString(){
        String rephrase;
        if(Vars.userString!=null && Vars.userString.length()>=1){
            rephrase = Vars.userString.substring(0,Vars.userString.length()-1);
            Vars.userString = rephrase;
        }
        EditText editText = (EditText)findViewById(R.id.radial_text_field);
        if(editText!=null) editText.setText(Vars.userString);
        print("Deleted last character");
    }

    public void appendUserString(String s){
        if(Vars.userString==null) Vars.userString = "";
        Vars.userString = Vars.userString + s;
        updateUserString();
    }

    //Set the text of the edit text box
    public void updateUserString(){
        if(Vars.userString!=null){
            EditText editText = (EditText)findViewById(R.id.radial_text_field);
            if(editText!=null) editText.setText(Vars.userString);
        }
    }

    public static <AnyType> void print(AnyType s){
        System.out.println(s);
    }
    public static <AnyType> void printContinuous(AnyType s) {System.out.print(s);}
}
