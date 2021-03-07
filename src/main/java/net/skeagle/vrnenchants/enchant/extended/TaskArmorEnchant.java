package net.skeagle.vrnenchants.enchant.extended;

import net.skeagle.vrnenchants.VRNMain;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.extended.armorequip.ArmorEquipEvent;
import net.skeagle.vrnenchants.enchant.extended.armorequip.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.BiConsumer;

public class TaskArmorEnchant extends BukkitRunnable implements Listener {

    private static final HashMap<UUID, ItemStack> entityMap = new HashMap<>();

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            for (Entity entity : w.getEntities()) {
                if (entity instanceof LivingEntity) {
                    if (entityMap.containsKey(entity.getUniqueId())) {
                        LivingEntity e = (LivingEntity) entity;
                        Bukkit.getScheduler().runTaskLater(VRNMain.getInstance(), () -> {
                            ItemStack[] armor = updateArmor(e, entityMap.get(e.getUniqueId()), false);
                            run((ench, level) -> ench.onTick(e, getParts(armor), level), entityMap.get(e.getUniqueId()));
                        }, 0);
                    }
                }
            }
        }
    }

    private void run(BiConsumer<ArmorEnchant, Integer> ench, ItemStack piece) {
        ArrayList<ArmorEnchant> temp_cache = new ArrayList<>();
        for (final Map.Entry<BaseEnchant, Integer> e : BaseEnchant.getEnchants(piece).entrySet())
            if (e.getKey() instanceof ArmorEnchant && !temp_cache.contains(e.getKey())) {
                temp_cache.add((ArmorEnchant) e.getKey());
                ench.accept((ArmorEnchant) e.getKey(), e.getValue());
            }
    }

    private boolean isArmorNotNull(ItemStack i) {
        return i != null && !i.getType().isAir();
    }

    private int getParts(ItemStack[] armor) {
        int parts = 0;
        for (ItemStack piece : armor) {
            for (int i = 0; i < 4; i++)
                if (isArmorNotNull(piece))
                    parts++;
        }
        return parts;
    }

    private ItemStack[] updateArmor(LivingEntity entity, ItemStack piece, boolean setNull) {
        ItemStack[] armor = entity.getEquipment().getArmorContents().clone();
        ArmorType type = ArmorType.matchType(piece);
        Integer index = null;
        if (type == ArmorType.BOOTS)
            index = 0;
        if (type == ArmorType.LEGGINGS)
            index = 1;
        if (type == ArmorType.CHESTPLATE)
            index = 2;
        if (type == ArmorType.HELMET)
            index = 3;
        if (index != null) {
            if (setNull)
                armor[index] = null;
            else
                armor[index] = piece;
        }
        return armor;
    }

    @EventHandler
    public void equip(ArmorEquipEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity en = (LivingEntity) e.getEntity();
        if (isArmorNotNull(e.getOldArmorPiece()) && isArmorNotNull(e.getNewArmorPiece())) {
            if (ArmorType.matchType(e.getNewArmorPiece()) == null) return;
                run((ench, level) -> ench.onUnEquip(en, getParts(updateArmor(en, e.getOldArmorPiece(), false)), level), e.getOldArmorPiece());
                run((ench, level) -> ench.onEquip(en, getParts(updateArmor(en, e.getNewArmorPiece(), false)), level), e.getNewArmorPiece());
            return;
        }
        if (isArmorNotNull(e.getNewArmorPiece())) {
            entityMap.put(en.getUniqueId(), e.getNewArmorPiece());
            ItemStack armorpiece = e.getNewArmorPiece();
            ItemStack[] armor = updateArmor(en, armorpiece, false);
            run((ench, level) -> ench.onEquip(en, getParts(armor), level), armorpiece);
        }
        else {
            if (getParts(updateArmor(en, e.getOldArmorPiece(), true)) != 0) {
                entityMap.put(en.getUniqueId(), e.getOldArmorPiece());
                run((ench, level) -> ench.onUnEquip(en, getParts(updateArmor(en, e.getOldArmorPiece(), true)), level), e.getOldArmorPiece());
                return;
            }
            run((ench, level) -> ench.onUnEquip(en, 0, level), e.getOldArmorPiece());
            entityMap.remove(en.getUniqueId());
        }
    }
}
