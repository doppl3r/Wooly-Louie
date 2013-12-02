package game;

import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class People {
	private LinkedList<Person> people;
	
	public People(){
		people = new LinkedList<Person>();
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < people.size(); i++){
			people.get(i).draw(canvas, paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < people.size(); i++){
			people.get(i).update(mod, mainX, mainY);
		}
	}
	public void add(int x1, int y1){ people.add(new Person(x1, y1)); }
	public void remove(int i){ people.remove(i); }
	public void down(int x1, int y1){
		
	}
	public void move(int x1, int y1){
		
	}
	public void up(int x1, int y1, int difference){
		if (difference <= 32){
			for (int i = 0; i < people.size(); i++){
				people.get(i).setWaitTime(0);
				people.get(i).setObjectiveX(x1);
			}
		}
	}
	public void choosePerson(int x1){
		int objective = Game.land.getObjectiveX();
		int nearestIndex = 0;
		for (int i = 1; i < people.size(); i++){
			if (Math.abs(people.get(i).getX() - objective) < 
				Math.abs(people.get(nearestIndex).getX() - objective)){
				nearestIndex = i;
			}
		}
		people.get(nearestIndex).setObjectiveX(objective);
	}
	/*
	 * person class
	 */
	public class Person {
		private int maxWalkTime;
		private int maxWaitTime;
		private int maxSpeed;
		private int originX;
		private int objectiveX;
		private int range;
		private int stopRange;
		private int type;
		private int gender;
		private int age;
		private double walkTime;
		private double waitTime;
		private double personX;
		private double personY;
		private double speed;
		private boolean busy;
		private int direction;
		private SpriteSheet sprite;
		
		public Person(int x, int y){
			maxWalkTime = 3;
			maxWaitTime = 5;
			maxSpeed = 100;
			range = 128;
			stopRange = 16;
			objectiveX = -1;
			//set up sprite
			type = (int)(Math.random()*8);
			gender = type%2;
			sprite = new SpriteSheet(GamePanel.textures.people, 8, 1, 0.0);
			int width = sprite.getBitWidth();
			int height = sprite.getBitHeight();
			personX = originX = x;
			personY = GamePanel.getHeight() - sprite.getBitHeight()*4 - 32;
			sprite.build(personX,personY,width*4,height*4);
			sprite.animate(type);
		}
		public void draw(Canvas canvas, Paint paint){
			canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
		}
		public void update(double mod, double mainX, double mainY){
			if (walkTime > 0){
				//wander in range
				if (objectiveX < 0){
					if (Math.abs((personX+(speed*mod))-originX) < range){ personX += (speed*mod); }
					else{ speed*=-1; personX += (speed*mod); }
					walkTime -= (1*mod);
				}
				else{ 
					//set direction and move
					if (personX < objectiveX) direction = 1;
					else direction = -1;
					speed = maxSpeed*direction;
					if (Math.abs(objectiveX - (personX+(speed*mod))) < stopRange){
						walkTime = 0;
						waitTime = maxWaitTime;
						originX = objectiveX;
						objectiveX = -1;
					}
					else personX += (speed*mod);
				}
			}
			else{ //go towards objective
				if (waitTime > 0) waitTime -= (1*mod);
				else{
					if ((int)(Math.random()*2)==0) direction = -1;
					else direction = 1;
					speed = maxSpeed*direction;
					walkTime = (int)(Math.random()*maxWalkTime);
					waitTime = (int)(Math.random()*maxWaitTime);
				}
			}
			sprite.update(mainX+personX, mainY+personY);
		}
		public int getGender(){ return gender; }
		public int getAge(){ return age; }
		public int getX(){ return (int)personX; }
		public int getY(){ return (int)personY; }
		public boolean isBusy(){ return busy; }
		public void setObjectiveX(int x){
			waitTime = 0;
			objectiveX = x;
			busy = true;
		}
		public void setWaitTime(int wait){ waitTime = wait; }
	}
}
