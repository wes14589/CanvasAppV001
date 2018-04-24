package canvas.gp3.canvasappv001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    
    private DrawingView drawView;   //instance variable
    private ImageButton currPaint;  //represents paint colour button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        drawView = (DrawingView)findViewById(R.id.drawing); //instantiation of variable by retrieving a reference to it from the layout
        
        //retrieve first paint colour button in palette
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);   //retrieve linear layout
        currPaint = (ImageButton)paintLayout.getChildAt(0); //retrive first button
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));   //use image on button to show current selection
    }
    
    public void paintClicked(View view){    //lets user choose colour
        //check paint colour selected is not current one
        if(view != currPaint){  //update colour
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
        }
    }
}
