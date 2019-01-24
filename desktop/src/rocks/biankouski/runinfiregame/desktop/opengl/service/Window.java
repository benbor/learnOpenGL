package rocks.biankouski.runinfiregame.desktop.opengl.service;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by borisb on 9/1/17.
 */

public class Window {

    private final int frameWidth;
    private final int frameHeight;
    private int m_width; //width of the window
    private int m_height; //height of the window
    private long id;

    public Window() {

        Configuration.DEBUG.set(true);
//        Configuration.DEBUG_FUNCTIONS.set(true);
        Configuration.DEBUG_LOADER.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_STACK.set(true);

        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));



        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        String title = "MyTitle";  // The title of the window, WARNING, if title is
        // null, the code will segfault at glfwCreateWindow()
        boolean resizable = true; // Whether or not the window is resizable


        m_width = 1024;
        m_height = 768;

        glfwDefaultWindowHints(); // Loads GLFW's default window settings

        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        //Минорная
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        //Установка профайла для которого создается контекст
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //Выключение возможности изменения размера окна
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);


        id = glfwCreateWindow(m_width, m_height, title, 0, 0); //Does the actual window creation
        if (id == 0) throw new RuntimeException("Failed to create window");

        glfwMakeContextCurrent(id); //glfwSwapInterval needs a context on the calling thread, otherwise will cause NO_CURRENT_CONTEXT error
        GL.createCapabilities(); //Will let lwjgl know we want to use this context as the
        GLUtil.setupDebugMessageCallback(System.out);

        int width[] = new int[1];
        int height[] = new int[1];
        glfwGetFramebufferSize(id, width, height);
        frameWidth = width[0];
        frameHeight = height[0];

        glViewport(0, 0,  frameWidth, frameHeight);
    }


    public long getId() {
        return id;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public boolean update() {

        try {
            TimeUnit.MILLISECONDS.sleep(10);
//                System.out.println("Cycle" + (numberCycle++).toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        glfwSwapBuffers(id);

        return !glfwWindowShouldClose(id);
    }
}
