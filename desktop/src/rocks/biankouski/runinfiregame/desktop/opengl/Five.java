package rocks.biankouski.runinfiregame.desktop.opengl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import org.javatuples.Pair;
import rocks.biankouski.runinfiregame.desktop.opengl.service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

public class Five {

    private final Observer observer;
    private final Camera camera;

    public Five (Window window, Observer observer, Camera camera) {

        this.camera = camera;
        this.observer = observer;


        renderSquareViaUniform(window);
        glfwTerminate();
    }

    public static DI initDi () {
        DI dic = new DI();
        dic.share(Controller.class, di -> new Controller(di.get(Window.class)));
        dic.share(Five.class, di -> new Five(di.get(Window.class), di.get(Observer.class), di.get(Camera.class)));

        dic.share(Window.class, di -> new Window());
        dic.share(Observer.class, di -> new Observer(di.get(Camera.class), di.get(Controller.class)));
        dic.share(Camera.class, di -> {
            Window window = di.get(Window.class);
            return new PerspectiveCamera(45f, window.getFrameWidth(), window.getFrameHeight());
        });
        return dic;
    }

    private void renderSquareViaUniform (Window window)  {

        Color4f lightColor = Color4f.white;
        Lighter lighter = new Lighter(lightColor);


        List<ColoredBox> cubs = Arrays.asList(
                new ColoredBox(new Vector3(2.0f, 0.0f, 2.0f), new Color4f(1.0f, 0.5f, 0.31f, 1f), lighter, camera),
                new ColoredBox(new Vector3(0.0f, 0.0f, -5.0f), new Color4f(0.2f, 0.2f, 0.2f, 1f), lighter, camera)
        );


        glEnable(GL_DEPTH_TEST);

        float lastTime = 0f;

        while (window.update()) {
            glfwPollEvents();
            float newTime = (float) glfwGetTime();
            float deltaTime = newTime - lastTime;
            lastTime = newTime;

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            observer.update(deltaTime);
            glClearColor(0.1f, 0.1f, 0.5f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            camera.update();

            // draw lighter
            lighter.draw(camera.combined, deltaTime);

            //draw cubes
            for (ColoredBox cube : cubs) {
                cube.draw(camera.combined, deltaTime);
            }

        }
    }
}
