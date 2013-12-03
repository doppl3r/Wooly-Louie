package content;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import buttons.Button;
import textures.SpriteSheet;

public class SlideHOME {
    private Context context;
    private SpriteSheet background;
    private Button title;
    private double titleY = -150;
    private double titleMaxY;


    public SlideHOME(Context context){
        this.context=context;
    }
    public void draw(Canvas canvas, Paint paint){
        if (background == null){
            background = new SpriteSheet(MainPanel.textures.bg1,1,1,0);
            background.update(0,0,canvas.getWidth(),canvas.getHeight());
            title = new Button(MainPanel.textures.title,canvas.getWidth()/2,(int)titleY,
                1,1,true,context);
            title.setFade(0);
            titleMaxY = (canvas.getHeight()/2)-100;
        }
        else{
            background.draw(canvas,paint);
            title.draw(canvas);
        }
    }
    public void update(double mod){
        if (title != null){
            if (titleY < titleMaxY){
                titleY += (1000*mod);
                title.update(title.getX(),titleY);
            }
            if (title.getFade() < 255){
                title.addFade(mod*500);
            }
        }
    }
    public void down(int x, int y){
        if (title.down(x,y)) { title.setFade(0); titleY = -150; }
    }
    public void move(int x, int y){
        if (title.move(x,y)) { title.setFade(0); titleY = -150; }
    }
    public void up(int x, int y){
        if (title.up(x,y)) { title.setFade(0); titleY = -150; }
    }
}
