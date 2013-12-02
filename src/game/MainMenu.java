package game;

import buttons.Button;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MainMenu {
	private boolean isActive = true;
	private boolean adjustMenu = true;
	private Button start;
	
	public MainMenu(Context context){
		start = new Button(GamePanel.textures.start, 0, 0, true, context);
		start.resize(8);
	}
	public void draw(Canvas canvas, Paint paint){
		if (isActive){
			//draw menu components
			if (adjustMenu){
				start.update(canvas.getWidth()/2, canvas.getHeight()/2);
				adjustMenu = false;
			}
			else{
				start.draw(canvas);
			}
		}
	}
	public void update(double mod){
		if (isActive){
			//update menu components
			//start.update(start.getX(), start.getY()+(mod*25));
		}
	}
	public void setActivity(boolean isActive){ this.isActive=isActive; }
	public void down(int x, int y){
		if (isActive){
			start.down(x, y);
		}
	}
	public void move(int x, int y){
		if (isActive){
			start.move(x, y);
		}
	}
	public void up(int x, int y){
		if (isActive){
			if (start.up(x, y)){
				isActive = false;
				GamePanel.game.setActivity(true);
			}
		}
	}
}
