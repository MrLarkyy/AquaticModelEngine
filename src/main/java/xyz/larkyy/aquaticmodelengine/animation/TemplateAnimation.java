package xyz.larkyy.aquaticmodelengine.animation;

import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class TemplateAnimation {

    /**
     * String - bone name
     * Timeline - the timeline of each bone
     */
    private final Map<String,Timeline> timelines = new HashMap<>();
    private final String name;
    private final double length;
    private LoopMode loopMode;
    private boolean override = false;

    public TemplateAnimation(String name, double length, LoopMode loopMode) {
        this.name = name;
        this.length = length;
        this.loopMode = loopMode;
    }

    public String getName() {
        return name;
    }

    public Timeline addTimeline(String bone, Timeline timeline) {
        if (timelines.containsKey(bone)) {
            return timelines.get(bone);
        }
        timelines.put(bone,timeline);
        return timeline;
    }

    public Timeline getTimeline(String bone) {
        return timelines.get(bone);
    }

    public Vector getPosition(String bone, double time) {
        if (!timelines.containsKey(bone)) {
            return null;
        }
        return timelines.get(bone).getPositionFrame(time);
    }

    public boolean isOverride() {
        return override;
    }

    public double getLength() {
        return length;
    }

    public LoopMode getLoopMode() {
        return loopMode;
    }

    public void setLoopMode(LoopMode loopMode) {
        this.loopMode = loopMode;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
