#version 330 core

out vec4 color;

uniform vec4 inputColor;
uniform vec4 lightColor;

void main()
{
    color = vec4(lightColor * inputColor);
}
