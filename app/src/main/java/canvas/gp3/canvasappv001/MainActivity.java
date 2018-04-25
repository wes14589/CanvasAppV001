package canvas.gp3.canvasappv001;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    
    private DrawingView drawView;   //instance variable
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;  //represents paint colour button (instance variable)
    private float smallBrush, mediumBrush, largeBrush;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Instantiate variables

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawView = (DrawingView)findViewById(R.id.drawing); //instantiation of variable by retrieving a reference to it from the layout
        
        //retrieve first paint colour button in palette
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);   //retrieve linear layout
        currPaint = (ImageButton)paintLayout.getChildAt(0); //retrieve first button
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));   //use image on button to show current selection

        drawBtn = (ImageButton)findViewById(R.id.draw_btn); //retrieves reference to button from layout
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton)findViewById(R.id.rubber_btn);   //retrieve reference to button
        eraseBtn.setOnClickListener(this);  //'listens' for clicks

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);
    }
    
    public void paintClicked(View view) {    //lets user choose colour

        drawView.setErase(false);   //switch back to drawing
        drawView.setBrushSize(drawView.getLastBrushSize()); //sets brush size back to the last one when used

        String color = null;
        //check paint colour selected is not current one
        ImageButton imgView = null;
        if (view != currPaint) {  //update colour
            imgView = (ImageButton) view;
            color = view.getTag().toString();
        }

        drawView.setColor(color);

        //updates user interface to show the new colour selection
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed)); //turn button clicked to black
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));   //turn previous button to grey
        currPaint = (ImageButton) view;    //button clicked is now currPaint
    }

    @Override
    public void onClick(View view){     //responds to clicks
        if(view.getId() == R.id.draw_btn){      //draw button is clicked

            final Dialog brushDialog = new Dialog(this);    //when button clicked presents dialog with three button sizes
            brushDialog.setTitle("Brush size:");    //sets Title

            brushDialog.setContentView(R.layout.brush_chooser); //sets the layout

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){  //if small button clicked
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);   //sets the user back to drawing
                    brushDialog.dismiss();  //dialog is dismissed straight away once button clicked
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){ //if medium button clicked
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){  //if large button clicked
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }
        else if(view.getId() == R.id.rubber_btn){  //switch to erase and choose size
            final Dialog brushDialog = new Dialog(this);    //user chooses brush size
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);  //if small clicked
                    brushDialog.dismiss();  //dialog is dismissed straight away once button clicked
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush); //if medium clicked
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);  //if large clicked
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){ //button to save drawing
            drawView.setDrawingCacheEnabled(true); // assign some cache to the app to save the drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
            String imgSaved = MediaStore.Images.Media.insertImage( //write image to file
                    getContentResolver(), drawView.getDrawingCache(),
                    UUID.randomUUID().toString()+".png", "drawing");

            if(imgSaved!=null){
                Toast savedToast = Toast.makeText(getApplicationContext(),
                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else{
                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
        } // end of if

    }
}
