package game;

import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Clouds {
	private LinkedList<Cloud> clouds;
	private int limit;
	
	public Clouds(){
		clouds = new LinkedList<Cloud>();
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < clouds.size(); i++){
			clouds.get(i).draw(canvas, paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < clouds.size(); i++){
			clouds.get(i).update(mod, mainX, mainY, limit);
		}
	}
	public void construct(int limit){
		this.limit = limit;
		int currentX = 0;
		int currentY = 0;
		int minY = 0;
		int spacingX = 320;
		int spacingY = 192;
		while (currentX < limit){
			currentX += (int)(Math.random()*(spacingX/2))+(spacingX/2);
			currentY = (int)(Math.random()*spacingY)+minY;
			add(currentX, currentY);
		}
	}
	public void add(int x1, int y1){
		clouds.add(new Cloud(x1, y1));
	}
	public void remove(int i){ clouds.remove(i); }
	/*
	 * Cloud class
	 */
	public static class Cloud {
		private SpriteSheet sprite;
		private SpriteSheet shadow;
		private float cloudX;
		private float cloudY;
		private float speed;
		
		public Cloud(int x1, int y1){
			sprite = new SpriteSheet(GamePanel.textures.clouds, 1, 7, 0.0);
			shadow = new SpriteSheet(GamePanel.textures.clouds, 1, 7, 0.0);
			
			int width = sprite.getBitWidth();
			int height = sprite.getBitHeight();
			sprite.update(x1,y1,width*4,height*4);
			shadow.update(x1,y1,width*4,height*4);
			sprite.animate((int)(Math.random()*6));
			shadow.animate(6);
			cloudX = x1;
			cloudY = y1;
			speed = 20;
		}
		public void draw(Canvas canvas, Paint paint){
			canvas.drawBitmap(shadow.getBitmap(), shadow.getSpriteRect(), shadow.getDestRect(), paint);
			canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
		}
		public void update(double mod, double x1, double y1, int limit){
			cloudX -= (mod*speed);
			if (cloudX + sprite.getBitWidth() <= 0) cloudX = limit;
			sprite.update(x1+cloudX, y1+cloudY);
			shadow.update(x1+cloudX, GamePanel.getHeight()-32);
		}
		public void setX(float x1){ cloudX = x1; }
		public float getX(){ return cloudX; }
	}
}
