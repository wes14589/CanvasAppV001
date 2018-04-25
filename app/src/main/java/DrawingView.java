import android.view.View;
import android.content.Context;
import android.util.AttributeSet;

public class DrawingView extends View {

    import android.graphics.Bitmap;
    import android.graphics.Canvas;
    import android.graphics.Paint;
    import android.graphics.Path;
    import android.view.MotionEvent.
        
    private Path drawPath;  //draws the path
    private Paint drawPaint, canvasPaint;    //paint for drawing and canvas
    private int paintColor = 0xFF660000;    //initial paint colour
    private Canvas drawCanvas;  //the canvas
    private Bitmap canvasBitmap;    //bitmap representation of canvas
    
    public DrawingView(Context context,AttributesSet attrs) {
        super(context, attrs);
        setupDrawing();                
    }

    private void setupDrawing(){    //get drawing area setup for interaction
        
        //INSTANTIATE VARIABLES        
        drawPath = new Path();  //draws the path for the user
        drawPaint = new Paint();    //draws the paint on the canvas in place of the user's path
        
        drawPaint.setColor(paintColor); //initialises paint colour
        
        drawpaint.setAntiAlias(true);   //All initialisations here make the drawing appear smoother
        drawPaint.setStrokeWidth(20);   //arbitary brush size until alteration of sizes
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        
        canvasPaint = new Paint(Paint.DITHER_FLAG);       
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){     //view given size   
        
        super.onSizeChanged(w, h, oldw, oldh);  //call superclass method
        
        //INSTANTIATE        
        canvasBitmap = Bitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
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
}

