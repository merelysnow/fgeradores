package com.github.merelysnow.geradores.listeners;

import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.database.FactionGeneratorsDataBase;
import com.google.common.collect.Maps;
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
    private final FactionGeneratorsDataBase factionGeneratorsDataBase;

    @EventHandler
    private void onCreate(EventFactionsCreate e) {

        FactionGenerators factionGenerators = factionGeneratorsDataBase.load(e.getFactionTag());

        if(factionGenerators == null) {
            factionGenerators = new FactionGenerators(e.getFactionTag(), Maps.newHashMap(), false);

            factionGeneratorsDataBase.create(factionGenerators);
            factionGeneratorsCache.put(e.getFactionTag(), factionGenerators);
        }
    }

    @EventHandler
    private void onDisband(EventFactionsDisband e) {

        final Player player = e.getMPlayer().getPlayer();
        final Faction faction = e.getFaction();
        final FactionGenerators factionGenerators = factionGeneratorsDataBase.load(faction.getTag());

        if(factionGenerators == null) {
            return;
        }

        if(!factionGenerators.getStoragedSpawners().isEmpty()) {
            e.setCancelled(true);
            player.sendMessage("§cVocê não pode desfazer a facção com spawners armazenados.");
            return;
        }

        factionGeneratorsCache.remove(faction.getTag());
        factionGeneratorsDataBase.delete(factionGenerators);
    }
}
