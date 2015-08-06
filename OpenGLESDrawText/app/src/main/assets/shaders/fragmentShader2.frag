#version 300 es

precision mediump float;                

in vec4 v_color;
in vec2 v_texCoord;                     

layout(location = 1) out vec4 outColor;                     

uniform sampler2D s_baseMap;            
uniform sampler2D s_lightMap;           
uniform sampler2D s_backgroundMap; 

void main( )                             
{                                       
	vec4 baseColor;                       
  	vec4 lightColor;                      
                                        
  	baseColor = texture( s_baseMap, v_texCoord ); 
  	lightColor = texture( s_lightMap, v_texCoord );

  	outColor = baseColor * (lightColor + 0.25);   
}