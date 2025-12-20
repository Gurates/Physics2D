import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class liquidPhysics {
    
    private final int LEFT_WALL = 0;
    private final int RIGHT_WALL = 750;
    
    private double currentGravity = 500.0;
    private final List<liquidParticles> particles;

    public liquidPhysics() {
        this.particles = new ArrayList<>();
    }


    public void spawnLiquid(int width) {
        Random rand = new Random();
        int randomX = rand.nextInt(RIGHT_WALL - LEFT_WALL - 20) + LEFT_WALL + 10;
        liquidParticles newP = new liquidParticles(randomX, 50, 1.0);
        newP.gravityForce = currentGravity;
        particles.add(newP);
    }

    public List<liquidParticles> getParticles() {
        return particles;
    }

    public void update() {
        for (liquidParticles p : particles) {
            wallCollision(p);
        }
    }

    private void wallCollision(liquidParticles p) {
        if (p.position.x - p.radius <= LEFT_WALL) {
            p.position.x = LEFT_WALL + p.radius;
            p.velocity.x *= -p.RESTITUTION;
            System.out.println("Left wall hit");
        }
        else if (p.position.x + p.radius >= RIGHT_WALL) {
            p.position.x = RIGHT_WALL - p.radius;
            p.velocity.x *= -p.RESTITUTION;
            System.out.println("Right wall hit");
        }
    }
    public List<liquidParticles> getLParticles() {
        return particles;
    }
}