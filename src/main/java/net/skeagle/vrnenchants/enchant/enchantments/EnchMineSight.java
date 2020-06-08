package net.skeagle.vrnenchants.enchant.enchantments;

import lombok.Getter;
import net.minecraft.server.v1_15_R1.*;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.inventivetalent.glow.GlowAPI;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.HashMap;

import static net.skeagle.vrnenchants.util.VRNUtil.sayActionBar;

public class EnchMineSight extends BaseEnchant {

    @Getter
    private static final Enchantment instance = new EnchMineSight();

    private EnchMineSight() {
        super("Mine Sight", 3);
        setRarity(100);
        setRarityFactor(40);
    }

    private final HashMap<Block, EntityShulker> blockCorrespondingEntity = new HashMap<>();
    private final ArrayList<Player> cooldown = new ArrayList<>();

    @Override
    protected void onInteract(final int level, final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Player p = e.getPlayer();
        if (cooldown.contains(p)) {
            sayActionBar(p, "&cYou cannot do this yet.");
            return;
        }
        final ArrayList<Block> blocks = getBlocks(p.getLocation().getBlock(), level);
        for (final Block b : blocks) {
            outline(p, b);
        }
        doGlow(blockCorrespondingEntity, p);
        cooldown.add(p);
        Common.runLater((level) * 20, () -> {
            PacketPlayOutEntityDestroy destroy;
            for (final EntityShulker shulk : blockCorrespondingEntity.values()) {
                destroy = new PacketPlayOutEntityDestroy(shulk.getId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
            }
        });
        Common.runLater(20 * (60 * (6 - level)), () -> {
            cooldown.remove(p);
            sayActionBar(p, "&aYou are now able to use mine sight again.");
        });
    }

    public String setDescription() {
        return "Right clicking your pickaxe will allow you to see nearby ores for a few seconds.";
    }

    private ArrayList<Block> getBlocks(final Block start, final int level) {
        final int radius = level + 3;
        final ArrayList<Block> blocks = new ArrayList<>();
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {
                    final Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return filterBlocks(blocks);
    }

    private void outline(final Player p, final Block b) {
        final Location loc = b.getLocation();
        final WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
        final EntityShulker shulk = new EntityShulker(EntityTypes.SHULKER, world);
        shulk.setPositionRotation(loc.getX() + 0.5D, loc.getY(), loc.getZ() + 0.5D, 0, 0);
        shulk.setHeadRotation(0);
        shulk.setInvisible(true);
        shulk.setInvulnerable(true);
        shulk.setNoAI(true);
        shulk.setSilent(true);
        shulk.setFlag(6, true);
        final PacketPlayOutSpawnEntityLiving spawnEntityLiving = new PacketPlayOutSpawnEntityLiving(shulk);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawnEntityLiving);
        final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(shulk.getId(), shulk.getDataWatcher(), false);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(entityMetadata);
        blockCorrespondingEntity.put(b, shulk);
    }

    private enum Sorter {
        COAL(GlowAPI.Color.BLACK, CompMaterial.COAL_ORE),
        IRON(GlowAPI.Color.GRAY, CompMaterial.IRON_ORE),
        GOLD(GlowAPI.Color.GOLD, CompMaterial.GOLD_ORE),
        LAPIS(GlowAPI.Color.BLUE, CompMaterial.LAPIS_ORE),
        REDSTONE(GlowAPI.Color.DARK_RED, CompMaterial.REDSTONE_ORE),
        DIAMOND(GlowAPI.Color.AQUA, CompMaterial.DIAMOND_ORE),
        EMERALD(GlowAPI.Color.DARK_GREEN, CompMaterial.EMERALD_ORE),
        QUARTZ(GlowAPI.Color.WHITE, CompMaterial.NETHER_QUARTZ_ORE);

        private final GlowAPI.Color color;
        private final CompMaterial block;

        Sorter(final GlowAPI.Color color, final CompMaterial block) {
            this.color = color;
            this.block = block;
        }

        public GlowAPI.Color getColor() {
            return color;
        }

        public CompMaterial getBlock() {
            return block;
        }

        public GlowAPI.Color getColorFrom(final Block b) {
            for (final Sorter sorter : Sorter.values()) {
                if (b.getType() == sorter.getBlock().getMaterial()) {
                    return sorter.getColor();
                }
            }
            return GlowAPI.Color.WHITE;
        }
    }

    private void doGlow(final HashMap<Block, EntityShulker> shulkMap, final Player p) {
        for (final Sorter sort : Sorter.values()) {
            for (final Block b : shulkMap.keySet()) {
                GlowAPI.setGlowing(shulkMap.get(b).getBukkitEntity(), sort.getColorFrom(b), p);
            }
        }
    }

    private ArrayList<Block> filterBlocks(final ArrayList<Block> blocks) {
        final ArrayList<Block> newBlocks = new ArrayList<>();
        for (final Block b : blocks) {
            for (final Sorter sort : Sorter.values()) {
                if (b.getType() == sort.getBlock().getMaterial()) {
                    newBlocks.add(b);
                }
            }
        }
        return newBlocks;
    }

}
