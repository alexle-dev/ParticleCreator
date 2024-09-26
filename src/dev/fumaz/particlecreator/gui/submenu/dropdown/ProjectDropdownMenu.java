package dev.fumaz.particlecreator.gui.submenu.dropdown;

import dev.fumaz.particlecreator.Main;
import dev.fumaz.particlecreator.gui.Project;
import dev.fumaz.particlecreator.gui.button.Button;
import dev.fumaz.particlecreator.gui.button.DropdownMenuButton;
import dev.fumaz.particlecreator.gui.submenu.DropdownMenu;
import dev.fumaz.particlecreator.icons.Icons;
import dev.fumaz.particlecreator.util.Exports;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ProjectDropdownMenu extends DropdownMenu {

    public ProjectDropdownMenu(Project project, JFrame frame, Button parent, Icons icons) {
        super(project, frame, parent, icons);

        add(renameButton());
        addSeparator();
        add(newProjectButton());
        add(saveButton());
        add(loadButton());
        addSeparator();
        add(exitButton());
    }

    private DropdownMenuButton newProjectButton() {
        DropdownMenuButton newProject = new DropdownMenuButton("New Project", getIcons().getNew());
        newProject.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(getFrame(), "Are you sure you want to create a new project?\nThis will delete all unsaved progress!", "New Project", JOptionPane.YES_NO_OPTION);

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            String name = JOptionPane.showInputDialog(getFrame(), "Enter a name for the new project", "New Project", JOptionPane.QUESTION_MESSAGE);

            if (name == null) {
                return;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(getFrame(), "The project name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Main.getParticleCreatorManager().switchProject(new Project(name));
        });
        newProject.setHorizontalAlignment(SwingConstants.LEFT);

        return newProject;
    }

    private DropdownMenuButton saveButton() {
        DropdownMenuButton save = new DropdownMenuButton("Save Project", getIcons().getSave());
        save.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Save Project File");
            chooser.setDialogType(JFileChooser.SAVE_DIALOG);
            chooser.setFileHidingEnabled(true);
            chooser.setFileFilter(new FileNameExtensionFilter("YAML File", "yml"));
            chooser.setSelectedFile(new File(getProject().getProjectName() + ".yml"));

            int option = chooser.showSaveDialog(getFrame());

            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            if (!file.getName().endsWith(".yml")) {
                file = new File(file.getAbsolutePath() + ".yml");
            }

            try {
                Exports.save(getProject().getPixels(), getProject().getMappings(), file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getFrame(), "An error occurred while saving the file!\nPlease send this to fumaz:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        save.setHorizontalAlignment(SwingConstants.LEFT);

        return save;
    }

    private DropdownMenuButton loadButton() {
        DropdownMenuButton load = new DropdownMenuButton("Open Project", getIcons().getLoad());
        load.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setDialogTitle("Open Project File");
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setFileHidingEnabled(true);
            chooser.setFileFilter(new FileNameExtensionFilter("YAML File", "yml"));

            int option = chooser.showOpenDialog(getFrame());

            if (option != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();

            try {
                Exports.open(getProject().getPixels(), file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getFrame(), "An error occurred while opening the file!\nPlease send this to fumaz:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        load.setHorizontalAlignment(SwingConstants.LEFT);

        return load;
    }

    private DropdownMenuButton renameButton() {
        DropdownMenuButton rename = new DropdownMenuButton("Rename Project", getIcons().getRename());
        rename.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(getFrame(), "Enter a new name for the project", "Rename Project", JOptionPane.QUESTION_MESSAGE);

            if (name == null) {
                return;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(getFrame(), "The project name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            getProject().setProjectName(name);
        });
        rename.setHorizontalAlignment(SwingConstants.LEFT);

        return rename;
    }

    private DropdownMenuButton exitButton() {
        DropdownMenuButton exit = new DropdownMenuButton("Exit", getIcons().getExit());
        exit.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(getFrame(), "Are you sure you want to exit?\nThis will delete all unsaved progress!", "Exit", JOptionPane.YES_NO_OPTION);

            if (option != JOptionPane.YES_OPTION) {
                return;
            }

            Main.getParticleCreatorManager().exit();
        });
        exit.setHorizontalAlignment(SwingConstants.LEFT);

        return exit;
    }
}
