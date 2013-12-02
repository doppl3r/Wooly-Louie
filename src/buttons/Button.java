package buttons;

import android.util.Log;
import textures.SpriteSheet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Vibrator;

public class Button {
	private boolean pressed, hide, center;
	private SpriteSheet sprite;
    private int fade;
	private double x, y, xSize, ySize, padding, moveY;
	private Vibrator vibrator;
	
	public Button(Bitmap newBitmap, int x, int y, boolean center, Context context){
		SpriteSheet newSprite = new SpriteSheet(newBitmap, 1, 2, 0.0);
		//basic button
		this.x=x;
		this.y=y;
		this.center=center;
        fade = 255;
		sprite = new SpriteSheet(newSprite.getBitmap(), 
				newSprite.getHFrames(), newSprite.getVFrames(), newSprite.getRate());
		xSize = sprite.getBitWidth();
		ySize = sprite.getBitHeight();
		if (center) sprite.center();
		sprite.update(x, y);
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
		padding = 12; //adjust to player's fingers
	}
	public void draw(Canvas canvas){
		Paint paint = new Paint();
        paint.setARGB(fade, 255, 255, 255);
		if (!hide) canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        paint.setARGB(255, 255, 255, 255);
	}
	public void resize(int newWidth, int newHeight){
		xSize=newWidth;
		ySize=newHeight;
		sprite.resize(newWidth, newHeight);
		sprite.update(x, y);
	}
	public void resize(int ratio){
		xSize *= ratio;
		ySize *= ratio;
		sprite.resize((int)xSize, (int)ySize);
		sprite.update(x, y);
	}
	public boolean down(int x1, int y1){
        //Log.d("hey", "" + fade);
		if (!hide){
			if (center){
				if (Math.abs(x1-x) < ((xSize/2)+padding) && Math.abs(y1-y) < ((ySize/2)+padding)){
                    if (fade >= 255){
                        if (!pressed) vibrator.vibrate(25);
                        sprite.animate(1, 0);
                    }
                    pressed = true;
				} else { pressed = false; sprite.animate(0, 0); }
			}
			else{
				if (Math.abs(x1-x-(xSize/2)) < ((xSize/2)+padding) && Math.abs(y1-y-(ySize/2)) < ((ySize/2)+padding)){
                    if (fade >= 255){
                        if (!pressed) vibrator.vibrate(25);
                        sprite.animate(1, 0);
                    }
                    pressed = true;
				} else { pressed = false; sprite.animate(0, 0); }
			}
		}
		return pressed;
	}
	public boolean move(int x1, int y1){
		down(x1, y1);
		return pressed;
	}
	public boolean up(int x1, int y1){ 
		if (pressed && !hide && fade >= 255){
			sprite.animate(0, 0);
			pressed = false;
			return true;
		}
		else return false; 
	}
	public void setPadding(int padding){ this.padding=padding; }
    public void setFade(int fade){ this.fade=fade; }
	public void hide(){ hide = true; }
	public void reveal(){ hide = false; }
    public void update(double mod, double x, double y){
        //this is unique to drop-down buttons
        if (fade < 255){
            if (moveY < 64)  moveY += (mod*500);
            else moveY = 64;
            sprite.animate(0, 0);
        }
        else {
            if (moveY > 0) moveY -= (mod*500);
            else moveY = 0;
        }
        sprite.update(x, y-moveY);
    }
	public void update(double x, double y){ 
		this.x=x;
		this.y=y;
		sprite.update(x, y); 
	}
	public boolean isPressed(){ return pressed; }
	public double getX(){ return x; }
	public double getY(){ return y; }
}
