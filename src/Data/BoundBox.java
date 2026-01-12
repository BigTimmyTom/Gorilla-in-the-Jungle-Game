package Data;

public class BoundBox {
    // X-axis Fields
    private int x1, x2;

    // Y-axis Fields
    private int y1, y2;


    public BoundBox(int x1, int x2, int y1, int y2){
        this.x1 = x1;
        this.x2 = x2;

        this.y1 = y1;
        this.y2 = y2;
    }

    public BoundBox(Vector2D position, int x2, int y2){
        this.x1 = position.getX();
        this.x2 = x2;

        this.y1 = position.getY();
        this.y2 = y2;
    }

    public void setBox(int x1, int x2, int y1, int y2){
        this.x1 = x1;
        this.x2 = x2;

        this.y1 = y1;
        this.y2 = y2;
    }

    public void setPosition(int x, int y){
        this.x1 = x;
        this.y1 = y;
    }

    //Getter Methods
    public int getX1(){return x1;}
    public int getX2(){return x2;}
    public int getY1(){return y1;}
    public int getY2(){return y2;}

    public int getHeight(){return y2-y1;}
    public int getWidth(){return x2-x1;}

    public int getCenterX(){return (x1+x2)/2;}
    public int getCenterY(){return (y1+y2)/2;}

    public BoundBox getBoundBox(){return new BoundBox(this.x1,this.x2,this.y1,this.y2);}

    public boolean collisionDetect(BoundBox item){
        return this.x1 < item.x2 &&
               this.x2 > item.x1 &&
               this.y1 < item.y2 &&
               this.y2 > item.y1;
    }
}


