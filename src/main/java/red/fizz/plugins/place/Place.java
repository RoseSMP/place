package red.fizz.plugins.place;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;
import red.fizz.plugins.place.stuff.BlockListener;
import red.fizz.plugins.place.stuff.PlaceCommandExecutor;

public final class Place extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Hello, world!");
        Statics.delay = 20;
        Bukkit.getLogger().info("delay = " + Statics.delay);
        Statics.delay = 30;
        Bukkit.getLogger().info("delay = " + Statics.delay);
        Bukkit.getLogger().warning("Enough debugging now.");
        this.loadPlace();
        this.getCommand("place").setExecutor(new PlaceCommandExecutor());
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
    }

    public Boolean worldExists(final String worldName) {
        return this.getServer().getWorld(worldName) != null;
    }

    public void loadPlace() {
        if (!this.worldExists("place")) {
            this.getLogger().info("World place does not exist, creating...");
            final WorldCreator wc = new WorldCreator("place");
            wc.type(WorldType.FLAT);
            wc.generateStructures(false);
            this.getServer().createWorld(wc);
        }
        this.getLogger().info("World place loaded.");
    }
    
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Goodbye, world!");
    }
}