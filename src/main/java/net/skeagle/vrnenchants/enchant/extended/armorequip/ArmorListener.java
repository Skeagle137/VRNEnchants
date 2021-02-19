package net.skeagle.vrnenchants.enchant.extended.armorequip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ArmorListener implements Listener {

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public final void inventoryClick(final InventoryClickEvent e) {
        boolean shift = false, numberkey = false;
        if (e.getAction() == InventoryAction.NOTHING)
            return;
        if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT))
            shift = true;
        if (e.getClick().equals(ClickType.NUMBER_KEY))
            numberkey = true;
        if (e.getSlotType() != InventoryType.SlotType.ARMOR && e.getSlotType() != InventoryType.SlotType.QUICKBAR && e.getSlotType() != InventoryType.SlotType.CONTAINER)
            return;
        if (e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER))
            return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER))
            return;
        if (!(e.getWhoClicked() instanceof Player))
            return;
        ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
        if (!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot())
            return;
        if (shift) {
            newArmorType = ArmorType.matchType(e.getCurrentItem());
            if (newArmorType == null)
                return;
            boolean equipping = true;
            if (e.getRawSlot() == newArmorType.getSlot())
                equipping = false;
            if (newArmorType.equals(ArmorType.HELMET) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) ||
                    newArmorType.equals(ArmorType.CHESTPLATE) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) ||
                    newArmorType.equals(ArmorType.LEGGINGS) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) ||
                    newArmorType.equals(ArmorType.BOOTS) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getBoots()))) {
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getWhoClicked(),
                        ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType,
                        equipping ? null : e.getCurrentItem(),
                        equipping ? e.getCurrentItem() : null);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if (armorEquipEvent.isCancelled())
                    e.setCancelled(true);
            }
            return;
        }
        ItemStack newArmorPiece = e.getCursor();
        ItemStack oldArmorPiece = e.getCurrentItem();
        if (numberkey) {
            if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                if (!isAirOrNull(hotbarItem)) {
                    newArmorType = ArmorType.matchType(hotbarItem);
                    newArmorPiece = hotbarItem;
                    oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                } else {
                    newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
                }
            }
        } else {
            if (isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem()))
                newArmorType = ArmorType.matchType(e.getCurrentItem());
        }
        if (newArmorType != null && e.getRawSlot() == newArmorType.getSlot()) {
            ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
            if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey)
                method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled())
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.useItemInHand().equals(Event.Result.DENY))
            return;
        if (e.getAction() == Action.PHYSICAL)
            return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ArmorType newArmorType = ArmorType.matchType(e.getItem());
            if (newArmorType == null)
                return;
            if (newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) ||
                    newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) ||
                    newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) ||
                    newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())) {
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    e.setCancelled(true);
                    e.getPlayer().updateInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent e) {
        ArmorType type = ArmorType.matchType(e.getOldCursor());
        if (e.getRawSlots().isEmpty())
            return;
        if (type != null && type.getSlot() == e.getRawSlots().stream().findFirst().orElse(0)) {
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, e.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                e.setResult(Event.Result.DENY);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e) {
        ArmorType type = ArmorType.matchType(e.getBrokenItem());
        if (type != null) {
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                ItemStack i = e.getBrokenItem().clone();
                i.setAmount(1);
                if (i.hasItemMeta()) {
                    Damageable d = (Damageable) i.getItemMeta();
                    if (d != null)
                        d.setDamage(d.getDamage() - 1);
                }
                if (type.equals(ArmorType.HELMET))
                    p.getInventory().setHelmet(i);
                else if (type.equals(ArmorType.CHESTPLATE))
                    p.getInventory().setChestplate(i);
                else if (type.equals(ArmorType.LEGGINGS))
                    p.getInventory().setLeggings(i);
                else if (type.equals(ArmorType.BOOTS))
                    p.getInventory().setBoots(i);
            }
        }
    }

    @EventHandler
    public void entityDeathEvent(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player)
            return;
        for (ItemStack i : e.getEntity().getEquipment().getArmorContents())
            if (!isAirOrNull(i))
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(e.getEntity(), ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        if (e.getKeepInventory())
            return;
        for (ItemStack i : e.getEntity().getInventory().getArmorContents())
            if (!isAirOrNull(i))
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(e.getEntity(), ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
    }

    public static boolean isAirOrNull(ItemStack i){
        return i == null || i.getType().isAir();
    }
}
