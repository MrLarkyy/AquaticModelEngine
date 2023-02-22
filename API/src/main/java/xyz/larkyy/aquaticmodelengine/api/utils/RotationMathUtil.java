package xyz.larkyy.aquaticmodelengine.api.utils;

public class RotationMathUtil {

    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];
    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);

    static {
        for(int var0 = 0; var0 < 257; ++var0) {
            double var1 = (double)var0 / 256.0;
            double var3 = Math.asin(var1);
            COS_TAB[var0] = Math.cos(var3);
            ASIN_TAB[var0] = var3;
        }
    }
    public static double atan2(double var0, double var2) {
        double var4 = var2 * var2 + var0 * var0;
        if (Double.isNaN(var4)) {
            return Double.NaN;
        } else {
            boolean var6 = var0 < 0.0;
            if (var6) {
                var0 = -var0;
            }

            boolean var7 = var2 < 0.0;
            if (var7) {
                var2 = -var2;
            }

            boolean var8 = var0 > var2;
            double var9;
            if (var8) {
                var9 = var2;
                var2 = var0;
                var0 = var9;
            }

            var9 = fastInvSqrt(var4);
            var2 *= var9;
            var0 *= var9;
            double var11 = FRAC_BIAS + var0;
            int var13 = (int)Double.doubleToRawLongBits(var11);
            double var14 = ASIN_TAB[var13];
            double var16 = COS_TAB[var13];
            double var18 = var11 - FRAC_BIAS;
            double var20 = var0 * var16 - var2 * var18;
            double var22 = (6.0 + var20 * var20) * var20 * 0.16666666666666666;
            double var24 = var14 + var22;
            if (var8) {
                var24 = 1.5707963267948966 - var24;
            }

            if (var7) {
                var24 = Math.PI - var24;
            }

            if (var6) {
                var24 = -var24;
            }

            return var24;
        }
    }

    public static double fastInvSqrt(double var0) {
        double var2 = 0.5 * var0;
        long var4 = Double.doubleToRawLongBits(var0);
        var4 = 6910469410427058090L - (var4 >> 1);
        var0 = Double.longBitsToDouble(var4);
        var0 *= 1.5 - var2 * var0 * var0;
        return var0;
    }

    public static float wrapDegrees(float var0) {
        float var1 = var0 % 360.0F;
        if (var1 >= 180.0F) {
            var1 -= 360.0F;
        }

        if (var1 < -180.0F) {
            var1 += 360.0F;
        }

        return var1;
    }
}
