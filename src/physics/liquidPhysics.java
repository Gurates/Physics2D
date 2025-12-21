import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;

public class liquidPhysics {
    
    private final int LEFT_WALL = 0;
    private final int RIGHT_WALL = 980;
    private final int TOP_WALL = 0;
    
    private double currentGravity = 500.0;
    private final List<liquidParticles> particles;

    private double interactionRadius = 30.0;
    private double restDensity = 0.5;        // intensity
    private double stiffness = 150.0;        // compressibility
    private double viscosity = 0.8;          // friction

    private Map<Integer, List<liquidParticles>> grid = new HashMap<>();
    private int gridSize = (int) interactionRadius;

    public liquidPhysics() {
        this.particles = new ArrayList<>();
    }

    public void spawnLiquid(int width) {
        Random rand = new Random();
        int randomX = rand.nextInt(400) + 300; 
        int randomY = rand.nextInt(200) + 100;
        
        liquidParticles newP = new liquidParticles(randomX, randomY, 2);
        newP.gravityForce = currentGravity;
        newP.mass = 0.5;
        particles.add(newP);
    }

    public List<liquidParticles> getParticles() {
        return particles;
    }
    
    public List<liquidParticles> getLParticles() {
        return particles;
    }

    public void update(double deltaTime) {
        updateGrid();
        calculateDensity();
        calculateForces();
        
        for (liquidParticles p : particles) {
            wallCollision(p);
        }
    }

    private void updateGrid() {
        grid.clear();
        for (liquidParticles p : particles) {
            int cellX = (int) (p.position.x / gridSize);
            int cellY = (int) (p.position.y / gridSize);
            int key = cellX * 1000 + cellY;

            grid.computeIfAbsent(key, k -> new ArrayList<>()).add(p);
        }
    }

    private List<liquidParticles> getNeighbors(liquidParticles p) {
        List<liquidParticles> neighbors = new ArrayList<>();
        int cellX = (int) (p.position.x / gridSize);
        int cellY = (int) (p.position.y / gridSize);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int key = (cellX + x) * 1000 + (cellY + y);
                if (grid.containsKey(key)) {
                    neighbors.addAll(grid.get(key));
                }
            }
        }
        return neighbors;
    }

    private void calculateDensity() {
        for (liquidParticles p : particles) {
            p.density = 0;
            List<liquidParticles> neighbors = getNeighbors(p);

            for (liquidParticles n : neighbors) {
                if (p == n) continue;
                double dist = getDistance(p, n);
                if (dist < interactionRadius) {

                    double q = 1 - (dist / interactionRadius);
                    p.density += q * q; 
                }
            }
            // calculate pressure based on density
            p.pressure = stiffness * (p.density - restDensity);
        }
    }

    private void calculateForces() {
        for (liquidParticles p : particles) {
            Vector2 pressureForce = new Vector2(0, 0);
            Vector2 viscosityForce = new Vector2(0, 0);
            
            List<liquidParticles> neighbors = getNeighbors(p);

            for (liquidParticles n : neighbors) {
                if (p == n) continue;
                
                double dist = getDistance(p, n);
                if (dist < interactionRadius && dist > 0.001) {
                    double q = 1 - (dist / interactionRadius);
                    
                    double dx = (n.position.x - p.position.x) / dist;
                    double dy = (n.position.y - p.position.y) / dist;

                    //compression
                    double press = (p.pressure + n.pressure) * 0.5 * q * q;
                    pressureForce.x -= dx * press;
                    pressureForce.y -= dy * press;

                    double velXDiff = n.velocity.x - p.velocity.x;
                    double velYDiff = n.velocity.y - p.velocity.y;
                    
                    viscosityForce.x += velXDiff * viscosity * q;
                    viscosityForce.y += velYDiff * viscosity * q;
                }
            }

            p.velocity.x += pressureForce.x * 0.1;
            p.velocity.y += pressureForce.y * 0.1;
            
            p.velocity.x += viscosityForce.x * 0.1;
            p.velocity.y += viscosityForce.y * 0.1;
        }
    }

    private double getDistance(liquidParticles p1, liquidParticles p2) {
        return Math.sqrt(Math.pow(p1.position.x - p2.position.x, 2) + Math.pow(p1.position.y - p2.position.y, 2));
    }

    private void wallCollision(liquidParticles p) {
        if (p.position.x - p.radius <= LEFT_WALL) {
            p.position.x = LEFT_WALL + p.radius;
            p.velocity.x *= -0.5;
        }
        else if (p.position.x + p.radius >= RIGHT_WALL) {
            p.position.x = RIGHT_WALL - p.radius;
            p.velocity.x *= -0.5;
        }

        if(p.position.y + p.radius <= TOP_WALL) {
            p.position.y = TOP_WALL - p.radius;
            p.velocity.y *= -0.5;
        }
    }
}