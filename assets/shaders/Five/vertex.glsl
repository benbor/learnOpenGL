#version 330 core
layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

out vec3 Normal;
out vec3 FragPosition;

uniform mat4 transform;
uniform mat4 model;

void main()
{
    gl_Position = transform * model * vec4(position, 1.0f);

    FragPosition = vec3(model * vec4(position, 1.0f));
    Normal = vec3(model * vec4(normal, 0.0f));
}

