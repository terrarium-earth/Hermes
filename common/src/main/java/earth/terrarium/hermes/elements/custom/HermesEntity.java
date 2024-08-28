package earth.terrarium.hermes.elements.custom;

import dev.dediamondpro.minemark.LayoutData;
import dev.dediamondpro.minemark.LayoutStyle;
import dev.dediamondpro.minemark.elements.Element;
import dev.dediamondpro.minemark.elements.Inline;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.base.BasicBasicElement;
import earth.terrarium.hermes.utils.AttributeParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.xml.sax.Attributes;

public class HermesEntity extends BasicBasicElement<HtmlStyle, HtmlRenderer> implements Inline {

    private final static int BLOCK_SIZE = 24;

    private final EntityType<?> type;
    private final CompoundTag tag;
    private final float scale;
    private final float offset;

    private float height;
    private float width;
    private Entity entity;

    public HermesEntity(@NotNull HtmlStyle style, @NotNull LayoutStyle layoutStyle, @Nullable Element<HtmlStyle, HtmlRenderer> parent, @NotNull String qName, @Nullable Attributes attributes) {
        super(style, layoutStyle, parent, qName, attributes);
        assert attributes != null;

        this.type = AttributeParser.parseEntityType(attributes, "type", null);
        this.tag = AttributeParser.parseNbt(attributes, "nbt", null);
        this.scale = AttributeParser.parseFloat(attributes, "scale", 1f);
        this.offset = AttributeParser.parseFloat(attributes, "offset", 0f);
        this.height = AttributeParser.parseFloat(attributes, "width", 0f);
        this.width = AttributeParser.parseFloat(attributes, "height", 0f);
    }

    private Entity getEntity() {
        if (this.entity != null) return this.entity;
        Level level = Minecraft.getInstance().level;
        if (level == null) return null;
        this.entity = this.type.create(level);
        if (this.tag != null && this.entity != null) {
            this.entity.load(this.tag);
        }
        return this.entity;
    }

    @Override
    protected void drawElement(float x, float y, float width, float height, float mouseX, float mouseY, HtmlRenderer renderData) {
        Entity entity = this.getEntity();
        if (!(entity instanceof LivingEntity living)) return;

        float scale = this.scale * BLOCK_SIZE;

        float f = (x + x + width) / 2.0F;
        float g = (y + y + width) / 2.0F;
        float h = (float)Math.atan((f - mouseX) / 40.0F);
        float i = (float)Math.atan((g - mouseY) / 40.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(i * 20.0F * (float) (Math.PI / 180.0));
        quaternionf.mul(quaternionf2);
        float j = living.yBodyRot;
        float k = living.getYRot();
        float l = living.getXRot();
        float m = living.yHeadRotO;
        float n = living.yHeadRot;
        living.yBodyRot = 180.0F + h * 20.0F;
        living.setYRot(180.0F + h * 40.0F);
        living.setXRot(-i * 20.0F);
        living.yHeadRot = living.getYRot();
        living.yHeadRotO = living.getYRot();
        float o = living.getScale();
        Vector3f vector3f = new Vector3f(0.0F, 0.25f + living.getBbHeight() / 2.0F + this.offset * o, 0.0F);
        float p = scale / o;
        InventoryScreen.renderEntityInInventory(renderData.getGraphics(), f, g, p, vector3f, quaternionf, quaternionf2, living);
        living.yBodyRot = j;
        living.setYRot(k);
        living.setXRot(l);
        living.yHeadRotO = m;
        living.yHeadRot = n;
    }

    @Override
    protected float getWidth(LayoutData layoutData, HtmlRenderer renderData) {
        Entity entity = this.getEntity();
        if (!(entity instanceof LivingEntity living)) return 0f;
        this.width = this.width == 0 ? living.getBbWidth() : this.width;

        int scale = Math.round(this.scale * BLOCK_SIZE);
        return this.width * scale;
    }

    @Override
    protected float getHeight(LayoutData layoutData, HtmlRenderer renderData) {
        Entity entity = this.getEntity();
        if (!(entity instanceof LivingEntity living)) return 0f;
        this.height = this.height == 0 ? living.getBbHeight() : this.height;

        int scale = Math.round(this.scale * BLOCK_SIZE);
        return this.height * scale;
    }

    @Override
    protected float getPadding(LayoutData layoutData, HtmlRenderer renderData) {
        return 8f;
    }
}
