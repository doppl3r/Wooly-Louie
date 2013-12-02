package pack.virga.main;
import game.GamePanel;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PanelThread extends SurfaceView implements SurfaceHolder.Callback {
	private GraphicThread thread;
	public PanelThread(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		thread = new GraphicThread(holder, context);
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	public void surfaceCreated(SurfaceHolder holder) { thread.setRunning(true); thread.start(); }
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);
		while (retry) {	try { thread.join(); retry = false;	} catch (InterruptedException e) { }}
	}
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			thread.down((int)event.getX(), (int)event.getY()); //first touch
			try {GraphicThread.sleep(15);} //sleep thread to keep fps
			catch (InterruptedException e) {e.printStackTrace();}
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			thread.move((int)event.getX(), (int)event.getY()); //update every movement while down
			try {GraphicThread.sleep(15);} //sleep thread to keep fps //75 is good
			catch (InterruptedException e) {e.printStackTrace();}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			thread.up((int)event.getX(), (int)event.getY()); //do something 1x after untouching screen
			try {GraphicThread.sleep(5);} //sleep thread to keep fps
			catch (InterruptedException e) {e.printStackTrace();}
		}
		return true;
	}
	public static class GraphicThread extends Thread {
		private boolean mRun;
		private SurfaceHolder mSurfaceHolder;
		private static GamePanel gamePanel;
		private int screenWidth, screenHeight;
		
		public GraphicThread(SurfaceHolder surfaceHolder, Context context) {
			mSurfaceHolder = surfaceHolder;
			gamePanel = new GamePanel(context);
		}
		public void run() {
			while (mRun) {
				Canvas canvas = null;
				try {
					canvas = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						canvas.drawColor(Color.rgb(0, 0, 0));
						// update screen resolution for other devices
						if (screenWidth != canvas.getWidth() || screenHeight != canvas.getHeight()){
							screenWidth = canvas.getWidth();
							screenHeight = canvas.getHeight();
						}
						else{
							// check game properties
							gamePanel.updateMod(true);
							gamePanel.update();
							gamePanel.draw(canvas, screenWidth, screenHeight);
							gamePanel.updateMod(false);
						}
					}
				} finally { if (canvas != null) { mSurfaceHolder.unlockCanvasAndPost(canvas); }}
			}
		}
		public void setRunning(boolean b) { mRun = b; }
		public void down(int x, int y){ gamePanel.down(x,y); }
		public void move(int x, int y){ gamePanel.move(x,y); }
		public void up(int x, int y){ gamePanel.up(x,y); }
	}
}
