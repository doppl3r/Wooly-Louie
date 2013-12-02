package game;

import textures.SpriteSheet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Earth {
	private int[][] tiles;
	private Bitmap newTerrain;
	private SpriteSheet sprite;
	
	public Earth(int tileCount, int tileSize){
		tiles = new int[1][tileCount];
		sprite = new SpriteSheet(GamePanel.textures.terrain, 1, 1, 0.0);
		newTerrain = GamePanel.textures.renderMatrix(tiles, sprite, 4);
		sprite.updateSprite(0, 0, tileCount*tileSize*4, tileSize*4);
		sprite.resize(tileCount*tileSize*4, tileSize*4);
	}
	public void draw(Canvas canvas, Paint paint){
		canvas.drawBitmap(newTerrain, sprite.getSpriteRect(), sprite.getDestRect(), paint);
	}
	public void update(double mod, double x1, double y1){
		sprite.update(x1, (GamePanel.getHeight()-32)+y1);
	}
	public void down(int x1, int y1){
		
	}
	public void move(int x1, int y1){
		
	}
	public void up(int x1, int y1){
		
	}
	public int getWidth() {
		return newTerrain.getWidth();
	}
	public int getHeight() {
		return newTerrain.getHeight();
	}
}
