package dev.fumaz.particlecreator.gui.submenu;

import dev.fumaz.particlecreator.gui.Project;
import dev.fumaz.particlecreator.gui.button.Button;
import dev.fumaz.particlecreator.icons.Icons;
import dev.fumaz.particlecreator.util.Theme;

import javax.swing.*;
import java.awt.*;

public class DropdownMenu extends JPopupMenu {

    private final Project project;
    private final JFrame frame;
    private final Button parent;
    private final Icons icons;

    public DropdownMenu(Project project, JFrame frame, Button parent, Icons icons) {
        this.project = project;
        this.frame = frame;
        this.parent = parent;
        this.icons = icons;

        setLightWeightPopupEnabled(false);
        setBackground(Theme.DROPDOWN_MENU_BACKGROUND_COLOR);

        setBorder(BorderFactory.createLineBorder(Theme.BUTTON_HOVER_COLOR));
    }

    @Override
    public void paintComponent(final Graphics g) {
        g.setColor(Theme.BACKGROUND_COLOR);
        g.fillRect(0,0, getWidth(), getHeight());
    }

    public Project getProject() {
        return project;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Icons getIcons() {
        return icons;
    }

    public Button getParentButton() {
        return parent;
    }

    public Dimension getSize() {
        return new Dimension(parent.getWidth(), getPreferredSize().height);
    }

    @Override
    public void show() {
        setPreferredSize(getSize());

        show(parent, 0, parent.getHeight());

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }
}
