package earth.terrarium.hermes.demo;

import com.teamresourceful.resourcefullib.client.screens.BaseCursorScreen;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import earth.terrarium.hermes.api.rendering.HtmlRenderer;
import earth.terrarium.hermes.api.rendering.HtmlStyle;
import earth.terrarium.hermes.elements.Parser;
import earth.terrarium.hermes.HermesWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Demo implements ClientModInitializer {

    public static final boolean DEMO = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("hermes.demo");

    @Override
    public void onInitializeClient() {
        if (!DEMO) return;
        System.out.println("Hermes Demo Enabled");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) ->
            dispatcher.register(ClientCommandManager.literal("hermesdemo").executes(context -> {
                try {
                    File file = FabricLoader.getInstance().getConfigDir().resolve("hermes-demo.html").toFile();
                    String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    Minecraft.getInstance().tell(() ->
                            Minecraft.getInstance().setScreen(new HermesDemoScreen(text))
                    );
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }))
        );
    }

    private static class HermesDemoScreen extends BaseCursorScreen {

        private final MineMarkElement<HtmlStyle, HtmlRenderer> parsed;
        private HermesWidget widget;

        protected HermesDemoScreen(String text) {
            super(CommonComponents.EMPTY);
            this.parsed = new Parser(DemoStyle.create()).parse(text);
        }

        @Override
        protected void init() {
            if (this.parsed == null) return;
            this.widget = addRenderableWidget(new HermesWidget(0, 0, width, height, this.parsed));
        }

        @Override
        public void onClose() {
            super.onClose();
            if (this.widget != null) this.widget.close();
        }
    }
}
