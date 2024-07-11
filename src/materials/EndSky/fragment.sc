$input v_texcoord0, v_posTime

#include <bgfx_shader.sh>
#include <newb/main.sh>

SAMPLER2D(s_MatTexture, 0);

void main() {
  vec4 diffuse = texture2D(s_MatTexture, v_texcoord0);

  // end sky gradient
  vec3 color = renderEndSky(getEndHorizonCol(), getEndZenithCol(), normalize(v_posTime.xyz), v_posTime.w);

  // stars
  color += mix(vec3(0.0, 0.0, 0.0), 1.4*diffuse.rgb, 0.7*smoothstep(-0.5, 1.0, normalize(v_posTime.y)));

  color = colorCorrection(color);

  gl_FragColor = vec4(color, 1.0);
}
