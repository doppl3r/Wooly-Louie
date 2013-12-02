package textures;
import pack.virga.main.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class Textures extends View {
	//game textures
	public Bitmap terrain, mine_wood, mine_metal,
            clouds, trees, people, rocks, factory_wood, factory_metal,
            wood, rock, metal;

    //gui textures
    public Bitmap start, arrow, build_mine, build_factory, build_tree,
        exit, farm, upgrade, text1, text2;
	public Textures(Context context){
		super(context);
		//set up bitmap for all sdk's
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;  // API Level 1          
        options.inScaled = false;
        //game textures
		terrain = BitmapFactory.decodeResource(getResources(), R.drawable.terrain, options);
        mine_wood = BitmapFactory.decodeResource(getResources(), R.drawable.mine_wood, options);
        mine_metal = BitmapFactory.decodeResource(getResources(), R.drawable.mine_metal, options);
		factory_wood = BitmapFactory.decodeResource(getResources(), R.drawable.factory_wood, options);
        factory_metal = BitmapFactory.decodeResource(getResources(), R.drawable.factory_metal, options);
		clouds = BitmapFactory.decodeResource(getResources(), R.drawable.clouds, options);
		trees = BitmapFactory.decodeResource(getResources(), R.drawable.trees, options);
		people = BitmapFactory.decodeResource(getResources(), R.drawable.people, options);
		rocks = BitmapFactory.decodeResource(getResources(), R.drawable.rocks, options);
        wood = BitmapFactory.decodeResource(getResources(), R.drawable.wood, options);
        rock = BitmapFactory.decodeResource(getResources(), R.drawable.rock, options);
        metal = BitmapFactory.decodeResource(getResources(), R.drawable.metal, options);
		//gui
		start = BitmapFactory.decodeResource(getResources(), R.drawable.start, options);
		build_tree = BitmapFactory.decodeResource(getResources(), R.drawable.build_tree, options);
        build_mine = BitmapFactory.decodeResource(getResources(), R.drawable.build_mine, options);
        build_factory = BitmapFactory.decodeResource(getResources(), R.drawable.build_factory, options);
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit, options);
        farm = BitmapFactory.decodeResource(getResources(), R.drawable.farm, options);
		upgrade = BitmapFactory.decodeResource(getResources(), R.drawable.upgrade, options);
        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, options);
		text1 = BitmapFactory.decodeResource(getResources(), R.drawable.text1, options);
		text2 = BitmapFactory.decodeResource(getResources(), R.drawable.text2, options);
	}
	public Bitmap renderMatrix(int[][] matrix, SpriteSheet sprite, int scale){
		//set up paint/canvas properties
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setARGB(255, 0, 0, 0);
		Canvas canvas = new Canvas();
		//get the columns and rows of the 2d array
		int rows = matrix.length;
		int cols = matrix[0].length;
		//draw the textures to the bitmap from the 2d array
		Bitmap bitM = Bitmap.createBitmap(cols*sprite.getSpriteWidth()*scale,
				rows*sprite.getSpriteHeight()*scale, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(bitM);
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < cols; col++){
				sprite.animate(0);
				sprite.update(col*sprite.getSpriteWidth()*scale, row*sprite.getSpriteHeight()*scale,
					sprite.getSpriteWidth()*scale, sprite.getSpriteHeight()*scale);
				canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
			}
		}
		return bitM;
	}
	public Bitmap renderBox(int width, int height, int r, int g, int b){
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(width, height, conf);
		for (int i = 0; i < width-1; i++){
			for (int j = 0; j < height-1; j++){
				bmp.setPixel(i, j, Color.rgb(r,g,b));
			}
		}	
		return bmp;
	}
	public Bitmap renderBorder(Bitmap bmp1, int x1, int y1, int x2, int y2){
		int width = (x2)+2;
		int height = (y2)+2;
		//create empty bitmap
		Bitmap bmp2 = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		//fill left and right
		for (int y = 0; y < y2; y++){
			for (int x = 0; x < x2; x++){
				if (Color.alpha(bmp1.getPixel(x+x1, y+y1)) > 0) {
					bmp2.setPixel(x+1, y, Color.WHITE); //top
					bmp2.setPixel(x+2, y+1, Color.WHITE); //right	
					if (y < y2 - 1) bmp2.setPixel(x+1, y+2, Color.WHITE); //bottom	
					bmp2.setPixel(x, y+1, Color.WHITE); //left
				}
			}
		}
		return bmp2;
	}
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		//use once, it's a heavier process
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
	public static Bitmap crop(Bitmap bitmapOrg, int x1, int y1, int x2, int y2){
		return Bitmap.createBitmap(bitmapOrg, x1, y1, x2-x1, y2-y1);
	}
}
