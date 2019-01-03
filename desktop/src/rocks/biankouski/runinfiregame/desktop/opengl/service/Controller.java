package rocks.biankouski.runinfiregame.desktop.opengl.service;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwGetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

/**
 * Created by boris on 9/17/17.
 */

public class Controller {
    private final Window window;
    private final boolean[] pressedKeys = new boolean[512];

    private float lastMouseX = 0f;
    private float lastMouseY = 0f;

    public Controller(Window window) {
        this.window = window;

        //glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetKeyCallback(window.getId(), this::keyCallback);
        glfwSetCursorPosCallback(window.getId(), this::mouseCallback);
        glfwSetMouseButtonCallback(window.getId(), this::mouseButtonCallback);

    }


    private void mouseButtonCallback(long window1, int button, int action, int mods) {
        // и приложение после этого закроется
        if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
            glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
    }

    private void mouseCallback(long window1, double xpos, double ypos) {
        lastMouseX = (float) xpos;
        lastMouseY = (float) ypos;
    }

    private void keyCallback(long window1, int keyCode, int scancode, int action, int mods) {
        switch (action) {
            case GLFW_PRESS:
            case GLFW_REPEAT:
                pressedKeys[keyCode] = true;
                break;
            case GLFW_RELEASE:
                pressedKeys[keyCode] = false;
                break;
            default:
                throw new RuntimeException();
        }

        // и приложение после этого закроется
        if (keyCode == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getId(), true);
        }

        if (keyCode == GLFW_KEY_M && action == GLFW_PRESS) {
            if (glfwGetInputMode(window.getId(), GLFW_CURSOR) == GLFW_CURSOR_NORMAL) {
                glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            } else {
                glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        }
    }

    public boolean isPressed(int KeyCode) {
        return pressedKeys[KeyCode];
    }


    public float getMouseX() {
        return lastMouseX;
    }

    public float getMouseY() {
        return lastMouseY;
    }
}
