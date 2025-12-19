public class Ractangle {
    public Vector2 position;
    public Vector2 velocity;
    public int size;
    public double gravityForce = 500;
    public double mass = 1.0;
    public final double RESTITUTION = 0.7;

    public Ractangle(double x,double y, int size) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.size = size;
    }

    public void integrate(double deltaTime) {
        Vector2 gravity = new Vector2(0,gravityForce * mass);
        velocity.addSelf(gravity.scale(deltaTime));
        position.addSelf(velocity.scale(deltaTime));
    }
}
