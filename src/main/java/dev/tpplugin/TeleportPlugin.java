package dev.tpplugin;

import dev.tpplugin.commands.TpCommand;
import dev.tpplugin.commands.TpsCommand;
import dev.tpplugin.commands.TpssCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCmd("tp",   new TpCommand(this));
        registerCmd("tps",  new TpsCommand(this));
        registerCmd("tpss", new TpssCommand(this));
        getLogger().info("TeleportPlugin включён!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TeleportPlugin выключён.");
    }

    /** Регистрирует executor и tab-completer одним вызовом. */
    private <T extends org.bukkit.command.CommandExecutor & org.bukkit.command.TabCompleter>
    void registerCmd(String name, T handler) {
        var cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(handler);
            cmd.setTabCompleter(handler);
        } else {
            getLogger().severe("Команда /" + name + " не найдена в plugin.yml!");
        }
    }
}
