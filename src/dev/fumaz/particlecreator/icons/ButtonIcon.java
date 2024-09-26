package dev.fumaz.particlecreator.icons;

import javax.swing.*;
import java.awt.*;

public class ButtonIcon extends Icon {

    private final static int BUTTON_ICON_WIDTH = 16;
    private final static int BUTTON_ICON_HEIGHT = 16;

    public ButtonIcon(String name, String type) {
        super(name, type);
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(super.getIcon().getImage().getScaledInstance(BUTTON_ICON_WIDTH, BUTTON_ICON_HEIGHT, Image.SCALE_SMOOTH));
    }
}
