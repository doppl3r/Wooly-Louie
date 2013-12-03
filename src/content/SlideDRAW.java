package content;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import textures.SpriteSheet;

public class SlideDRAW {
    private SpriteSheet background;
    private SpriteSheet sampleFace;

    public SlideDRAW(Context context){

    }
    public void draw(Canvas canvas, Paint paint){
        if (background == null){
            background = new SpriteSheet(MainPanel.textures.bg1,1,1,0);
            background.update(0,0,canvas.getWidth(),canvas.getHeight());
            sampleFace = new SpriteSheet(MainPanel.textures.sampleFace,1,1,0);
            sampleFace.update(0,0,canvas.getWidth(),canvas.getHeight());
        }
        else {
            background.draw(canvas,paint);
            sampleFace.draw(canvas, paint);
        }
    }
    public void update(double mod){

    }
    public void down(int x, int y){

    }
    public void move(int x, int y){

    }
    public void up(int x, int y){

    }
}
