package rocks.biankouski.runinfiregame.desktop.opengl;

//import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;


import java.util.concurrent.TimeUnit;

import rocks.biankouski.runinfiregame.desktop.opengl.service.ShaderProgram;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_MAX_VERTEX_ATTRIBS;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Two {

    private ShaderProgram shaderProgram;

    public Two() {

        Window window = new Window();

        glfwSetKeyCallback(window.getId(), (window1, key, scancode, action, mods) -> {
            // и приложение после этого закроется
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window.getId(), true);
            }
        });

        int answer[] = new int[1];
        glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, answer);
        //glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        this.shaderProgram = new ShaderProgram("shaders/Two/vertex.glsl", "shaders/Two/fragment.glsl");

        //renderSquareViaVAO(window);
        renderSquareViaUniform(window.getId());
        glfwTerminate();
    }

    private void renderSquareViaUniform(long window) {
        float vertices[] = {
                // Позиции         // Цвета
                -0.5f,  0.5f,  0.0f,  1.0f, 0.0f, 0.0f,   // Up left corner 0
                 0.5f,  0.5f,  0.0f,  1.0f, 1.0f, 1.0f,   // Up right       1
                 0.5f, -0.5f,  0.0f,  0.0f, 0.0f, 1.0f,    // Down right    2
                -0.5f, -0.5f,  0.0f,  1.0f, 1.0f, 1.0f    // Down left      3
        };
        int indices[] = {  // Помните, что мы начинаем с 0!
                0, 1, 2,   // Первый треугольник
                0, 3, 2    // Второй треугольник
        };

        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        int  VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4 , 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4 , 3 * 4);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);


            glBindVertexArray(VAO);

            double timeValue = glfwGetTime();
            float greenValue = (float)((Math.sin(timeValue) / 2) + 0.5);
            int vertexColorLocation = glGetUniformLocation(shaderProgram.getShaderProgramId(), "mixColor");
            glUseProgram(shaderProgram.getShaderProgramId());
            glUniform3f(vertexColorLocation, 0.0f, greenValue, 0.0f);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);


            glfwSwapBuffers(window);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
