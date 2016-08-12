precision mediump float;

varying vec2 v_TexCoordinate;

//varying vec4 v_Color;

uniform sampler2D u_Texture;
  
// The entry point for our fragment shader.
void main()                    		
{                              	
	// Pass through the color
    gl_FragColor = texture2D(u_Texture,v_TexCoordinate);
}