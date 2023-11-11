package com.github.merelysnow.geradores.listener;

import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.repository.FactionGeneratorsRepository;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDisband;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GeneratorsFactionListener implements Listener {

    private final FactionGeneratorsCache factionGeneratorsCache;
    private final FactionGeneratorsRepository factionGeneratorsRepository;

    @EventHandler
    private void onCreate(EventFactionsCreate event) {

        FactionGenerators factionGenerators = factionGeneratorsRepository.load(event.getFactionTag());

        if (factionGenerators == null) {
            factionGenerators = FactionGenerators.builder().factionTag(event.getFactionTag()).build();

            factionGeneratorsRepository.create(factionGenerators);
            factionGeneratorsCache.put(event.getFactionTag(), factionGenerators);
        }
    }

    @EventHandler
    private void onDisband(EventFactionsDisband event) {

        final Player player = event.getMPlayer().getPlayer();
        final Faction faction = event.getFaction();
        final FactionGenerators factionGenerators = factionGeneratorsRepository.load(faction.getTag());
        if (factionGenerators == null) return;

        if (factionGenerators.getStoragedSpawners().values().stream().mapToDouble(a -> a).sum() > 0) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode desfazer a facção com spawners armazenados.");
            return;
        }

        factionGeneratorsCache.remove(faction.getTag());
        factionGeneratorsRepository.delete(factionGenerators);
    }
}
