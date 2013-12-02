package game;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Environment {
	private int mainX, mainY;
	private int tileCount = 128;
	private int tileSize = 32;
	private int downX;
	private int downY;
	private int dragX;
	private int dragY;
	private int objectiveX;
	private double rate1;
	private double rate2;
	private double rate3;
	private double rate4;
    private Rect edgeRect;
	private Mountains layer1;
	private Mountains layer2;
	private Mountains layer3;
	private Earth layer4;
	private Clouds clouds;
	public Factories factories;
	public Mines mines;
	public Trees trees;
	public Rocks rocks;
    public Player player;
	
	public Environment(){
		mainX = (GamePanel.getWidth()/2)-((tileCount*tileSize)/2);
		mainY = 0;
		//sprite = new SpriteSheet(GamePanel.textures.terrain, 1, 1, 0.0);
		//newTerrain = GamePanel.textures.renderMatrix(tiles, sprite, 4);
		layer1 = new Mountains();
		layer2 = new Mountains();
		layer3 = new Mountains();
		layer4 = new Earth(tileCount, tileSize);
		rate1 = 0.25;
		rate2 = 0.50;
		rate3 = 0.75;
		rate4 = 1.0;
        edgeRect = new Rect();
		factories = new Factories();
		mines = new Mines();
		trees = new Trees();
		rocks = new Rocks();
		clouds = new Clouds();
        player = new Player(layer4.getWidth() / 2, 0);
		//test
		//factories.add(layer4.getWidth() / 2); //add factory_wood to the center of the map
		for (int i = 0; i < 24; i++)
		trees.add((int)(Math.random()*layer4.getWidth()), 0, true);
		for (int i = 0; i < 12; i++)
		rocks.add((int)(Math.random()*layer4.getWidth()), 0);
		clouds.construct(layer4.getWidth());
		//draw mountains
		addMountains();
	}
	public void draw(Canvas canvas, Paint paint){
		//draw layers
		layer1.draw(canvas, paint);
		layer2.draw(canvas, paint);
		layer3.draw(canvas, paint);
		layer4.draw(canvas, paint);
		//objects
		clouds.draw(canvas, paint); //draw clouds
		rocks.draw(canvas, paint);  //draw rocks
		trees.draw(canvas, paint);  //draw trees
		factories.draw(canvas, paint); //draw factories
		mines.draw(canvas, paint);
        //player
        player.draw(canvas, paint);
        //end of world
        if (mainX-(downX-dragX) > 0 ||
            mainX-(downX-dragX) < -(layer4.getWidth() - GamePanel.getWidth())){
            paint.setARGB(255,0,0,0);
            canvas.drawRect(edgeRect, paint);
        }
        else paint.setARGB(255,255,255,255);
	}
	public void update(double mod){
		//mountains
		layer1.update(mod, (mainX-(downX-dragX))*rate1, mainY);
		layer2.update(mod, (mainX-(downX-dragX))*rate2, mainY);
		layer3.update(mod, (mainX-(downX-dragX))*rate3, mainY);
		layer4.update(mod, (mainX-(downX-dragX))*rate4, mainY);
		//objects
		factories.update(mod, (mainX - (downX - dragX)) * rate4, mainY);//factories
		mines.update(mod,  (mainX-(downX-dragX))*rate4, mainY);//mines
		clouds.update(mod, (mainX-(downX-dragX))*rate4, mainY);//clouds
		trees.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
		rocks.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
        //player
        player.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
        //update edge rectangle
        if (mainX-(downX-dragX) > 0){
            edgeRect.left=0;
            edgeRect.top=0;
            edgeRect.right=mainX-(downX-dragX);
            edgeRect.bottom=GamePanel.getHeight();
        }
        else if (mainX-(downX-dragX) < -(layer4.getWidth() - GamePanel.getWidth())){
            edgeRect.left=mainX-(downX-dragX)+(layer4.getWidth());
            edgeRect.top=0;
            edgeRect.right=GamePanel.getWidth();
            edgeRect.bottom=GamePanel.getHeight();
        }
	}
	public void down(int x1, int y1){
        if (!Game.gui.isPressed()){
            downX = dragX = x1;
            factories.down(x1, y1);
            mines.down(x1, y1);
            trees.down(x1, y1);
            rocks.down(x1, y1);
        }
	}
	public void move(int x1, int y1){
		//check drag
        //Log.d("hey", "" + Game.gui.move(x1, y1));
        if (!Game.gui.isPressed()){
            dragX = x1;
            factories.move(x1, y1, Math.abs(downX - dragX));
            mines.move(x1, y1, Math.abs(downX-dragX));
            trees.move(x1, y1, Math.abs(downX-dragX));
            rocks.move(x1, y1, Math.abs(downX-dragX));
        }
	}
	public void up(int x1, int y1){
		//release drag
		mainX -= (downX-dragX);
		mainY -= (downY-dragY);
		//deselectAll();
		objectiveX = -mainX + x1;
        //Log.d("hey","pressed: "+Game.gui.isPressed());
        if (!Game.gui.isPressed()){
            if (trees.up(x1, y1, Math.abs(downX-dragX))){
                player.setObjectX(objectiveX);
                deselectAll(0);
                unMarkAll(0);
            }
            else if (rocks.up(x1, y1, Math.abs(downX-dragX))){
                player.setObjectX(objectiveX);
                deselectAll(1);
                unMarkAll(1);
            }
            else if (mines.up(x1, y1, Math.abs(downX-dragX))){
                player.setObjectX(objectiveX);
                deselectAll(2);
                unMarkAll(2);
            }
            else if (factories.up(x1, y1, Math.abs(downX-dragX))){
                player.setObjectX(objectiveX);
                deselectAll(3);
                unMarkAll(3);
            }
            else{
                player.setTarget(objectiveX,y1,(downX-dragX)); //move player
                //deselectAll(-1);
                //unMarkAll(-1);
            }
        }
        else {
            Game.gui.setPressed(false);
            //Log.d("hey","pressed: "+Game.gui.isPressed());
        }
        if (mainX-(downX-dragX) > 0){
            mainX = 0;
            downX = x1;
        }
        else if (mainX-(downX-dragX) < -(layer4.getWidth() - GamePanel.getWidth())) {
            mainX = -(layer4.getWidth() - GamePanel.getWidth());
            downX = x1;
        }
        resetEdgeRect();
		downX = downY = dragX = dragY = 0;
	}
	public void deselectAll(int i){
        //to deselect all, use '-1'
        if (i != 0) trees.deselectAll();
		if (i != 1) rocks.deselectAll();
        if (i != 2) mines.deselectAll();
		if (i != 3) factories.deselectAll();
	}
    public void unMarkAll(int i){
        //to un-mark all, use '-1'
        if (i != 0) trees.unMarkAll();
        if (i != 1) rocks.unMarkAll();
        if (i != 2) mines.unMarkAll();
        if (i != 3) factories.unMarkAll();
        //do this for other s!
    }
    public boolean isBuildable(){
        //check if the spot is open to build something
        boolean build = true;
        if (!trees.isBuildable(player.getX()) ||
            !rocks.isBuildable(player.getX()) ||
            !mines.isBuildable(player.getX()) ||
            !factories.isBuildable(player.getX())){
            Game.gui.addSplashText("You cannot build here!", player.getX()+mainX-160, GamePanel.getHeight()-48);
            build = false;
        }
        return build;
    }
	public void addMountains(){
		int spacing = 0;
		int maxSpacing = 360;
		int x = -256;
		int y = GamePanel.getHeight()-192; //480 - 256 = 224
		int offset = 32;
		int yOffset = 0;
		int height = 0;
		
		//layer1
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate1){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer1.add(x, y+yOffset, height, 0);
		}
		//layer2
		x = -256; y += offset; maxSpacing = 720;
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate2){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer2.add(x, y+yOffset, height, 1);
		}
		//layer3
		x = -256; y += offset; maxSpacing = 1080;
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate3){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer3.add(x, y+yOffset, height, 2);
		}
	}
	public int getMainX(){ return mainX; }
	public int getMainY(){ return mainY; }
    public int getDragDifference(){ return (downX - dragX); }
    public int getRawX(){ return mainX - (downX - dragX); }
	public int getObjectiveX(){ return objectiveX; }
    public void resetEdgeRect(){ edgeRect.top=edgeRect.right=edgeRect.bottom=edgeRect.left=0; }
}
