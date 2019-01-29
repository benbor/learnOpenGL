package rocks.biankouski.runinfiregame.desktop.opengl.service;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

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
    private final Vector3 worldPosition;
    private final Color4f objectColor;
    private final Lighter lighter;
    private final Camera camera;
    private float lifetime;


    private int VAO;
    private int VBO;
    private final int locationOfTransform;
    private final int locationOfModel;
    private final int locationOfInputColor;
    private final int locationOfLightColor;
    private final int locationOfLightPosition;
    private final int locationOfViewPosition;

    public ColoredBox(Vector3 position, Color4f objectColor, Lighter lighter, Camera camera) {

        this.worldPosition = position;
        this.objectColor = objectColor;
        this.lighter = lighter;
        this.camera = camera;

        lifetime = 0f;

        shaderProgram = new ShaderProgram("shaders/Five/vertex.glsl", "shaders/Five/fragment.glsl");

        locationOfTransform = glGetUniformLocation(shaderProgram.getShaderProgramId(), "transform");
        locationOfModel = glGetUniformLocation(shaderProgram.getShaderProgramId(), "model");
        locationOfInputColor = glGetUniformLocation(shaderProgram.getShaderProgramId(), "inputColor");
        locationOfLightColor = glGetUniformLocation(shaderProgram.getShaderProgramId(), "lightColor");
        locationOfLightPosition = glGetUniformLocation(shaderProgram.getShaderProgramId(), "lightPosition");
        locationOfViewPosition = glGetUniformLocation(shaderProgram.getShaderProgramId(), "viewPosition");

        float[] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
        };


        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);


        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 3 * 4);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void draw(Matrix4 cameraTranslate, float deltaTime) {

        lifetime += deltaTime;
//        lifetime = 0;


        Matrix4 model = new Matrix4().translate(worldPosition).rotate(new Vector3(0f, 1f, 0f), lifetime * 10f);

        glUseProgram(shaderProgram.getShaderProgramId());
        glBindVertexArray(VAO);

        glUniform3fv(locationOfInputColor, objectColor.value3());
        glUniform3fv(locationOfLightColor, lighter.getColor().value3());

        Vector3 lighterP = lighter.getPosition();
        glUniform3f(locationOfLightPosition, lighterP.x, lighterP.y, lighterP.z);
        Vector3 viewP = camera.position;
        glUniform3f(locationOfViewPosition, viewP.x, viewP.y, viewP.z);

        glUniformMatrix4fv(locationOfTransform, false, cameraTranslate.val);
        glUniformMatrix4fv(locationOfModel, false, model.val);

        glDrawArrays(GL_TRIANGLES, 0, 36);

        glBindVertexArray(0);
        glUseProgram(0);
    }

}
