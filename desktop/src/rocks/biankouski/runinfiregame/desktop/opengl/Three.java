package rocks.biankouski.runinfiregame.desktop.opengl;

//import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;



import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import rocks.biankouski.runinfiregame.desktop.opengl.service.ShaderProgram;
import rocks.biankouski.runinfiregame.desktop.opengl.service.Window;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Three {

    private ShaderProgram shaderProgram;
    private int texture;
    private Integer numberCycle = 0;

    BufferedImage image = null;

    public Three() {

        Window window = new Window();


        glfwSetKeyCallback(window.getId(), (window1, key, scancode, action, mods) -> {
            // и приложение после этого закроется
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window.getId(), true);
            }
        });

        int answer[] = new int[1];
        glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, answer);
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        this.shaderProgram = new ShaderProgram("shaders/Three/vertex.glsl", "shaders/Three/fragment.glsl");


        renderSquareViaUniform(window.getId());
        glfwTerminate();
    }

    private void loadTexture() {

        try {

            System.setProperty("java.awt.headless", "true");
            BufferedImage bufferedImage = ImageIO.read(Paths.get("badlogic.jpg").toUri().toURL());

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
            transform.translate(0, -bufferedImage.getHeight());
            AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedImage = operation.filter(bufferedImage, null);

            int[] pixels = new int[width * height];
            bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
            texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, pixels);
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            System.out.println("end loading texture");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void renderSquareViaUniform(long window) {


        float vertices[] = {
                // Позиции          // Цвета             // Текстурные координаты
                0.5f, 0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1f, 1f,   // Верхний правый
                0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,   1f, 0f,   // Нижний правый
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,   0f, 0f,   // Нижний левый
                -0.5f, 0.5f, 0.0f,  1.0f, 1.0f, 0.0f,   0f, 1f    // Верхний левый
        };

        int indices[] = {
                0, 1, 2,   // Первый треугольник
                2, 3, 0    // Второй треугольник
        };

        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        int VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4);
        glEnableVertexAttribArray(2);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        loadTexture();


        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);


//            double timeValue = glfwGetTime();
//            float greenValue = (float)((Math.sin(timeValue) / 2) + 0.5);
//            int location = glGetUniformLocation(shaderProgram, "ourTexture");
//            glUniform1f();
            //glEnableVertexAttribArray(texAttrib);

//            int vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor");
//            glUniform4f(vertexColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);

            glBindVertexArray(VAO);
            glBindTexture(GL_TEXTURE_2D, texture);
            glUseProgram(shaderProgram.getShaderProgramId());

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
            glBindTexture(GL_TEXTURE_2D, 0);
            glUseProgram(0);


            try {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println("Cycle" + (numberCycle++).toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }

            glfwSwapBuffers(window);
        }


    }
}
