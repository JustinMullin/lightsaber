#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform float bladePower;
uniform vec2 bladePosition;
uniform vec4 bladeColor;

void main() {
  float glow = pow(1.0 - clamp(length(bladePosition - gl_FragCoord.xy)/500.0, 0.0, 1.0), 2.0);
  float lighting = pow(1.0 - clamp(length(bladePosition - gl_FragCoord.xy)/750.0, 0.0, 1.0), 1.5);
  gl_FragColor =
    v_color * texture2D(u_texture, v_texCoords) *
    (0.3+ 1.2*vec4(vec3(lighting+glow) * (vec3(0.7) + bladeColor.rgb * 0.3), 1) * bladePower);
}