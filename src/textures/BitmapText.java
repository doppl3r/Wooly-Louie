package textures;
import android.graphics.Canvas;
import android.graphics.Paint;
import game.GamePanel;


public final class BitmapText{
	//private boolean rendered;
    private SpriteSheet text1, text2;
    private String text;
    private int x, y;
    private double fade;
	//private Bitmap render;
	public BitmapText(){
        text1 = new SpriteSheet(GamePanel.textures.text1, 16, 6, 0.0);
        text2 = new SpriteSheet(GamePanel.textures.text2, 16, 6, 0.0);

        text1.resize(2);
        text2.resize(2);

        fade = 255;
	}
    public BitmapText(String text, int x, int y){
        text1 = new SpriteSheet(GamePanel.textures.text1, 16, 6, 0.0);
        text2 = new SpriteSheet(GamePanel.textures.text2, 16, 6, 0.0);

        text1.resize(2);
        text2.resize(2);

        fade = 255;

        this.text = text;
        this.x = x;
        this.y = y;
    }
	public final void draw(String text, int x, int y, Canvas canvas, Paint paint){
        //paint.setAlpha((int)(fade));
        for (int i = 0; i < text.length(); i++){
            //top
            text2.update(x+(i*text2.getBitWidth()),y-text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //right
            text2.update(x+text2.getRatio()+(i*text2.getBitWidth()),y);
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //bottom
            text2.update(x+(i*text2.getBitWidth()),y+text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //left
            text2.update(x - text2.getRatio() + (i * text2.getBitWidth()), y);
            text2.animate((int) (text.charAt(i)) - 32);
            text2.draw(canvas,paint);
        }
        for (int i = 0; i < text.length(); i++){
            //center
            text1.update(x+(i*text1.getBitWidth()),y);
            text1.animate((int)(text.charAt(i))-32);
            text1.draw(canvas,paint);
        }
        //paint.setAlpha(255);
	}
    public void draw(Canvas canvas, Paint paint){
        //paint.setAlpha((int)(fade));
        for (int i = 0; i < text.length(); i++){
            //top
            text2.update(x+(i*text2.getBitWidth()),y-text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //right
            text2.update(x+text2.getRatio()+(i*text2.getBitWidth()),y);
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //bottom
            text2.update(x+(i*text2.getBitWidth()),y+text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas,paint);
            //left
            text2.update(x - text2.getRatio() + (i * text2.getBitWidth()), y);
            text2.animate((int) (text.charAt(i)) - 32);
            text2.draw(canvas,paint);
        }
        for (int i = 0; i < text.length(); i++){
            //center
            text1.update(x+(i*text1.getBitWidth()),y);
            text1.animate((int)(text.charAt(i))-32);
            text1.draw(canvas,paint);
        }
        //paint.setAlpha(255);
    }
    public void setString(String st){ text = st; }
    public void setX(int x){ this.x=x; }
    public void setY(int y){ this.y=y; }
    public void setXY(int x, int y){ this.x=x; this.y=y; }
    public void fadeUp(double i){
        if (fade > 0){
            fade -= i*10;
            y -= i;
        }
        else fade = 0;
    }
    public String getText(){ return text; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public double getFade(){ return fade; }
}
