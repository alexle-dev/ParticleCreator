package dev.fumaz.particlecreator.gui.button;

import dev.fumaz.particlecreator.gui.fonts.ButtonFont;
import dev.fumaz.particlecreator.icons.Icon;
import dev.fumaz.particlecreator.util.Theme;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private final String text;
    private final Icon icon;

    public Button(String text, Icon icon) {
        this.text = text;
        this.icon = icon;

        setText(text);
        setFont(new ButtonFont());

        setBorder(null);

        setBackground(Theme.BACKGROUND_COLOR);
        setForeground(Theme.BUTTON_TEXT_COLOR);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setBackground(Theme.BUTTON_HOVER_COLOR);
            }

            public void mouseExited(MouseEvent evt) {
                setBackground(Theme.BACKGROUND_COLOR);
            }
        });

        if (icon != null) {
            setIcon(icon.getIcon());
        }
    }

    public Button(String text) {
        this(text, null);
    }
}
