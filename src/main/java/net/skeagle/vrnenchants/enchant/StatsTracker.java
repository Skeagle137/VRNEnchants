package net.skeagle.vrnenchants.enchant;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class StatsTracker implements Listener {

    boolean may_use_firststrike;

    public StatsTracker(UUID uuid) {

    }

    @EventHandler
    public void calcFirstStrike(EntityDamageByEntityEvent e) {

    }
}
