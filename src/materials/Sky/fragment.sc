#ifdef OPAQUE
$input v_fogColor, v_worldPos, v_underwaterRainTime
#endif

#include <bgfx_shader.sh>
#include <newb/main.sh>

void main() {
#ifdef OPAQUE
  vec3 viewDir = normalize(v_worldPos);
  bool underWater = v_underwaterRainTime.x > 0.5;
  float rainFactor = v_underwaterRainTime.y;

  vec3 zenithCol;
  vec3 horizonCol;
  vec3 horizonEdgeCol;
  if (underWater) {
    vec3 fogcol = getUnderwaterCol(v_fogColor);
    zenithCol = fogcol;
    horizonCol = fogcol;
    horizonEdgeCol = fogcol;
  } else {
    vec3 fs = getSkyFactors(v_fogColor);
    zenithCol = getZenithCol(rainFactor, v_fogColor, fs);
    horizonCol = getHorizonCol(rainFactor, v_fogColor, fs);
    horizonEdgeCol = getHorizonEdgeCol(horizonCol, rainFactor, v_fogColor);
  }

  vec3 skyColor = nlRenderSky(horizonEdgeCol, horizonCol, zenithCol, -viewDir, v_fogColor, v_underwaterRainTime.z, rainFactor, false, underWater, false);
  
  vec4 ars = renderAurora(v_worldPos, v_underwaterRainTime.z, rainFactor, v_fogColor);
  
  float fade = clamp(2.0-2.0*length(v_worldPos)*0.0022, 0.0, 1.0);

  #if defined(NL_AURORA) && !defined(AURORA_IN_VC)
    skyColor.rgb += mix(vec3(0.0, 0.0, 0.0), ars.rgb, ars.a*fade);
  #endif
  
  skyColor = colorCorrection(skyColor);

  gl_FragColor = vec4(skyColor, 1.0);
#else
  gl_FragColor = vec4(0.0,0.0,0.0,0.0);
#endif
}
