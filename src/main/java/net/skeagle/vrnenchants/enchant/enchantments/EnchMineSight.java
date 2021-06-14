package net.skeagle.vrnenchants.enchant.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntityPacket;
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
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnchDescription("Right clicking your pickaxe will allow you to see nearby ores for a few seconds.")
public class EnchMineSight extends BaseEnchant implements ICooldown {

    private static final Enchantment instance = new EnchMineSight();

    private EnchMineSight() {
        super("Mine Sight", 3, Target.PICKAXES);
        setRarity(Rarity.LEGENDARY);
        setCooldownMessage("&aYou are now able to use mine sight again.");
    }

    private final Map<Block, Shulker> blockCorrespondingEntity = new HashMap<>();
    private final List<ChatColor> colorsUsed = new ArrayList<>();

    @Override
    protected void onInteract(int level, PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player p = e.getPlayer();
        List<Block> blocks = getBlocks(p.getLocation().getBlock(), level);
        outline(p, blocks, level);
        setCooldown(p);
    }

    private List<Block> getBlocks(Block start, int level) {
        int radius = (level * 2) + 3;
        List<Block> blocks = new ArrayList<>();
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++)
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++)
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++)
                    blocks.add(new Location(start.getWorld(), x, y, z).getBlock());
        return filterBlocks(blocks);
    }

    private void outline(Player p, List<Block> blocks, int duration) {
        for (Block b : blocks) {
            Shulker shulk = new Shulker(EntityType.SHULKER, ((CraftWorld) b.getWorld()).getHandle());
            shulk.setInvisible(true);
            shulk.setXRot(0f);
            shulk.setYRot(0f);
            shulk.setPos(b.getLocation().getX() + 0.5, b.getLocation().getY(), b.getLocation().getZ() + 0.5);
            shulk.setInvulnerable(true);
            shulk.setNoAi(true);
            shulk.setSilent(true);
            SynchedEntityData data = shulk.getEntityData();
            data.set(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), (byte) 96);
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundAddMobPacket(shulk));
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundSetEntityDataPacket(shulk.getId(), data, false));
            blockCorrespondingEntity.put(b, shulk);
        }
        for (ChatColor color : colorsUsed)
            setTeams(p, color);
        Task.syncDelayed(() -> {
            ClientboundRemoveEntityPacket destroy;
            for (Shulker shulk : blockCorrespondingEntity.values()) {
                destroy = new ClientboundRemoveEntityPacket(shulk.getId());
                ((CraftPlayer) p).getHandle().connection.send(destroy);
            }
        },(long) (duration * 1.25) * 20);
    }

    @Override
    public int cooldown(int level) {
        return 60 * (5 - level);
    }

    private enum Sorter {
        COAL(ChatColor.BLACK, Material.COAL_ORE),
        IRON(ChatColor.GRAY, Material.IRON_ORE),
        GOLD(ChatColor.YELLOW, Material.GOLD_ORE),
        LAPIS(ChatColor.BLUE, Material.LAPIS_ORE),
        REDSTONE(ChatColor.RED, Material.REDSTONE_ORE),
        DIAMOND(ChatColor.AQUA, Material.DIAMOND_ORE),
        EMERALD(ChatColor.DARK_GREEN, Material.EMERALD_ORE),
        QUARTZ(ChatColor.WHITE, Material.NETHER_QUARTZ_ORE),
        NETHERITE(ChatColor.DARK_RED, Material.ANCIENT_DEBRIS),
        NETHER_GOLD(ChatColor.YELLOW, Material.NETHER_GOLD_ORE),
        GILDED_BLACKSTONE(ChatColor.YELLOW, Material.GILDED_BLACKSTONE),
        COPPER(ChatColor.GOLD, Material.COPPER_ORE);

        private final ChatColor color;
        private final Material block;

        Sorter(ChatColor color, Material block) {
            this.color = color;
            this.block = block;
        }

        public Material getBlock() {
            return block;
        }

        public static ChatColor getColorFrom(Block b) {
            for (Sorter sorter : Sorter.values())
                if (b.getType() == sorter.block)
                    return sorter.color;
            return ChatColor.WHITE;
        }
    }

    private void setTeams(Player p, ChatColor color) {
        Scoreboard board = new Scoreboard();
        String s = "MINESIGHT@@" + color.getChar();
        PlayerTeam nmsTeam = board.addPlayerTeam(s);
        nmsTeam.setCollisionRule(Team.CollisionRule.NEVER);
        nmsTeam.setDisplayName(new TextComponent("minesight" + color.getChar()));
        nmsTeam.setColor(ChatFormatting.getByCode(color.getChar()));
        System.out.println(nmsTeam.getColor());
        ((CraftPlayer) p).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(nmsTeam, true));
        for (Block b : blockCorrespondingEntity.keySet()) {
            if (Sorter.getColorFrom(b) == color)
                ((CraftPlayer) p).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket(nmsTeam,
                        blockCorrespondingEntity.get(b).getStringUUID(), ClientboundSetPlayerTeamPacket.Action.ADD));
        }
    }

    private List<Block> filterBlocks(List<Block> blocks) {
        List<Block> newBlocks = new ArrayList<>();
        for (Block b : blocks)
            for (Sorter sort : Sorter.values())
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
