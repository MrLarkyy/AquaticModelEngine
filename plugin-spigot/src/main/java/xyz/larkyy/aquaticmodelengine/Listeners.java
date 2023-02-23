package xyz.larkyy.aquaticmodelengine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer().getUniqueId());
        if (holder == null) {
            return;
        }
        AquaticModelEngine.getInstance().getModelHandler().removeHolder(holder);
    }
}
