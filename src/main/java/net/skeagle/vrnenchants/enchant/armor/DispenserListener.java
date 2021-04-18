package net.skeagle.vrnenchants.enchant.armor;

import net.skeagle.vrnenchants.enchant.armor.armorequip.ArmorEquipEvent;
import net.skeagle.vrnenchants.enchant.armor.armorequip.ArmorType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;

public class DispenserListener implements Listener {

    @EventHandler
    public void dispenseArmorEvent(BlockDispenseArmorEvent e) {
        ArmorType type = ArmorType.matchType(e.getItem());
        if (type != null) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getTargetEntity(), ArmorEquipEvent.EquipMethod.DISPENSER, type, null, e.getItem());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled())
                e.setCancelled(true);
        }
    }
}
