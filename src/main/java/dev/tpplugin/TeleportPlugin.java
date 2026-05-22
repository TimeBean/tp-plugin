package dev.tpplugin;

import dev.tpplugin.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCmd("tpl",  new TplCommand(this));   // /tpl  (tplocation)  — по координатам
        registerCmd("tpp",  new TppCommand(this));   // /tpp  (tpplayer)    — к игроку
        registerCmd("tpy",  new TpyCommand(this));   // /tpy  (tpyank)      — притянуть к себе
        registerCmd("tpe",  new TpeCommand(this));   // /tpe  (tpeach)      — игрок1 к игроку2
        registerCmd("tph",  new TphCommand(this));   // /tph  (tphelp)      — справка для игрока
        registerCmd("tpha", new TphaCommand(this));  // /tpha (tphelpadmin) — справка для админа
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
