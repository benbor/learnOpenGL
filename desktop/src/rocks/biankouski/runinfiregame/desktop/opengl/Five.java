package rocks.biankouski.runinfiregame.desktop.opengl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.concurrent.TimeUnit;

import rocks.biankouski.runinfiregame.desktop.opengl.service.Controller;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DI;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DrawableInterface;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Lighter;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Observer;
import rocks.biankouski.runinfiregame.desktop.opengl.service.ShaderProgram;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Texture;
import rocks.biankouski.runinfiregame.desktop.opengl.service.WoodenBox;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetIntegerv;

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

        DrawableInterface lighter = new Lighter(new ShaderProgram("shaders/Five/_light_vertex.glsl", "shaders/Five/_light_fragment.glsl"));
        DrawableInterface woodenBox = new WoodenBox(
                new ShaderProgram("shaders/Four/vertex.glsl", "shaders/Four/fragment.glsl"),
                new Texture("box.jpg")
        );


        glEnable(GL_DEPTH_TEST);

        while (!glfwWindowShouldClose(window.getId())) {
            glfwPollEvents();
            float deltaTime = (float) glfwGetTime();

            observer.update(deltaTime);
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            camera.update();

            Matrix4 lighterTrans = new Matrix4(camera.combined);
            lighterTrans.translate(new Vector3(1.0f, 1.0f, 1.0f)).scale(0.5f,0.5f, 1f);
            lighter.draw(lighterTrans);

            Matrix4 cubeTrans = new Matrix4(camera.combined);
            cubeTrans.translate(new Vector3(-1.0f, -1.0f, -1.0f)).rotate(new Vector3(0.5f, 1f, 0f), deltaTime * 5f).scale(1.5f,0.5f, 1f);
            woodenBox.draw(cubeTrans);

            try {
                TimeUnit.MILLISECONDS.sleep(10);
//                System.out.println("Cycle" + (numberCycle++).toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            glfwSwapBuffers(window.getId());
        }
    }
}
