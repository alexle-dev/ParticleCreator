package dev.fumaz.particlecreator.icons;

public class Icons {

    private final Icon LOGO = getIcon("logo", "png", IconType.NORMAL);
    private final Icon PARTICLE = getIcon("particle", "png", IconType.BUTTON);
    private final Icon COLOR = getIcon("color", "png", IconType.BUTTON);
    private final Icon SAVE = getIcon("save", "png", IconType.BUTTON);
    private final Icon LOAD = getIcon("load", "png", IconType.BUTTON);
    private final Icon TEMPLATE = getIcon("template", "png", IconType.BUTTON);
    private final Icon NEW = getIcon("new", "png", IconType.BUTTON);
    private final Icon EXIT = getIcon("exit", "png", IconType.BUTTON);
    private final Icon MENU = getIcon("menu", "png", IconType.BUTTON);
    private final Icon RENAME = getIcon("rename", "png", IconType.BUTTON);

    public Icon getIcon(String name, String type, IconType iconType) {
        return iconType.getIcon(name, type);
    }

    public Icon getLogo() {
        return LOGO;
    }

    public Icon getParticle() {
        return PARTICLE;
    }

    public Icon getColor() {
        return COLOR;
    }

    public Icon getSave() {
        return SAVE;
    }

    public Icon getLoad() {
        return LOAD;
    }

    public Icon getTemplate() {
        return TEMPLATE;
    }

    public Icon getNew() {
        return NEW;
    }

    public Icon getExit() {
        return EXIT;
    }

    public Icon getMenu() {
        return MENU;
    }

    public Icon getRename() {
        return RENAME;
    }
}
