package rocks.biankouski.runinfiregame.desktop.opengl.service;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.glViewport;

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
        GLFWErrorCallback errorCallback;
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        String title = "MyTitle";  // The title of the window, WARNING, if title is
        // null, the code will segfault at glfwCreateWindow()
        boolean resizable = true; // Whether or not the window is resizable


        m_width = 1024;
        m_height = 768;

        glfwDefaultWindowHints(); // Loads GLFW's default window settings

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
}
