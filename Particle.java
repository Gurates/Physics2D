import java.util.Vector;

public class Particle {
    public Vector2 position;
    public Vector2 velocity;
    public int radius;
    public double gravityForce = 9.807;

    public Particle(double x, double y, int radius) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.radius = radius;
    }

    public void integrate(double deltaTime) {
        Vector2 gravity = new Vector2(0,gravityForce);
        velocity.addSelf(gravity.scale(deltaTime));
        position.addSelf(velocity.scale(deltaTime));
}

}
