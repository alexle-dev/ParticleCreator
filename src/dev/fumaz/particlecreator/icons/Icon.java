package dev.fumaz.particlecreator.icons;

import dev.fumaz.particlecreator.Main;

import javax.swing.*;

public class Icon {

    protected final static String ICONS_PATH = "/dev/fumaz/particlecreator/icons/types/";

    private final String name;
    private final String type;

    public Icon(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ImageIcon getIcon() {
        return new ImageIcon(Main.class.getResource(ICONS_PATH + name + "." + type));
    }
}
