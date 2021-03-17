package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrnenchants.VRNEnchants;
import net.skeagle.vrnenchants.enchant.BaseEnchant;
import net.skeagle.vrnenchants.enchant.ICooldown;
import net.skeagle.vrnenchants.enchant.Rarity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class EnchMineSight extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchMineSight();

    private EnchMineSight() {
        super("Mine Sight", 3, EnchantmentTarget.TOOL);
        setRarity(Rarity.LEGENDARY);
        setCooldownMessage("&aYou are now able to use mine sight again.");
    }

    private final HashMap<Block, EntityShulker> blockCorrespondingEntity = new HashMap<>();
    private final ArrayList<EnumChatFormat> colorsUsed = new ArrayList<>();

    @Override
    protected void onInteract(final int level, final PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        final Player p = e.getPlayer();
        final ArrayList<Block> blocks = getBlocks(p.getLocation().getBlock(), level);
        outline(p, blocks, level);
        setCooldown(p);
    }

    public String setDescription() {
        return "Right clicking your pickaxe will allow you to see nearby ores for a few seconds.";
    }

    private ArrayList<Block> getBlocks(final Block start, final int level) {
        final int radius = (level * 2) + 3;
        final ArrayList<Block> blocks = new ArrayList<>();
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++)
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++)
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++)
                    blocks.add(new Location(start.getWorld(), x, y, z).getBlock());
        return filterBlocks(blocks);
    }

    private void outline(final Player p, ArrayList<Block> blocks, int duration) {
        for (final Block b : blocks) {
            final Location loc = b.getLocation();
            final WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
            final EntityShulker shulk = new EntityShulker(EntityTypes.SHULKER, world);
            shulk.setPositionRotation(loc.getX() + 0.5D, loc.getY(), loc.getZ() + 0.5D, 0, 0);
            shulk.setHeadRotation(0);
            shulk.setInvulnerable(true);
            shulk.setNoAI(true);
            shulk.setSilent(true);
            final DataWatcher watcher = shulk.getDataWatcher();
            watcher.set(new DataWatcherObject<>(0, DataWatcherRegistry.a), (byte) 96);
            final PacketPlayOutSpawnEntityLiving spawnEntityLiving = new PacketPlayOutSpawnEntityLiving(shulk);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawnEntityLiving);
            final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(shulk.getId(), watcher, false);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(entityMetadata);
            blockCorrespondingEntity.put(b, shulk);
        }
        for (EnumChatFormat color : colorsUsed)
            setTeams(p, color);
        Bukkit.getScheduler().runTaskLater(VRNEnchants.getInstance(), () -> {
            PacketPlayOutEntityDestroy destroy;
            for (final EntityShulker shulk : blockCorrespondingEntity.values()) {
                destroy = new PacketPlayOutEntityDestroy(shulk.getId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(destroy);
            }
        }, (long) (duration * 1.25) * 20);
    }

    @Override
    public int cooldown(int level) {
        return 60 * (5 - level);
    }

    private enum Sorter {
        COAL(EnumChatFormat.BLACK, Material.COAL_ORE),
        IRON(EnumChatFormat.GRAY, Material.IRON_ORE),
        GOLD(EnumChatFormat.GOLD, Material.GOLD_ORE),
        LAPIS(EnumChatFormat.BLUE, Material.LAPIS_ORE),
        REDSTONE(EnumChatFormat.RED, Material.REDSTONE_ORE),
        DIAMOND(EnumChatFormat.AQUA, Material.DIAMOND_ORE),
        EMERALD(EnumChatFormat.DARK_GREEN, Material.EMERALD_ORE),
        QUARTZ(EnumChatFormat.WHITE, Material.NETHER_QUARTZ_ORE),
        NETHERITE(EnumChatFormat.DARK_RED, Material.ANCIENT_DEBRIS),
        NETHER_GOLD(EnumChatFormat.YELLOW, Material.NETHER_GOLD_ORE),
        GILDED_BLACKSTONE(EnumChatFormat.YELLOW, Material.GILDED_BLACKSTONE);

        private final EnumChatFormat color;
        private final Material block;

        Sorter(final EnumChatFormat color, final Material block) {
            this.color = color;
            this.block = block;
        }

        public Material getBlock() {
            return block;
        }

        public static EnumChatFormat getColorFrom(final Block b) {
            for (final Sorter sorter : Sorter.values())
                if (b.getType() == sorter.block)
                    return sorter.color;
            return EnumChatFormat.WHITE;
        }
    }

    private void setTeams(Player p, EnumChatFormat color) {
        Scoreboard board = new Scoreboard();
        ScoreboardTeam team = new ScoreboardTeam(board, "MINESIGHT@@" + color.character);
        team.setPrefix(new ChatMessage("§" + color.character));
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS);
        team.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.ALWAYS);
        team.setDisplayName(new ChatMessage("minesight" + color.character));
        team.setColor(color);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(team, 0));
        for (Block b : blockCorrespondingEntity.keySet())
            if (Sorter.getColorFrom(b) == color)
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(
                        new PacketPlayOutScoreboardTeam(team, Collections.singletonList(String.valueOf(blockCorrespondingEntity.get(b).getUniqueID())), 3));
    }

    private ArrayList<Block> filterBlocks(final ArrayList<Block> blocks) {
        final ArrayList<Block> newBlocks = new ArrayList<>();
        for (final Block b : blocks)
            for (final Sorter sort : Sorter.values())
                if (b.getType() == sort.getBlock()) {
                    if (!colorsUsed.contains(Sorter.getColorFrom(b)))
                        colorsUsed.add(Sorter.getColorFrom(b));
                    newBlocks.add(b);
                }
        return newBlocks;
    }

    public static Enchantment getInstance() {
        return instance;
    }

}
