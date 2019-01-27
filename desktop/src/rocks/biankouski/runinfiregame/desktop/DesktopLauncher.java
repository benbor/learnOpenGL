package rocks.biankouski.runinfiregame.desktop;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.utils.GdxNativesLoader;

import rocks.biankouski.runinfiregame.desktop.opengl.Five;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DI;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

public class DesktopLauncher {

    public static void main(String[] arg) {
        System.out.println("Main started successfully");
        System.setProperty("java.awt.headless", "true");
//
        GdxNativesLoader.load();



        DI di = Five.initDi();
        di.get(Five.class);

    }


}
