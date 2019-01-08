package rocks.biankouski.runinfiregame.desktop;

import com.badlogic.gdx.utils.GdxNativesLoader;

import rocks.biankouski.runinfiregame.desktop.opengl.Five;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DI;

public class DesktopLauncher {

    public static void main(String[] arg) {
        System.setProperty("java.awt.headless", "true");

        GdxNativesLoader.load();


        DI di = Five.initDi();
        di.get(Five.class);

    }


}
