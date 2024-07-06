package earth.terrarium.hermes.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.logging.LogUtils;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class Shader {

    private static final Logger LOGGER = LogUtils.getLogger();

    protected final String vertexShader;
    protected final String fragmentShader;

    protected final List<Uniform<?>> uniforms = new ArrayList<>();
    protected boolean compiled = false;
    protected int program;

    protected Shader(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void compile() {
        if (this.compiled) return;
        if (this.vertexShader.isEmpty() || this.fragmentShader.isEmpty()) return;

        int vertexShaderId = GlStateManager.glCreateShader(GL20.GL_VERTEX_SHADER);
        GlStateManager.glShaderSource(vertexShaderId, List.of(this.vertexShader));
        GlStateManager.glCompileShader(vertexShaderId);
        if (GlStateManager.glGetShaderi(vertexShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            LOGGER.error("Failed to compile vertex shader: {}", GL20.glGetShaderInfoLog(vertexShaderId));
            return;
        }

        int fragmentShaderId = GlStateManager.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GlStateManager.glShaderSource(fragmentShaderId, List.of(this.fragmentShader));
        GlStateManager.glCompileShader(fragmentShaderId);
        if (GlStateManager.glGetShaderi(fragmentShaderId, GL20.GL_COMPILE_STATUS) == 0) {
            LOGGER.error("Failed to compile fragment shader: {}", GL20.glGetShaderInfoLog(fragmentShaderId));
            return;
        }

        this.program = GlStateManager.glCreateProgram();
        GlStateManager.glAttachShader(this.program, vertexShaderId);
        GlStateManager.glAttachShader(this.program, fragmentShaderId);
        GlStateManager.glLinkProgram(this.program);
        if (GlStateManager.glGetProgrami(this.program, GL20.GL_LINK_STATUS) == 0) {
            LOGGER.error("Failed to link program: {}", GL20.glGetProgramInfoLog(this.program));
            return;
        }

        GlStateManager.glDeleteShader(vertexShaderId);
        GlStateManager.glDeleteShader(fragmentShaderId);

        addUniforms();

        this.compiled = true;
    }

    protected abstract void addUniforms();

    protected <T> void addUniform(Uniform.Type type, String name, Supplier<T> value) {
        this.uniforms.add(new Uniform<>(this, type, name, value));
    }

    public void enable() {
        if (!this.compiled) throw new IllegalStateException("Shader not compiled");
        GlStateManager._glUseProgram(this.program);
    }

    public void disable() {
        GlStateManager._glUseProgram(0);
    }

    public void uploadUniforms() {
        this.uniforms.forEach(Uniform::upload);
    }
}
