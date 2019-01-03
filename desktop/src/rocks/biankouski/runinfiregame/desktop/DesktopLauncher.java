package rocks.biankouski.runinfiregame.desktop;

import com.badlogic.gdx.utils.GdxNativesLoader;

import java.util.Arrays;

import rocks.biankouski.runinfiregame.desktop.opengl.Five;
import rocks.biankouski.runinfiregame.desktop.opengl.Four;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DI;

public class DesktopLauncher {

	public static void main (String[] arg) {
        System.setProperty("java.awt.headless", "true");
        GdxNativesLoader.load();


//            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//            new LwjglApplication(new RunInFireGame(), config);
//            new Lwjgl3Application(new FifteenPuzzleGame(), config);
//            new One();
//            new Two();
//            new Three();
        DI di = Four.initDi();
        di.get(Four.class);
//            DI di = Five.initDi();
//			di.get(Five.class);

	}


}
