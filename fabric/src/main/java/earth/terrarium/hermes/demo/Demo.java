package earth.terrarium.hermes.demo;

import earth.terrarium.hermes.api.DefaultTagProvider;
import earth.terrarium.hermes.api.TagElement;
import earth.terrarium.hermes.api.themes.DefaultTheme;
import earth.terrarium.hermes.client.DocumentWidget;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Demo implements ClientModInitializer {

    public static final boolean DEMO = FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("hermes.demo");

    @Override
    public void onInitializeClient() {
        if (!DEMO) return;
        System.out.println("Hermes Demo Enabled");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, access) -> {
            dispatcher.register(ClientCommandManager.literal("hermesdemo").executes(context -> {
                try {
                    DefaultTagProvider provider = new DefaultTagProvider();
                    File file = FabricLoader.getInstance().getConfigDir().resolve("hermes-demo.txt").toFile();
                    String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    List<TagElement> elements = provider.parse(text);
                    Minecraft.getInstance().tell(() ->
                            Minecraft.getInstance().setScreen(new HermesDemoScreen(elements))
                    );
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }));
        });
    }

    private static class HermesDemoScreen extends Screen {

        private final List<TagElement> elements;

        protected HermesDemoScreen(List<TagElement> elements) {
            super(CommonComponents.EMPTY);
            this.elements = elements;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void init() {
            super.init();

            DocumentWidget widget = new DocumentWidget(0, 0, this.width, this.height, new DefaultTheme(), this.elements);
            this.addRenderableOnly(widget);
            ((List<GuiEventListener>) this.children()).add(widget);
        }
    }
}
