package earth.terrarium.hermes.shader.impl;

import earth.terrarium.hermes.shader.Shader;
import earth.terrarium.hermes.shader.Uniform;
import net.minecraft.Util;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RoundedRectShader extends Shader {

    private static final String VERTEX = """
    #version 150
    
    in vec3 Position;
    
    uniform mat4 modelViewMat;
    uniform mat4 projMat;
    
    void main() {
        gl_Position = projMat * modelViewMat * vec4(Position, 1.0);
    }
    """;

    private static final String FRAGMENT = """
    #version 150
    
    uniform mat4 modelViewMat;
    uniform mat4 projMat;
    uniform vec4 backgroundColor;
    uniform vec4 borderColor;
    uniform vec4 borderRadius;
    uniform float borderWidth;
    uniform vec2 size;
    uniform vec2 center;
    uniform float scaleFactor;
    
    out vec4 fragColor;
    
    // From: https://iquilezles.org/articles/distfunctions2d/
    float sdRoundedBox(vec2 p, vec2 b, vec4 r){
        r.xy = (p.x > 0.0) ? r.xy : r.zw;
        r.x  = (p.y > 0.0) ? r.x  : r.y;
        vec2 q = abs(p)-b+r.x;
        return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - r.x;
    }
    
    void main() {
        vec4 color = backgroundColor;
        if (color.a == 0.0) {
            discard;
        }

        vec2 halfSize = size / 2.0;
        float distance = sdRoundedBox(gl_FragCoord.xy - center, halfSize, borderRadius * scaleFactor);
        float smoothed = 1.0 - distance;
        float border = 1.0 - smoothstep(borderWidth, borderWidth, abs(distance));
    
        fragColor = mix(mix(vec4(0.0), borderColor, border), color, smoothed);
    }
    """;

    private static final RoundedRectShader INSTANCE = Util.make(new RoundedRectShader(), Shader::compile);

    private Matrix4f modelViewMat;
    private Matrix4f projectionMat;

    private Vector4f backgroundColor;
    private Vector4f borderColor;
    private Vector4f borderRadius;
    private float borderWidth;
    private Vector2f size;
    private Vector2f center;
    private float scaleFactor;

    protected RoundedRectShader() {
        super(VERTEX, FRAGMENT);
    }

    @Override
    protected void addUniforms() {
        addUniform(Uniform.Type.MAT4, "modelViewMat", () -> modelViewMat);
        addUniform(Uniform.Type.MAT4, "projMat", () -> projectionMat);
        addUniform(Uniform.Type.VEC4, "backgroundColor", () -> backgroundColor);
        addUniform(Uniform.Type.VEC4, "borderColor", () -> borderColor);
        addUniform(Uniform.Type.VEC4, "borderRadius", () -> borderRadius);
        addUniform(Uniform.Type.FLOAT, "borderWidth", () -> borderWidth);
        addUniform(Uniform.Type.VEC2, "size", () -> size);
        addUniform(Uniform.Type.VEC2, "center", () -> center);
        addUniform(Uniform.Type.FLOAT, "scaleFactor", () -> scaleFactor);
    }

    public static boolean use(
            Matrix4f modelViewMat, Matrix4f projectionMat,
            Vector4f backgroundColor, Vector4f borderColor,
            Vector4f borderRadius, float borderWidth,
            Vector2f size, Vector2f center, float scaleFactor
    ) {
        if (!INSTANCE.compiled) return false;
        INSTANCE.modelViewMat = modelViewMat;
        INSTANCE.projectionMat = projectionMat;
        INSTANCE.backgroundColor = backgroundColor;
        INSTANCE.borderColor = borderColor;
        INSTANCE.borderRadius = borderRadius;
        INSTANCE.borderWidth = borderWidth;
        INSTANCE.size = size;
        INSTANCE.center = center;
        INSTANCE.scaleFactor = scaleFactor;
        INSTANCE.enable();
        INSTANCE.uploadUniforms();
        return true;
    }

    public static void unuse() {
        INSTANCE.disable();
    }
}
