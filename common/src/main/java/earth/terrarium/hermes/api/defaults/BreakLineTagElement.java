package earth.terrarium.hermes.api.defaults;

import earth.terrarium.hermes.api.TagElement;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class BreakLineTagElement implements TagElement {

    public BreakLineTagElement(Map<String, String> parameters) {}

    @Override
    public int getHeight(int width) {
        return Minecraft.getInstance().font.lineHeight;
    }
}
