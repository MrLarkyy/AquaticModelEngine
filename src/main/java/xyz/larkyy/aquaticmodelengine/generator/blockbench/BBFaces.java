package xyz.larkyy.aquaticmodelengine.generator.blockbench;

public class BBFaces {

    private final BBFace north;
    private final BBFace east;
    private final BBFace south;
    private final BBFace west;
    private final BBFace up;
    private final BBFace down;

    public BBFaces(BBFace north, BBFace east, BBFace south, BBFace west, BBFace up, BBFace down) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public BBFace getDown() {
        return down;
    }

    public BBFace getUp() {
        return up;
    }

    public BBFace getEast() {
        return east;
    }

    public BBFace getNorth() {
        return north;
    }

    public BBFace getSouth() {
        return south;
    }

    public BBFace getWest() {
        return west;
    }

    public static class BBFace {

        private final double[] uv;
        // Integer is being used, to be able to set the texture as null
        private final Integer texture;

        private final int rotation;

        public BBFace(double[] uv, Integer texture, int rotation) {
            this.uv = uv;
            this.texture = texture;
            this.rotation = rotation;
        }

        public double[] getUv() {
            return uv;
        }

        public int getRotation() {
            return rotation;
        }

        public Integer getTexture() {
            return texture;
        }
    }

}
