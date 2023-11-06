package com.github.merelysnow.geradores.inventory;

import com.github.merelysnow.geradores.data.GeneratorsLogs;
import com.github.merelysnow.geradores.database.GeneratorsLogsDataBase;
import com.github.merelysnow.geradores.utils.DateFormatter;
import com.github.merelysnow.geradores.utils.EntityName;
import com.github.merelysnow.geradores.utils.item.ItemStackBuilder;
import com.google.common.collect.Lists;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

public class GeneratorsLogsView extends PaginatedView<GeneratorsLogs> {

    private final GeneratorsLogsDataBase generatorsLogsDataBase;

    public GeneratorsLogsView(GeneratorsLogsDataBase generatorsLogsDataBase) {
        super(6, "Logs da facção");
        this.generatorsLogsDataBase = generatorsLogsDataBase;

        setSource(ctx -> Lists.newArrayList(generatorsLogsDataBase.selectMany(ctx.get("factionTag"))));
        setLayout("XXXXXXXXX",
                "XOOOOOOOX",
                "<OOOOOOO>",
                "XOOOOOOOX",
                "XOOOOOOOX",
                "XXXXXXXXX");

        setCancelOnClick(true);
        setCancelOnDrag(true);
        setCancelOnDrop(true);

        setPreviousPageItem((context, item) -> item.withItem(context.hasPreviousPage() ? new ItemStackBuilder(Material.ARROW)
                .displayName("§cPag. anterior")
                .build() : null));
        setNextPageItem((context, item) -> item.withItem(context.hasNextPage() ? new ItemStackBuilder(Material.ARROW)
                .displayName("§aProx. pagina")
                .build() : null));
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<GeneratorsLogs> paginatedViewSlotContext, @NotNull ViewItem viewItem, @NotNull GeneratorsLogs generatorsLogs) {

        final ItemStackBuilder itemStackBuilder = new ItemStackBuilder(generatorsLogs.getAction().equals("ADD") ? Material.STORAGE_MINECART : Material.MINECART);
        final String action = generatorsLogs.getAction().equals("ADD") ? "adicionou" : "removeu";

        itemStackBuilder.displayName(String.format("§a%s %s", generatorsLogs.getPlayer(), action));
        itemStackBuilder.lore("§7Tipo: §f" + EntityName.valueOf(generatorsLogs.getEntityType()).getName(),
                "§7Quantia: §f" + NumberFormat.getInstance().format(generatorsLogs.getAmount()),
                "§7Data: §f" + DateFormatter.formatDateWithHour(generatorsLogs.getDate()));

        viewItem.withItem(itemStackBuilder.build());
    }
}
