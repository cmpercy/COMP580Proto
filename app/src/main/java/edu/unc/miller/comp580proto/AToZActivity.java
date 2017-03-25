package edu.unc.miller.comp580proto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AToZActivity extends AppCompatActivity {

    public static int indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_to_z);
        initialize();
    }

    public void aToZButtonClicked(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.a_to_h_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 0;  //0 -> a-h triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.i_to_p_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 8;  //8 -> i-p triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.q_to_x_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 16; //16 -> q-x triggered
                hideUserString();
                startActivity(intent);
                break;
            case R.id.y_to_z_button:
                intent = new Intent(this,RadialActivity.class);
                indicator = 24; //24 -> y-z triggered
                hideUserString();
                startActivity(intent);
                break;
            default:
                //Do nothing
                break;
        }
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

}
