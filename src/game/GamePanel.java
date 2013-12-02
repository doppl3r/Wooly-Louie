package game;
import textures.Textures;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GamePanel {
	public static Textures textures;
	public static MainMenu menu;
    public static Game game;
    public Context context;
	private double now;
	private double then;
	private double delta;
	private double mod; //modifier
    private double mLastTime;
    private int pixelsPerSecond = 1; //256 will be fast
    private int frameSamplesCollected = 0;
    private int frameSampleTime = 0;
    private int fps = 0;
    private static int width;
    private static int height;
    private Paint paint;
    
	public GamePanel(Context context){
		this.context = context;
		//set up paint
		paint = new Paint();
		paint.setARGB(255,255,255,255);
		paint.setTextSize(24);
		paint.setAntiAlias(true);
	}
	public void draw(Canvas canvas, int screenWidth, int screenHeight){
		if (width <= 0 || height <= 0){
			//initialize width and height before initializing objects
			width = canvas.getWidth();
			height = canvas.getHeight();
			//initialize game objects
			textures = new Textures(context);
			menu = new MainMenu(context);
			game = new Game(context);
		}
		else{
			//draw menus and game
			menu.draw(canvas, paint);
			game.draw(canvas, paint);
		}
		//draw fps
		paint.setARGB(255,255,255,255);


		//canvas.drawText("FPS: "+fps, 4, 24, paint);


		//Log.d("mod: "+mod,"fps"+fps);
	}
	public void update(){
		//update statistics
		updateFPS();
		if (menu != null) menu.update(mod); //update menu
		if (game != null) game.update(mod); //update game
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
		menu.down(x,y);
		game.down(x,y);
	}
	public void move(int x, int y){
		menu.move(x,y);
		game.move(x,y);
	}
	public void up(int x, int y){
		menu.up(x,y);
		game.up(x,y);
	}
	public static int getWidth(){ return width; } //get context and canvas width
	public static int getHeight(){ return height; }
}
