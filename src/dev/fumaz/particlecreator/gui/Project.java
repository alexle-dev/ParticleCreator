package dev.fumaz.particlecreator.gui;

import dev.fumaz.particlecreator.Coordinate;
import dev.fumaz.particlecreator.gui.button.Button;
import dev.fumaz.particlecreator.gui.submenu.dropdown.ProjectDropdownMenu;
import dev.fumaz.particlecreator.icons.Icons;
import dev.fumaz.particlecreator.particle.Particle;
import dev.fumaz.particlecreator.particle.ParticleType;
import dev.fumaz.particlecreator.template.Cape;
import dev.fumaz.particlecreator.template.CrescentWings;
import dev.fumaz.particlecreator.template.DragonWings;
import dev.fumaz.particlecreator.template.Template;
import dev.fumaz.particlecreator.template.Wings;
import dev.fumaz.particlecreator.util.SizedStack;
import dev.fumaz.particlecreator.util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class Project {

    public final static double VERSION = 2.0;

    private final static int WIDTH = 19;
    private final static int HEIGHT = 13;
    private final static int PIXEL_SIZE = 30;
    private final static String CHARSET = "A B C D E F G H I J K L M N P Q R S T U V W X Y Z a b c d e f g h i j k l m n p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9 ~ - = + < > . , { } ; ' : `";
    private final static String UNTITLED = "Untitled";

    private final Particle[][] pixels = new Particle[HEIGHT][WIDTH];
    private final List<Template> templates = Arrays.asList(new Cape(), new Wings(), new CrescentWings(), new DragonWings());
    private final SizedStack<Action> undo = new SizedStack<>(250);
    private final SizedStack<Action> redo = new SizedStack<>(250);
    private final JColorChooser colorChooser = new JColorChooser(Color.BLACK);
    private final Icons icons = new Icons();

    private String projectName;
    private JFrame frame;
    private Particle particle = new Particle(ParticleType.DUST, Color.BLACK);
    private Color background = Theme.UNCOLORED_PIXEL_COLOR;
    private boolean running = true;
    private Image offScreenImage;

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public Project() {
        this(UNTITLED);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;

        if (frame != null) {
            frame.setTitle(getFrameTitle());
        }
    }

    private String getFrameTitle() {
        return "Particle Creator v" + VERSION + " - " + projectName;
    }

    public void show() {
        frame = new JFrame(getFrameTitle());
        frame.setIconImage(icons.getLogo().getIcon().getImage());
        frame.setSize(WIDTH * PIXEL_SIZE, HEIGHT * PIXEL_SIZE + 80);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setFocusable(true);
        frame.setBackground(Theme.BACKGROUND_COLOR);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setPreferredSize(new Dimension(frame.getWidth(), 50));
        buttons.setBackground(Theme.BACKGROUND_COLOR);

        GridLayout layout = new GridLayout(1, 6);
        buttons.setLayout(layout);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button projectButton = new Button("Project", icons.getMenu());
        projectButton.addActionListener(e -> {
            ProjectDropdownMenu dropdownMenu = new ProjectDropdownMenu(this, frame, projectButton, icons);
            dropdownMenu.show();
        });

        Button particleButton = new Button("Particle", icons.getParticle());
        particleButton.addActionListener(e -> {
            ParticleType[] colorable = ParticleType.getColorable();
            StringJoiner colorableParticles = new StringJoiner(", ");
            for (ParticleType type : colorable) {
                colorableParticles.add(type.getFormattedName());
            }

            String type = (String) JOptionPane.showInputDialog(frame, "Please note only " + colorableParticles + " particles can have colors.\nColors selected for other particles are purely aesthetic.", "Choose a particle", JOptionPane.INFORMATION_MESSAGE, null, ParticleType.getFormattedNames(), particle.getType().getFormattedName());

            if (type == null) {
                return;
            }

            Color color = showColorPicker(frame);

            particle = new Particle(ParticleType.fromFormattedName(type), color);
        });

        Button colorButton = new Button("Color", icons.getColor());
        colorButton.addActionListener(e -> {
            particle = new Particle(ParticleType.DUST, showColorPicker(frame));
        });

        Button templateButton = new Button("Templates", icons.getTemplate());
        templateButton.addActionListener(e -> {
            Template template = (Template) JOptionPane.showInputDialog(frame, null, "Choose Template", JOptionPane.INFORMATION_MESSAGE, null, templates.toArray(), templates.get(0));

            if (template == null) {
                return;
            }

            template.load(pixels);
        });

        buttons.add(projectButton);
        buttons.add(particleButton);
        buttons.add(colorButton);
        buttons.add(templateButton);

        frame.add(buttons, BorderLayout.NORTH);
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.repaint();

                int width = (int) (frame.getWidth() * 0.5);
                buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, frame.getWidth() - width));
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Particle p = particle;

                if (SwingUtilities.isRightMouseButton(e)) {
                    p = null;
                }

                Dimension panelSize = panel.getSize();

                int effectivePixelSizeX = panelSize.width / WIDTH;
                int effectivePixelSizeY = panelSize.height / HEIGHT;

                int x = e.getX();
                int y = e.getY();

                int pixelX = x / effectivePixelSizeX;
                int pixelY = y / effectivePixelSizeY;

                if (pixelX < 0 || pixelY < 0 || pixelX >= WIDTH || pixelY >= HEIGHT) {
                    return;
                }

                if (pixels[pixelY][pixelX] == null) {
                    if (p == null) {
                        return;
                    }
                } else if (pixels[pixelY][pixelX].equals(p)) {
                    return;
                }

                undo.push(new Action(new Coordinate(pixelX, pixelY), pixels[pixelY][pixelX], p));
                pixels[pixelY][pixelX] = p;
            }
        });

        panel.setFocusable(true);
        panel.setRequestFocusEnabled(true);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "clear");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK), "help");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), "legend");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undo");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "redo");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK), "background");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J, KeyEvent.CTRL_DOWN_MASK), "pick");

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
                        "CTRL + B - Change background color\n" +
                        "CTRL + J - Pick color under cursor\n" +
                        "CTRL + Z - Undo your last action\n" +
                        "CTRL + Shift + Z - Redo your last action\n" +
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

        panel.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undo.isEmpty()) {
                    return;
                }

                Action action = undo.pop();
                action.undo(pixels);
                redo.push(action);
            }
        });

        panel.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redo.isEmpty()) {
                    return;
                }

                Action action = redo.pop();
                action.redo(pixels);
                undo.push(action);
            }
        });

        panel.getActionMap().put("background", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(frame, "Choose a background color", background);

                if (color == null) {
                    return;
                }

                background = color;
            }
        });

        panel.getActionMap().put("pick", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point point = MouseInfo.getPointerInfo().getLocation();

                int x = point.x - frame.getLocation().x;
                int y = point.y - frame.getLocation().y;

                int pixelX = x / PIXEL_SIZE;
                int pixelY = (y - 80) / PIXEL_SIZE;

                if (pixelX < 0 || pixelY < 0 || pixelX >= WIDTH || pixelY >= HEIGHT) {
                    return;
                }

                Particle particle = pixels[pixelY][pixelX];

                if (particle == null) {
                    return;
                }

                Project.this.particle = particle;
            }
        });

        clear();
        panel.requestFocus();

        new Thread(() -> {
            while (running && frame.isVisible()) {
                update(panel.getGraphics(), panel);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update(Graphics graphics, JPanel panel) {
        int panelWidth = panel.getWidth();
        int panelHeight = panel.getHeight();
        int pixelWidth = panelWidth / WIDTH;
        int pixelHeight = panelHeight / HEIGHT;

        if (offScreenImage == null || offScreenImage.getWidth(null) != panelWidth || offScreenImage.getHeight(null) != panelHeight) {
            offScreenImage = panel.createImage(panelWidth, panelHeight);
        }

        Graphics offScreenGraphics = offScreenImage.getGraphics();

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Particle particle = pixels[y][x];
                Color color = particle == null ? background : particle.getColor();
                offScreenGraphics.setColor(color);
                offScreenGraphics.fillRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);
                offScreenGraphics.setColor(color == Color.black ? Theme.UNCOLORED_PIXEL_COLOR : Color.black);
                offScreenGraphics.drawRect(x * pixelWidth, y * pixelHeight, pixelWidth, pixelHeight);

                if (particle != null) {
                    offScreenGraphics.drawString(particle.getType().getFormattedName(), x * pixelWidth + 5, y * pixelHeight + 15);
                }
            }
        }

        graphics.drawImage(offScreenImage, 0, 0, panel);
    }

    public void clear() {
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

    public Map<Character, Particle> getMappings() {
        Map<Character, Particle> mappings = new HashMap<>();
        Iterator<Character> characters = Arrays.stream(CHARSET.split(" "))
                .map(s -> s.charAt(0))
                .iterator();

        mappings.put('O', null);

        getParticles().forEach(particle -> mappings.put(characters.next(), particle));

        return mappings;
    }

    public Particle[][] getPixels() {
        return pixels;
    }

    private Color showColorPicker(JFrame frame) {
        colorChooser.setColor(particle.getColor());
        JDialog dialog = JColorChooser.createDialog(frame, "Choose a color", true, colorChooser, null, null);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                ((Window) e.getComponent()).dispose();
            }
        });

        dialog.show();

        return colorChooser.getColor();
    }

    public String getProjectName() {
        return projectName;
    }

    public int getFrameWidth() {
        return frame.getWidth();
    }

    public void setFrameWidth(int width) {
        frame.setSize(width, frame.getHeight());
    }

    public int getFrameHeight() {
        return frame.getHeight();
    }

    public void setFrameHeight(int height) {
        frame.setSize(frame.getWidth(), height);
    }

    public void close() {
        frame.dispose();
        running = false;
    }
}
