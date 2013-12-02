package game;
import java.util.LinkedList;

import textures.BitmapText;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Factories {
    private LinkedList<Factory> factories;
    private SpriteSheet metalIcon;
    private BitmapText text;
    private int selected, woodCost, rockCost;
    private boolean upgrade;

    public Factories() {
        selected = -1;
        factories = new LinkedList<Factory>();
        metalIcon = new SpriteSheet(GamePanel.textures.metal, 1, 1, 0.0);
        text = new BitmapText();
        woodCost = 10;
        rockCost = 20;
    }
    public void draw(Canvas canvas, Paint paint){
        int selected = this.selected;
        for (int i = 0; i < factories.size(); i++){
            factories.get(i).draw(canvas, paint);
        }
        //draw info
        if (factories.size() > 0){
            if (selected > -1 && selected < factories.size()){
                int fix=0; //show tile no mater what
                if (factories.get(selected).getMetalQuantity() <= 0) fix = 1;
                for (int i = 0; i < factories.get(selected).getMetalQuantity()+fix; i++){
                    metalIcon.resize(32, 32);
                    metalIcon.update(16 + (i * 32), 48);
                    metalIcon.draw(canvas);

                }
                text.draw("["+factories.get(selected).getMetalQuantity()+"/"+factories.get(selected).getMaxMetalQuantity()+"]",
                        16+((factories.get(selected).getMetalQuantity()+fix)*32)+6,
                        56, canvas, paint);
                if (factories.get(selected).getMetalQuantity() < factories.get(selected).getMaxMetalQuantity()) {
                    paint.setARGB(150, 0, 0, 0);
                    canvas.drawRect(16, 48,
                            16 + (int) (factories.get(selected).getTimerPercentage() *
                                    (factories.get(selected).getMetalQuantity() + fix) * 32),
                            80, paint);
                    paint.setARGB(255, 255, 255, 255);
                }
            }
        }
    }
    public void update(double mod, double mainX, double mainY){
        for (int i = 0; i < factories.size(); i++){
            factories.get(i).update(mod, mainX, mainY);
        }
    }
    public void add(int x1){ factories.add(new Factory(x1)); }
    public void remove(int i){ factories.remove(i); }
    public void down(int x1, int y1){ }
    public void move(int x1, int y1, int difference){ }
    public boolean up(int x1, int y1, int difference){
        boolean up = false;
        if (difference <= 32){
            for (int i = factories.size()-1; i >= 0; i--){
                if (selected > -1) factories.get(selected).showBorder(false);
                //set border properties
                if (factories.get(i).select(x1, y1) && !factories.get(i).showBorder){
                    Game.gui.setGUI(false,
                            false,
                            false,
                            factories.get(i).getMetalQuantity()>0, //farm metal from factory
                            true, //was factories.get(i).isUpgradable()
                            true);
                    up = true;
                    selected = i;
                    factories.get(selected).showBorder(true);
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
        for (int j = 0;j< factories.size();j++) factories.get(j).showBorder(false);
        selected = -1;
    }
    public void unMarkAll(){
        for (int j = 0;j<factories.size();j++) factories.get(j).setMark(false);
        selected = -1;
    }
    public void markSelected(boolean marked, boolean upgrade){
        this.upgrade = upgrade;
        if (selected > -1) factories.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (factories.get(selected).isMarked()){
                if (!upgrade){
                    Game.land.player.addMetal(factories.get(selected).getMetalQuantity());
                    Game.gui.addSplashText("+"+(factories.get(selected).getMetalQuantity())+" Metal",
                            Game.land.player.getObjectX()+GamePanel.game.getMainX()-64,
                            GamePanel.getHeight()-48);
                    factories.get(selected).setMetalQuantity(0);
                }
                else{
                    if (factories.get(selected).isUpgradable()){
                        factories.get(selected).upgrade();
                    }
                    else {
                        Game.gui.addSplashText("Insufficient resources!",
                                Game.land.player.getX()+GamePanel.game.getMainX()-192,
                                GamePanel.getHeight()-80);
                        Game.gui.addSplashText("Metal x"+factories.get(selected).getMetalUpgradeCost(),
                                Game.land.player.getX()+GamePanel.game.getMainX()-64,
                                GamePanel.getHeight()-48);
                    }
                }

                //factories.get(selected).resetTimer();
                deselectAll();
                unMarkAll();
                Game.gui.resetGUI();
            }
        }
    }
    public boolean isBuildable(int x){
        //if the space is open, it can build
        boolean build = true;
        for (int i = 0; i < factories.size(); i++){
            if (Math.abs(x-factories.get(i).getX()) < factories.get(i).getSpriteWidth()*4){
                build = false;
                break;
            }
        }
        return build;
    }
    public int getSelectedIndex(){ return selected; }
    public int getWoodCost(){ return woodCost; }
    public int getRockCost(){ return rockCost; }
    /*
     * Factory class
     */
    public static class Factory {
        private SpriteSheet sprite;
        private SpriteSheet border;
        private boolean showBorder;
        private boolean marked;
        private boolean upgraded;
        private double maxAnimationTime = 10;
        private double animationTime = maxAnimationTime;
        private double rate;
        private double timer;
        private int timerRate;
        private int maxTimer;
        private int factoryX;
        private int factoryY;
        private int metals;
        private int maxMetals;
        private int width;
        private int height;
        private int metalUpgradeCost;

        public Factory(int x){
            metals = 1;
            timer = maxTimer = 5000;
            rate = 400; //animation speed
            timerRate = 334; //167=1min
            maxMetals = 5;
            metalUpgradeCost = 10;

            sprite = new SpriteSheet(GamePanel.textures.factory_wood, 6, 6, 0.5);
            width = sprite.getBitWidth();
            height = sprite.getBitHeight();
            //create border properties
            factoryX = x;
            factoryY = GamePanel.getHeight() - height*4 - 32;
            sprite.build(x, factoryY,width*4,height*4);
            sprite.animate(0);
            border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(), sprite.getSpriteTop(), width, height), 1, 1, 0.0);
            border.build(x, factoryY,((width+2)*4),(height+2)*4);
        }
        public void draw(Canvas canvas, Paint paint){
            if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
            canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        }
        public void update(double mod, double mainX, double mainY){
            process(mod);
            //update animation
            if (animationTime > 0) animationTime -= (rate*mod);
            else{
                if (metals < maxMetals){
                    sprite.animate();
                    animationTime = maxAnimationTime;
                    border.setBitmap(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(),	sprite.getSpriteTop(), width, height));
                }
                else sprite.animate(0);
            }
            //update position
            sprite.update(mainX+factoryX-(width*2), mainY+ factoryY);
            if (showBorder) border.update(mainX+factoryX-4-(width*2), mainY+ factoryY -4);
        }
        public int getSpriteWidth(){ return sprite.getSpriteWidth(); }
        public int getMaxMetalQuantity(){ return maxMetals; }
        public int getMetalQuantity(){ return metals; }
        public int getMetalUpgradeCost(){ return metalUpgradeCost; }
        public int getX(){ return factoryX; }
        public int getY(){ return factoryY; }
        public boolean getUpgraded(){ return upgraded; }
        public double getRate(){ return rate; }
        public void setX(int x){ this.factoryX = x; }
        public void setY(int y){ this.factoryY = y; }
        public void showBorder(boolean show){ showBorder = show; if (show) border.resetDest(); }
        public void setMark(boolean marked){ this.marked=marked; }
        public void setMetalQuantity(int q){ metals=q; }
        public void resetTimer(){ timer = maxTimer; }
        public boolean isMarked(){ return marked; }
        public boolean isUpgradable(){ return Game.land.player.getMetal() >= metalUpgradeCost && !upgraded; }
        public boolean select(int x, int y){
            boolean select = false;
            if (x >= sprite.getDestRect().left && x < sprite.getDestRect().right &&
                y >= sprite.getDestRect().top && y < sprite.getDestRect().bottom){
                select = true;
            }
            return select;
        }
        public void process(double mod){
            //grow if the timer is ready
            if (metals < maxMetals){ //only check upgrades if under development
                if (timer > 0) timer -= (mod*timerRate); //tick the tock
                else { //if it's ready to grow, do this stuff
                    timer = maxTimer; //reset ticker
                    metals++;
                    if (showBorder){
                        Game.gui.setGUI(false,
                            false,
                            false,
                            getMetalQuantity()>0, //farm rocks from mine
                            true, //was isUpgradable();
                            true);
                    }
                }
            }
        }
        public void upgrade(){
            if (!upgraded){
                timerRate = 668; //15 seconds iteration
                upgraded = true;
                maxMetals = 10;
                rate = 800;
                Game.gui.addSplashText("Upgraded!",
                        Game.land.player.getObjectX()+GamePanel.game.getMainX()-64,
                        GamePanel.getHeight()-48);
                Game.land.player.addMetal(-metalUpgradeCost);
                sprite = new SpriteSheet(GamePanel.textures.factory_metal, 6, 6, 0.5);
                sprite.build(factoryX,factoryY,width*4,height*4);
                //sprite.animate(0);
                //re-render border
                border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                        sprite.getSpriteLeft(),
                        sprite.getSpriteTop(), width, height), 1, 1, 0.0);
                border.build(factoryX,factoryY,((width+2)*4),(height+2)*4);
            }
        }
        public double getTimerPercentage(){ return 1 - (timer/maxTimer); }
    }
}
