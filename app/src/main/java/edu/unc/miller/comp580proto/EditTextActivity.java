package edu.unc.miller.comp580proto;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class EditTextActivity extends AppCompatActivity {

    RelativeLayout rl;
    DisplayMetrics displaymetrics;
    private int screenheight,screenwidth;
    private ArrayList<Region> editTextRegionList = new ArrayList<>();
    static boolean changedActivity;                                                                     //flag to suspend action of touch events
    boolean checkingAZ0,checkingAZ1,checkingAZ2,checkingAZ3,checkingAZ4, checkingAZ5, checkingAZ6;      //flags used for touch event confirmation
    boolean startedCheckingTime;
    private Calendar calendar;
    private Timestamp tstamp;
    private long time0,time1;
    boolean canResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        //---ROUTINES FOR EDIT TEXT BUTTONS BELOW---
        //Check ET0 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = editTextRegionList.get(0);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                //System.out.println("Running EditRegion0");
                checkingAZ0 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, do something
                if(Math.abs(time0-time1)>Vars.EDIT_TEXT_TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionZero();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ0 = false;
            }
        }
        //Check ET1 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = editTextRegionList.get(1);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                //System.out.println("Running EditRegion1");
                checkingAZ1 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, do something
                if(Math.abs(time0-time1)>Vars.EDIT_TEXT_TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionOne();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ1 = false;
            }
        }
        //Check ET2 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = editTextRegionList.get(2);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                //System.out.println("Running EditRegion2");
                checkingAZ2 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, do something
                if(Math.abs(time0-time1)>Vars.EDIT_TEXT_TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionTwo();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ2 = false;
            }
        }
        //Check ET3 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = editTextRegionList.get(3);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                //System.out.println("Running EditRegion3");
                checkingAZ3 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, do something
                if(Math.abs(time0-time1)>Vars.EDIT_TEXT_TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionThree();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ3 = false;
            }
        }
        //Check ET4 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = editTextRegionList.get(4);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                //System.out.println("Running EditRegion4");
                checkingAZ4 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, do something
                if(Math.abs(time0-time1)>Vars.EDIT_TEXT_TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionFour();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ4 = false;
            }
        }
        return true;
    }

    public void AZButtonFunctionZero(){
        System.out.println("Left Arrow");
        resetTimer(); resetFlags();
    }

    public void AZButtonFunctionOne(){
        System.out.println("Right Arrow");
        resetTimer(); resetFlags();
    }

    public void AZButtonFunctionTwo(){
        System.out.println("Delete");
        resetTimer(); resetFlags();
    }

    public void AZButtonFunctionThree(){
        System.out.println("Space");
        resetTimer(); resetFlags();
    }

    public void AZButtonFunctionFour(){
        System.out.println("Help");
        resetTimer(); resetFlags();
    }

    //Resets timing variables to allow for repeated execution on holds
    public void resetTimer(){
        time0 = 0; time1 = 0;
        changedActivity = false;
    }

    //Reset flags to indicate which region is active, is used to allow access to other regions after triggering one's function
    public void resetFlags(){
        checkingAZ0=false; checkingAZ1=false; checkingAZ2=false;
        checkingAZ3=false; checkingAZ4=false; checkingAZ5=false;
        checkingAZ6=false;
    }

    public void initialize(){
        updateUserString();
        enterImmersiveMode();
        grabScreenProperties();
        setImageViewLayoutListeners();
    }

    //Alternative onClick functions for the button in the EditText activity
    public void editTextButtonFunction(View view){
       switch(view.getId()){
           case R.id.left_arrow:
               //Insert logic for left_arrow button
               System.out.println("Left");
               break;
           case R.id.right_arrow:
               //Insert logic for right_arrow button
               System.out.println("Right");
               break;
           case R.id.delete_button:
               //Insert logic for delete button
               //Recall that there is a delete last char method in the RadialActivity class called deleteLastCharUserString
               System.out.println("Delete");
               break;
           case R.id.space_button:
               //Insert logic for space button
               //Recall that there is a space method already made in the RadialActivity class called spacePressed
               System.out.println("Space");
               break;
           case R.id.help_button:
               //Using the help button to return to the AZ Activity
               Intent intent = new Intent(this, AToZActivity.class);
               AToZActivity.changedActivity = false;
               startActivity(intent);
               break;
           default:
               break;
       }
    }

    int num = 5;
    int viewID;
    public void setImageViewLayoutListeners(){
        int id0 = R.id.left_arrow;      int id1 = R.id.right_arrow;
        int id2 = R.id.delete_button;   int id3 = R.id.space_button;
        int id4 = R.id.help_button;
        for(int i=0; i<num; i++){
            viewID = -1;
            if(i==0){viewID=id0;} if(i==1){viewID=id1;}
            if(i==2){viewID=id2;} if(i==3){viewID=id3;}
            if(i==4){viewID=id4;}
            ImageView iv = (ImageView)findViewById(viewID);
            iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
                        return;
                    }
                    //Create slide regions when ready and if they don't already exist
                    if(editTextRegionList.size()<num){
                        int xOffset = 0; int yOffset = 0;
                        Region region = new Region(i+xOffset,(i+xOffset)+Vars.EDIT_TEXT_BUTTON_WIDTH,i1+yOffset,(i1+yOffset)+Vars.EDIT_TEXT_BUTTON_HEIGHT,"EditText Region"+editTextRegionList.size());
                        System.out.println("Made: "+"EditText Region"+editTextRegionList.size());
                        editTextRegionList.add(region);
                    }else{
                        drawOverEditTextRegions();
                    }
                }
            });
        }

    }

    //Draws visual regions for the EditTextRegions
    private boolean drawnEditTextOverlay;
    public void drawOverEditTextRegions(){
        rl = (RelativeLayout)findViewById(R.id.activity_edit_text);
        if(!drawnEditTextOverlay){
            for(int i=0; i<editTextRegionList.size(); i++){
                TextView tv = new TextView(this);
                Region region = editTextRegionList.get(i);
                tv.setText(region.getLabel());
                tv.setX(region.getX0()); tv.setY(region.getY0());
                tv.setWidth((int)Math.abs((region.getX1()-region.getX0()))); tv.setHeight((int)Math.abs(region.getY1()-region.getY0()));
                tv.setBackgroundColor(Color.RED);
                System.out.println("Region: "+region.getX0()+" "+region.getY0()+" "+region.getX1()+" "+region.getY1());
                rl.addView(tv);
            }
            drawnEditTextOverlay = true;
        }
    }

    public void appendUserString(String s){
        if(Vars.userString==null) Vars.userString = "";
        Vars.userString = Vars.userString + s;
        updateUserString();
    }

    //Set the text of the edit text box, editText id must match the editText id for this activity
    public void updateUserString(){
        if(Vars.userString!=null){
            EditText editText = (EditText)findViewById(R.id.editactivity_text_field);
            if(editText!=null) editText.setText(Vars.userString);
        }
    }

    //Grab the initial time when the user entered the region
    public void setTimeZero(){
        //System.out.println("Setting time zero");
        calendar = Calendar.getInstance();
        tstamp = new Timestamp(calendar.getTime().getTime());
        time0 = tstamp.getTime();
        startedCheckingTime = true;
    }

    //Grab the latest time that the user was last in the region
    public void setTimeOne(){
        //System.out.println("Setting time one");
        calendar = Calendar.getInstance();
        tstamp = new Timestamp(calendar.getTime().getTime());
        time1 = tstamp.getTime();
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
        }
    }

    //force back button to return to home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AToZActivity.class);
        AToZActivity.changedActivity = false;
        startActivity(intent);
    }

    protected void onResume(){super.onResume(); updateUserString();}

    protected void onPause(){super.onPause();}

}
