package game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import textures.SpriteSheet;

public class Player {

    private int wood;
    private int rocks;
    private int metal;
    private int color;
    private int targetX;
    private int radius;
    private int objectX;
    private int width;
    private int height;

    private double x;
    private double y;
    private double arrowX;
    private double arrowY;
    private double arrowYMAX;
    private double arrowFade;
    private double speed;

    private SpriteSheet sprite;
    private SpriteSheet arrow;

    public Player(int x, int y){
        //test
        /*wood = 100;
        rocks = 100;
        metal = 100;*/

        color = (int)(Math.random()*8);
        radius = 8;
        speed = 256;
        arrowYMAX = GamePanel.getHeight()-96;
        sprite = new SpriteSheet(GamePanel.textures.people, 8, 1, 0.0);
        arrow  = new SpriteSheet(GamePanel.textures.arrow , 1, 1, 0.0);
        width = sprite.getBitWidth();
        height = sprite.getBitHeight();
        this.x=x;
        this.y = GamePanel.getHeight() - sprite.getBitHeight()*4 - 32;
        sprite.build(this.x,this.y,width*8,height*4); //stretch player-width
        sprite.animate(color);
        arrow.build(0,0,arrow.getBitWidth()*4,arrow.getBitHeight()*4);
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        if (arrowFade > 0 && arrowY < arrowYMAX){ //draw arrow, arrowYMAX fixes update issue
            paint.setAlpha((int)arrowFade); //transparent arrow over time
            canvas.drawBitmap(arrow.getBitmap(), arrow.getSpriteRect(), arrow.getDestRect(), paint);
            paint.setAlpha(255);
        }
    }
    public void update(double mod, double mainX, double mainY){
        //set direction and move
        if (targetX > 0){
            if (Math.abs(x - targetX) > radius){
                if (x < targetX) x+=(speed*mod);
                else x-=(speed*mod);
            }
            else{
                targetX = -1;
                Game.land.trees.farmMarked();
                Game.land.rocks.farmMarked();
                Game.land.mines.farmMarked();
                Game.land.factories.farmMarked();
            }
        }
        //fade arrow
        if (arrowFade > 0){
            arrowFade -= 500*mod;
            arrowY -= 100*mod;
        }
        //update sprites
        sprite.update(mainX+x-(width*4), mainY+y); //center horizontally = (width*4)
        arrow.update(mainX+arrowX-(width*4), mainY+arrowY);
    }

    public void buildTree(){
        //splash the message of reduction
        if (wood >= Game.land.trees.getWoodCost()){
            Game.gui.addSplashText("-"+Game.land.trees.getWoodCost()+" Wood",
                (int)x+GamePanel.game.getMainX()-64,GamePanel.getHeight()-48);
            wood -= Game.land.trees.getWoodCost(); //subtract from inventory
            Game.land.trees.add((int)x,(int)y, false);
            Game.gui.resetGUI();
        }
        else {
            Game.gui.addSplashText("Insufficient resources!",
                    (int)x+GamePanel.game.getMainX()-192,
                    GamePanel.getHeight()-80);
            Game.gui.addSplashText("Wood x"+Game.land.trees.getWoodCost(),
                    (int)x+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-48);
        }
    }

    public void buildMine(){
        //splash the message of reduction
        if (wood >= Game.land.mines.getWoodCost() &&
            rocks>= Game.land.mines.getRockCost()){
            Game.gui.addSplashText("-"+Game.land.mines.getWoodCost()+" Wood",
                    (int)x+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-80);
            Game.gui.addSplashText("-"+Game.land.mines.getRockCost()+" Rocks",
                    (int)x+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-48);
            wood  -= Game.land.mines.getWoodCost(); //subtract from inventory
            rocks -= Game.land.mines.getRockCost();
            Game.land.mines.add((int)x);
            Game.gui.resetGUI();
        }
        else {
            Game.gui.addSplashText("Insufficient resources!",
                (int)x+GamePanel.game.getMainX()-192,
                GamePanel.getHeight()-80);
            Game.gui.addSplashText("Wood x"+Game.land.mines.getWoodCost()+", Rocks x"+
                Game.land.mines.getRockCost(),(int)x+GamePanel.game.getMainX()-160,
                GamePanel.getHeight()-48);
        }
    }

    public void buildFactory(){
        //splash the message of reduction
        if (wood >= Game.land.factories.getWoodCost() &&
            rocks>= Game.land.factories.getRockCost()){
            Game.gui.addSplashText("-"+Game.land.factories.getWoodCost()+" Wood",
                    (int)x+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-80);
            Game.gui.addSplashText("-"+Game.land.factories.getRockCost()+" Rocks",
                    (int)x+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-48);
            wood  -= Game.land.factories.getWoodCost(); //subtract from inventory
            rocks -= Game.land.factories.getRockCost();
            Game.land.factories.add((int)x);
            Game.gui.resetGUI();
        }
        else{
            Game.gui.addSplashText("Insufficient resources!",
                (int)x+GamePanel.game.getMainX()-192,
                GamePanel.getHeight()-80);
            Game.gui.addSplashText("Wood x"+Game.land.factories.getWoodCost()+", Rocks x"+
                Game.land.factories.getRockCost(),(int)x+GamePanel.game.getMainX()-160,
                GamePanel.getHeight()-48);
        }
    }

    //Player input
    public void down(double x, double y){}
    public void move(double x, double y){}
    public void up(int x1, int y1){}
    public void setTarget(int x, int y, int difference){
        if (Math.abs(difference) <= 4){
            targetX = x;
            arrowX = x-32; //center arrow
            arrowY = arrowYMAX; //vertically align arrow
            arrowFade = 255;
        }
    }
    public void setObjectX(int objectX){ this.objectX=objectX; }
    public void addWood(int i){ wood += i; }
    public void addRocks(int i){ rocks += i; }
    public void addMetal(int i){ metal += i; }
    public int getObjectX(){ return objectX; }
    public int getWood(){ return wood; }
    public int getRocks(){ return rocks; }
    public int getMetal(){ return metal; }
    public int getX(){ return (int)x; }
    public int getY(){ return (int)y; }
}
