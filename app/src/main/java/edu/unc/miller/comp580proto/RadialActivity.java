package edu.unc.miller.comp580proto;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class RadialActivity extends AppCompatActivity {

    ArrayList<Region> regionList;
    RelativeLayout rl;
    DisplayMetrics displaymetrics;
    int screenheight,screenwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        initialize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        print("X: "+e.getX()+" Y: "+e.getY());
        switch(e.getAction()){
            case MotionEvent.ACTION_MOVE:
                //print("Moving");
                //print("x: "+e.getX()+" y: "+e.getY()+" width: "+screenwidth+" height"+screenheight);
                //Check to see if the user's input is within a button's region
                for(int i=0; i<regionList.size();i++){
                    Region tempregion = regionList.get(i);
                    if(tempregion.checkBounds(e.getX(),e.getY())){print("------------------"+tempregion.getLabel()+"--------------------");return true;}
                }
            default:
                //Do nothing
        }
        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e){
//        for(int i=0; i<regionList.size();i++){
//            print(regionList.get(i));
//        }
//        return true;
//    }

    protected void onResume(){super.onResume();}

    protected void onPause(){super.onPause();}

    public void initialize(){
        View.OnClickListener buttonListener = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Stuff to be executed when a button is clicked
                Button registeredButton = (Button)findViewById(v.getId());
                if(registeredButton!=null)print(registeredButton.getText());
            }
        };
        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenheight = displaymetrics.heightPixels;
        screenwidth = displaymetrics.widthPixels;

        rl = (RelativeLayout)findViewById(R.id.activity_radial);
        int radius = 250;
        int numbutt = 8;
        double section = 360/numbutt;

        regionList = new ArrayList<>(numbutt);
        //Create a series of "circular" buttons in a circle
        for(int i=0;i<numbutt;i++){
            double degrees = i*section;
            double xpos = radius*Math.cos(Math.toRadians(degrees))+(screenwidth/2)-75;  //50 is the xsize of the circle image (I moved it over some though 1.5*50)
            double ypos = radius*Math.sin(Math.toRadians(degrees))+(screenheight/2)-50; //50 is the ysize of the circle image
            ImageView iv = new ImageView(this);
            imageResSetter(iv,i);
            //Set coordinates of the circle imageview
            iv.setX((float)xpos);
            iv.setY((float)ypos);
            iv.setScaleX(2);
            iv.setScaleY(2);
            rl.addView(iv);
            //Set information and coordinates for the buttons
            Button butt = new Button(this);
            butt.setId(i);
            butt.setX((float)xpos-50);
            butt.setY((float)ypos-40);
            //butt.setAlpha(0.0f);
            butt.setWidth(140);
            butt.setHeight(125);
            buttonTextSetter(butt,i);
            butt.setOnClickListener(buttonListener);
            //Create button regions (used for slide motion detection) and add them to a list
            float regionx = butt.getX(); float regiony = butt.getY();
            String label = butt.getText().toString();
            Region buttonRegion = new Region(regionx,regionx+140.0f,regiony,regiony+125.0f,label);
            regionList.add(buttonRegion);
            rl.addView(butt);
        }
        //Create the button sequence of text

    }

    //Pick the text to use for the button
    public void buttonTextSetter(Button b, int in){
        if(in==0){b.setText("A");}if(in==1){b.setText("B");}if(in==2){b.setText("C");}
        if(in==3){b.setText("D");}if(in==4){b.setText("E");}if(in==5){b.setText("F");}
        if(in==6){b.setText("G");}if(in==7){b.setText("H");}
    }

    //Pick the resource to load into an imageview
    public void imageResSetter(ImageView imv, int in){
        if(in==0){imv.setImageResource(R.drawable.a);}if(in==1){imv.setImageResource(R.drawable.b);}if(in==2){imv.setImageResource(R.drawable.c);}
        if(in==3){imv.setImageResource(R.drawable.d);}if(in==4){imv.setImageResource(R.drawable.e);}if(in==5){imv.setImageResource(R.drawable.f);}
        if(in==6){imv.setImageResource(R.drawable.g);}if(in==7){imv.setImageResource(R.drawable.h);}
    }

    public static <AnyType> void print(AnyType s){
        System.out.println(s);
    }

}
