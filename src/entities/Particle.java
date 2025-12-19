public class Particle {
    public Vector2 position;
    public Vector2 velocity;
    public double radius;
    public double gravityForce = 500;
    public double mass = 1.0;
    public final double RESTITUTION = 0.7;

    public Particle(double x, double y, double radius) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.radius = radius;
    }

    public void integrate(double deltaTime) {
        Vector2 gravity = new Vector2(0,gravityForce * mass);
        velocity.addSelf(gravity.scale(deltaTime));
        position.addSelf(velocity.scale(deltaTime));
    }
}