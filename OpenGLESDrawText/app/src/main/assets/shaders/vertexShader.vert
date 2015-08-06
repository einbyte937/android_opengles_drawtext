#version 300 es

layout(location = 0) in vec4 a_position;
layout(location = 1) in vec4 a_colour;
	 
uniform mat4 OrthoMatrix;

out vec4 v_colour;

void main()                  
{                            
	gl_Position = OrthoMatrix * a_position;
	v_colour = a_colour;
}