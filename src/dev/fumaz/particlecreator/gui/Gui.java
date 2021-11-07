package dev.fumaz.particlecreator.gui;

import dev.fumaz.particlecreator.particle.Particle;
import dev.fumaz.particlecreator.particle.ParticleType;
import dev.fumaz.particlecreator.template.Cape;
import dev.fumaz.particlecreator.template.CrescentWings;
import dev.fumaz.particlecreator.template.DragonWings;
import dev.fumaz.particlecreator.template.Template;
import dev.fumaz.particlecreator.template.Wings;
import dev.fumaz.particlecreator.util.Exports;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Gui {

    public final static double VERSION = 1.0;

    private final static int WIDTH = 19;
    private final static int HEIGHT = 13;
    private final static int PIXEL_SIZE = 30;
    private final static String CHARSET = "A B C D E F G H I J K L M N P Q R S T U V W X Y Z a b c d e f g h i j k l m n p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9 ~ - = + < > . , { } ; ' : `";

    private final Particle[][] pixels = new Particle[HEIGHT][WIDTH];
    private final List<Template> templates = Arrays.asList(new Cape(), new Wings(), new CrescentWings(), new DragonWings());
    private Particle particle = new Particle(ParticleType.REDSTONE, Color.BLACK);
    private boolean running = true;

    public void show() {
        JFrame frame = new JFrame("Particle Creator");
        frame.setSize(WIDTH * PIXEL_SIZE, HEIGHT * PIXEL_SIZE + 80);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setFocusable(true);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() - 80));

        JPanel buttons = new JPanel();
        buttons.setPreferredSize(new Dimension(frame.getWidth(), 50));

        GridLayout layout = new GridLayout(1, 5);
        buttons.setLayout(layout);

        JButton particleButton = new JButton("Particle");
        particleButton.addActionListener(e -> {
            ParticleType type = (ParticleType) JOptionPane.showInputDialog(frame, "Please note only REDSTONE particle can have colors.\nColors selected for other particles are purely aesthetic.", "Choose a particle", JOptionPane.INFORMATION_MESSAGE, null, ParticleType.values(), particle.getType());

            if (type == null) {
                return;
            }

            Color color = JColorChooser.showDialog(frame, "Choose a color", particle.getColor());

            particle = new Particle(type, color);
        });

        JButton colorButton = new JButton("Color");
        colorButton.addActionListener(e -> {
            particle = new Particle(ParticleType.REDSTONE, JColorChooser.showDialog(frame, "Choose a color", particle.getColor()));
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Save File");
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.setFileHidingEnabled(true);
            chooser.setFileFilter(new FileNameExtensionFilter("YAML File", "yml"));

            int option = chooser.showSaveDialog(frame);

            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            if (!file.getName().endsWith(".yml")) {
                file = new File(file.getAbsolutePath() + ".yml");
            }

            try {
                Exports.save(pixels, getMappings(), file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while saving the file!\nPlease send this to fumaz:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton openButton = new JButton("Open");
        openButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Open File");
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setFileHidingEnabled(true);
            chooser.setFileFilter(new FileNameExtensionFilter("YAML File", "yml"));

            int option = chooser.showOpenDialog(frame);

            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            try {
                Exports.open(pixels, file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while opening the file!\nPlease send this to fumaz:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton templateButton = new JButton("Template");
        templateButton.addActionListener(e -> {
            Template template = (Template) JOptionPane.showInputDialog(frame, null, "Choose Template", JOptionPane.INFORMATION_MESSAGE, null, templates.toArray(), templates.get(0));

            if (template == null) {
                return;
            }

            template.load(pixels);
        });

        buttons.add(particleButton);
        buttons.add(colorButton);
        buttons.add(saveButton);
        buttons.add(openButton);
        buttons.add(templateButton);

        frame.add(buttons, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Particle p = particle;

                if (SwingUtilities.isRightMouseButton(e)) {
                    p = null;
                }

                int x = e.getX();
                int y = e.getY();

                int pixelX = x / PIXEL_SIZE;
                int pixelY = y / PIXEL_SIZE;

                if (pixelX < 0 || pixelY < 0 || pixelX >= WIDTH || pixelY >= HEIGHT) {
                    return;
                }

                pixels[pixelY][pixelX] = p;
            }
        });

        panel.setFocusable(true);
        panel.setRequestFocusEnabled(true);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "clear");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK), "help");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), "legend");

        panel.getActionMap().put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        panel.getActionMap().put("help", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Particle Creator " + VERSION + "\n" +
                        "Made with <3 by Fumaz\n\n" +
                        "CTRL + H - Shows this message\n" +
                        "CTRL + C - Clears the board\n" +
                        "Left Click - Colors a pixel\n" +
                        "Right Click - Erases a pixel", "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.getActionMap().put("legend", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "TODO", "TODO", JOptionPane.WARNING_MESSAGE);
            }
        });

        clear();
        panel.requestFocus();

        new Thread(() -> {
            while (running && frame.isVisible()) {
                update(panel.getGraphics());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update(Graphics graphics) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Particle particle = pixels[y][x];
                Color color = particle == null ? null : particle.getColor();
                boolean empty = particle == null;

                if (empty) {
                    color = Color.WHITE; // todo probably change this
                }

                graphics.setColor(color);
                graphics.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                graphics.setColor(color == Color.black ? Color.white : Color.black);
                graphics.drawRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }
        }
    }

    private void clear() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                pixels[y][x] = null;
            }
        }
    }

    private Set<Particle> getParticles() {
        Set<Particle> particles = new HashSet<>();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Particle particle = pixels[y][x];

                if (particle == null) {
                    continue;
                }

                particles.add(particle);
            }
        }

        return particles;
    }

    private Map<Color, Particle> getLegend() {
        Map<Color, Particle> legend = new HashMap<>();

        getParticles().forEach(particle -> {
            legend.put(particle.getColor(), particle);
        });

        return legend;
    }

    private Map<Particle, Integer> getParticlesAmounts() {
        Map<Particle, Integer> particles = new HashMap<>();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Particle particle = pixels[i][j];

                if (particle == null) {
                    continue;
                }

                particles.put(particle, particles.getOrDefault(particle, 0) + 1);
            }
        }

        return particles;
    }

    private Map<Character, Particle> getMappings() {
        Map<Character, Particle> mappings = new HashMap<>();
        Iterator<Character> characters = Arrays.stream(CHARSET.split(" "))
                .map(s -> s.charAt(0))
                .iterator();

        mappings.put('O', null);

        getParticles().forEach(particle -> mappings.put(characters.next(), particle));

        return mappings;
    }

}
