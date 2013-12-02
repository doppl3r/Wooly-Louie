package game;

import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Mountains{
	private LinkedList<SpriteSheet> sprites;
	
	public Mountains(){
		sprites = new LinkedList<SpriteSheet>();
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < size(); i++){
			canvas.drawBitmap(sprites.get(i).getBitmap(), 
				sprites.get(i).getSpriteRect(), 
				sprites.get(i).getDestRect(), 
				paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < size(); i++){
			sprites.get(i).update(mainX+sprites.get(i).getX(),
					mainY+sprites.get(i).getY());
		}
	}
	public void down(int x1, int y1){
		
	}
	public void move(int x1, int y1){
		
	}
	public void up(int x1, int y1){
		
	}
	public void add(int x, int y, int height, int type){
		int r = 0;
		int g = 0;
		int b = 0;
		//set up rgb
		if (type==0) { r=160; g=230; b=220; }
		else if (type==1) { r=135; g=200; b=190; }
		else if (type==2) { r=110; g=170; b=165; }
		int width = height*2;
		//add sprite
		sprites.add(new SpriteSheet(renderMountain(height,r,g,b), 1, 1, 0.0));
		sprites.get(sprites.size()-1).build(x,y,width*4,height*4);
	}
	public void remove(int i){ sprites.remove(i); }
	public int size(){ return sprites.size(); }
	public Bitmap renderMountain(int height, int r, int g, int b){
		int noise = 0;
		int volume = 8;
		int x1 = 0; 
		int width = height*2;
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		for (int i = height-1; i >= 0; i--){
			for (int j = x1; j < x1+width; j++){
				noise = (int)(Math.random()*volume)-(volume/2);
				bmp.setPixel(j, i, Color.rgb(r+noise,g+noise,b+noise));
			}
			x1++; width-=2;
		}	return bmp;
	}
}
