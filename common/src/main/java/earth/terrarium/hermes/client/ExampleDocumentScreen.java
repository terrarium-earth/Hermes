package earth.terrarium.hermes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.hermes.api.DefaultTagProvider;
import earth.terrarium.hermes.api.TagProvider;
import earth.terrarium.hermes.api.themes.DefaultTheme;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public class ExampleDocumentScreen extends Screen {

    public ExampleDocumentScreen() {
        super(CommonComponents.EMPTY);
    }

    @Override
    protected void init() {
        super.init();
        String text = """
            <p>Column Test</p>
            <carousel height="100" index="1">
                <carousel-item>
                    <p>Test</p>
                </carousel-item>
                <carousel-item>
                    <img src="minecraft:textures/block/stone.png" width="100" height="100" textureWidth="16" textureHeight="16"/>
                </carousel-item>
            </carousel>
            """;
        TagProvider provider = new DefaultTagProvider();
        addRenderableWidget(new DocumentWidget(this.width / 2 - 100, 10, 200, 200, new DefaultTheme(), provider.parse(text)));
    }

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
    }
}
