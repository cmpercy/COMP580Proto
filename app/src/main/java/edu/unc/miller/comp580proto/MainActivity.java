package edu.unc.miller.comp580proto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.speech.tts.TextToSpeech.*;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnInitListener{

    RelativeLayout rl;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
        Intent checkTTS = new Intent();
        checkTTS.setAction(Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS, 0); //check if tts is available
    }

    public void mainButtonClicked(View view){
        switch(view.getId()){
            case R.id.numbers_and_symbols_button:
                //TODO
                break;
            case R.id.a_to_z_button:
                Intent intent = new Intent(this, AToZActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_text_button:
                //TODO
                break;
            case R.id.most_popular_words_button:
                //TODO
                break;
            default:
                //Do nothing, no identified button press
                break;
        }
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
                tts = new TextToSpeech(this, this); //initialize tts if available
            }
        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US); //set language to US English
        }
    }

    public void initialize(){
        updateUserString();
        //Enter immersive mode on app start
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    //Set the text of the edit text box
    public void updateUserString(){
        if(Vars.userString != null){
            EditText editText = (EditText)findViewById(R.id.main_text_field);
            if(editText!=null) editText.setText(Vars.userString);
        }
    }

    //call to push Vars.userString to be spoken
    public void stringToSpeech() {
        tts.speak(Vars.userString, TextToSpeech.QUEUE_ADD, null);
        Vars.userString = ""; //reset userString after spoken
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

    @Override
    public void onStart(){
        super.onStart();
    }
}
