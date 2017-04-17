package edu.unc.miller.comp580proto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.Locale;

public class AToZActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    public static int indicator;
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

    public void aToZButtonClicked(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.zone1_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 0;  //0 -> a-h triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.zone2_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 8;  //8 -> i-p triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.zone3_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 16; //16 -> q-x triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.zone4_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 24; //24 -> y-z triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.numbers_button:
                //need to implement
                break;
            case R.id.enter_button:
                stringToSpeech();
                break;
            default:
                //Do nothing
                break;
        }
    }

    //You'll need to deactivate the alphabet buttons
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_UP:
                if(centralRegion.checkBounds(motionEvent.getRawX(),motionEvent.getRawY())){
                    //Return to main activity
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void initialize(){
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

    //Set the text of the edit text box
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
    }

    //force back button to return to home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //call to push Vars.userString to be spoken
    public void stringToSpeech() {
        tts.speak(Vars.userString, TextToSpeech.QUEUE_ADD, null);
        Vars.userString = ""; //reset userString after spoken
        EditText text = (EditText) findViewById(R.id.a_to_z_text_field);
        if (text.getText() != null) text.getText().clear(); //clear text field after spoken
    }
}
