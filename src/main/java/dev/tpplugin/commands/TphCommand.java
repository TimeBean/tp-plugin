package dev.tpplugin.commands;

import dev.tpplugin.TeleportPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * /tph — справка по доступным командам телепортации для игрока.
 * Алиас: tphelp
 * Пермишн: tpplugin.tp.help
 */
public class TphCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.help";

    private final TeleportPlugin plugin;

    public TphCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§cУ вас нет прав для просмотра справки!");
            return true;
        }

        sender.sendMessage("§6§m--------------------§r §eTeleportPlugin §6§m--------------------");
        sender.sendMessage("");

        if (sender.hasPermission("tpplugin.tp.coords")) {
            sender.sendMessage("§a/tpl §f<x> <y> <z>");
            sender.sendMessage("  §7Телепортировать себя по координатам.");
            sender.sendMessage("  §7Алиас: §f/tplocation");
            sender.sendMessage("");
        }

        if (sender.hasPermission("tpplugin.tp.player")) {
            sender.sendMessage("§a/tpp §f<игрок>");
            sender.sendMessage("  §7Телепортировать себя к указанному игроку.");
            sender.sendMessage("  §7Алиас: §f/tpplayer");
            sender.sendMessage("");
        }

        if (sender.hasPermission("tpplugin.tp.pull")) {
            sender.sendMessage("§a/tpy §f<игрок>");
            sender.sendMessage("  §7Притянуть указанного игрока к себе.");
            sender.sendMessage("  §7Алиас: §f/tpyank");
            sender.sendMessage("");
        }

        if (sender.hasPermission("tpplugin.tp.force")) {
            sender.sendMessage("§a/tpe §f<игрок1> <игрок2>");
            sender.sendMessage("  §7Телепортировать первого игрока ко второму.");
            sender.sendMessage("  §7Алиас: §f/tpeach");
            sender.sendMessage("");
        }

        boolean hasAny = sender.hasPermission("tpplugin.tp.coords")
                || sender.hasPermission("tpplugin.tp.player")
                || sender.hasPermission("tpplugin.tp.pull")
                || sender.hasPermission("tpplugin.tp.force");

        if (!hasAny) {
            sender.sendMessage("§7У вас нет прав ни на одну команду телепортации.");
            sender.sendMessage("");
        }

        if (sender.hasPermission("tpplugin.tp.help.admin")) {
            sender.sendMessage("§7Подробная справка для администратора: §f/tpha");
            sender.sendMessage("");
        }

        sender.sendMessage("§6§m--------------------------------------------");
        return true;
    }

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        return Collections.emptyList();
    }
}
