import java.awt.Graphics2D;
import java.io.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;

public class SimulationApp extends JPanel {

    private final Particle particle;  


    public SimulationApp() {
        this.particle = new Particle(100, 50, 20);

        Timer timer = new Timer(16, e -> {
            particle.integrate(0.016);
            repaint();
        });
        timer.start();
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
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        int x = (int) particle.position.x;
        int y = (int) particle.position.y;
        int radius = particle.radius; 
        g2.fillOval(x, y, radius *2,radius*2);
    }
}