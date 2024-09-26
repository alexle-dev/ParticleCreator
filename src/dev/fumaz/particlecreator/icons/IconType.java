package dev.fumaz.particlecreator.icons;

public enum IconType {

    NORMAL,
    BUTTON;

    public Icon getIcon(String name, String type) {
        return switch (this) {
            case NORMAL -> new Icon(name, type);
            case BUTTON -> new ButtonIcon(name, type);
        };
    }
}
