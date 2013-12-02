package textures;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpriteSheet {
	private int bitWidth, bitHeight, spriteWidth, spriteHeight, hFrames, vFrames, frames, x, y;
	private double currentFrame, rate, ratio;
	private boolean finished, centered;
	private Rect spriteRect, destRect;
	private Bitmap bitmap;
	
	public SpriteSheet(Bitmap bitmap, int hFrames, int vFrames, double rate){
		this.hFrames=hFrames;
		this.vFrames=vFrames;
		this.bitmap=bitmap;
		this.rate=rate;
		spriteWidth=bitWidth=bitmap.getWidth()/this.hFrames;
		spriteHeight=bitHeight=bitmap.getHeight()/this.vFrames;
		
		frames = (int)(hFrames*vFrames); //good time to start
		spriteRect = new Rect(0,0,bitWidth,bitHeight);
		destRect = new Rect();
	}
	public boolean animate(){
		finished = false;
		if (currentFrame+rate < frames) currentFrame+=rate;
		else{
			finished = true;
			currentFrame = 0;
		}
		//adjust sprite location
		spriteRect.top = (((int)currentFrame)/hFrames)*spriteHeight;
		spriteRect.bottom = spriteRect.top + spriteHeight;
		spriteRect.left = (((int)currentFrame)%hFrames)*spriteWidth;
		spriteRect.right = spriteRect.left + spriteWidth;
		return finished;
	}
	public void animate(int frame){
		//adjust sprite location
		spriteRect.top = ((frame)/hFrames)*spriteHeight;
		spriteRect.bottom = spriteRect.top + spriteHeight;
		spriteRect.left = ((frame)%hFrames)*spriteWidth;
		spriteRect.right = spriteRect.left + spriteWidth;
	}
	public void animate(int start, int end){
		end++;
		//animates between a certain frame
		if (currentFrame < start) currentFrame = start;
		if (currentFrame+rate < end) currentFrame+=rate;
		else{
			finished = true;
			currentFrame = start;
		}
		//adjust sprite location
		spriteRect.top = (((int)currentFrame)/hFrames)*spriteHeight;
		spriteRect.bottom = spriteRect.top + spriteHeight;
		spriteRect.left = (((int)currentFrame)%hFrames)*spriteWidth;
		spriteRect.right = spriteRect.left + spriteWidth;
	}
	public void build(double x, double y, int xSize, int ySize){
		update(x,y,xSize,ySize);
		this.x=(int)x;
		this.y=(int)y;
	}
	public void update(double x, double y){
		//texture placement
		if (centered){
			destRect.top = (int)(y-(bitHeight/2));
			destRect.bottom = destRect.top + bitHeight;
			destRect.left = (int)(x-(bitWidth/2));
			destRect.right = destRect.left + bitWidth;
		}
		else{
			destRect.top = (int)y;
			destRect.bottom = destRect.top + bitHeight;
			destRect.left = (int)x;
			destRect.right = destRect.left + bitWidth;
		}
	}
	public void update(double x, double y, int xSize, int ySize){
		bitWidth = xSize;
		bitHeight = ySize;
		update(x,y);
	}
    public void draw(Canvas canvas){
        //this will draw the bitmap
        canvas.drawBitmap(bitmap, spriteRect, destRect, null);
    }
    public void draw(Canvas canvas, Paint paint){
        //this will draw the bitmap
        canvas.drawBitmap(bitmap, spriteRect, destRect, paint);
    }
	public void updateSprite(int x1, int y1, int x2, int y2){
		spriteRect.top = y1;
		spriteRect.right = x2;
		spriteRect.bottom = y2;
		spriteRect.left = x1;
	}
	public void reflect(){
		int oldLeft = spriteRect.left;
		spriteRect.left = spriteRect.right;
		spriteRect.right = oldLeft;
	}
	public void flip(){
		int oldTop = spriteRect.top;
		spriteRect.top = spriteRect.bottom;
		spriteRect.bottom = oldTop;
	}
	public void resize(int bitWidth, int bitHeight){
		this.bitWidth=bitWidth;
		this.bitHeight=bitHeight;
	}
    public void resize(double ratio){
        this.ratio=ratio;
        bitWidth *= ratio;
        bitHeight *= ratio;
        resize(bitWidth, bitHeight);
        update(x, y);
    }
	public void reset(){
		currentFrame=0;
		bitWidth=bitmap.getWidth()/hFrames;
		bitHeight=bitmap.getHeight()/vFrames;
	}
	public void resetDest(){
		destRect.top = 0;
		destRect.bottom = 0;
		destRect.left = 0;
		destRect.right = 0;
	}
	public void setBitmap(Bitmap bitmap){ this.bitmap = bitmap; }
	public Bitmap getBitmap(){ return bitmap; }
	public Rect getDestRect(){ return destRect; }
	public Rect getSpriteRect(){ return spriteRect; }
	public int getBitWidth(){ return bitWidth; }
	public int getBitHeight(){ return bitHeight; }
    public int getOriginalBitWidth(){ return bitmap.getWidth(); }
    public int getOriginalBitHeight(){ return bitmap.getHeight(); }
	public int getHFrames(){ return hFrames; }
	public int getVFrames(){ return vFrames; }
	public int getSpriteWidth(){ return spriteWidth; }
	public int getSpriteHeight(){ return spriteHeight; }
	public int getSpriteTop(){ return spriteRect.top; }
	public int getSpriteRight(){ return spriteRect.right; }
	public int getSpriteBottom(){ return spriteRect.bottom; }
	public int getSpriteLeft(){ return spriteRect.left; }
	public int getX(){ return x; }
	public int getY(){ return y; }
	public double getRate(){ return rate; }
    public double getRatio(){ return ratio; }
	public void center(){ centered = true; }
	public boolean isCentered(){ return centered; }
	public boolean isFinished(){ return finished; }
	public boolean isAnimating(){ return currentFrame > 0; }
}