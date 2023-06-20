package red.fizz.plugins.place.stuff;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import red.fizz.plugins.place.Statics;

public class PlaceCommandExecutor implements CommandExecutor {

    @SuppressWarnings("Deprecated")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (!sender.hasPermission("place.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
        }
        if (args.length != 1) {
            sender.sendMessage("Usage: /place <delay>");
            return true;
        }
        try {
            Statics.delay = Integer.parseInt(args[0]);
            sender.sendMessage(ChatColor.BLUE + "You have set the delay to " + Statics.delay + "s.");
            return false;
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "You must enter a valid number.");
        }
        return true;
    }
}
