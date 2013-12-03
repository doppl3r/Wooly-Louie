package content;
import audio.AudioHandler;
import textures.Textures;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MainPanel {
	public static Textures textures;
    public static SlideHOME homeSlide;
    public static SlideCAMERA camSlide;
    public static SlideDRAW drawSlide;
    public static SlideUPLOAD uploadSlide;
    public static AudioHandler audio;
    public static GUI gui;
    public Context context;
	private double now;
	private double then;
	private double delta;
	private double mod; //modifier
    private double mLastTime;
    private static int currentSlide = 0; //start at camera
    private int pixelsPerSecond = 1; //256 will be fast
    private int frameSamplesCollected = 0;
    private int frameSampleTime = 0;
    private int fps = 0;
    private static int width;
    private static int height;
    private Paint paint;
    
	public MainPanel(Context context){
		this.context = context;
		//set up paint
        audio = new AudioHandler(context);
		paint = new Paint();
		paint.setARGB(255,255,255,255);
		paint.setTextSize(24);
		paint.setAntiAlias(false);
	}
	public void draw(Canvas canvas, int screenWidth, int screenHeight){
		if (width <= 0 || height <= 0){
			//initialize width and height before initializing objects
			width = canvas.getWidth();
			height = canvas.getHeight();
			//initialize content objects
			textures = new Textures(context);
            homeSlide = new SlideHOME(context);
			camSlide = new SlideCAMERA(context);
            drawSlide = new SlideDRAW(context);
            uploadSlide = new SlideUPLOAD(context);
            gui = new GUI(context);
		}
		else{
			//draw menus and content
            switch(currentSlide){
                case(0): homeSlide.draw(canvas,paint); break;
                case(1): camSlide.draw(canvas,paint); break;
                case(2): drawSlide.draw(canvas,paint); break;
                case(3): uploadSlide.draw(canvas,paint); break;
            }
            gui.draw(canvas,paint);
		}
		//draw fps
		//paint.setARGB(255,255,255,255);
		//canvas.drawText("FPS: "+fps, 4, 24, paint);
	}
	public void update(){
		//update statistics
		updateFPS();
        switch(currentSlide){
            case(0): if (homeSlide != null) homeSlide.update(mod); break;
            case(1): if (camSlide != null) camSlide.update(mod); break;
            case(2): if (drawSlide != null) drawSlide.update(mod); break;
            case(3): if (uploadSlide != null) uploadSlide.update(mod); break;
        }
	}
	public void updateFPS() {
        long tempNow = System.currentTimeMillis();    
        if (mLastTime != 0) {
    		int time = (int) (tempNow - mLastTime);
    		frameSampleTime += time;
    		frameSamplesCollected++;
    		if (frameSamplesCollected == 10) {
        		if (frameSampleTime != 0)
        		fps = (int) (10000 / frameSampleTime);
        		frameSampleTime = 0;
        		frameSamplesCollected = 0;
    		}        		
    	}
        mLastTime = tempNow;
    }
	public void updateMod(boolean start){
		//adjust modifier from start to finish
		if (start){
			now = System.currentTimeMillis();
			delta = (now - then)/1000;
			if (delta > 0.05) delta = 0.05; //fix time offset for onPause();
			mod = delta*pixelsPerSecond;
		}
		else then = now;
	}
	public void down(int x, int y){
		switch (currentSlide){
            case(0): homeSlide.down(x,y); break;
            case(1): camSlide.down(x,y); break;
            case(2): drawSlide.down(x,y); break;
            case(3): uploadSlide.down(x,y); break;
        }
        gui.down(x,y);
	}
	public void move(int x, int y){
        switch (currentSlide){
            case(0): homeSlide.move(x, y); break;
            case(1): camSlide.move(x, y); break;
            case(2): drawSlide.move(x, y); break;
            case(3): uploadSlide.move(x, y); break;
        }
        gui.move(x, y);
	}
	public void up(int x, int y){
        switch (currentSlide){
            case(0): homeSlide.up(x, y); break;
            case(1): camSlide.up(x, y); break;
            case(2): drawSlide.up(x, y); break;
            case(3): uploadSlide.up(x, y); break;
        }
        gui.up(x,y);
	}
	public static int getWidth(){ return width; } //get context and canvas width
	public static int getHeight(){ return height; }
    public static void setCurrentSlide(int i){ currentSlide = i; }
}
