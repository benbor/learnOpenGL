package rocks.biankouski.runinfiregame.desktop.opengl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import rocks.biankouski.runinfiregame.desktop.opengl.service.*;

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
        DrawableInterface lighter = new Lighter(lightColor);
        DrawableInterface woodenBox = new ColoredBox(new Color4f(1.0f, 0.5f, 0.31f, 1f), lightColor);


        glEnable(GL_DEPTH_TEST);

        while (window.update()) {
            glfwPollEvents();
            float deltaTime = (float) glfwGetTime();

            observer.update(deltaTime);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            camera.update();

            Matrix4 lighterTrans = new Matrix4(camera.combined);
            lighterTrans.translate(new Vector3(1.0f, 1.0f, 1.0f)).scale(0.5f,0.5f, 1f);
            lighter.draw(lighterTrans);

            Matrix4 cubeTrans = new Matrix4(camera.combined);
            cubeTrans.translate(new Vector3(-1.0f, -1.0f, -1.0f)).rotate(new Vector3(0.5f, 1f, 0f), deltaTime * 5f).scale(1.5f,0.5f, 1f);
            woodenBox.draw(cubeTrans);

        }
    }
}
