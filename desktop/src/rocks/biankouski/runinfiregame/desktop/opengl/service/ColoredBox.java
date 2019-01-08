package rocks.biankouski.runinfiregame.desktop.opengl.service;

import com.badlogic.gdx.math.Matrix4;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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

public class ColoredBox implements DrawableInterface{
    private final ShaderProgram shaderProgram;
    private final Color4f objectColor;
    private final Color4f lighterColor;


    private int VAO;
    private int VBO;
    private final int locationOfTransform;
    private final int locationOfInputColor;
    private final int locationOfLightColor;

    public ColoredBox(Color4f objectColor, Color4f lighterColor) {
        this.objectColor = objectColor;
        this.lighterColor = lighterColor;


        shaderProgram = new ShaderProgram("shaders/Five/vertex.glsl", "shaders/Five/fragment.glsl");

        locationOfTransform = glGetUniformLocation(shaderProgram.getShaderProgramId(), "transform");
        locationOfInputColor = glGetUniformLocation(shaderProgram.getShaderProgramId(), "inputColor");
        locationOfLightColor = glGetUniformLocation(shaderProgram.getShaderProgramId(), "lightColor");

        float[] vertices = {
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

    public void draw(Matrix4 trans) {

        glUseProgram(shaderProgram.getShaderProgramId());
        glBindVertexArray(VAO);

        glUniform4fv(locationOfInputColor, objectColor.value());
        glUniform4fv(locationOfLightColor, lighterColor.value());
        glUniformMatrix4fv(locationOfTransform, false, trans.val);

        glDrawArrays(GL_TRIANGLES, 0, 36);

        glBindVertexArray(0);
        glUseProgram(0);
    }

}
