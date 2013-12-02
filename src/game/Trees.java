package game;

import java.util.LinkedList;

import android.util.Log;
import textures.BitmapText;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Trees {
    private LinkedList<Tree> trees;
    private SpriteSheet woodIcon;
    private BitmapText text;
    private int selected, woodCost;

    public Trees(){
        selected = -1;
        trees = new LinkedList<Tree>();
        woodIcon = new SpriteSheet(GamePanel.textures.wood, 1, 1, 0.0);
        text = new BitmapText();
        woodCost = 1;
    }
    public void draw(Canvas canvas, Paint paint){
        int selected = this.selected; //this.selected changes often...that's bad so we need a fixed index :D
        for (int i = 0; i < trees.size(); i++){
            trees.get(i).draw(canvas, paint);
        }
        //draw info
        if (trees.size() > 0){
            if (selected > -1 && selected < trees.size()){ //other objects might need this fix
                for (int i = 0; i < trees.get(selected).getWoodQuantity(); i++){
                    woodIcon.resize(32,32);
                    woodIcon.update(16+(i*32),48);
                    woodIcon.draw(canvas);

                }
                text.draw("["+trees.get(selected).getWoodQuantity()+"/"+trees.get(selected).getMaxWoodQuantity()+"]",
                    16+(trees.get(selected).getWoodQuantity()*32)+6,
                    56, canvas, paint);
                    //Log.d("hey",""+selected);
                if (trees.get(selected).getWoodQuantity() < trees.get(selected).getMaxWoodQuantity()){
                    paint.setARGB(150,0,0,0);
                    canvas.drawRect(16, 48,
                        16 +(int)(trees.get(selected).getTimerPercentage()*
                            trees.get(selected).getWoodQuantity()*32),
                        80,paint);
                    paint.setARGB(255,255,255,255);
                }
            }
        }
    }
    public void update(double mod, double mainX, double mainY){
        for (int i = 0; i < trees.size(); i++){
            trees.get(i).update(mod, mainX, mainY);
        }
    }
    public void add(int x1, int y1, boolean random){ trees.add(new Tree(x1, y1, random)); }
    public void remove(int i){ trees.remove(i); }
    public void down(int x1, int y1){ }
    public void move(int x1, int y1, int difference){}
    public boolean up(int x1, int y1, int difference){
        boolean up = false;
        if (difference <= 32){
            for (int i = trees.size()-1; i >= 0; i--){
                if (selected > -1) trees.get(selected).showBorder(false);
                //set border properties
                if (trees.get(i).select(x1, y1) && !trees.get(i).showBorder){
                    Game.gui.setGUI(false,false,false,true,false,true);
                    up = true;
                    selected = i;
                    trees.get(selected).showBorder(true);
                    break;
                }
                else{
                    Game.gui.resetGUI();
                    unMarkAll();
                }
            }
        }
        return up;
    }
    public void deselectAll(){
        for (int j = 0;j<trees.size();j++) trees.get(j).showBorder(false);
        selected = -1;
    }
    public void unMarkAll(){
        for (int j = 0;j<trees.size();j++) trees.get(j).setMark(false);
        selected = -1;
    }
    public void markSelected(boolean marked){
        if (selected > -1) trees.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (trees.get(selected).isMarked()){
                Game.land.player.addWood(trees.get(selected).getWoodQuantity());
                Game.gui.addSplashText("+"+(trees.get(selected).getWoodQuantity())+" Wood",
                    Game.land.player.getObjectX()+GamePanel.game.getMainX()-64,
                    GamePanel.getHeight()-48);
                trees.remove(selected);
                Game.gui.resetGUI();
                selected = -1;
            }
        }
    }
    public boolean isBuildable(int x){
        //if the space is open, it can build
        boolean build = true;
        for (int i = 0; i < trees.size(); i++){
            if (Math.abs(x-trees.get(i).getX()) < trees.get(i).getSpriteWidth()*2){
                build = false;
                break;
            }
        }
        return build;
    }
    public int getSelectedIndex(){ return selected; }
    public int getWoodCost(){ return woodCost; }
    /*
     * Tree class
     */
    public class Tree {
        private SpriteSheet sprite;
        private SpriteSheet border;
        private SpriteSheet shadow;
        private boolean showBorder;
        private boolean marked;
        private double timer; //current timer
        private int timerRate; //how fast the timer goes by
        private int maxTimer; //max time on grow
        private int type; //0-3
        private int treeX;
        private int treeY;
        private int maxWood; //max quantity
        private int wood; //current quantity
        private int width;
        private int height;

        public Tree(int x, int y, boolean random){
            maxTimer = 5000;
            timerRate = 167; //at maxTime = 5000, 30 seconds = 167, 1 minutes = 83
            if (random) type = (int)(Math.random()*4);
            if (random) timer = (int)(Math.random()*maxTimer);
            else timer = maxTimer;
            maxWood = 4;
            wood = type+1; //set wood to stage of life
            sprite = new SpriteSheet(GamePanel.textures.trees, 6, 1, 0.0);
            shadow = new SpriteSheet(GamePanel.textures.trees, 6, 1, 0.0);
            width = sprite.getBitWidth();
            height = sprite.getBitHeight();
            //create border properties
            treeX = x;
            treeY = GamePanel.getHeight() - height*4 - 32;
            sprite.build(x,treeY,width*4,height*4);
            sprite.animate(type);
            shadow.build(x, y, width*4, height*4);
            shadow.animate(5);
            border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(),
                    sprite.getSpriteTop(), width, height), 1, 1, 0.0);
            border.build(x,treeY,((width+2)*4),(height+2)*4);
        }
        public void draw(Canvas canvas, Paint paint){
            if (showBorder){
                //draw the border
                canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
                //draw amount of resources

            }
            canvas.drawBitmap(shadow.getBitmap(), shadow.getSpriteRect(), shadow.getDestRect(), paint);
            canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        }
        public void update(double mod, double mainX, double mainY){
            grow(mod);
            sprite.update(mainX+treeX-(width*2), mainY+treeY);
            shadow.update(mainX+treeX-(width*2), mainY+treeY+shadow.getSpriteHeight()*4);
            if (showBorder) border.update(mainX+treeX-4-(width*2), mainY+treeY-4);
        }
        public int getSpriteWidth(){ return sprite.getSpriteWidth(); }
        public int getMaxWoodQuantity(){ return maxWood; }
        public int getWoodQuantity(){ return wood; }
        public int getX(){ return treeX; }
        public int getY(){ return treeY; }
        public int getType(){ return type; }
        public void setX(int x){ this.treeX = x; }
        public void setY(int y){ this.treeY = y; }
        public void setType(int type){ this.type = type; }
        public void showBorder(boolean show){ showBorder = show; if (show) border.resetDest(); }
        public void setMark(boolean marked){ this.marked=marked; }
        public boolean isMarked(){ return marked; }
        public boolean select(int x, int y){
            boolean select = false;
            if (x >= sprite.getDestRect().left && x < sprite.getDestRect().right &&
                y >= sprite.getDestRect().top && y < sprite.getDestRect().bottom){
                select = true;
            }
            return select;
        }
        public void grow(double mod){
            //grow if the timer is ready
            if (wood < maxWood){ //only check upgrades if under development
                if (timer > 0) timer -= (mod*timerRate); //tick the tock
                else { //if it's ready to grow, do this stuff
                    timer = maxTimer; //reset ticker
                    type++;
                    wood = type + 1;
                    sprite.animate(type);
                    //re-render border
                    border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                        sprite.getSpriteLeft(),
                        sprite.getSpriteTop(), width, height), 1, 1, 0.0);
                    border.build(treeX,treeY,((width+2)*4),(height+2)*4);
                }
            }
        }
        public double getTimerPercentage(){ return 1 - (timer/maxTimer); }
    }
}