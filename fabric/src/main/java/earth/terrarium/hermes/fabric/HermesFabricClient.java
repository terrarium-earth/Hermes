package earth.terrarium.hermes.fabric;

import earth.terrarium.hermes.Hermes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class HermesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("hermes").then(
                ClientCommandManager.literal("test").executes(context -> {
                    Hermes.test();
                    return 1;
                })
            ));
        });
    }
}
