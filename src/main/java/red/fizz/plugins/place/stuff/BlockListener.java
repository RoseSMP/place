package red.fizz.plugins.place.stuff;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import red.fizz.plugins.place.Place;
import red.fizz.plugins.place.Statics;

import java.util.ArrayList;
import java.util.HashMap;

// 90% of this is from 2022, dont judge plz <3

public class BlockListener implements Listener {

    HashMap<String, Integer> cooldown;
    DiscordWebhook webhook;
    Plugin plugin = Place.getPlugin(Place.class);

    public BlockListener() {
        this.cooldown = new HashMap<String, Integer>();
        this.webhook = new DiscordWebhook("https://discord.com/api/webhooks/8675309/abcdefg");
    }

    public Integer getCountdownFromConfig() {
        return Statics.delay;
    }

    public void startCountdown(final Player p) {
        final CountdownTimer timer = new CountdownTimer(this.getCountdownFromConfig(), () -> this.cooldown.put(p.getName(), this.getCountdownFromConfig()), () -> {
            if (p.isOnline()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You can now place blocks again!"));
                this.cooldown.remove(p.getName());
            }
        }, t -> {
            if (p.isOnline()) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't place blocks for " + t.getSecondsLeft() + " seconds!"));
            }
        });
        timer.scheduleTimer();
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        if (!e.getPlayer().hasPermission("rplace.place")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "You don't have permission to place blocks! (rplace.place)");
        }
        else {
            if (!this.cooldown.containsKey(e.getPlayer().getName())) {
                final int x = e.getBlock().getX();
                final int z = e.getBlock().getZ();
                final int y = -61;
                final String block = e.getPlayer().getInventory().getItemInMainHand().getType().toString();
                final Material material = e.getPlayer().getInventory().getItemInMainHand().getType();
                if (block.contains("CONCRETE")) {
                    final Location b = new Location(Bukkit.getWorld("place"), (double)x, (double)y, (double)z);
                    if (b.getBlock().getType() == Material.GRASS_BLOCK || b.getBlock().getType() == Material.OBSIDIAN || b.getBlock().getType() == Material.BEDROCK) {
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't place here, silly!"));
                        e.setCancelled(true);
                    }
                    else {
                        b.getBlock().setType(material);
                        this.startCountdown(e.getPlayer());
                    }
                }
                else {
                    e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't place this type of block!"));
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final World world = Bukkit.getWorld("place");
        final Location l = new Location(world, 0.0, -60.0, 0.0);
        p.teleport(l);
        p.getInventory().clear();
        final ArrayList<Material> materials = new ArrayList<Material>();
        materials.add(Material.WHITE_CONCRETE);
        materials.add(Material.ORANGE_CONCRETE);
        materials.add(Material.MAGENTA_CONCRETE);
        materials.add(Material.LIGHT_BLUE_CONCRETE);
        materials.add(Material.YELLOW_CONCRETE);
        materials.add(Material.LIME_CONCRETE);
        materials.add(Material.PINK_CONCRETE);
        materials.add(Material.GRAY_CONCRETE);
        materials.add(Material.LIGHT_GRAY_CONCRETE);
        materials.add(Material.CYAN_CONCRETE);
        materials.add(Material.PURPLE_CONCRETE);
        materials.add(Material.BLUE_CONCRETE);
        materials.add(Material.BROWN_CONCRETE);
        materials.add(Material.GREEN_CONCRETE);
        materials.add(Material.RED_CONCRETE);
        materials.add(Material.BLACK_CONCRETE);
        for (final Material m : materials) {
            final ItemStack stack = new ItemStack(m, 1);
            p.getInventory().addItem(new ItemStack[] { stack });
        }
        p.setAllowFlight(true);
        p.setFlying(true);
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        e.setCancelled(true);
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't break blocks!"));
    }

    @EventHandler
    public void onDropItem(final PlayerDropItemEvent e) {
        e.setCancelled(true);
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't drop items!"));
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(false);
    }

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission("rplace.admin")) {
            e.setFormat("§8[§c§lAdmin§8] §c" + e.getPlayer().getName() + " §8» §f" + e.getMessage());
        }
        else {
            e.setFormat("§7" + e.getPlayer().getName() + " §8» §f" + e.getMessage());
        }
    }

    @EventHandler
    public void onHunger(final FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        this.cooldown.remove(p.getName());
    }
}
