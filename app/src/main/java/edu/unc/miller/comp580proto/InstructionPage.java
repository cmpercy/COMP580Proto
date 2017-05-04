package edu.unc.miller.comp580proto;

import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.w3c.dom.Text;


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
        setHyperLinks();
    }

   private void setHyperLinks(){
       TextView t1 = (TextView) findViewById(R.id.title_how_to_make_a_sentence);
       TextView t2 = (TextView) findViewById(R.id.title_how_to_change_regions);
       TextView t3 = (TextView) findViewById(R.id.title_how_to_exit_a_region);
       TextView t4 = (TextView) findViewById(R.id.title_how_to_edit_a_sentence);
       TextView t5 = (TextView) findViewById(R.id.title_how_to_submit_a_sentence);

       t1.setMovementMethod(LinkMovementMethod.getInstance());
       t2.setMovementMethod(LinkMovementMethod.getInstance());
       t3.setMovementMethod(LinkMovementMethod.getInstance());
       t4.setMovementMethod(LinkMovementMethod.getInstance());
       t5.setMovementMethod(LinkMovementMethod.getInstance());

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
