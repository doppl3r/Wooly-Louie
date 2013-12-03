package content;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import buttons.Button;

public class GUI {
    Context context;
    boolean buildButtons;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;

    public GUI(Context context){
        this.context=context;

    }
    public void draw(Canvas canvas, Paint paint){
        if (!buildButtons){
            button1 = new Button(MainPanel.textures.empty,0,canvas.getHeight()-96,1,2,false,context);
            button2 = new Button(MainPanel.textures.capture,96,canvas.getHeight()-96,1,2,false,context);
            button3 = new Button(MainPanel.textures.draw,192,canvas.getHeight()-96,1,2,false,context);
            button4 = new Button(MainPanel.textures.upload,288,canvas.getHeight()-96,1,2,false,context);
            button5 = new Button(MainPanel.textures.empty,384,canvas.getHeight()-96,1,2,false,context);
            buildButtons = true;
        }
        else{
            button1.draw(canvas);
            button2.draw(canvas);
            button3.draw(canvas);
            button4.draw(canvas);
            button5.draw(canvas);
        }
    }
    public void update(double mod){

    }
    public void down(int x, int y){
        button1.down(x,y);
        button2.down(x,y);
        button3.down(x,y);
        button4.down(x,y);
        button5.down(x,y);
    }
    public void move(int x, int y){
        button1.move(x,y);
        button2.move(x,y);
        button3.move(x,y);
        button4.move(x,y);
        button5.move(x,y);
    }
    public void up(int x, int y){
        if (button1.up(x,y)) {
            MainPanel.setCurrentSlide(0);
            MainPanel.audio.playSound(0);
        }
        if (button2.up(x,y)){
            MainPanel.setCurrentSlide(1);
            MainPanel.audio.playSound(0);
        }
        if (button3.up(x,y)){
            MainPanel.setCurrentSlide(2);
            MainPanel.audio.playSound(0);
        }
        if (button4.up(x,y)){
            MainPanel.setCurrentSlide(3);
            MainPanel.audio.playSound(0);
        }
        if (button5.up(x,y)){
            MainPanel.setCurrentSlide(0);
            MainPanel.audio.playSound(0);
        }
    }
}
