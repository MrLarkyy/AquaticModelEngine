package xyz.larkyy.aquaticmodelengine.generator.java;

public class JavaDisplay {

    private final double[] translation;
    private final double[] scale;

    public JavaDisplay() {
        translation = new double[] {0,25.6,0};
        scale = new double[] {4,4,4};
    }

    public double[] getScale() {
        return scale;
    }

    public double[] getTranslation() {
        return translation;
    }
}
