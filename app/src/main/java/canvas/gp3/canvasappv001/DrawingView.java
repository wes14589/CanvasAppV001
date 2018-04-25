package canvas.gp3.canvasappv001;

import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

public class DrawingView extends View {

    private Path drawPath;  //draws the path
    private Paint drawPaint, canvasPaint;    //paint for drawing and canvas
    private int paintColor = 0xFF660000;    //initial paint colour
    private Canvas drawCanvas;  //the canvas
    private Bitmap canvasBitmap;    //bitmap representation of canvas
    private float brushSize, lastBrushSize; //lastBrushSize keeps track of brush size when switched to eraser
    private boolean erase = false;     //flag to whether user erases or not

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){    //get drawing area setup for interaction

        //INSTANTIATE VARIABLES
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;  //assigns current brush size to this for easy reverting back

        drawPath = new Path();  //draws the path for the user
        drawPaint = new Paint();    //draws the paint on the canvas in place of the user's path

        drawPaint.setColor(paintColor); //initialises paint colour

        drawPaint.setAntiAlias(true);   //All initialisations here make the drawing appear smoother
        drawPaint.setStrokeWidth(brushSize);   //arbitary brush size until alteration of sizes
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBrushSize(float newSize){    //updates brush size

        //value is passed from dimensions file
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){   //sets the last brush size  //called from MainActivity
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize(){    //retrieves the last brush size     //called from MainActivity
        return lastBrushSize;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){     //view given size

        super.onSizeChanged(w, h, oldw, oldh);  //call superclass method

        //INSTANTIATE
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        //canvasBitmap = Bitmap(w, h, Bitmap.Config.ARGB_8888);
        //drawCanvas = new Canvas(canvasBitmap);
    }

    @Override   //This method executes every time the user uses touch interaction to draw
    protected void onDraw(Canvas canvas){   //draw view

        //This draws the canvas and the drawing path
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){     //detects user touch (listens for touch events)

        float touchX = event.getX();    //retrieve user touch X and Y positions
        float touchY = event.getY();

        //switch statement. implements drawing functions of down, up and move
        //MotionEvent parameter responds to particular touch events

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:   //move to position touched
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:   //draw path along with line touched
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:   //path is drawn and reset for next op
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();   //onDraw method executes
        return true;
    }

    public void setColor(String newColor){  //sets colour
        invalidate();

        paintColor = Color.parseColor(newColor);    //sets the colour for drawing
        drawPaint.setColor(paintColor);
    }

    public void setErase(boolean isErase){      //setting erase to true or false

        erase = isErase;    //updates variable

        if(erase) { //erase or switch back to drawing
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else drawPaint.setXfermode(null);

    }
}
