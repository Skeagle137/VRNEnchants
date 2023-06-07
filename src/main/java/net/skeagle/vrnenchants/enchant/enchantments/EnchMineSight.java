package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.skeagle.vrnenchants.enchant.*;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.LocationUtils;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

@EnchDescription("Right clicking your pickaxe will allow you to see nearby ores for a few seconds.")
public class EnchMineSight extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchMineSight();

    private final Map<UUID, ShulkerBlockData> shulkerData = new HashMap<>();

    private EnchMineSight() {
        super("Mine Sight", 3, Target.PICKAXES);
        setRarity(Rarity.LEGENDARY);
        setCooldownMessage("&aYou are now able to use mine sight again.");
        new EventListener<>(PlayerQuitEvent.class, e -> shulkerData.remove(e.getPlayer().getUniqueId()));
    }

    @Override
    protected void onInteract(int level, PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = e.getPlayer();
        ShulkerBlockData data = new ShulkerBlockData();
        List<Block> blocks = data.getBlocks(player, level);
        data.outline(player, blocks);
        shulkerData.put(player.getUniqueId(), data);

        EventListener<BlockBreakEvent> blockEvent = new EventListener<>(BlockBreakEvent.class, ev -> {
            ShulkerBlockData blockData = shulkerData.get(player.getUniqueId());
            blockData.removeShulker(player, ev.getBlock());
            shulkerData.put(player.getUniqueId(), blockData);
        });

        Task.syncDelayed(() -> {
            blockEvent.unregister();
            shulkerData.get(player.getUniqueId()).removeAll(player);
            shulkerData.remove(player.getUniqueId());
        }, (long) ((level + 1) * 1.5) * 20);
        this.setCooldown(player);
    }

    @Override
    public int cooldown(int level) {
        return 30 * (7 - level);
    }

    private enum Sorter {
        COAL_ORE(ChatColor.BLACK),
        IRON_ORE(ChatColor.GRAY),
        GOLD_ORE(ChatColor.YELLOW),
        GILDED_BLACKSTONE(ChatColor.YELLOW),
        LAPIS_ORE(ChatColor.BLUE),
        REDSTONE_ORE(ChatColor.RED),
        DIAMOND_ORE(ChatColor.AQUA),
        EMERALD_ORE(ChatColor.DARK_GREEN),
        QUARTZ_ORE(ChatColor.WHITE),
        DEBRIS(ChatColor.DARK_RED),
        COPPER_ORE(ChatColor.GOLD);

        private final ChatColor color;

        Sorter(ChatColor color) {
            this.color = color;
        }

        public static ChatColor getColorFrom(Block block) {
            for (Sorter sorter : Sorter.values())
                if (block.getType().toString().endsWith(sorter.toString()))
                    return sorter.color;
            return null;
        }
    }

    public static Enchantment getInstance() {
        return instance;
    }

    private static class ShulkerBlockData {

        private final Map<Block, Shulker> blockMap;
        private final Set<ChatColor> colorsUsed;

        private ShulkerBlockData() {
            this.blockMap = new HashMap<>();
            this.colorsUsed = new HashSet<>();
        }

        private List<Block> getBlocks(Player player, int level) {
            int radius = ((level * 2) + 5) / 2;
            List<Block> blocks = new ArrayList<>();
            Location loc = LocationUtils.center(player.getLocation());
            for (double x = loc.getX() - radius; x <= loc.getX() + radius; x++)
                for (double y = loc.getY() - radius; y <= loc.getY() + radius; y++)
                    for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; z++)
                        blocks.add(new Location(loc.getWorld(), x, y, z).getBlock());
            return this.filterBlocks(blocks);
        }

        private List<Block> filterBlocks(List<Block> blocks) {
            List<Block> newBlocks = new ArrayList<>();
            for (Block block : blocks) {
                ChatColor color = Sorter.getColorFrom(block);
                if (color != null) {
                    colorsUsed.add(Sorter.getColorFrom(block));
                    newBlocks.add(block);
                }
            }
            return newBlocks;
        }

        private void outline(Player player, List<Block> blocks) {
            Shulker shulk;
            SynchedEntityData synchedData;
            for (Block b : blocks) {
                shulk = new Shulker(EntityType.SHULKER, ((CraftWorld) b.getWorld()).getHandle());
                shulk.setInvisible(true);
                shulk.setXRot(0f);
                shulk.setYRot(0f);
                shulk.setPos(b.getLocation().getX() + 0.5, b.getLocation().getY(), b.getLocation().getZ() + 0.5);
                shulk.setInvulnerable(true);
                shulk.setNoAi(true);
                shulk.setSilent(true);
                synchedData = shulk.getEntityData();
                synchedData.set(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), (byte) 96);
                ((CraftPlayer) player).getHandle().connection.send(new ClientboundAddEntityPacket(shulk));
                ((CraftPlayer) player).getHandle().connection.send(new ClientboundSetEntityDataPacket(shulk.getId(), synchedData.getNonDefaultValues()));
                blockMap.put(b, shulk);
            }
            for (ChatColor color : this.colorsUsed) {
                this.setTeams(player, color);
            }
        }

        private void setTeams(Player p, ChatColor color) {
            Scoreboard board = new Scoreboard();
            String s = "MINESIGHT@@" + color.getChar();
            PlayerTeam nmsTeam = board.addPlayerTeam(s);
            nmsTeam.setCollisionRule(Team.CollisionRule.NEVER);
            nmsTeam.setDisplayName(MutableComponent.create(new LiteralContents("minesight" + color.getChar())));
            nmsTeam.setColor(ChatFormatting.getByCode(color.getChar()));
            ((CraftPlayer) p).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(nmsTeam, true));
            for (Block b : blockMap.keySet()) {
                if (Sorter.getColorFrom(b) == color)
                    ((CraftPlayer) p).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket(nmsTeam,
                            blockMap.get(b).getStringUUID(), ClientboundSetPlayerTeamPacket.Action.ADD));
            }
        }

        private void removeShulker(Player player, Block block) {
            Shulker shulker = blockMap.get(block);
            if (shulker != null) {
                ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(shulker.getId()));
            }
            blockMap.remove(block);
        }

        private void removeAll(Player player) {
            ClientboundRemoveEntitiesPacket packet;
            for (Shulker shulker : blockMap.values()) {
                if (shulker == null) continue;
                packet = new ClientboundRemoveEntitiesPacket(shulker.getId());
                ((CraftPlayer) player).getHandle().connection.send(packet);
            }
            blockMap.clear();
        }

    }

}
