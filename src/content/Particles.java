package content;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

public class Particles {
    private int downRadius;
    private int dragRadius;
    private LinkedList<Particle> particles;

    public Particles(){
        particles = new LinkedList<Particle>();
        downRadius = 96;
        dragRadius = 96;
        randomize(200);
    }
    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < particles.size(); i++){
            particles.get(i).draw(canvas,paint);
        }
    }
    public void update(double mod){
        for (int i = 0; i < particles.size(); i++){
            particles.get(i).update(mod,i);
        }
    }
    public void down(int x, int y){
        for (int i = 0; i < particles.size(); i++){
            if (Math.abs(x-(particles.get(i).x)) < downRadius &&
                Math.abs(y-(particles.get(i).y)) < downRadius){
                particles.get(i).inRange = true;
                particles.get(i).setTarget(x,y);
            } else particles.get(i).inRange = false;
        }
    }
    public void move(int x, int y){
        for (int i = 0; i < particles.size(); i++){
            if (Math.abs(x-(particles.get(i).x)) < dragRadius &&
                Math.abs(y-(particles.get(i).y)) < dragRadius){
                particles.get(i).inRange = true;
                particles.get(i).setTarget(x,y);
            } else particles.get(i).inRange = false;
        }
    }
    public void up(int x, int y){
        for (int i = 0; i < particles.size(); i++){
            particles.get(i).inRange = false;
        }
    }
    public void randomize(int amount){
        int randX = 0;
        int randY = 0;
        for (int i = 0; i < amount; i++){
            randX = (int)(Math.random()*MainPanel.getWidth());
            randY = MainPanel.getHeight()-((int)(Math.random()*100))-96;
            particles.add(new Particle(randX,randY));
        }
    }
    public class Particle {
        double x;
        double y;
        double x_speed;
        double y_speed;
        double radius;
        int targetX;
        int targetY;
        boolean inRange;

        public Particle(double x, double y){
            this.x=x;
            this.y=y;
            x_speed = 250;
            y_speed = 250;
            radius = (int)(Math.random()*10)+5;
        }
        public void draw(Canvas canvas, Paint paint){
            paint.setColor(Color.rgb(40, 40, 40));
            canvas.drawCircle((float)x,(float)y,(float)radius,paint);
        }
        public void update(double mod, int index){
            if (inRange){
                if (x < targetX) x += (mod*x_speed);
                else if (x > targetX) x-= (mod*x_speed);
                if (y < targetY) y += (mod*y_speed);
                else if (y > targetY) y-= (mod*y_speed);
                //check collision
                for (int i = 0; i < particles.size(); i++) {
                    if (i != index){
                        if (Math.abs(x-particles.get(i).x) < radius/2 &&
                            Math.abs(y-particles.get(i).y) < radius/2){
                            if (x < particles.get(i).x) x -= (mod*x_speed);
                            else if (x > particles.get(i).x) x += (mod*x_speed);
                            if (y < particles.get(i).y) y -= (mod*y_speed);
                            else if (y > particles.get(i).y) y += (mod*y_speed);
                        }
                    }
                }
            }
        }
        public void setTarget(int x, int y){
            targetX=x;
            targetY=y;
        }
    }
}
