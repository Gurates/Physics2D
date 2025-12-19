import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Math;

public class SimulationApp extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private final PhysicsEngine engine;
    private final int FLOOR_Y = 540;
    
    // UI
    private JTextField gravityField;
    private JTextField radiusField;
    private JTextField massField;
    
    // Mouse etkileşimi
    private boolean isDragging = false;
    private int offsetX, offsetY;
    private Particle draggedParticle = null;

    public SimulationApp() {
        this.engine = new PhysicsEngine();

        this.setLayout(new BorderLayout()); 
        
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        JPanel controlPanel = createControlPanel();
        this.add(controlPanel, BorderLayout.EAST);
        
        this.add(getSimulationArea(), BorderLayout.CENTER);


        Particle initialParticle = engine.getParticles().get(0);
        gravityField.setText(String.valueOf(initialParticle.gravityForce));
        radiusField.setText(String.valueOf(initialParticle.radius));
        massField.setText(String.valueOf(initialParticle.mass));

        Timer timer = new Timer(16, this);
        timer.start();
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); 
        panel.setPreferredSize(new Dimension(220, getHeight())); 

        JLabel title = new JLabel("CONTROLS");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // gravity
        JPanel gravityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gravityField = new JTextField(8);
        gravityPanel.add(new JLabel("Gravity (N):"));
        gravityPanel.add(gravityField);
        panel.add(gravityPanel);
        
        // Radius
        JPanel radiusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radiusField = new JTextField(8);
        radiusPanel.add(new JLabel("Radius (px):"));
        radiusPanel.add(radiusField);
        panel.add(radiusPanel);
        
        // Mass
        JPanel massPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        massField = new JTextField(8);
        massPanel.add(new JLabel("Mass (kg):"));
        massPanel.add(massField);
        panel.add(massPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 10))); 
        
        // Apply Button
        JButton applyButton = new JButton("Apply All Parameters");
        applyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyButton.addActionListener(e -> {
            applyNewGravity();
            applyNewRadius();
            applyNewMass();
        });
        panel.add(applyButton);

        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Spawn Object Button
        JButton spawnObjecButton = new JButton("Spawn New Object");
        spawnObjecButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnObjecButton.addActionListener(e -> {
            engine.spawnNewParticle(SimulationApp.this.getWidth());
        });
        panel.add(spawnObjecButton);
        
        return panel;
    }
    
    private JComponent getSimulationArea() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                SimulationApp.this.paintComponent(g);
            }
        };
    }

    private void applyNewGravity() {
        try {
            double newGravity = Double.parseDouble(gravityField.getText());
            engine.setAllGravity(newGravity);
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
            engine.setAllRadius(newRadius);
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
            engine.setAllMass(newMass);
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Invalid mass value", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modular Physics Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600); 
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
        
        for (Particle p : engine.getParticles()) {
            g2.setColor(Color.BLUE); 
            
            int x = (int) (p.position.x - p.radius);
            int y = (int) (p.position.y - p.radius);
            int size = (int) (p.radius * 2);
            g2.fillOval(x, y, size, size);
        }

        g2.setColor(Color.RED);
        g2.fillRect(0, FLOOR_Y, getWidth(), 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double deltaTime = 0.016;
        engine.update(deltaTime);
        repaint();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        draggedParticle = null; 

        for (Particle p : engine.getParticles()) {
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
            engine.spawnNewParticle(getWidth());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}