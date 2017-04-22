package edu.unc.miller.comp580proto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class EditTextActivity extends AppCompatActivity {

    RelativeLayout rl;
    DisplayMetrics displaymetrics;
    private int screenheight,screenwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
    }

    public void initialize(){
        updateUserString();
        enterImmersiveMode();
        grabScreenProperties();
    }

    //On-click functions for the edit text buttons.  I will make them slide at a later point in time
    public void editTextButtonFunction(View view){
       switch(view.getId()){
           case R.id.left_arrow:
               //Insert logic for left_arrow button
               //System.out.println("Left");
               break;
           case R.id.right_arrow:
               //Insert logic for right_arrow button
               //System.out.println("Right");
               break;
           case R.id.delete_button:
               //Insert logic for delete button
               //Recall that there is a delete last char method in the RadialActivity class called deleteLastCharUserString
               //System.out.println("Delete");
               break;
           case R.id.space_button:
               //Insert logic for space button
               //Recall that there is a space method already made in the RadialActivity class called spacePressed
               //System.out.println("Space");
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
