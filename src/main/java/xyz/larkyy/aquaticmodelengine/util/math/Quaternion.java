package xyz.larkyy.aquaticmodelengine.util.math;

import org.bukkit.util.EulerAngle;
import org.joml.Math;

public class Quaternion {

    /**
     * The first component of the vector part.
     */
    private float x;
    /**
     * The second component of the vector part.
     */
    private float y;
    /**
     * The third component of the vector part.
     */
    private float z;
    /**
     * The real/scalar part of the quaternion.
     */
    private float w;

    public Quaternion() {
        w = 1.0f;
    }

    public Quaternion(EulerAngle eulerAngle) {
        w = 1.0f;
        rotationXYZ(eulerAngle);
    }

    public Quaternion rotationXYZ(float angleX, float angleY, float angleZ) {
        float sx = Math.sin(angleX * 0.5f);
        float cx = Math.cosFromSin(sx, angleX * 0.5f);
        float sy = Math.sin(angleY * 0.5f);
        float cy = Math.cosFromSin(sy, angleY * 0.5f);
        float sz = Math.sin(angleZ * 0.5f);
        float cz = Math.cosFromSin(sz, angleZ * 0.5f);

        float cycz = cy * cz;
        float sysz = sy * sz;
        float sycz = sy * cz;
        float cysz = cy * sz;
        w = cx*cycz - sx*sysz;
        x = sx*cycz + cx*sysz;
        y = cx*sycz - sx*cysz;
        z = cx*cysz + sx*sycz;

        return this;
    }

    public Quaternion rotationXYZ(EulerAngle eulerAngle) {
        return rotationXYZ((float) eulerAngle.getX(), (float) eulerAngle.getY(), (float) eulerAngle.getZ());
    }

    public Quaternion mul(Quaternion q) {
        return set(Math.fma(w, q.x(), Math.fma(x, q.w(), Math.fma(y, q.z(), -z * q.y()))),
                Math.fma(w, q.y(), Math.fma(-x, q.z(), Math.fma(y, q.w(), z * q.x()))),
                Math.fma(w, q.z(), Math.fma(x, q.y(), Math.fma(-y, q.x(), z * q.w()))),
                Math.fma(w, q.w(), Math.fma(-x, q.x(), Math.fma(-y, q.y(), -z * q.z()))));
    }

    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public EulerAngle getEulerAnglesXYZ() {
        var eulerX = Math.atan2(x * w - y * z, 0.5f - x * x - y * y);
        var eulerY = Math.safeAsin(2.0f * (x * z + y * w));
        var eulerZ = Math.atan2(z * w - x * y, 0.5f - y * y - z * z);
        return new EulerAngle(eulerX,eulerY,eulerZ);
    }

    public EulerAngle getEulerAnglesXYZ2() {
        double x2 = x+x, y2 = y+y, z2 = z+z;
        double xx = x * x2, xy = x * y2, xz = x * z2;
        double yy = y * y2, yz = y * z2, zz = z * z2;
        double wx = w * x2, wy = w * y2, wz = w * z2;

        double ex, ey, ez,
                m11 = 1 - (yy + zz),
                m12 = xy - wz,
                m13 = -xz - wy,
                m22 = 1-(xx+zz),
                m23 = yz - wx,
                m32 = yz + wx,
                m33 = 1 - (xx + yy);

        ey = Math.asin(Math.clamp(m13,-1,1));
        if (Math.abs(m13) < 0.9999999) {
            ex = Math.atan2(-m23,m33);
            ez = Math.atan2(-m12,m11);
        }
        else {
            ex = Math.atan2(m32,m22);
            ez = 0;
        }

        return new EulerAngle(ex,-ey,ez);
    }

    public float w() {
        return w;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
}
