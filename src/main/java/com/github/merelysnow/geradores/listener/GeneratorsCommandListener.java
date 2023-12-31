package com.github.merelysnow.geradores.listener;

import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.inventory.GeneratorsView;
import com.github.merelysnow.geradores.repository.FactionGeneratorsRepository;
import com.google.common.collect.ImmutableMap;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@RequiredArgsConstructor
public class GeneratorsCommandListener implements Listener {

    private final FactionGeneratorsCache factionGeneratorsCache;
    private final FactionGeneratorsRepository factionGeneratorsRepository;
    private final ViewFrame viewFrame;

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent e) {

        final Player player = e.getPlayer();
        final MPlayer mPlayer = MPlayer.get(player);
        final String message = e.getMessage();

        if (!message.startsWith("/f geradores")) return;

        e.setCancelled(true);
        if (!mPlayer.hasFaction()) {
            player.sendMessage("§cVocê precisa possuir uma facção.");
            return;
        }

        if (mPlayer.getRole().isLessThan(Rel.OFFICER)) {
            player.sendMessage("§cVocê precisa ser capitão ou superior para executar esse comando.");
            return;
        }

        final Faction faction = mPlayer.getFaction();
        FactionGenerators factionGenerators = factionGeneratorsCache.get(faction.getTag());

        if (factionGenerators == null) {
            factionGenerators = FactionGenerators.builder().factionTag(faction.getTag()).build();

            factionGeneratorsRepository.create(factionGenerators);
            factionGeneratorsCache.put(faction.getTag(), factionGenerators);
        }

        if (factionGenerators.isOpen()) {
            player.sendMessage("§cOops! Já possui um jogador gerenciando os geradores armazenados.");
            return;
        }

        viewFrame.open(GeneratorsView.class, player, ImmutableMap.of("factionGeradores", factionGenerators));
    }
}
