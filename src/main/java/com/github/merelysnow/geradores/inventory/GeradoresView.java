package com.github.merelysnow.geradores.inventory;

import com.github.merelysnow.geradores.data.FactionGeradores;
import com.github.merelysnow.geradores.database.FactionGeradoresDataBase;
import com.github.merelysnow.geradores.utils.EntityName;
import com.github.merelysnow.geradores.utils.InventoryUtil;
import com.github.merelysnow.geradores.utils.SkullBuilder;
import com.github.merelysnow.geradores.utils.item.ItemStackBuilder;
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
                            .filter(itemStack -> itemStack != null)
                            .filter(itemStack -> itemStack.getType() == Material.MOB_SPAWNER)
                            .forEach(itemStack -> {
                                final ItemStackBuilder itemStackBuilder = new ItemStackBuilder(itemStack);

                                if(itemStackBuilder.getLore() == null) {
                                    return;
                                }

                                for(String line : itemStackBuilder.getLore()) {
                                    System.out.println(line.split("Tipo: ")[0]);
                                }

                                //TODO terminar isso aqui
                            });
                });

        for(Map.Entry<EntityType, Integer> spawner : factionGeradores.getStoragedSpawners().entrySet()
                .stream().filter(entry -> entry.getValue() > 0).collect(Collectors.toList())) {
            context.availableSlot().withItem(new SkullBuilder()
                    .setOwner("MHF_" + spawner.getKey().name().toUpperCase())
                    .displayName("§a" + NumberFormat.getInstance().format(spawner.getValue()) + "x gerador(es) de " + EntityName.valueOf(spawner.getKey()))

                    .lore("§fBotão esquerdo: §7Coletar 1 gerador.",
                            "§fBotão direito: §7Coletar todos os geradores.")
                    .build()
            ).onClick(event -> {

                final Player player = event.getPlayer();
                context.close();

                if(event.isLeftClick()) {

                    if(InventoryUtil.isFull(player)) {
                        player.sendMessage("§cEsvazie o seu inventario.");
                        return;
                    }

                    System.out.println(String.format("[GERADORES-LOG] A facção [%s] removeu 1x spawner de %s (%s)", factionGeradores.getFactionTag().toUpperCase(),  spawner.getKey().name().toUpperCase(), player.getName()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("sgive %s %s 1", player.getName(), spawner.getKey().name().toUpperCase()));
                    factionGeradores.withdrawAmountPlaced(spawner.getKey(), 1);
                    factionGeradoresDataBase.save(factionGeradores);
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.5F, 1.5F);
                    return;
                }

                if(event.isRightClick()) {
                    if(InventoryUtil.isFull(player)) {
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
