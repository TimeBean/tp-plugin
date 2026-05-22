package dev.tpplugin.commands;

import dev.tpplugin.TeleportPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * /tpha — подробная справка для администраторов: все команды и пермишены.
 * Алиас: tphelpadmin
 * Пермишн: tpplugin.tp.help.admin
 */
public class TphaCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tpplugin.tp.help.admin";

    private final TeleportPlugin plugin;

    public TphaCommand(TeleportPlugin plugin) {
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
            sender.sendMessage("§cУ вас нет прав для просмотра административной справки!");
            return true;
        }

        sender.sendMessage("§4§m---§r §cTeleportPlugin §7— §cАдмин-справка §4§m---");
        sender.sendMessage("");

        // ── Команды ──────────────────────────────────────────────────────────

        sender.sendMessage("§e§lКОМАНДЫ:");
        sender.sendMessage("");

        sender.sendMessage("§a/tpl §f<x> <y> <z>  §8(алиас: /tplocation)");
        sender.sendMessage("  §7Телепортировать себя по координатам. Поддерживает относительные (~N).");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.coords");
        sender.sendMessage("");

        sender.sendMessage("§a/tpp §f<игрок>  §8(алиас: /tpplayer)");
        sender.sendMessage("  §7Телепортировать себя к указанному онлайн-игроку.");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.player");
        sender.sendMessage("");

        sender.sendMessage("§a/tpy §f<игрок>  §8(алиас: /tpyank)");
        sender.sendMessage("  §7Притянуть указанного игрока к своей позиции.");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.pull");
        sender.sendMessage("");

        sender.sendMessage("§a/tpe §f<игрок1> <игрок2>  §8(алиас: /tpeach)");
        sender.sendMessage("  §7Телепортировать первого игрока ко второму.");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.force");
        sender.sendMessage("");

        sender.sendMessage("§a/tph  §8(алиас: /tphelp)");
        sender.sendMessage("  §7Справка по командам плагина для игрока.");
        sender.sendMessage("  §7Показывает только те команды, на которые есть права.");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.help");
        sender.sendMessage("");

        sender.sendMessage("§a/tpha  §8(алиас: /tphelpadmin)");
        sender.sendMessage("  §7Эта справка. Полный список команд и пермишенов.");
        sender.sendMessage("  §7Пермишн: §ftpplugin.tp.help.admin");
        sender.sendMessage("");

        // ── Пермишены ─────────────────────────────────────────────────────────

        sender.sendMessage("§e§lПЕРМИШЕНЫ:");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.coords");
        sender.sendMessage("  §7/tpl <x> <y> <z> — телепортация себя по координатам.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.player");
        sender.sendMessage("  §7/tpp <игрок> — телепортация себя к игроку.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.pull");
        sender.sendMessage("  §7/tpy <игрок> — притянуть игрока к себе.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.force");
        sender.sendMessage("  §7/tpe <игрок1> <игрок2> — принудительная телепортация.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.help");
        sender.sendMessage("  §7/tph — просмотр справки для игрока.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.help.admin");
        sender.sendMessage("  §7/tpha — просмотр этой административной справки.");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§ftpplugin.tp.*");
        sender.sendMessage("  §7Все права на телепортацию (включает все вышеперечисленные).");
        sender.sendMessage("  §7По умолчанию: §fop");
        sender.sendMessage("");

        sender.sendMessage("§4§m------------------------------------------");
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
