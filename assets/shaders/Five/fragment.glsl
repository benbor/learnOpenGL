#version 330 core
out vec4 color;

in vec3 Normal;
in vec3 FragPosition;

uniform vec3 lightPosition;
uniform vec3 viewPosition;

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

    // Specualar
    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPosition - FragPosition);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0f), 256);
//    float spec = pow(dot(viewDir, reflectDir), 32);
    vec3 specular = specularStrength * spec * lightColor;

    //combine
    vec3 result = (ambient + diffuse + specular) * inputColor;
    color = vec4(result, 1.0f);
//    color = result;
}
