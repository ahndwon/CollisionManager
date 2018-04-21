public abstract class View {
    private Vector2 pos;
    private Vector2 direction;
    private Vector2 velocity;
    private float speed;
    private int width;
    private int height;
    private CollisionManager.Shape shape;

    public View() {
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void invertX() {
        setDirection(new Vector2(getDirection().getX() * -1, getDirection().getY()));
    }

    public void invertY() {
        setDirection(new Vector2(getDirection().getX(),getDirection().getY() * -1));
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CollisionManager.Shape getShape() {
        return shape;
    }

    public void setShape(CollisionManager.Shape shape) {
        this.shape = shape;
    }
}
