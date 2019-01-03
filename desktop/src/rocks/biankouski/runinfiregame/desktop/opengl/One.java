package rocks.biankouski.runinfiregame.desktop.opengl;

//import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;


import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class One {

    private int shaderProgram;

    public One() {
        System.out.println(System.getProperty("java.version"));
        long window = this.createWindow();

        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        initializeShaders();
        renderSquareViaVAO(window);
//        renderSquareViaEBO(window);
        glfwTerminate();
    }

    private void renderSquareViaEBO(long window) {
        float vertices[] = {
                0.5f,  0.5f, 0.0f,  // Верхний правый угол
                0.5f, -0.5f, 0.0f,  // Нижний правый угол
                -0.5f, -0.5f, 0.0f,  // Нижний левый угол
                -0.5f,  0.5f, 0.0f   // Верхний левый угол
        };
        int indices[] = {  // Помните, что мы начинаем с 0!
                0, 1, 3,   // Первый треугольник
                1, 2, 3    // Второй треугольник
        };

        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        int  VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 , 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glUseProgram(shaderProgram);
            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);


            glfwSwapBuffers(window);
        }
    }

    private void renderSquareViaVAO(long window) {
        float[] vertices = {
                // Первый треугольник
                0.5f,  0.5f, 0.0f,  // Верхний правый угол
                0.5f, -0.5f, 0.0f,  // Нижний правый угол
                -0.5f,  0.5f, 0.0f,  // Верхний левый угол
                // Второй треугольник
                0.5f, -0.5f, 0.0f,  // Нижний правый угол
                -0.5f, -0.5f, 0.0f,  // Нижний левый угол
                -0.5f,  0.5f, 0.0f   // Верхний левый угол
        };

        int VAO = initializeVAO(vertices);


        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glUseProgram(shaderProgram);
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glDrawArrays(GL_TRIANGLES, 3, 3);
            glBindVertexArray(0);


            glfwSwapBuffers(window);
        }
    }

    private int initializeVAO(float[] vertices) {
        int VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        int  VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4 , 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);

        return VAO;
    }

    private void initializeShaders() {
        String vertexShaderCode = " #version 330 core \n" +
                "layout (location = 0) in vec3 position;\n" +
                "void main() \n" +
                "{\n" +
                "   gl_Position = vec4(position.x, position.y, position.z, 1.0);\n" +
                "}\n";
        int verexShader = compileShader(vertexShaderCode, GL_VERTEX_SHADER);

        String fragmentShaderCode = " #version 330 core \n" +
                "out vec4 color;\n" +
                "void main() \n" +
                "{\n" +
                "   color = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "}\n";
        int fragmentShader = compileShader(fragmentShaderCode, GL_FRAGMENT_SHADER);


        this.shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, verexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        int[] resultBuffer = new int[1];
        glGetProgramiv(shaderProgram, GL_LINK_STATUS, resultBuffer);
        int result = resultBuffer[0];


        if (result == 0)
        {
            String log = glGetProgramInfoLog(shaderProgram, 512);
            System.out.print(log);
            System.exit(1);
        }


        glDeleteShader(verexShader);
        glDeleteShader(fragmentShader);



    }

    private int compileShader(String source, int type)
    {

        int shader = glCreateShader(type);

        glShaderSource(shader, source);
        glCompileShader(shader);
        int[] resultBuffer = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, resultBuffer);
        int result = resultBuffer[0];


        if (result == 0)
        {
            String log = glGetShaderInfoLog(shader, 512);
            System.out.print(log);
            System.exit(1);
        }

        return shader;
    }


    protected long createWindow() {
        GLFWErrorCallback errorCallback;
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        String title = "MyTitle";  // The title of the window, WARNING, if title is
        // null, the code will segfault at glfwCreateWindow()
        boolean resizable = true; // Whether or not the window is resizable

        int m_width = 1024; //width of the window
        int m_height = 768; //height of the window

        glfwDefaultWindowHints(); // Loads GLFW's default window settings

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        //Минорная
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        //Установка профайла для которого создается контекст
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        //Выключение возможности изменения размера окна
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        long window = glfwCreateWindow(m_width, m_height, title, 0, 0); //Does the actual window creation
        if (window == 0) throw new RuntimeException("Failed to create window");

        glfwMakeContextCurrent(window); //glfwSwapInterval needs a context on the calling thread, otherwise will cause NO_CURRENT_CONTEXT error
        GL.createCapabilities(); //Will let lwjgl know we want to use this context as the

        glViewport(0, 0, m_width, m_height);


        glfwSetKeyCallback(window, (window1, key, scancode, action, mods) -> {
            // и приложение после этого закроется
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                glfwSetWindowShouldClose(window, true);
        });

        return window;
    }
}
