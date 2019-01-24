#version 330 core
out vec4 color;

in vec3 Normal;
in vec3 FragPosition;

uniform vec3 lightPosition;

uniform vec3 lightColor;
uniform vec3 inputColor;

void main()
{
    // Ambient
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;


    // Diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPosition - FragPosition);
    float diff = max(dot(norm, lightDir), 0.0f);
    vec3 diffuse = diff * lightColor;

    vec3 result = (ambient + diffuse) * inputColor;
    color = vec4(result, 1.0f);
//    color = result;
}
