package net.skeagle.vrnenchants.enchant.armor.armorequip;


import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;

public final class ArmorEquipEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private final EquipMethod equipType;
    private final ArmorType type;
    private ItemStack oldArmorPiece, newArmorPiece;

    public ArmorEquipEvent(LivingEntity e, EquipMethod equipType, ArmorType type, ItemStack oldArmorPiece, ItemStack newArmorPiece) {
        super(e);
        this.equipType = equipType;
        this.type = type;
        this.oldArmorPiece = oldArmorPiece;
        this.newArmorPiece = newArmorPiece;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public ArmorType getType() {
        return type;
    }

    public void setOldArmorPiece(ItemStack oldArmorPiece) {
        this.oldArmorPiece = oldArmorPiece;
    }

    public ItemStack getOldArmorPiece() {
        return oldArmorPiece;
    }

    public void setNewArmorPiece(ItemStack newArmorPiece) {
        this.newArmorPiece = newArmorPiece;
    }

    public ItemStack getNewArmorPiece() {
        return newArmorPiece;
    }

    public EquipMethod getEquipType() {
        return equipType;
    }

    public enum EquipMethod {
        SHIFT_CLICK,
        DRAG,
        PICK_DROP,
        HOTBAR,
        HOTBAR_SWAP,
        DISPENSER,
        BROKE,
        DEATH
    }
}
