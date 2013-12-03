package content;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import textures.SpriteSheet;

public class SlideDRAW {
    private SpriteSheet background;
    private SpriteSheet sampleFace;
    private Particles particles;

    public SlideDRAW(Context context){
        particles = new Particles();
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
            sampleFace.draw(canvas,paint);
            particles.draw(canvas,paint);
        }
    }
    public void update(double mod){
        particles.update(mod);
    }
    public void down(int x, int y){
        particles.down(x,y);
    }
    public void move(int x, int y){
        particles.move(x,y);
    }
    public void up(int x, int y){
        particles.up(x,y);
    }
}
