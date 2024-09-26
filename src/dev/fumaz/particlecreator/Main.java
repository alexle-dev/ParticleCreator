package dev.fumaz.particlecreator;

public class Main {

    private static ParticleCreatorManager particleCreatorManager;

    public static void main(String[] args) {
        particleCreatorManager = new ParticleCreatorManager();
    }

    public static ParticleCreatorManager getParticleCreatorManager() {
        return particleCreatorManager;
    }

}
