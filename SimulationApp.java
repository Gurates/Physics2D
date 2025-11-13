import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class SimulationApp extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private final ArrayList<Particle> particles;
    private final int FLOOR_Y = 540;
    
    // UI
    private final JTextField gravityField;
    private final JTextField radiusField;
    private final JTextField massField;
    
    private boolean isDragging = false;
    private int offsetX, offsetY;
    private Particle draggedParticle = null;
    public SimulationApp() {
        
        this.particles = new ArrayList<>();
        this.particles.add(new Particle(100, 50, 20));

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        // UI
        setLayout(new FlowLayout());
        
        // gravity
        gravityField = new JTextField(5);
        gravityField.setText(String.valueOf(this.particles.get(0).gravityForce));
        add(new JLabel("Gravity:"));
        add(gravityField);
        
        // Radius
        radiusField = new JTextField(5);
        radiusField.setText(String.valueOf(this.particles.get(0).radius));
        add(new JLabel("Radius:"));
        add(radiusField);
        
        // Mass
        massField = new JTextField(5);
        massField.setText(String.valueOf(this.particles.get(0).mass));
        add(new JLabel("Mass:"));
        add(massField);

        // apply button
        JButton applyButton = new JButton("Apply");
        
        // Spawn Object Button
        JButton spawnObjecButton = new JButton("Spawn Object");
        add(spawnObjecButton);
        spawnObjecButton.addActionListener(e -> {
            spawnNewParticle();
        });

        add(applyButton);

        applyButton.addActionListener(e -> {
            applyNewGravity();
            applyNewRadius();
            applyNewMass();
        });

        Timer timer = new Timer(16, this);
        timer.start();
    }

    private void applyNewGravity() {
        try {
            double newGravity = Double.parseDouble(gravityField.getText());
            for(Particle p : particles) {
                p.gravityForce = newGravity;
            }
            repaint();
        } 
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid gravity value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyNewRadius(){
        try{
            int newRadius = Integer.parseInt(radiusField.getText());
            if(newRadius <= 0){
                JOptionPane.showMessageDialog(this, "Radius must be a positive value", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for(Particle p : particles) {
                p.radius = newRadius;
            }
            repaint();
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid radius value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyNewMass(){
        try{
            double newMass = Double.parseDouble(massField.getText());
            if(newMass <= 0){
                JOptionPane.showMessageDialog(this, "Mass must be a positive value", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for(Particle p : particles) {
                p.mass = newMass;
            }
            repaint();
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid mass value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Physics Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.getContentPane().add(new SimulationApp());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.black); 
        g.fillRect(0, 0, getWidth(), getHeight()); 
        Graphics2D g2 = (Graphics2D) g;
        
        for (Particle p : this.particles) {
            g2.setColor(Color.BLUE); 
            
            int x = (int) (p.position.x - p.radius);
            int y = (int) (p.position.y - p.radius);
            int size = p.radius * 2;
            g2.fillOval(x, y, size, size);
        }

        g2.setColor(Color.RED);
        g2.fillRect(0, FLOOR_Y, getWidth(), 30);
    }

    private void spawnNewParticle(){
        Random rand = new Random();
        int randomX = rand.nextInt(getWidth() - 40) + 20;
        int randomRadius = rand.nextInt(16) + 15;
        
        Particle newp = new Particle(randomX, 50, randomRadius);
        this.particles.add(newp);
        repaint();  
    }
    private void Collision(Particle p){ 
        double bottomEdge = p.position.y + p.radius;
        
        if(bottomEdge >= FLOOR_Y){
            p.position.y = FLOOR_Y - p.radius;

            //sekme
            p.velocity.y *= -p.RESTITUTION;
            p.gravityForce = 500;
        }

        if (Math.abs(p.position.y - FLOOR_Y) < 5.0) {
            p.velocity.y = 0;
            p.gravityForce = 0;
            p.position.y = FLOOR_Y - p.radius;
        }

    }

    // yere olan uzaklık
    private double getDistanceToFloor() {
        final int FLOOR_Y = 540; 
        if (particles.isEmpty()) return 0;
        double bottomEdgeY = particles.get(0).position.y + particles.get(0).radius;
        return FLOOR_Y - bottomEdgeY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double deltaTime = 0.016;
        
        for (Particle p : this.particles) {
            p.integrate(deltaTime);
            Collision(p);
        }
        
        repaint();
        if (!particles.isEmpty()) {
            double speed = particles.get(0).velocity.magnitude();
            System.out.println("Particle Speed: " + speed + " Distance to Floor: " + getDistanceToFloor());
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        draggedParticle = null; 

        for (Particle p : particles) {
            double distance = Math.sqrt(
                Math.pow(e.getX() - p.position.x, 2) + 
                Math.pow(e.getY() - p.position.y, 2)
            );
            
            if (distance <= p.radius) {
                isDragging = true;
                draggedParticle = p;
                offsetX = (int) (e.getX() - p.position.x);
                offsetY = (int) (e.getY() - p.position.y);
                
                p.velocity.x = 0;
                p.velocity.y = 0;
                p.gravityForce = 0;
                break; 
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging && draggedParticle != null) {
            draggedParticle.position.x = e.getX() - offsetX;
            draggedParticle.position.y = e.getY() - offsetY;
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging && draggedParticle != null) {
            isDragging = false;
            applyNewGravity(); 
            draggedParticle = null; 
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            spawnNewParticle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}

}