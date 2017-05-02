package edu.unc.miller.comp580proto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

public class RadialActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private final String TAG = "RadialActivity";
    ArrayList<Region> regionList;
    ArrayList<Button> buttonList;
    ArrayList<ImageView> imageViewList;
    Region[] exteriorRegionArray = new Region[4];
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
    private boolean checkingExteriorRegionZero, checkingExteriorRegionOne, checkingExteriorRegionTwo, checkingExteriorRegionThree;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 0); //check if tts is available
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //^^ prevents softkeyboard from opening despite edittext gaining focus
        EditText text = (EditText) findViewById(R.id.radial_text_field);
        text.setRawInputType(InputType.TYPE_CLASS_TEXT);
        text.setTextIsSelectable(true);
        text.requestFocus();
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
                                    stringToSpeech(charStack.peek());
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
                if(!changedActivity&&!checkingExteriorRegionOne&&!checkingExteriorRegionTwo&&!checkingExteriorRegionThree){
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
                if(!changedActivity&&!checkingExteriorRegionZero&&!checkingExteriorRegionTwo&&!checkingExteriorRegionThree){
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
                if(!changedActivity&&!checkingExteriorRegionZero&&!checkingExteriorRegionOne&&!checkingExteriorRegionThree){
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

                //Check exterior_region_three; prevent it from clashing with the above method
                if(!changedActivity&&!checkingExteriorRegionZero&&!checkingExteriorRegionOne&&!checkingExteriorRegionTwo){
                    Region tempRegion = exteriorRegionArray[3];
                    //If event is in bounds of this region...
                    if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                        System.out.println("Running exRegionThree");
                        checkingExteriorRegionThree = true;
                        if(!startedCheckingTime) setTimeZero();
                        if(startedCheckingTime) setTimeOne();
                        //If the user has been in the region long enough, swap keyboard menus
                        if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                            changedActivity = true;
                            exteriorButtonFunctionThree();
                        }
                    }else{
                        //Reset variables when the user has left one of the exterior button regions
                        canResume = true;
                        changedActivity = false;
                        startedCheckingTime = false;
                        checkingExteriorRegionThree = false;
                    }
                }

                break;
            //Handle up motion event on the screen
            case MotionEvent.ACTION_UP:
                //If you lift up on the central select button, return to the previous AtoZ screen
                if(centralRegion.checkBounds(e.getRawX(),e.getRawY())){
                    appendUserString(" ");
                    print(" ");
                    Intent intent = new Intent(this, AToZActivity.class);
                    AToZActivity.changedActivity = false;
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

    //Handle events triggered by exterior button three
    public void exteriorButtonFunctionThree(){
        AToZActivity.indicator = (menu.equals("Number Menu")) ? 24 : 32;
        reinitialize();
    }

    //Main method to create the radial buttons used for the keyboard layout
    public void makeRadialRegions(){
        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        int numbutt = 8;
        if(menu.equals("Number Menu")) numbutt = 10;        //number menu requires 10 buttons
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
                    stringToSpeech(registeredButton.getText().toString());
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
            butt.setX((float)xpos-40);
            butt.setY((float)ypos-40);
            butt.setAlpha(0.0f);      //makes buttons invisible when uncommented
            butt.setWidth(buttonWidth);
            butt.setHeight(buttonHeight);
            buttonTextSetter(butt,setter);   //set the text for the circular button
            butt.setOnClickListener(buttonListener);
            buttonList.add(butt);
            //Create button regions (used for slide motion detection) and add them to a list
            float regionx = butt.getX(); float regiony = butt.getY();
            String label = butt.getText().toString();
            //Create the interactive region for the button
            Region buttonRegion = new Region(regionx,regionx+(float)buttonWidth,regiony,regiony+(float)buttonHeight,label);
            regionList.add(buttonRegion);
            rl.addView(butt);
            setter++;
        }
        AToZActivity.indicator = 0;
    }

    //Method to create regions for the exterior buttons
    public void makeExteriorRegions(){
        setExteriorRegionImages();
        //setExteriorButtonText();
        Region buttonRegion;
        float topx1 = getResources().getDimension(R.dimen.exterior_buttons_width); float topy0 = getResources().getDimension(R.dimen.editable_text_box_height);
        float topy1 = getResources().getDimension(R.dimen.exterior_buttons_height);
        buttonRegion = new Region(0.0f,topx1,topy0,topy0+topy1,"Exterior Button 0");
        exteriorRegionArray[0] = buttonRegion;
        buttonRegion = new Region(screenwidth-topx1,screenwidth,topy0,topy0+topy1,"Exterior Button 1");
        exteriorRegionArray[1] = buttonRegion;
        buttonRegion = new Region(0.0f,topx1,screenheight-topy1/2,screenheight+topy1,"Exterior Button 2");
        exteriorRegionArray[2] = buttonRegion;
        buttonRegion = new Region(screenwidth-topx1,screenwidth,screenheight-topy1/2,screenheight+topy1,"NUMBERS");
        exteriorRegionArray[3] = buttonRegion;
        //drawOverExteriorRegions();              //uncomment to draw green boxes over active exterior regions
    }

    //Draws green boxes over active exterior regions
    public void drawOverExteriorRegions(){
        //Draw exterior regions from i=0 to i=exteriorRegionArray.length-1
        for(int i=0; i<exteriorRegionArray.length; i++){
            TextView tv = new TextView(this);
            Region region = exteriorRegionArray[i];
            tv.setText(region.getLabel());
            tv.setTextColor(Color.BLACK);
            tv.setX(region.getX0()); tv.setY(region.getY0());
            tv.setWidth((int)Math.abs((region.getX1()-region.getX0()))); tv.setHeight((int)Math.abs(region.getY1()-region.getY0()));
            tv.setBackgroundColor(Color.GREEN);
            rl.addView(tv);
        }
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
        //Set the images for the exterior buttons
        setExteriorRegionImages();
        //Throw away any character the user might've passed over while swapping keyboards
        if(!charStack.empty()) charStack.pop();
        //Reset the triggers to prevent instant keyboard swaps on new keyboard start
        changedActivity = false;
        canResume = false;
    }

    //Setter for exterior region images
    public void setExteriorRegionImages(){
        ImageView iv;
        GlideDrawableImageViewTarget imageViewTarget;
        int drawable;
        iv = (ImageView)findViewById(R.id.exterior_button_0);
        //Determine drawable for ExButton0
        drawable = (menu.equals("Menu 1")) ? R.drawable.zone2 : R.drawable.zone1;
        imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this)
                .load(drawable)
                .override((int)getResources().getDimension(R.dimen.exterior_buttons_width),(int)getResources().getDimension(R.dimen.exterior_buttons_height))
                .fitCenter()
                .into(imageViewTarget);
        iv = (ImageView)findViewById(R.id.exterior_button_1);
        //Determine drawable for ExButton1
        drawable = (menu.equals("Menu 1")||menu.equals("Menu 2")) ? R.drawable.zone3 : R.drawable.zone2;
        imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this)
                .load(drawable)
                .override((int)getResources().getDimension(R.dimen.exterior_buttons_width),(int)getResources().getDimension(R.dimen.exterior_buttons_height))
                .fitCenter()
                .into(imageViewTarget);
        iv = (ImageView)findViewById(R.id.exterior_button_2);
        //Determine drawable for ExButton2
        drawable = (menu.equals("Menu 1")||menu.equals("Menu 2")||menu.equals("Menu 3")) ? R.drawable.zone4 : R.drawable.zone3;
        imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this)
                .load(drawable)
                .override((int)getResources().getDimension(R.dimen.exterior_buttons_width),(int)getResources().getDimension(R.dimen.exterior_buttons_height))
                .fitCenter()
                .into(imageViewTarget);
        iv = (ImageView)findViewById(R.id.exterior_button_3);
        //Determine drawable for ExButton3
        drawable = (menu.equals("Number Menu")) ? R.drawable.zone4 : R.drawable.zone5;
        imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(this)
                .load(drawable)
                .override((int)getResources().getDimension(R.dimen.exterior_buttons_width),(int)getResources().getDimension(R.dimen.exterior_buttons_height))
                .fitCenter()
                .into(imageViewTarget);
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
        if(in==0){b.setText("e");}if(in==1){b.setText("t");}if(in==2){b.setText("a");}
        if(in==3){b.setText("o");}if(in==4){b.setText("i");}if(in==5){b.setText("n");}
        if(in==6){b.setText("s");}if(in==7){b.setText("r");}if(in==8){b.setText("h");}
        if(in==9){b.setText("d");}if(in==10){b.setText("l");}if(in==11){b.setText("u");}
        if(in==12){b.setText("c");}if(in==13){b.setText("m");}if(in==14){b.setText("f");}
        if(in==15){b.setText("y");}if(in==16){b.setText("w");}if(in==17){b.setText("g");}
        if(in==18){b.setText("p");}if(in==19){b.setText("b");}if(in==20){b.setText("v");}
        if(in==21){b.setText("k");}if(in==22){b.setText("x");}if(in==23){b.setText("q");}
        if(in==24){b.setText("j");}if(in==25){b.setText("z");}if(in==26){b.setText(".");}
        if(in==27){b.setText(",");}if(in==28){b.setText("!");}if(in==29){b.setText("?");}
        if(in==30){b.setText(";");}if(in==31){b.setText("@");}if(in==32){b.setText("0");}
        if(in==33){b.setText("1");}if(in==34){b.setText("2");}if(in==35){b.setText("3");}
        if(in==36){b.setText("4");}if(in==37){b.setText("5");}if(in==38){b.setText("6");}
        if(in==39){b.setText("7");}if(in==40){b.setText("8");}if(in==41){b.setText("9");}
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
        if(in==30){imv.setImageResource(R.drawable.semicolon);}if(in==31){imv.setImageResource(R.drawable.at);}if(in==32){imv.setImageResource(R.drawable.zero);}
        if(in==33){imv.setImageResource(R.drawable.one);}if(in==34){imv.setImageResource(R.drawable.two);}if(in==35){imv.setImageResource(R.drawable.three);}
        if(in==36){imv.setImageResource(R.drawable.four);}if(in==37){imv.setImageResource(R.drawable.five);}if(in==38){imv.setImageResource(R.drawable.six);}
        if(in==39){imv.setImageResource(R.drawable.seven);}if(in==40){imv.setImageResource(R.drawable.eight);}if(in==41){imv.setImageResource(R.drawable.nine);}
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

    public void appendUserString(String s){
        if(Vars.userString == null) Vars.userString = "";
        Vars.userString = Vars.userString + s;
        updateUserString();
    }

    //Set the text of the edit text box, editText id must match the editText id for this activity
    public void updateUserString(){
        if(Vars.userString!=null){
            EditText editText = (EditText)findViewById(R.id.radial_text_field);
            if(editText!=null) editText.setText(Vars.userString);
            editText.setSelection(editText.getText().length()); //put cursor at the end of the text
        }
    }

    public void setMenuType(){
        if(setter==0) menu = "Menu 1";
        else if(setter==8) menu = "Menu 2";
        else if(setter==16) menu = "Menu 3";
        else if(setter==24) menu = "Menu 4";
        else if(setter==32) {menu = "Number Menu";   System.out.println("Set menu to numbahs");}
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

    protected void onResume(){
        super.onResume();
        updateUserString();
    }

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
        Intent intent = new Intent(this, AToZActivity.class);
        AToZActivity.changedActivity = false;
        startActivity(intent);
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, this); //initialize tts if available
            }
        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US); //set language to US English
            tts.setPitch(1.1f);
        }
    }

    //call to push Vars.userString to be spoken
    public void stringToSpeech(String s) {
        tts.speak(s, TextToSpeech.QUEUE_ADD, null);
    }

    public static <AnyType> void print(AnyType s){
        System.out.println(s);
    }
}
