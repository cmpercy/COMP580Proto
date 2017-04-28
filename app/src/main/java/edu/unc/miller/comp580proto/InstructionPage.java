package edu.unc.miller.comp580proto;

import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import com.bumptech.glide.Glide;


/**
 * Created by ejchen on 4/27/2017.
 */

public class InstructionPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.instruction_page);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        init();
        setAnimatedGif();
    }

    private void setAnimatedGif(){
        System.out.print("inside set AnimatedGif");
        ImageView view1 = (ImageView) findViewById(R.id.how_to_make_a_sentence);
//        ImageView view2 = (ImageView) findViewById(R.id.how_to_change_regions);
//        ImageView view3 = (ImageView) findViewById(R.id.how_to_exit_a_region);
//        ImageView view4 = (ImageView) findViewById(R.id.how_to_edit_a_sentence);
//        ImageView view5 = (ImageView) findViewById(R.id.how_to_submit_a_sentence);
        int gif1 = R.drawable.how_to_make_a_sentence;
        Glide.with(this)
                .load(gif1)
                .asGif()
                .into(view1);
    }

    private void init(){
        Button backButton = (Button)findViewById(R.id.exit_instruction);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.print("return to keyboard");
                Intent intent = new Intent(InstructionPage.this,AToZActivity.class);
                startActivity(intent);
            }
        });
    }
}
