package earth.terrarium.hermes.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.function.Supplier;

public class Uniform<T> {

    private final int id;
    private final Type type;
    private final Supplier<T> value;

    private T lastValue;

    public Uniform(Shader shader, Type type, String name, Supplier<T> value) {
        this.id = GlStateManager._glGetUniformLocation(shader.program, name);
        this.type = type;
        this.value = value;
    }

    public void upload() {
        if (id == -1) return;
        if (value == null) return;
        if (type == null) return;
        T newValue = value.get();
        if (newValue == null) return;
        if (newValue.equals(lastValue)) return;
        lastValue = newValue;
        switch (type) {
            case FLOAT -> GL20.glUniform1f(this.id, (Float) newValue);
            case INT -> GL20.glUniform1i(this.id, (Integer) newValue);
            case BOOL -> GL20.glUniform1i(this.id, (Boolean) newValue ? 1 : 0);
            case VEC2 -> {
                Vector2f vec2 = (Vector2f) newValue;
                GL20.glUniform2f(this.id, vec2.x, vec2.y);
            }
            case VEC3 -> {
                Vector3f vec3 = (Vector3f) newValue;
                GL20.glUniform3f(this.id, vec3.x, vec3.y, vec3.z);
            }
            case VEC4 -> {
                Vector4f vec4 = (Vector4f) newValue;
                GL20.glUniform4f(this.id, vec4.x, vec4.y, vec4.z, vec4.w);
            }
            case MAT4 -> {
                Matrix4f mat4 = (Matrix4f) newValue;
                GL20.glUniformMatrix4fv(this.id, false, mat4.get(new float[16]));
            }
            case TEXTURE0 -> {
                int i = GlStateManager._getActiveTexture();
                ResourceLocation id = (ResourceLocation) newValue;
                TextureManager manager = Minecraft.getInstance().getTextureManager();
                AbstractTexture texture = manager.getTexture(id);
                GL20.glUniform1i(this.id, type.extraInfo);
                RenderSystem.activeTexture(GL13.GL_TEXTURE0 + type.extraInfo);
                RenderSystem.bindTexture(texture.getId());
                RenderSystem.activeTexture(i);
            }
        }
    }

    public enum Type {
        FLOAT,
        INT,
        BOOL,
        VEC2,
        VEC3,
        VEC4,
        MAT4,
        TEXTURE0(0),
        ;

        private final int extraInfo;

        Type() {
            this.extraInfo = 0;
        }

        Type(int extraInfo) {
            this.extraInfo = extraInfo;
        }
    }
}
