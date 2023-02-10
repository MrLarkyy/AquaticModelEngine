package xyz.larkyy.aquaticmodelengine.animation;

import org.bukkit.Bukkit;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.animation.keyframe.PositionKeyFrame;
import xyz.larkyy.aquaticmodelengine.animation.keyframe.RotationKeyFrame;
import xyz.larkyy.aquaticmodelengine.util.math.TimelineUtil;

import java.util.TreeMap;

public class Timeline {

    private final TreeMap<Double, PositionKeyFrame> positionTimeline = new TreeMap<>();
    private final TreeMap<Double, RotationKeyFrame> rotationTimeline = new TreeMap<>();

    public void addPositionFrame(double time, Vector vector, InterpolationType interpolationType) {
        positionTimeline.put(time,new PositionKeyFrame(interpolationType,vector));
    }

    public void addRotationFrame(double time, EulerAngle eulerAngle, InterpolationType interpolationType) {
        rotationTimeline.put(time,new RotationKeyFrame(interpolationType,eulerAngle));
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

    public EulerAngle getRotation(double time) {
        if (rotationTimeline.isEmpty()) {
            return new EulerAngle(0,0,0);
        }

        double lowerD = getLower(time,rotationTimeline);
        double higherD = getHigher(time,rotationTimeline);


        var lower = rotationTimeline.get(lowerD);
        var higher = rotationTimeline.get(higherD);

        if (lowerD == higherD) {
            return lower.getEulerAngle();
        }

        var lowerVector = new Vector(lower.getEulerAngle().getX(),lower.getEulerAngle().getY(),lower.getEulerAngle().getZ());
        var higherVector = new Vector(higher.getEulerAngle().getX(),higher.getEulerAngle().getY(),higher.getEulerAngle().getZ());

        var interpolation = getInterpolation(lower,higher);

        // Progress
        double d = (time - lowerD) / (higherD - lowerD);

        if (interpolation == InterpolationType.LINEAR) {
            var vector = TimelineUtil.lerp(lowerVector,higherVector,d);
            return new EulerAngle(vector.getX(), vector.getY(), vector.getZ());
        } else if (interpolation == InterpolationType.SMOOTH) {
            double lowerLowerD = getLower(lowerD,rotationTimeline);
            double higherHigherD = getHigher(higherD,rotationTimeline);

            var lowerLower = rotationTimeline.get(lowerLowerD);
            var higherHigher = rotationTimeline.get(higherHigherD);

            var lowerLowerVector = new Vector(lowerLower.getEulerAngle().getX(), lowerLower.getEulerAngle().getY(), lowerLower.getEulerAngle().getZ());
            var higherHigherVector = new Vector(higherHigher.getEulerAngle().getX(), higherHigher.getEulerAngle().getY(), higherHigher.getEulerAngle().getZ());

            var result = TimelineUtil.smoothLerp(lowerLowerVector,lowerVector,higherVector,higherHigherVector,d);
            return new EulerAngle(result.getX(),result.getY(),result.getZ());
        } else if (interpolation == InterpolationType.STEP) {
            return lower.getEulerAngle();
        }
        return new EulerAngle(0,0,0);
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
