package red.fizz.plugins.place.stuff;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

// This code is bad
// please change this before production
// PLEASE
//  - fizz

public class CountdownTimer implements Runnable  {
    private JavaPlugin plugin;
    private Integer assignedTaskId;
    private int seconds;
    private int secondsLeft;
    private Consumer<CountdownTimer> everySecond;
    private Runnable beforeTimer;
    private Runnable afterTimer;

    public CountdownTimer(final JavaPlugin plugin, final int seconds, final Runnable beforeTimer, final Runnable afterTimer, final Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = seconds;
        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }

    @Override
    public void run() {
        if (this.secondsLeft < 1) {
            this.afterTimer.run();
            if (this.assignedTaskId != null) {
                Bukkit.getScheduler().cancelTask((int)this.assignedTaskId);
            }
            return;
        }
        if (this.secondsLeft == this.seconds) {
            this.beforeTimer.run();
        }
        this.everySecond.accept(this);
        --this.secondsLeft;
    }

    public int getTotalSeconds() {
        return this.seconds;
    }

    public int getSecondsLeft() {
        return this.secondsLeft;
    }

    public void scheduleTimer() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.plugin, (Runnable)this, 0L, 20L);
    }
}
