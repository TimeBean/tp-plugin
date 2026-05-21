package dev.tpplugin.commands;

import dev.tpplugin.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TpCommand implements CommandExecutor, TabCompleter {

    /** /tp <x> <y> <z> — телепортация себя по координатам */
    private static final String PERM_COORDS  = "tpplugin.tp.coords";
    /** /tp <игрок1> <игрок2> — телепортация чужого игрока к другому */
    private static final String PERM_FORCE   = "tpplugin.tp.force";

    private final TeleportPlugin plugin;

    public TpCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (args.length == 3) {
            // ── Режим 1: /tp <x> <y> <z> ──────────────────────────────────
            return handleCoords(sender, args);
        } else if (args.length == 2) {
            // ── Режим 2: /tp <игрок1> <игрок2> ────────────────────────────
            return handleForce(sender, args);
        } else {
            sender.sendMessage("§eИспользование:");
            sender.sendMessage("§f  /tp <x> <y> <z>          §7— телепортация по координатам");
            sender.sendMessage("§f  /tp <игрок1> <игрок2>    §7— телепортировать игрока к другому");
            return true;
        }
    }

    // ── /tp <x> <y> <z> ────────────────────────────────────────────────────

    private boolean handleCoords(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cЭта команда доступна только для игроков!");
            return true;
        }
        if (!player.hasPermission(PERM_COORDS)) {
            player.sendMessage("§cУ вас нет прав для телепортации по координатам!");
            return true;
        }

        double x, y, z;
        try {
            x = parseCoord(args[0], player.getLocation().getX());
            y = parseCoord(args[1], player.getLocation().getY());
            z = parseCoord(args[2], player.getLocation().getZ());
        } catch (NumberFormatException e) {
            player.sendMessage("§cОшибка: координаты должны быть числами (или ~N для относительных)!");
            return true;
        }

        if (y < player.getWorld().getMinHeight() || y > player.getWorld().getMaxHeight()) {
            player.sendMessage("§cКоордината Y §f" + (int) y +
                    " §cвне допустимого диапазона (§f" + player.getWorld().getMinHeight() +
                    " §c– §f" + player.getWorld().getMaxHeight() + "§c)!");
            return true;
        }

        Location dest = new Location(
                player.getWorld(), x, y, z,
                player.getLocation().getYaw(),
                player.getLocation().getPitch()
        );
        player.teleport(dest);
        player.sendMessage("§aТелепортирован к §f" +
                formatCoord(x) + " " + formatCoord(y) + " " + formatCoord(z) + "§a.");
        return true;
    }

    // ── /tp <игрок1> <игрок2> ───────────────────────────────────────────────

    private boolean handleForce(CommandSender sender, String[] args) {
        if (!sender.hasPermission(PERM_FORCE)) {
            sender.sendMessage("§cУ вас нет прав для принудительной телепортации игроков!");
            return true;
        }

        Player from = Bukkit.getPlayerExact(args[0]);
        Player to   = Bukkit.getPlayerExact(args[1]);

        if (from == null) {
            sender.sendMessage("§cИгрок §f" + args[0] + " §cне найден или не в сети!");
            return true;
        }
        if (to == null) {
            sender.sendMessage("§cИгрок §f" + args[1] + " §cне найден или не в сети!");
            return true;
        }
        if (from.equals(to)) {
            sender.sendMessage("§cНельзя телепортировать игрока к самому себе!");
            return true;
        }

        from.teleport(to.getLocation());

        from.sendMessage("§aВас телепортировали к §f" + to.getName() + "§a.");
        to.sendMessage("§7К вам телепортировали §f" + from.getName() + "§7.");
        if (!(sender instanceof Player p) || !p.equals(from)) {
            sender.sendMessage("§aИгрок §f" + from.getName() +
                    " §aтелепортирован к §f" + to.getName() + "§a.");
        }
        return true;
    }

    // ── Вспомогательные ────────────────────────────────────────────────────

    private double parseCoord(String input, double current) throws NumberFormatException {
        if (input.startsWith("~")) {
            String rel = input.substring(1);
            return rel.isEmpty() ? current : current + Double.parseDouble(rel);
        }
        return Double.parseDouble(input);
    }

    private String formatCoord(double coord) {
        if (coord == Math.floor(coord)) return String.valueOf((int) coord);
        return String.format("%.2f", coord);
    }

    // ── Tab-completion ──────────────────────────────────────────────────────

    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        // Нет ни одного права — ничего не показываем
        boolean hasCoords = sender.hasPermission(PERM_COORDS);
        boolean hasForce  = sender.hasPermission(PERM_FORCE);

        if (!hasCoords && !hasForce) return Collections.emptyList();

        String prefix = args.length > 0 ? args[args.length - 1].toLowerCase() : "";

        if (args.length == 1) {
            // Первый аргумент: может быть X (координата) или имя игрока
            List<String> suggestions = new java.util.ArrayList<>();
            if (hasCoords && sender instanceof Player p) {
                suggestions.add(String.valueOf((int) p.getLocation().getX()));
                suggestions.add("~");
            }
            if (hasForce) {
                Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> n.toLowerCase().startsWith(prefix))
                        .forEach(suggestions::add);
            }
            return suggestions;
        }

        if (args.length == 2) {
            // Смотрим: первый аргумент — координата или ник?
            boolean firstIsCoord = isCoordArg(args[0]);
            if (firstIsCoord && hasCoords && sender instanceof Player p) {
                return List.of(String.valueOf((int) p.getLocation().getY()), "~");
            }
            if (!firstIsCoord && hasForce) {
                // /tp <ник1> → предлагаем ник2
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> n.toLowerCase().startsWith(prefix))
                        .filter(n -> !n.equalsIgnoreCase(args[0]))
                        .collect(Collectors.toList());
            }
        }

        if (args.length == 3 && hasCoords && sender instanceof Player p) {
            return List.of(String.valueOf((int) p.getLocation().getZ()), "~");
        }

        return Collections.emptyList();
    }

    /** Эвристика: аргумент является координатой, если это число или начинается с ~ */
    private boolean isCoordArg(String arg) {
        if (arg.startsWith("~")) return true;
        try {
            Double.parseDouble(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
