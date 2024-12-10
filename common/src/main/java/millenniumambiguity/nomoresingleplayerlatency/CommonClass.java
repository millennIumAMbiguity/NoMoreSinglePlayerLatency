package millenniumambiguity.nomoresingleplayerlatency;

import net.minecraft.client.Minecraft;

public class CommonClass {

    private final Minecraft mc = Minecraft.getInstance();

    public boolean isSinglePlayer() {
        // Check if the client is connected to an integrated server
        return mc.hasSingleplayerServer();
    }

    public static void init() { }
}