package millenniumambiguity.nomoresingleplayerlatency;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Constants.MOD_ID)
public class NoMoreSinglePlayerLatencyMod {

    public NoMoreSinglePlayerLatencyMod(IEventBus modBus) {
        CommonClass.init();

        NeoForge.EVENT_BUS.register(InputEvent.class);
        NeoForge.EVENT_BUS.register(KnockbackEvent.class);
    }
}