package dev.fumaz.particlecreator.particle;

import java.util.Arrays;

public enum ParticleType {
    ANGRY_VILLAGER,
    ASH,
    BLOCK,
    BLOCK_MARKER,
    BUBBLE,
    BUBBLE_COLUMN_UP,
    BUBBLE_POP,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    CHERRY_LEAVES,
    CLOUD,
    COMPOSTER,
    CRIMSON_SPORE,
    CRIT,
    CURRENT_DOWN,
    DAMAGE_INDICATOR,
    DOLPHIN,
    DRAGON_BREATH,
    DRIPPING_DRIPSTONE_LAVA,
    DRIPPING_DRIPSTONE_WATER,
    DRIPPING_HONEY,
    DRIPPING_LAVA,
    DRIPPING_OBSIDIAN_TEAR,
    DRIPPING_WATER,
    DUST(true),
    DUST_PILLAR,
    DUST_PLUME,
    EFFECT,
    EGG_CRACK,
    ELDER_GUARDIAN,
    ELECTRIC_SPARK,
    ENCHANT,
    ENCHANTED_HIT,
    END_ROD,
    ENTITY_EFFECT(true),
    EXPLOSION,
    EXPLOSION_EMITTER,
    FALLING_DRIPSTONE_LAVA,
    FALLING_DRIPSTONE_WATER,
    FALLING_DUST,
    FALLING_HONEY,
    FALLING_LAVA,
    FALLING_NECTAR,
    FALLING_OBSIDIAN_TEAR,
    FALLING_SPORE_BLOSSOM,
    FALLING_WATER,
    FIREWORK,
    FISHING,
    FLAME,
    FLASH,
    GLOW,
    GLOW_SQUID_INK,
    GUST,
    GUST_EMITTER_LARGE,
    GUST_EMITTER_SMALL,
    HAPPY_VILLAGER,
    HEART,
    INFESTED,
    INSTANT_EFFECT,
    ITEM,
    ITEM_COBWEB,
    ITEM_SLIME,
    ITEM_SNOWBALL,
    LANDING_HONEY,
    LANDING_LAVA,
    LANDING_OBSIDIAN_TEAR,
    LARGE_SMOKE,
    LAVA,
    MYCELIUM,
    NAUTILUS,
    NOTE,
    OMINOUS_SPAWNING,
    POOF,
    PORTAL,
    RAID_OMEN,
    RAIN,
    REVERSE_PORTAL,
    SCRAPE,
    SCULK_CHARGE,
    SCULK_CHARGE_POP,
    SCULK_SOUL,
    SHRIEK,
    SMALL_FLAME,
    SMALL_GUST,
    SMOKE,
    SNEEZE,
    SNOWFLAKE,
    SONIC_BOOM,
    SOUL,
    SOUL_FIRE_FLAME,
    SPIT,
    SPLASH,
    SPORE_BLOSSOM_AIR,
    SQUID_INK,
    SWEEP_ATTACK,
    TOTEM_OF_UNDYING,
    TRIAL_OMEN,
    TRIAL_SPAWNER_DETECTION,
    TRIAL_SPAWNER_DETECTION_OMINOUS,
    UNDERWATER,
    VAULT_CONNECTION,
    VIBRATION,
    WARPED_SPORE,
    WAX_OFF,
    WAX_ON,
    WHITE_ASH,
    WHITE_SMOKE,
    WITCH;

    private final boolean colorable;
    private final String formattedName;

    ParticleType() {
        this(false);
    }

    ParticleType(boolean colorable) {
        this.colorable = colorable;

        String name = name();
        this.formattedName = Arrays.stream(name.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()).reduce((a, b) -> a + " " + b).orElse(name);
    }

    public boolean isColorable() {
        return colorable;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public static ParticleType[] getColorable() {
        return Arrays.stream(values()).filter(ParticleType::isColorable).toArray(ParticleType[]::new);
    }

    public static String[] getFormattedNames() {
        return Arrays.stream(values()).map(ParticleType::getFormattedName).toArray(String[]::new);
    }

    public static ParticleType fromFormattedName(String formattedName) {
        return Arrays.stream(values()).filter(type -> type.getFormattedName().equalsIgnoreCase(formattedName)).findFirst().orElse(null);
    }
}
