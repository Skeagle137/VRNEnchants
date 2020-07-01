package net.skeagle.vrnenchants.listener;

import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackHit implements Listener {

    private static Map<UUID, IDetectGround> activeProjectiles = new HashMap<>();

    public static void trackhit(Projectile projectile, IDetectGround inground) {
        activeProjectiles.put(projectile.getUniqueId(), inground);
    }

    @EventHandler
    public void trackProjectile(ProjectileLaunchEvent e) {

    }
}
