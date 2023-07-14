package com.github.merelysnow.geradores.listeners;

import com.github.merelysnow.geradores.cache.FactionGeradoresCache;
import com.github.merelysnow.geradores.data.FactionGeradores;
import com.github.merelysnow.geradores.database.FactionGeradoresDataBase;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsDisband;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GeradoresFactionListener implements Listener {

    private final FactionGeradoresCache factionGeradoresCache;
    private final FactionGeradoresDataBase factionGeradoresDataBase;

    @EventHandler
    private void onDisband(EventFactionsDisband e) {

        final Faction faction = e.getFaction();
        final FactionGeradores factionGeradores = factionGeradoresDataBase.load(faction.getTag());

        if(factionGeradores != null) {
            factionGeradoresCache.remove(faction.getTag());
            factionGeradoresDataBase.delete(factionGeradores);
        }
    }
}
