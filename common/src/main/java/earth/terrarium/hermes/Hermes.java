package earth.terrarium.hermes;

import earth.terrarium.hermes.client.ExampleDocumentScreen;
import net.minecraft.client.Minecraft;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Hermes {
    public static final String MOD_ID = "hermes";

    public static void test() {
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {
            Minecraft.getInstance().executeBlocking(() -> {
                Minecraft.getInstance().setScreen(new ExampleDocumentScreen());
            });
        });
    }
}