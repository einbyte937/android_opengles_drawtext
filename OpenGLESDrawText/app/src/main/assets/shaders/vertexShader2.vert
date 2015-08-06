#version 300 es

layout(location = 0) in vec4 a_position;
layout(location = 1) in vec4 a_color;   	   
layout(location = 3) in vec2 a_texCoord;  	   

uniform mat4 OrthoMatrix;

out vec4 v_color;
out vec2 v_texCoord;     	 

void main()                  
{                            
	gl_Position = OrthoMatrix * a_position;
	v_color = a_color;
    v_texCoord = a_texCoord; 
}