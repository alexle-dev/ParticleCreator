package dev.fumaz.particlecreator.gui.button;

import dev.fumaz.particlecreator.gui.fonts.DropdownButtonFont;
import dev.fumaz.particlecreator.icons.Icon;
import dev.fumaz.particlecreator.util.Theme;

import javax.swing.*;

public class DropdownMenuButton extends JMenuItem {

    public DropdownMenuButton(String text, Icon icon) {
        super(text, icon.getIcon());

        setFont(new DropdownButtonFont());

        setBorder(null);

        setBackground(Theme.BACKGROUND_COLOR);
        setForeground(Theme.BUTTON_TEXT_COLOR);
    }
}
