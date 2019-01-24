package rocks.biankouski.runinfiregame.desktop.opengl.service;

import com.badlogic.gdx.math.Vector3;

public final class Color4f {

    public static final Color4f white = new Color4f(1f, 1f, 1f, 1f);
    public static final Color4f black = new Color4f(0f, 0f, 0f, 1f);
    public static final Color4f red = new Color4f(1f, 0f, 0f, 1f);
    public static final Color4f green = new Color4f(0f, 1f, 0f, 1f);
    public static final Color4f blue = new Color4f(0f, 0f, 1f, 1f);

    private final float[] computed;

    public Color4f(float r, float g, float b, float a) {
        computed = new float[] {r,g, b, a};
    }

    public Color4f(Vector3 v) {
        this(v.x, v.y, v.z, 1.0f);
    }


    /**
     *
     * @return computed value as float[4]
     */
    public float[] value()
    {
        return computed;
    }

    public float[] value3()
    {
        return new float[] {computed[0], computed[1], computed[2]};
    }

}
