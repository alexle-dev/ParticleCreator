package dev.fumaz.particlecreator;

import dev.fumaz.particlecreator.gui.Project;

public class ParticleCreatorManager {

    private Project activeProject;

    public ParticleCreatorManager() {
        setActiveProject(new Project());
    }

    public Project getActiveProject() {
        return activeProject;
    }

    public void setActiveProject(Project activeProject) {
        this.activeProject = activeProject;
        this.activeProject.show();
    }

    public void setActiveProject(Project activeProject, int previousWidth, int previousHeight) {
        this.activeProject = activeProject;
        this.activeProject.show();
        this.activeProject.setFrameWidth(previousWidth);
        this.activeProject.setFrameHeight(previousHeight);
    }

    public void closeActiveProject() {
        this.activeProject.close();
        this.activeProject = null;
    }

    public void switchProject(Project project) {
        Project previousProject = getActiveProject();
        int previousWidth = previousProject.getFrameWidth();
        int previousHeight = previousProject.getFrameHeight();

        closeActiveProject();
        setActiveProject(project, previousWidth, previousHeight);
    }

    public void exit() {
        System.exit(0);
    }
}
