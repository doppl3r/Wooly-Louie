package textures;
import pack.woolylouie.main.R;
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
    //gui textures
    public Bitmap bg1;
    public Bitmap bg2;
    public Bitmap bg3;
    public Bitmap title;

    public Bitmap capture;
    public Bitmap draw;
    public Bitmap upload;
    public Bitmap empty;

    public Bitmap sampleFace;

	public Textures(Context context){
		super(context);
		//set up bitmap for all sdk's
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;  // API Level 1          
        options.inScaled = false;
        //content textures
		bg1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg1, options);
        bg2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg2, options);
        bg3 = BitmapFactory.decodeResource(getResources(), R.drawable.bg3, options);
        title = BitmapFactory.decodeResource(getResources(), R.drawable.title, options);
        //content textures
        capture = BitmapFactory.decodeResource(getResources(), R.drawable.capture, options);
        draw = BitmapFactory.decodeResource(getResources(), R.drawable.draw, options);
        upload = BitmapFactory.decodeResource(getResources(), R.drawable.upload, options);
        empty = BitmapFactory.decodeResource(getResources(), R.drawable.empty, options);
        //content textures
        sampleFace = BitmapFactory.decodeResource(getResources(), R.drawable.sampleface, options);
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
