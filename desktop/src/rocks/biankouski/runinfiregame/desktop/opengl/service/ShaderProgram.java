package rocks.biankouski.runinfiregame.desktop.opengl.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgramiv;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;

/**
 * Created by borisb on 9/1/17.
 */

public class ShaderProgram {

    private int shaderProgramId;

    public ShaderProgram (String vertexPath, String fragmentPath) {

        int vertexShader;
        int fragmentShader;

        vertexShader = createShader(vertexPath, GL_VERTEX_SHADER);
        fragmentShader = createShader(fragmentPath, GL_FRAGMENT_SHADER);

        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexShader);
        glAttachShader(shaderProgramId, fragmentShader);
        glLinkProgram(shaderProgramId);

        int[] resultBuffer = new int[1];
        glGetProgramiv(shaderProgramId, GL_LINK_STATUS, resultBuffer);
        int result = resultBuffer[0];

        if (result == 0) {
            String log = glGetProgramInfoLog(shaderProgramId, 512);
            System.out.print(log);
            System.exit(1);
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        //        return shaderProgramId;
    }

    private int createShader (String path, int type) {
        int shader = glCreateShader(type);

        String source = getShaderCode(path);
        glShaderSource(shader, source);
        glCompileShader(shader);
        int[] resultBuffer = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, resultBuffer);
        int result = resultBuffer[0];

        if (result == 0) {
            String log = glGetShaderInfoLog(shader, 512);
            System.out.print(log);
            System.out.print(source);
            System.exit(1);
        }

        return shader;
    }

    private String getShaderCode (String path) {
        try {
            String content = null;
            content = new String(Files.readAllBytes(Paths.get(path)));
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getShaderProgramId () {
        return shaderProgramId;
    }

}
