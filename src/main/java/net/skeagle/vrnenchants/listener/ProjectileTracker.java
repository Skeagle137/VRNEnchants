package net.skeagle.vrnenchants.listener;

import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.mineacademy.fo.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectileTracker implements Listener {

    private static final Map<UUID, IDetectGround> projectiles = new HashMap<>();

    public static void track(Projectile projectile, IDetectGround inground) {
        projectiles.put(projectile.getUniqueId(), inground);
        Common.runLater(600, () -> projectiles.remove(projectile.getUniqueId()));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onHit(ProjectileHitEvent e) {
        IDetectGround inground = projectiles.remove(e.getEntity().getUniqueId());
        if (inground != null) {
            inground.run(e);
        }
    }

    public interface IDetectGround {
        void run(ProjectileHitEvent e);
    }
}
