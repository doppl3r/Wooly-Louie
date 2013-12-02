package game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;

public class Atmosphere {
	//private float time;
	private float timeOffset;
	private float r1;
	private float r2;
	private float g1;
	private float g2;
	private float b1;
	private float b2;
	private float ratio = 0;
	private int minR = 0;
	private int minG = 0;
	private int minB = 0;
	private int targR = 0;
	private int targG = 0;
	private int targB = 0;
	private int index = 0;
	/*private int[][] rgb = {
		{145,255,225}, //day
		{145,255,225},
		{220,255,245},
		{165,160,200},
		{160,125,185},
		{ 60, 45, 75}, //night
		{ 60, 45, 75},
		{160,125,185},
		{165,160,200},
		{220,255,245},
	};*/
	/*private int[][] rgb = {
		{145,255,225},
		{145,255,225}, //day
		{220,255,245},
		{220,255,245},
		{ 50, 90, 80},
		{ 50, 90, 80}, //night
		{220,255,245},
		{220,255,245},
	};*/
	private int[][] rgb = {
		{145,255,225}, //day
		{220,255,245},
		{ 40, 70, 85},
		{ 40, 70, 85}, //night
		{220,255,245},
		{145,255,225},
	};
	public Atmosphere(){
		timeOffset = 240; //360
		setRGB(0,0);
		setRGB(0,1);
	}
	public void draw(Canvas canvas, Paint paint){
		//adjust shader
		paint.setShader(new LinearGradient(0, 0, 0, GamePanel.getHeight(), 
			Color.rgb((int)r1, (int)g1, (int)b1), Color.rgb((int)r2, (int)g2, (int)b2), TileMode.CLAMP)); 
		canvas.drawRect(0,0,GamePanel.getWidth(), GamePanel.getHeight(), paint); 
		paint.setShader(null); //reset shader
	}
	public void update(double mod, float time){
		//setRGB(time, 0);
		//setRGB(time, 1);
	}
	public void down(int x, int y){
		
	}
	public void move(int x, int y){
		
	}
	public void up(int x, int y){
		
	}
	public void setRGB(float time, int currentPoint){
		index = (int)((((time+(timeOffset*currentPoint))%1440)/1440)*rgb.length);
		ratio = ((time+(timeOffset*currentPoint))%(1440/rgb.length))/(1440/rgb.length);
		//set up minimum and target colors
		minR = rgb[index][0];
		minG = rgb[index][1];
		minB = rgb[index][2];
		targR = rgb[((index+1)%rgb.length)][0];
		targG = rgb[((index+1)%rgb.length)][1];
		targB = rgb[((index+1)%rgb.length)][2];
		//adjust to current time
		switch(currentPoint){
			case(0): //set point 1 rgb
				if (targR > minR) r1 = minR + (targR - minR) * ratio;
				else r1 = targR + (minR - targR) * (1 - ratio);
				if (targG > minG) g1 = minG + (targG - minG) * ratio;
				else g1 = targG + (minG - targG) * (1 - ratio);
				if (targB > minB) b1 = minB + (targB - minB) * ratio;
				else b1 = targB + (minB - targB) * (1 - ratio);
				break;
			
			case(1): //set point 2 rgb
				if (targR > minR) r2 = minR + (targR - minR) * ratio;
				else r2 = targR + (minR - targR) * (1 - ratio);
				if (targG > minG) g2 = minG + (targG - minG) * ratio;
				else g2 = targG + (minG - targG) * (1 - ratio);
				if (targB > minB) b2 = minB + (targB - minB) * ratio;
				else b2 = targB + (minB - targB) * (1 - ratio);
				break;
			default: setRGB(time, 0); break;
		}
	}
}
