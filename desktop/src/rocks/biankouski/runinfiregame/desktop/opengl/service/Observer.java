package rocks.biankouski.runinfiregame.desktop.opengl.service;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import org.lwjgl.system.MathUtil;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by boris on 9/6/17.
 */

public class Observer {

    private final Camera camera;
    private final Controller controller;

    private float lastX = 0f;
    private float lastY = 0f;

    private float yaw = -90f;
    private float pitch = 0f;

    private float sensitive = 0.04f;
    private float movementSensitive = 5f;


    public Observer(Camera camera, Controller controller) {

        this.camera = camera;
        this.controller = controller;

        camera.lookAt(0f, 0f, 0f);
        camera.position.set(0f, 0f, 10f);
    }

    public void update(float deltaTime) {
        updatePosition(deltaTime);
        updateView(deltaTime);
    }



    private void updateView(float deltaTime) {
        float deltaX = (lastX - controller.getMouseX()) * sensitive;
        float deltaY = (lastY - controller.getMouseY()) * sensitive;
        lastX = controller.getMouseX();
        lastY = controller.getMouseY();

        yaw -= deltaX;
        pitch += deltaY;

        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

        Vector3 direction = new Vector3();
        direction.x = MathUtils.cos(MathUtils.degreesToRadians * yaw) * MathUtils.cos(MathUtils.degreesToRadians * pitch);
        direction.y = MathUtils.sin(MathUtils.degreesToRadians * pitch);
        direction.z = MathUtils.sin(MathUtils.degreesToRadians * yaw) * MathUtils.cos(MathUtils.degreesToRadians * pitch);
        camera.direction.set(direction.nor());


    }


    private void updatePosition(float deltaTime) {

        float cameraSpeed = movementSensitive * deltaTime;
        if (controller.isPressed(GLFW_KEY_W)) {
            camera.position.mulAdd(camera.direction, cameraSpeed);
        }
        if (controller.isPressed(GLFW_KEY_S)) {
            camera.position.mulAdd(camera.direction, -cameraSpeed);
        }
        if (controller.isPressed(GLFW_KEY_A)) {
            camera.position.mulAdd(new Vector3(camera.direction).crs(camera.up), -cameraSpeed);
        }

        if (controller.isPressed(GLFW_KEY_D)) {
            camera.position.mulAdd(new Vector3(camera.direction).crs(camera.up), cameraSpeed);
        }
    }

}
