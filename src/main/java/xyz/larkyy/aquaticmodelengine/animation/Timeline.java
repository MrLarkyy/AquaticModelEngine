package xyz.larkyy.aquaticmodelengine.animation;

import org.bukkit.Bukkit;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.animation.keyframe.PositionKeyFrame;
import xyz.larkyy.aquaticmodelengine.util.math.TimelineUtil;

import java.util.TreeMap;

public class Timeline {

    private final TreeMap<Double, PositionKeyFrame> positionTimeline = new TreeMap<>();

    public void addPositionFrame(double time, Vector vector, InterpolationType interpolationType) {
        positionTimeline.put(time,new PositionKeyFrame(interpolationType,vector));
    }

    public Vector getPositionFrame(double time) {
        if (positionTimeline.isEmpty()) {
            return new Vector(0,0,0);
        }

        double lowerD = getLower(time,positionTimeline);
        double higherD = getHigher(time,positionTimeline);


        var lower = positionTimeline.get(lowerD);
        var higher = positionTimeline.get(higherD);

        if (lowerD == higherD) {
            return lower.getVector();
        }

        var interpolation = getInterpolation(lower,higher);

        // Progress
        double d = (time - lowerD) / (higherD - lowerD);

        if (interpolation == InterpolationType.LINEAR) {
            return TimelineUtil.lerp(lower.getVector(),higher.getVector(),d);
        } else if (interpolation == InterpolationType.SMOOTH) {
            double lowerLowerD = getLower(lowerD,positionTimeline);
            double higherHigherD = getHigher(higherD,positionTimeline);

            var lowerLower = positionTimeline.get(lowerLowerD);
            var higherHigher = positionTimeline.get(higherHigherD);

            return TimelineUtil.smoothLerp(lowerLower.getVector(),lower.getVector(),higher.getVector(),higherHigher.getVector(),d);
        } else if (interpolation == InterpolationType.STEP) {
            return lower.getVector();
        }

        return new Vector();
    }

    private InterpolationType getInterpolation(KeyFrame keyFrame1, KeyFrame keyFrame2) {
        if (keyFrame1.getInterpolationType() == InterpolationType.STEP) {
            return InterpolationType.STEP;
        }

        if (keyFrame1.getInterpolationType() == InterpolationType.SMOOTH || keyFrame2.getInterpolationType() == InterpolationType.SMOOTH) {
            return InterpolationType.SMOOTH;
        }

        return InterpolationType.LINEAR;
    }

    private double getLower(double value, TreeMap<Double,?> map) {
        Double d = map.lowerKey(value);
        if (d == null) {
            return map.firstKey();
        }
        return d;
    }

    private double getHigher(double value, TreeMap<Double,?> map) {
        Double d = map.higherKey(value);
        if (d == null) {
            return map.lastKey();
        }
        return d;
    }

}
