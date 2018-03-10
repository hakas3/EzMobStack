package io.edkek.mc.ezmobstack;

import com.google.gson.Gson;
import io.edkek.mc.ezmobstack.commands.ConfigEdit;
import io.edkek.mc.ezmobstack.commands.ICommand;
import io.edkek.mc.ezmobstack.listener.MobListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class EzMobStack extends JavaPlugin {
    public static EzMobStack INSTANCE;

    private MobListener listener;
    private Gson GSON = new Gson();
    private ICommand[] COMMANDS = new ICommand[] { new ConfigEdit() };
    private Metrics metrics;

    @Override
    public void onEnable() {
        INSTANCE = this;

        File cache = new File(getDataFolder(), "entity.cache");
        if (!cache.exists())
            listener = new MobListener();
        else {
            String contents = readFile(cache);
            if (contents == null)
                listener = new MobListener();
            else {
                listener = GSON.fromJson(contents, MobListener.class);
            }
        }

        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists())
            saveDefaultConfig();
        else {
            try {
                getConfig().load(config);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        listener.init(this);
        getServer().getPluginManager().registerEvents(listener, this);

        metrics = new Metrics(this);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    @Override
    public void onDisable() {
        listener.disable();
        INSTANCE = null;
    }

    public void saveCache() {
        String json = GSON.toJson(listener);
        File cache = new File(getDataFolder(), "entity.cache");

        writeFile(cache, json);
    }

    public MobListener getMobListener() {
        return listener;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (ICommand c : COMMANDS) {
            if (c.getName().equals(cmd.getName())) {
                if ((sender instanceof Player)) {
                    Player p = (Player)sender;

                    c.execute(p, args);
                } else {
                    sender.sendMessage("Command can only be used in-game!");
                }
                return true;
            }
        }
        return false;
    }

    private static void writeFile(File file, String contents) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(stream);
            writer.write(contents);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            return readStream(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readStream(InputStream is) {
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
