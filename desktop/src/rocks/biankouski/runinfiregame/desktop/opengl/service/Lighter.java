package rocks.biankouski.runinfiregame.desktop.opengl.service;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created by boris on 9/19/17.
 */

public class Lighter implements DrawableInterface{
    private final ShaderProgram shaderProgram;
    private final Vector3 position;

    private int VAO;
    private int VBO;
    private final int locationOfTransform;
    private final int locationOfInputColor;
    private final Color4f color;

    public Lighter(Color4f color) {
        this.color = color;

        this.shaderProgram = new ShaderProgram("shaders/Five/_light_vertex.glsl", "shaders/Five/_light_fragment.glsl");

        locationOfTransform = glGetUniformLocation(shaderProgram.getShaderProgramId(), "transform");
        locationOfInputColor = glGetUniformLocation(shaderProgram.getShaderProgramId(), "inputColor");

        position = new Vector3(0.0f, 3.0f, 0.0f);

        float vertices[] = {
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,

                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,

                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
        };


        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);


        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Color4f getColor() {
        return color;
    }

    public void draw(Matrix4 cameraTranslate, float deltaTime) {

        Matrix4 lighterTrans = new Matrix4(cameraTranslate).translate(position).scale(0.1f, 0.1f, 0.1f);

        glUseProgram(shaderProgram.getShaderProgramId());
        glBindVertexArray(VAO);
        glUniform4fv(locationOfInputColor, color.value());
        glUniformMatrix4fv(locationOfTransform, false, lighterTrans.val);
        glDrawArrays(GL_TRIANGLES, 0, 36);

        glBindVertexArray(0);
        glUseProgram(0);
    }

}
