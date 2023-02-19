package xyz.larkyy.aquaticmodelengine.api.model.template.player;

import java.util.Locale;

public enum LimbType {
    HEAD(1, 1, false),
    HIP(8, 8, false),
    WAIST(8, 8, false),
    CHEST(8, 8, false),
    RIGHT_ARM(2, 5, false),
    RIGHT_FOREARM(4, 7, false),
    LEFT_ARM(3, 6, false),
    LEFT_FOREARM(4, 7, false),
    RIGHT_LEG(9, 9, false),
    RIGHT_FORELEG(9, 9, false),
    LEFT_LEG(9, 9, false),
    LEFT_FORELEG(9, 9, false),
    RIGHT_ITEM(-1, -1, true),
    LEFT_ITEM(-1, -1, true);

    LimbType(int modelId, int slimId, boolean isItem) {
        this.modelId = modelId;
        this.slimId = slimId;
        this.isItem = isItem;
    }

    private final int modelId;

    private final int slimId;

    private final boolean isItem;

    public int getModelId() {
        return this.modelId;
    }

    public int getSlimId() {
        return this.slimId;
    }

    public boolean isItem() {
        return this.isItem;
    }

    public static LimbType get(String limb) {
        try {
            return valueOf(limb.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}
