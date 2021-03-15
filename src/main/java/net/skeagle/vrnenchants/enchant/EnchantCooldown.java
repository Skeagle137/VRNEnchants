package net.skeagle.vrnenchants.enchant;

import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantCooldown {

    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public final void set(LivingEntity e, long cooldown) {
        cooldownMap.put(e.getUniqueId(), cooldown);
    }

    public final boolean contains(LivingEntity e) {
        return cooldownMap.containsKey(e.getUniqueId());
    }

    public final void remove(LivingEntity e) {
        cooldownMap.remove(e.getUniqueId());
    }

    public long get(LivingEntity e){
        return cooldownMap.getOrDefault(e.getUniqueId(),30L);
    }

    public enum CooldownMessageType {
        ACTION,
        CHAT
    }
}
