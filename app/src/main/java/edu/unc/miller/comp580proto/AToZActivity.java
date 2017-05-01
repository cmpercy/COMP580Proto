package edu.unc.miller.comp580proto;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AToZActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    public static int indicator;
    static ArrayList<Region> AZRegionList = new ArrayList<>();
    private Calendar calendar;
    private Timestamp tstamp;
    private long time0,time1;
    static boolean changedActivity;         //needs to be reset when leaving other activities in order to keep reading touch input
    boolean startedCheckingTime;
    boolean canResume = true;
    boolean checkingAZ0,checkingAZ1,checkingAZ2,checkingAZ3,checkingAZ4, checkingAZ5, checkingAZ6, checkingAZ7;
    Context mContext;
    RelativeLayout rl;
    Region centralRegion;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_to_z);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        Intent checkTTS = new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 0); //check if tts is available
        initialize();
        setImageViewLayoutListeners();
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //^^ prevents softkeyboard from opening despite edittext gaining focus
        EditText text = (EditText) findViewById(R.id.a_to_z_text_field);
        text.setRawInputType(InputType.TYPE_CLASS_TEXT);
        text.setTextIsSelectable(true);
        text.requestFocus();
        setOnLongPressListeners(); //sets on long press listeners
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        //---ROUTINES FOR AZ BUTTONS BELOW---
        //Check AZ0 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(0);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionZero");
                checkingAZ0 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
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
        //Check AZ1 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(1);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionOne");
                checkingAZ1 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
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
        //Check AZ2 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(2);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionTwo");
                checkingAZ2 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
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
        //Check AZ3 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(3);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionThree");
                checkingAZ3 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
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
        //Check AZ4 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ5&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(4);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionFour");
                checkingAZ4 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
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
        //Check AZ5 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ6&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(5);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionFive");
                checkingAZ5 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionFive();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ5 = false;
            }
        }

        //Check AZ6 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ7){
            Region tempRegion = AZRegionList.get(6);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionSix");
                checkingAZ6 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionSix();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ6 = false;
            }
        }
        //Check AZ7 for possible keyboard menu transition
        if(!changedActivity&&!checkingAZ0&&!checkingAZ1&&!checkingAZ2&&!checkingAZ3&&!checkingAZ4&&!checkingAZ5&&!checkingAZ6){
            Region tempRegion = AZRegionList.get(7);
            //If event is in bounds of this region...
            if(tempRegion.checkBounds(e.getRawX(),e.getRawY())&&canResume){
                System.out.println("Running exRegionSeven");
                checkingAZ7 = true;
                if(!startedCheckingTime) setTimeZero();
                if(startedCheckingTime) setTimeOne();
                //If the user has been in the region long enough, swap keyboard menus
                if(Math.abs(time0-time1)>Vars.TRANSITION_HOLD_TIME){
                    changedActivity = true;
                    AZButtonFunctionSeven();
                }
            }else{
                //Reset variables when the user has left one of the exterior button regions
                canResume = true;
                changedActivity = false;
                startedCheckingTime = false;
                checkingAZ7 = false;
            }
        }
        return true;
    }

    //BE CAREFUL WITH THE NUMBER MAPPING OF THE AZBUTTONS
    public void AZButtonFunctionZero(){
        Intent intent = new Intent(this,RadialActivity.class);
        indicator = 32;  //0 -> 0-9 triggered
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionOne(){
        Intent intent = new Intent(this,RadialActivity.class);
        indicator = 0;  //0 -> group 1 triggered
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionTwo(){
        Intent intent = new Intent(this,RadialActivity.class);
        indicator = 8;  //0 -> group 2 triggered
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionThree(){
        Intent intent = new Intent(this,RadialActivity.class);
        indicator = 16;  //0 -> group 3 triggered
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionFour(){
        Intent intent = new Intent(this,RadialActivity.class);
        indicator = 24;  //0 -> group 4 triggered
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionFive(){
        System.out.println("Enter Activity");
        stringToSpeech();
    }

    public void AZButtonFunctionSix(){
        System.out.println("Edit Text Activity");
        Intent intent = new Intent(this, EditTextActivity.class);
        hideUserString();
        startActivity(intent);
    }

    public void AZButtonFunctionSeven(){
        System.out.println("-----Help Page Activity--------");
        //Insert what you want to help button to do here, preferably start a new activity
        Intent intent = new Intent(this, InstructionPage.class);
        startActivity(intent);
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

    //Method to attach layout change listeners to image views; needed to access
    //properties of image views once they've actually been drawn to the screen
    //Prevents return values of 0 when trying to access a view's properties
    int num = 5;            //number of buttons in the activity (not including enter)
    int viewID;
    public void setImageViewLayoutListeners(){
        int id0 = R.id.zone1_button; int id1 = R.id.zone2_button;
        int id2 = R.id.zone3_button; int id3 = R.id.zone4_button;
        int id4 = R.id.numbers_button;
        for(int i=0; i<num; i++){
            viewID = -1;
            if(i==0){viewID=id0;} if(i==1){viewID=id1;}
            if(i==2){viewID=id2;} if(i==3){viewID=id3;}
            if(i==4){viewID=id4;}
            ImageView iv = (ImageView)findViewById(viewID);
            iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
                        return;
                    }
                    //Create slide regions when ready and if they don't already exist
                    if(AZRegionList.size()<=(num-1)){
                        int xOffset = 50; int yOffset = 25;
                        Region region = new Region(i+xOffset,(i+xOffset)+Vars.AZ_BUTTON_WIDTH,i1+yOffset,(i1+yOffset)+Vars.AZ_BUTTON_HEIGHT,"AZ Region"+AZRegionList.size());
                        AZRegionList.add(region);
                    }else{
                        addEnterRegion();
                        addEditTextRegion();
                        addHelpRegion();
                    }
                }
            });
        }
    }

    public void addEnterRegion(){
        ImageView iv = (ImageView)findViewById(R.id.enter_button);
        iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
                    return;
                }
                //Create slide regions when ready and if they don't already exist
                if(AZRegionList.size()==num){
                    int xOffset = 50; int yOffset = 25;
                    Region region = new Region(i+xOffset,(i+xOffset)+Vars.ENTER_BUTTON_WIDTH,i1+yOffset,(i1+yOffset)+Vars.ENTER_BUTTON_HEIGHT,"ENTER REGION");
                    AZRegionList.add(region);
                }
            }
        });
    }

    public void addEditTextRegion(){
        ImageView iv = (ImageView)findViewById(R.id.edit_text_button);
        iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
                    return;
                }
                //Create slide regions when ready and if they don't already exist
                if(AZRegionList.size()==(num+1)){
                    int xOffset = 50; int yOffset = 25;
                    Region region = new Region(i+xOffset,(i+xOffset)+Vars.ENTER_BUTTON_WIDTH,i1+yOffset,(i1+yOffset)+Vars.ENTER_BUTTON_HEIGHT,"EDIT TEXT REGION");
                    AZRegionList.add(region);
                }
            }
        });
    }

    public void addHelpRegion(){
        ImageView iv = (ImageView)findViewById(R.id.help_button);
        iv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
                    return;
                }
                if(AZRegionList.size()==(num+2)){
                    int xOffset = 0; int yOffset = 0;
                    Region region = new Region(i+xOffset,(i+xOffset)+Vars.HELP_BUTTON_WIDTH,i1+yOffset,(i1+yOffset)+Vars.HELP_BUTTON_HEIGHT,"HELP BUTTON REGION");
                    AZRegionList.add(region);
                }else{
                    //drawOverAZRegions();
                }
            }
        });
    }

    boolean drawnAZButtonOverlay;

    //Draws visual regions for the AZ buttons
    public void drawOverAZRegions(){
        if(!drawnAZButtonOverlay){
            for(int i=0; i<AZRegionList.size();i++){
                TextView tv = new TextView(this);
                Region region = AZRegionList.get(i);
                tv.setText(region.getLabel());
                tv.setX(region.getX0()); tv.setY(region.getY0());
                tv.setWidth((int)Math.abs((region.getX1()-region.getX0()))); tv.setHeight((int)Math.abs(region.getY1()-region.getY0()));
                tv.setBackgroundColor(Color.GREEN);
                rl.addView(tv);
            }
            drawnAZButtonOverlay = true;
        }
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

    //Might need to check that these are still mapped correctly
    public void aToZButtonClicked(View view){
        //Intent intent;
        switch(view.getId()){
            case R.id.zone1_button:
                AZButtonFunctionOne();
                break;
            case R.id.zone2_button:
                AZButtonFunctionTwo();
                break;
            case R.id.zone3_button:
                AZButtonFunctionThree();
                break;
            case R.id.zone4_button:
                AZButtonFunctionFour();
                break;
            case R.id.numbers_button:
                AZButtonFunctionZero();
                break;
            case R.id.edit_text_button:
                AZButtonFunctionSix();
                break;
            case R.id.enter_button:
                AZButtonFunctionFive();
                break;
            case R.id.help_button:
                AZButtonFunctionSeven();
                break;
            default:
                //Do nothing
                break;
        }
    }

    public void initialize(){
        //Snag the relative layout for this activity
        rl = (RelativeLayout)findViewById(R.id.activity_a_to_z);
        //Update the text in the edit text box
        updateUserString();
        //Enter immersive mode on app start
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        //Make the central region
        centralRegion = new Region(0.0f,1000.0f,0.0f,1000.0f,"Select");
    }

    //Set the text of the edit text box, editText id must match the editText id for this activity
    public void updateUserString(){
        if(Vars.userString!=null){
            EditText editText = (EditText)findViewById(R.id.a_to_z_text_field);
            if(editText!=null) editText.setText(Vars.userString);
        }
    }

    //Hide the text from the edit text since radial activity is an overlay
    public void hideUserString(){
        EditText editText = (EditText)findViewById(R.id.a_to_z_text_field);
        if(editText!=null) editText.setText("");
        editText.setCursorVisible(false); //hides cursor in background so we don't see two
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //Re-enter immersive mode
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUserString();
        EditText text = (EditText) findViewById(R.id.a_to_z_text_field);
        text.setSelection(text.getText().length()); //put cursor at the end of the text
    }

    //call to push Vars.userString to be spoken
    public void stringToSpeech() {
        tts.speak(Vars.userString, TextToSpeech.QUEUE_ADD, null);
        Vars.userString = ""; //reset userString after spoken
        EditText text = (EditText) findViewById(R.id.a_to_z_text_field);
        if (text.getText() != null) text.getText().clear(); //clear text field after spoken
    }

    public void setOnLongPressListeners() {
        ImageView numbers = (ImageView) findViewById(R.id.numbers_button);
        numbers.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView zone1 = (ImageView) findViewById(R.id.zone1_button);
        zone1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView zone2 = (ImageView) findViewById(R.id.zone2_button);
        zone2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView zone3 = (ImageView) findViewById(R.id.zone3_button);
        zone3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView zone4 = (ImageView) findViewById(R.id.zone4_button);
        zone4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView enter = (ImageView) findViewById(R.id.enter_button);
        enter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView edit_text = (ImageView) findViewById(R.id.edit_text_button);
        edit_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
        ImageView help = (ImageView) findViewById(R.id.help_button);
        help.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                aToZButtonClicked(v);
                return true;
            }
        });
    }
}
