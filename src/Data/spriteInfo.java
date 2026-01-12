/* This is a way to pass a sprite's key information in one entity. (x, y, tag) */

package Data;

public class spriteInfo {
	// Fields
	private Vector2D v2d;
	private String spriteTag;
	private int oldX, oldY;

	public BoundBox playerBox;

	// Constructor for Player
    public spriteInfo(Vector2D v2d, String tag, int oldX, int oldY) {
        this.v2d = v2d;
        this.spriteTag = tag;

        this.oldX = oldX;
        this.oldY = oldY;

        this.playerBox = new BoundBox(v2d.getX()+32,v2d.getX()+96,
                                      v2d.getY()+96,v2d.getY()+128);
    }

	//Constructor for Interactable Items
    public spriteInfo(Vector2D v2d, String tag, BoundBox objectBox) {
        this.v2d = v2d;
        this.spriteTag = tag;

        this.playerBox = objectBox;
    }

	//Constructor for Backgrounds/Overlay
	public spriteInfo(Vector2D v2d, String tag){
		this.v2d = v2d;
        this.spriteTag = tag;
	}
	
	// Methods
	public String getTag(){return this.spriteTag;}
	
	public Vector2D getCoords(){return this.v2d;}

	public BoundBox getPlayerBox(){return this.playerBox;}

	public void setTag(String newTag){this.spriteTag = newTag;}
	
	public void setCoords(Vector2D newV2D){this.v2d = newV2D;}
	
	public void setCoords(int x, int y){
		this.v2d.setX(x);
		this.v2d.setY(y);

		this.playerBox = new BoundBox(x+32,x+96,y+96,y+128);
	}

	public void setOldPosition(int oldX, int oldY){
		this.oldX = oldX;
		this.oldY = oldY;
	}

	public int getOldX(){return oldX;}
	public int getOldY(){return oldY;}

	public String toString(){
		return "[" + this.v2d.getX() + ", " + this.v2d.getY() + ", " + this.spriteTag + "]";
	}
}
