#version 300 es

precision mediump float;                

in vec4 v_colour;
out vec4 o_fragColour;         

void main()                             
{                                       
	o_fragColour = v_colour;
}