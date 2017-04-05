package edu.unc.miller.comp580proto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;

public class RadialActivity extends AppCompatActivity {

    private final String TAG = "RadialActivity";
    ArrayList<Region> regionList;
    ArrayList<Button> buttonList;
    ArrayList<ImageView> imageViewList;
    Region[] exteriorRegionArray = new Region[3];
    Region centralRegion;
    Stack<String> charStack = new Stack<>();
    RelativeLayout rl;
    DisplayMetrics displaymetrics;
    private int setter;                                     //int value used to determine which characters to draw for the keyboard
    private int screenheight,screenwidth;
    private Calendar calendar;
    private Timestamp tstamp;
    private String menu;                                    //string indicating what state/menu/group the keyboard is in
    private long time0, time1;
    private boolean startedCheckingTime;                    //used for timing checks
    private boolean changedActivity;                        //indicates if the activity layout has changed recently
    private boolean canResume = true;                       //indicates if the motionevent in a region can be triggered/resumed
    private boolean checkingExteriorRegionZero, checkingExteriorRegionOne, checkingExteriorRegionTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
    }

    public void initialize(){
        updateUserString();
        enterImmersiveMode();
        grabScreenProperties();
        setter = AToZActivity.indicator;
        setMenuType();
        makeRadialRegions();
        makeExteriorRegions();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        switch(e.getAction()){
            //Handle movement gestures across the screen
            case MotionEvent.ACTION_MOVE:
                //If the event is in a region, push the char onto the stack (only one at a time)
                //Also, only checks the circular regions if the stack is empty
                if(charStack.empty()){
                    //Check each region to see if the touch event is in one of the button regions
                    for(int i=0; i<regionList.size();i++){
                        Region tempregion = regionList.get(i);
                        if(tempregion.checkBounds(e.getRawX(),e.getRawY())){
                                    charStack.push(tempregion.getLabel());
                                    Log.i(TAG,"Character pushed");
                        }
                    }

                }else{
                    if(centralRegion.checkBounds(e.getRawX(),e.getRawY())){
                        //Update the userString
                        appendUserString(charStack.pop());
                        Log.i(TAG,"Character released");
                    }
                }
                //---ROUTINES FOR EXTERIOR BUTTONS BELOW---
                //Check exterior_region_zero for possible keyboard menu transition
                if(!changedActivity&&!checkingExteriorRegionOne&&!checkingExteriorRegionTwo){
                    Region tempRegion = exteriorRegionArray[0];
                    //If event is in bounds of this region...
                    if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                        System.out.println("Running exRegionZero");
                        checkingExteriorRegionZero = true;
                        if(!startedCheckingTime) setTimeZero();
                        if(startedCheckingTime) setTimeOne();
                        //If the user has been in the region long enough, swap keyboard menus
                        if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                            changedActivity = true;
                            exteriorButtonFunctionZero();
                        }
                    }else{
                        //Reset variables when the user has left one of the exterior button regions
                        canResume = true;
                        changedActivity = false;
                        startedCheckingTime = false;
                        checkingExteriorRegionZero = false;
                    }
                }

                //Check exterior_region_one; prevent it from clashing with the above method
                if(!changedActivity&&!checkingExteriorRegionZero&&!checkingExteriorRegionTwo){
                    Region tempRegion = exteriorRegionArray[1];
                    //If event is in bounds of this region...
                    if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                        System.out.println("Running exRegionOne");
                        checkingExteriorRegionOne = true;
                        if(!startedCheckingTime) setTimeZero();
                        if(startedCheckingTime) setTimeOne();
                        //If the user has been in the region long enough, swap keyboard menus
                        if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                            changedActivity = true;
                            exteriorButtonFunctionOne();
                        }
                    }else{
                        //Reset variables when the user has left one of the exterior button regions
                        canResume = true;
                        changedActivity = false;
                        startedCheckingTime = false;
                        checkingExteriorRegionOne = false;
                    }
                }

                //Check exterior_region_two; prevent it from clashing with the above method
                if(!changedActivity&&!checkingExteriorRegionZero&&!checkingExteriorRegionOne){
                    Region tempRegion = exteriorRegionArray[2];
                    //If event is in bounds of this region...
                    if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                        System.out.println("Running exRegionTwo");
                        checkingExteriorRegionTwo = true;
                        if(!startedCheckingTime) setTimeZero();
                        if(startedCheckingTime) setTimeOne();
                        //If the user has been in the region long enough, swap keyboard menus
                        if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                            changedActivity = true;
                            exteriorButtonFunctionTwo();
                        }
                    }else{
                        //Reset variables when the user has left one of the exterior button regions
                        canResume = true;
                        changedActivity = false;
                        startedCheckingTime = false;
                        checkingExteriorRegionTwo = false;
                    }
                }
                break;
            //Handle up motion event on the screen
            case MotionEvent.ACTION_UP:
                //If you lift up on the central select button, return to the previous AtoZ screen
                if(centralRegion.checkBounds(e.getRawX(),e.getRawY())){
                    Intent intent = new Intent(this, AToZActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
        return true;
    }

    //Grab the initial time when the user entered the region
    public void setTimeZero(){
        System.out.println("Setting time zero");
        calendar = Calendar.getInstance();
        tstamp = new Timestamp(calendar.getTime().getTime());
        time0 = tstamp.getTime();
        startedCheckingTime = true;
    }

    //Grab the latest time that the user was last in the region
    public void setTimeOne(){
        System.out.println("Setting time one");
        calendar = Calendar.getInstance();
        tstamp = new Timestamp(calendar.getTime().getTime());
        time1 = tstamp.getTime();
    }

    //Handle events triggered by exterior button zero
    public void exteriorButtonFunctionZero(){
        if(menu.equals("Menu 1")){AToZActivity.indicator=8;}
        else{AToZActivity.indicator=0;}
        reinitialize();
    }

    //Handle events triggered by exterior button one
    public void exteriorButtonFunctionOne(){
        if(menu.equals("Menu 1")||menu.equals("Menu 2")){AToZActivity.indicator=16;}
        else{AToZActivity.indicator=8;}
        reinitialize();
    }

    //Handle events triggered by exterior button two
    public void exteriorButtonFunctionTwo(){
        if(menu.equals("Menu 4")){AToZActivity.indicator=16;}
        else{AToZActivity.indicator=24;}
        reinitialize();
    }

    //Main method to create the radial buttons used for the keyboard layout
    public void makeRadialRegions(){
        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        int numbutt = 8;
        int buttonWidth = Vars.BUTTON_WIDTH; int buttonHeight = Vars.BUTTON_HEIGHT;
        double section = 360/numbutt;

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
        //Re-initialize the arraylists
        regionList = new ArrayList<>(numbutt);
        buttonList = new ArrayList<>(numbutt);
        imageViewList = new ArrayList<>(numbutt);
        //Create a series of "circular" buttons in a circle
        for(int i=0;i<numbutt;i++){
            double degrees = i*section;
            double xpos = Vars.RADIAL_RADIUS*Math.cos(Math.toRadians(degrees))+(screenwidth/2)-25;  //50 is the xsize of the circle image (I moved it over some though 1.5*50)
            double ypos = Vars.RADIAL_RADIUS*Math.sin(Math.toRadians(degrees))+(screenheight/2)+10; //50 is the ysize of the circle image
            ImageView iv = new ImageView(this);
            imageResSetter(iv,setter);   //set the image for the circular button
            //Set coordinates of the circle imageview
            iv.setX((float)xpos);
            iv.setY((float)ypos);
            iv.setScaleX(2);
            iv.setScaleY(2);
            rl.addView(iv);
            imageViewList.add(iv);
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
            buttonList.add(butt);
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

    //Method to create regions for the exterior buttons
    public void makeExteriorRegions(){
        setExteriorButtonText();
        Region buttonRegion;
        buttonRegion = new Region(0.0f,Vars.EXTERIOR_BUTTON_WIDTH,Vars.EDITABLE_TEXT_BOX_HEIGHT,Vars.EXTERIOR_BUTTON_HEIGHT,"Exterior Button 1");
        exteriorRegionArray[0] = buttonRegion;
        buttonRegion = new Region(screenwidth-Vars.EXTERIOR_BUTTON_WIDTH,screenwidth,Vars.EDITABLE_TEXT_BOX_HEIGHT,Vars.EXTERIOR_BUTTON_HEIGHT,"Exterior Button 3");
        exteriorRegionArray[1] = buttonRegion;
        buttonRegion = new Region(0.0f,Vars.EXTERIOR_BUTTON_WIDTH,screenheight-Vars.EXTERIOR_BUTTON_HEIGHT,screenheight,"Exterior Button 3");
        exteriorRegionArray[2] = buttonRegion;
    }

    //Redraws and reinitializes the keyboard layout; gets called from an exterior button function
    public void reinitialize(){
        Log.i(TAG,"Reinitializing keyboard");
        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        //Remove the buttons and pictures from the view
        removeButtons();
        removeImages();
        //Redraw the views and reset their functions
        setter = AToZActivity.indicator;
        setMenuType();
        makeRadialRegions();
        //Reset the text for the exterior buttons
        setExteriorButtonText();
        //Throw away any character the user might've passed over while swapping keyboards
        if(!charStack.empty()) charStack.pop();
        //Reset the triggers to prevent instant keyboard swaps on new keyboard start
        changedActivity = false;
        canResume = false;
    }

    //Setter for exterior button text
    public void setExteriorButtonText(){
        Button button;
        button = (Button)findViewById(R.id.exterior_button_0);
        if(menu.equals("Menu 1")) {button.setText(R.string.menu_1);}
        else{button.setText(R.string.menu_1);}

        button = (Button)findViewById(R.id.exterior_button_1);
        if(menu.equals("Menu 1")||menu.equals("Menu 2")){button.setText(R.string.menu_3);}
        else{button.setText(R.string.menu_2);}

        button = (Button)findViewById(R.id.exterior_button_2);
        if(menu.equals("Menu 4")){button.setText(R.string.menu_3);}
        else{button.setText(R.string.menu_4);}

    }

    //Remove all dynamically created buttons from view; not the exterior buttons
    public void removeButtons(){
        for(int i=0; i<buttonList.size(); i++){
            rl.removeView(buttonList.get(i));
        }
    }

    //Remove all dynamically create imageViews from view
    public void removeImages(){
        for(int i=0; i<imageViewList.size(); i++){
            rl.removeView(imageViewList.get(i));
        }
    }

    //Pick the text to use for the button
    //English characters by frequency layout
    public void buttonTextSetter(Button b, int in){
        if(in==0){b.setText("E");}if(in==1){b.setText("T");}if(in==2){b.setText("A");}
        if(in==3){b.setText("O");}if(in==4){b.setText("I");}if(in==5){b.setText("N");}
        if(in==6){b.setText("S");}if(in==7){b.setText("R");}if(in==8){b.setText("H");}
        if(in==9){b.setText("D");}if(in==10){b.setText("L");}if(in==11){b.setText("U");}
        if(in==12){b.setText("C");}if(in==13){b.setText("M");}if(in==14){b.setText("F");}
        if(in==15){b.setText("Y");}if(in==16){b.setText("W");}if(in==17){b.setText("G");}
        if(in==18){b.setText("P");}if(in==19){b.setText("B");}if(in==20){b.setText("V");}
        if(in==21){b.setText("K");}if(in==22){b.setText("X");}if(in==23){b.setText("Q");}
        if(in==24){b.setText("J");}if(in==25){b.setText("Z");}if(in==26){b.setText(".");}
        if(in==27){b.setText(",");}if(in==28){b.setText("!");}if(in==29){b.setText("?");}
        if(in==30){b.setText(";");}if(in==31){b.setText("@");}
    }

    //Pick the resource to load into an imageview
    //24point font for the letters
    //English characters by frequency layout
    public void imageResSetter(ImageView imv, int in){
        if(in==0){imv.setImageResource(R.drawable.e);}if(in==1){imv.setImageResource(R.drawable.t);}if(in==2){imv.setImageResource(R.drawable.a);}
        if(in==3){imv.setImageResource(R.drawable.o);}if(in==4){imv.setImageResource(R.drawable.i);}if(in==5){imv.setImageResource(R.drawable.n);}
        if(in==6){imv.setImageResource(R.drawable.s);}if(in==7){imv.setImageResource(R.drawable.r);}if(in==8){imv.setImageResource(R.drawable.h);}
        if(in==9){imv.setImageResource(R.drawable.d);}if(in==10){imv.setImageResource(R.drawable.l);}if(in==11){imv.setImageResource(R.drawable.u);}
        if(in==12){imv.setImageResource(R.drawable.c);}if(in==13){imv.setImageResource(R.drawable.m);}if(in==14){imv.setImageResource(R.drawable.f);}
        if(in==15){imv.setImageResource(R.drawable.y);}if(in==16){imv.setImageResource(R.drawable.w);}if(in==17){imv.setImageResource(R.drawable.g);}
        if(in==18){imv.setImageResource(R.drawable.p);}if(in==19){imv.setImageResource(R.drawable.b);}if(in==20){imv.setImageResource(R.drawable.v);}
        if(in==21){imv.setImageResource(R.drawable.k);}if(in==22){imv.setImageResource(R.drawable.x);}if(in==23){imv.setImageResource(R.drawable.q);}
        if(in==24){imv.setImageResource(R.drawable.j);}if(in==25){imv.setImageResource(R.drawable.z);}if(in==26){imv.setImageResource(R.drawable.period);}
        if(in==27){imv.setImageResource(R.drawable.comma);}if(in==28){imv.setImageResource(R.drawable.exclamation);}if(in==29){imv.setImageResource(R.drawable.question);}
        if(in==30){imv.setImageResource(R.drawable.semicolon);}if(in==31){imv.setImageResource(R.drawable.at);}
    }

//    //Pick the text to use for the button
//    public void buttonTextSetter(Button b, int in){
//        if(in==0){b.setText("A");}if(in==1){b.setText("B");}if(in==2){b.setText("C");}
//        if(in==3){b.setText("D");}if(in==4){b.setText("E");}if(in==5){b.setText("F");}
//        if(in==6){b.setText("G");}if(in==7){b.setText("H");}if(in==8){b.setText("I");}
//        if(in==9){b.setText("J");}if(in==10){b.setText("K");}if(in==11){b.setText("L");}
//        if(in==12){b.setText("M");}if(in==13){b.setText("N");}if(in==14){b.setText("O");}
//        if(in==15){b.setText("P");}if(in==16){b.setText("Q");}if(in==17){b.setText("R");}
//        if(in==18){b.setText("S");}if(in==19){b.setText("T");}if(in==20){b.setText("U");}
//        if(in==21){b.setText("V");}if(in==22){b.setText("W");}if(in==23){b.setText("X");}
//        if(in==24){b.setText("Y");}if(in==25){b.setText("Z");}if(in==26){b.setText(".");}
//        if(in==27){b.setText(",");}if(in==28){b.setText("!");}if(in==29){b.setText("?");}
//        if(in==30){b.setText(";");}if(in==31){b.setText("@");}
//    }
//
//    //Pick the resource to load into an imageview
//    //24point font for the letters
//    public void imageResSetter(ImageView imv, int in){
//        if(in==0){imv.setImageResource(R.drawable.a);}if(in==1){imv.setImageResource(R.drawable.b);}if(in==2){imv.setImageResource(R.drawable.c);}
//        if(in==3){imv.setImageResource(R.drawable.d);}if(in==4){imv.setImageResource(R.drawable.e);}if(in==5){imv.setImageResource(R.drawable.f);}
//        if(in==6){imv.setImageResource(R.drawable.g);}if(in==7){imv.setImageResource(R.drawable.h);}if(in==8){imv.setImageResource(R.drawable.i);}
//        if(in==9){imv.setImageResource(R.drawable.j);}if(in==10){imv.setImageResource(R.drawable.k);}if(in==11){imv.setImageResource(R.drawable.l);}
//        if(in==12){imv.setImageResource(R.drawable.m);}if(in==13){imv.setImageResource(R.drawable.n);}if(in==14){imv.setImageResource(R.drawable.o);}
//        if(in==15){imv.setImageResource(R.drawable.p);}if(in==16){imv.setImageResource(R.drawable.q);}if(in==17){imv.setImageResource(R.drawable.r);}
//        if(in==18){imv.setImageResource(R.drawable.s);}if(in==19){imv.setImageResource(R.drawable.t);}if(in==20){imv.setImageResource(R.drawable.u);}
//        if(in==21){imv.setImageResource(R.drawable.v);}if(in==22){imv.setImageResource(R.drawable.w);}if(in==23){imv.setImageResource(R.drawable.x);}
//        if(in==24){imv.setImageResource(R.drawable.y);}if(in==25){imv.setImageResource(R.drawable.z);}if(in==26){imv.setImageResource(R.drawable.period);}
//        if(in==27){imv.setImageResource(R.drawable.comma);}if(in==28){imv.setImageResource(R.drawable.exclamation);}if(in==29){imv.setImageResource(R.drawable.question);}
//        if(in==30){imv.setImageResource(R.drawable.semicolon);}if(in==31){imv.setImageResource(R.drawable.at);}
//    }

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

    //Delete the last character of the userString
    public void deleteLastCharUserString(){
        String rephrase;
        if(Vars.userString != null && Vars.userString.length() >= 1){
            rephrase = Vars.userString.substring(0,Vars.userString.length()-1);
            Vars.userString = rephrase;
        }
        EditText editText = (EditText)findViewById(R.id.radial_text_field);
        if(editText != null) editText.setText(Vars.userString);
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

    public void setMenuType(){
        if(setter==0) menu = "Menu 1";
        else if(setter==8) menu = "Menu 2";
        else if(setter==16) menu = "Menu 3";
        else if(setter==24) menu = "Menu 4";
        else{throw new RuntimeException("Unknown menu type");}
    }

    public void enterImmersiveMode(){
        //Enter immersive mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void grabScreenProperties(){
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;
    }

    protected void onResume(){super.onResume(); updateUserString();}

    protected void onPause(){super.onPause();}

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

    //force back button to return to home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static <AnyType> void print(AnyType s){
        System.out.println(s);
    }
    public static <AnyType> void printContinuous(AnyType s) {System.out.print(s);}
}
