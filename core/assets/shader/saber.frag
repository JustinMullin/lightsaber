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

uniform vec2 bladeStart;
uniform vec2 bladeEnd;
uniform vec4 bladeColor;

float distanceFromBlade(vec2 v) {
  vec2 angleToBlade = v - bladeStart;
  vec2 bladeNormal = bladeEnd - bladeStart;
  float incident = clamp(dot(angleToBlade, bladeNormal) / dot(bladeNormal, bladeNormal), 0.0, 1.0);
  float narrowFactor = length(bladeStart-v) / length(bladeStart-bladeEnd) * 0.75;
  return length(angleToBlade - bladeNormal*incident) + narrowFactor;
}

void main() {
  vec2 v = gl_FragCoord.xy;
  float distance = distanceFromBlade(v);
  float blade = ceil(max(0.0, 4.0-distance));
  float bladeEdgeDim =
    -min(0.0, sign(
        (bladeEnd.x - bladeStart.x) * (v.y - bladeStart.y) -
        (bladeEnd.y - bladeStart.y) * (v.x - bladeStart.x))) *
    distance;
  float glow = 1.0-clamp(distance/10.0, 0.0, 1.0);
  float halo = 1.0-clamp(distance/40.0, 0.0, 1.0);
  gl_FragColor = vec4(
    vec3(blade)*(vec3(0.9)+bladeColor.rgb*0.1) -
    (bladeEdgeDim * (1.0 - bladeColor.rgb)) +
    (vec3(glow*0.5 + halo*0.4) * bladeColor.rgb),
    1);
}