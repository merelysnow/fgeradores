package com.github.merelysnow.geradores.inventory;

import com.github.merelysnow.geradores.data.FactionGeradores;
import com.github.merelysnow.geradores.database.FactionGeradoresDataBase;
import com.github.merelysnow.geradores.utils.EntityName;
import com.github.merelysnow.geradores.utils.InventoryUtil;
import com.github.merelysnow.geradores.utils.SkullBuilder;
import com.github.merelysnow.geradores.utils.item.ItemStackBuilder;
import com.google.common.collect.Maps;
import me.saiintbrisson.minecraft.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GeradoresView extends View {

    private final FactionGeradoresDataBase factionGeradoresDataBase;

    public GeradoresView(FactionGeradoresDataBase factionGeradoresDataBase) {
        super(6, "Geradores da facção");

        this.factionGeradoresDataBase = factionGeradoresDataBase;
        setLayout("XXXXXXXXX",
                "XXXXXXXXX",
                "XXXXXXXXX",
                "XOOOOOOOX",
                "XOOOOOOOX",
                "XXXXXXXXX");

        setCancelOnClick(true);
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {

        final FactionGeradores factionGeradores = context.get("factionGeradores");

        context.slot(13, new ItemStackBuilder(Material.REDSTONE_COMPARATOR)
                        .displayName("§aArmazenar todos")
                        .lore("§7Clique aqui para armazenar todos os",
                                "§7spawners do seu inventario.")
                        .build())
                .onClick(event -> {

                    final Player player = event.getPlayer();
                    context.close();

                    Arrays.stream(player.getInventory().getContents())
                            .filter(Objects::nonNull)
                            .filter(itemStack -> itemStack.getType() == Material.MOB_SPAWNER)
                            .forEach(itemStack -> {
                                final ItemStackBuilder itemStackBuilder = new ItemStackBuilder(itemStack);

                                if (itemStackBuilder.getLore() == null) {
                                    return;
                                }

                                itemStackBuilder.getLore()
                                        .stream()
                                        .filter(line -> line.contains("Tipo: "))
                                        .forEach(line -> {
                                            final String cleanLine = line.replaceAll("§[a-fA-F0-9]", "");
                                            final String[] splitString = cleanLine.split("Tipo: ");
                                            final String spawnerType = cleanLine.length() > 1 ? splitString[1] : "";

                                            System.out.println(String.format("[GERADORES-LOG] A facção [%s] armazenou %s x spawner de %s (%s)", factionGeradores.getFactionTag().toUpperCase(), itemStack.getAmount(), EntityName.fromTranslated(spawnerType).name().toUpperCase(), player.getName()));
                                            factionGeradores.addAmountPlaced(EntityName.fromTranslated(spawnerType), itemStack.getAmount());
                                            InventoryUtil.removeItem(player, itemStack, itemStack.getAmount());
                                        });
                            });

                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.5F, 1.5F);
                });

        for (Map.Entry<EntityType, Integer> spawner : factionGeradores.getStoragedSpawners().entrySet()) {
            context.availableSlot().withItem(new SkullBuilder()
                    .setOwner("MHF_" + spawner.getKey().name().toUpperCase())
                    .displayName("§a" + NumberFormat.getInstance().format(spawner.getValue()) + "x gerador(es) de " + EntityName.valueOf(spawner.getKey()).getName())

                    .lore("§fBotão esquerdo: §7Coletar 1 gerador.",
                            "§fBotão direito: §7Coletar todos os geradores.")
                    .build()
            ).onClick(event -> {

                final Player player = event.getPlayer();
                context.close();

                if (event.isLeftClick()) {

                    if (InventoryUtil.isFull(player)) {
                        player.sendMessage("§cEsvazie o seu inventario.");
                        return;
                    }

                    System.out.println(String.format("[GERADORES-LOG] A facção [%s] removeu 1x spawner de %s (%s)", factionGeradores.getFactionTag().toUpperCase(), spawner.getKey().name().toUpperCase(), player.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("sgive %s %s 1", player.getName(), spawner.getKey().name().toUpperCase()));
                    factionGeradores.withdrawAmountPlaced(spawner.getKey(), 1);
                    factionGeradoresDataBase.save(factionGeradores);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.5F, 1.5F);
                    return;
                }

                if (event.isRightClick()) {
                    if (InventoryUtil.isFull(player)) {
                        player.sendMessage("§cEsvazie o seu inventario.");
                        return;
                    }

                    System.out.println(String.format("[GERADORES-LOG] A facção [%s] removeu %s x spawners de %s (%s)", factionGeradores.getFactionTag().toUpperCase(), spawner.getValue(), spawner.getKey().name().toUpperCase(), player.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("sgive %s %s %s", player.getName(), spawner.getKey().name().toUpperCase(), spawner.getValue()));
                    factionGeradores.withdrawAmountPlaced(spawner.getKey(), spawner.getValue());
                    factionGeradoresDataBase.save(factionGeradores);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.5F, 1.5F);
                }
            });
        }
    }

    @Override
    protected void onOpen(@NotNull OpenViewContext context) {

        FactionGeradores factionGeradores = context.get("factionGeradores");

        factionGeradores.setOpen(true);
    }

    @Override
    protected void onClose(@NotNull ViewContext context) {
        FactionGeradores factionGeradores = context.get("factionGeradores");

        factionGeradores.setOpen(false);
    }
}
