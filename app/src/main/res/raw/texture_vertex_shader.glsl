uniform mat4 u_MVPMatrix;

attribute vec3 a_Position;
attribute vec4 a_Color;

attribute vec2 a_TexCoordinate;

varying vec2 v_TexCoordinate;
varying vec4 v_Color;
		  

void main()                                                 	
{
    v_Color = a_Color;
	v_TexCoordinate = a_TexCoordinate;

          
	// gl_Position is a special variable used to store the final position.
	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * vec4(a_Position,1);
}                                                          