package rocks.biankouski.runinfiregame.desktop.opengl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.concurrent.TimeUnit;

import rocks.biankouski.runinfiregame.desktop.opengl.service.Controller;
import rocks.biankouski.runinfiregame.desktop.opengl.service.DI;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Observer;
import rocks.biankouski.runinfiregame.desktop.opengl.service.ShaderProgram;
import rocks.biankouski.runinfiregame.desktop.opengl.service.WoodenBox;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Texture;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Window;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL20.GL_MAX_VERTEX_ATTRIBS;

public class Four {

    private final Observer observer;
    private final Camera camera;

    public Four (Window window, Observer observer, Camera camera) {

        this.camera = camera;
        this.observer = observer;

        int answer[] = new int[1];
        glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, answer);
        //        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        renderSquareViaUniform(window);
        glfwTerminate();
    }

    public static DI initDi () {
        DI dic = new DI();
        dic.share(Controller.class, di -> new Controller(di.get(Window.class)));
        dic.share(Four.class, di -> new Four(di.get(Window.class), di.get(Observer.class), di.get(Camera.class)));

        dic.share(Window.class, di -> new Window());
        dic.share(Observer.class, di -> new Observer(di.get(Camera.class), di.get(Controller.class)));
        dic.share(Camera.class, di -> {
            Window window = di.get(Window.class);
            return new PerspectiveCamera(45f, window.getFrameWidth(), window.getFrameHeight());
        });
        return dic;
    }

    private void renderSquareViaUniform (Window window) {

        WoodenBox cube = new WoodenBox(
                new ShaderProgram("shaders/Four/vertex.glsl", "shaders/Four/fragment.glsl"),
                new Texture("box.jpg")
        );

        Vector3[] cubos = new Vector3[]{
                new Vector3(0.0f, 0.0f, 0.0f),
                new Vector3(2.0f, 5.0f, -5.0f),
                new Vector3(-1.5f, -2.2f, -2.5f),
                new Vector3(-3.8f, -2.0f, 2.3f),
                new Vector3(2.4f, -0.4f, -3.5f),
                new Vector3(-1.7f, 3.0f, -7.5f),
                new Vector3(1.3f, -2.0f, 2.5f),
                new Vector3(1.5f, 2.0f, -2.5f),
                new Vector3(1.5f, 0.2f, 1.5f),
                new Vector3(-0.5f, 0.5f, 0.4f)
        };

        glEnable(GL_DEPTH_TEST);

        while (!glfwWindowShouldClose(window.getId())) {
            glfwPollEvents();
            float deltaTime = (float) glfwGetTime();

            observer.update(deltaTime);
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            camera.update();

            for (Vector3 cubPosition : cubos) {
                Matrix4 trans = new Matrix4(camera.combined);
                trans.translate(cubPosition).rotate(new Vector3(0.5f, 1f, 0f), deltaTime * 0f);
                cube.draw(trans);
            }

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
