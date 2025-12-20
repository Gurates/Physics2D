import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import java.lang.Math;

public class ParticlePhysics {
    
    private final List<Particle> particles;
    private final int FLOOR_Y = 540;
    private double currentGravity = 500.0;

    public ParticlePhysics() {
        this.particles = new ArrayList<>();
        this.particles.add(new Particle(100, 50, 20));
        this.particles.get(0).gravityForce = currentGravity;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    //update function
    public void update(double deltaTime) {
        for (Particle p : particles) {
            p.integrate(deltaTime);
            handleCollision(p);
        }
        resolveCollisions();
    }

    // collision
    private void handleCollision(Particle p) { 
        double bottomEdge = p.position.y + p.radius;
        
        if (bottomEdge >= FLOOR_Y) {
            p.position.y = FLOOR_Y - p.radius;

            p.velocity.y *= -p.RESTITUTION;
            p.gravityForce = currentGravity;
        }

        if (bottomEdge >= FLOOR_Y - 1.0 && Math.abs(p.velocity.y) < 5.0) {
             p.velocity.y = 0;
             p.gravityForce = 0;
             p.position.y = FLOOR_Y - p.radius;
        }
    }

    private void resolveCollisions(){
    
        int particleCount = particles.size();

        for (int i = 0; i < particleCount; i++) {
            Particle p1 = particles.get(i);
        
            for (int j = i + 1; j < particleCount; j++) {
                Particle p2 = particles.get(j);


                double dx = p1.position.x - p2.position.x;
                double dy = p1.position.y - p2.position.y;
                double distSq = dx * dx + dy * dy;

                double radiusSum = p1.radius + p2.radius;

                if (distSq < radiusSum * radiusSum) {
                    double distance = Math.sqrt(distSq);
                    double overlap = radiusSum - distance;
                    
                    Vector2 normal = new Vector2(dx, dy).normalize();
                    Vector2 correction = normal.scale(overlap / 2.0);
                    
                    p1.position.addSelf(correction);
                    p2.position.addSelf(correction.scale(-1));

                    Vector2 relativeVelocity = p1.velocity.subtract(p2.velocity);
                    double velAlongNormal = relativeVelocity.dot(normal);

                    if (velAlongNormal > 0) continue;

                    double e = Math.min(p1.RESTITUTION, p2.RESTITUTION);
                    double s = -(1 + e) * velAlongNormal;
                    s /= (1 / p1.mass + 1 / p2.mass);

                    Vector2 impulse = normal.scale(s);
                    p1.velocity.addSelf(impulse.scale(1 / p1.mass));
                    p2.velocity.addSelf(impulse.scale(-1 / p2.mass));
            }
        }
    }
    }

    public void spawnNewParticle(int width) {
        Random rand = new Random();
        int randomX = rand.nextInt(width - 80) + 40;
        int randomRadius = rand.nextInt(16) + 15;
        
        Particle newP = new Particle(randomX, 50, randomRadius);
        newP.gravityForce = currentGravity;
        particles.add(newP);
    }

    public void setAllGravity(double newGravity) {
        this.currentGravity = newGravity;
        for (Particle p : particles) {
            p.gravityForce = newGravity;
        }
    }

    public void setAllRadius(int newRadius) {
        for (Particle p : particles) {
            p.radius = newRadius;
        }
    }

    public void setAllMass(double newMass) {
        for (Particle p : particles) {
            p.mass = newMass;
        }
    }
}