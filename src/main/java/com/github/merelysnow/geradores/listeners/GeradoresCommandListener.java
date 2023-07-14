package com.github.merelysnow.geradores.listeners;

import com.github.merelysnow.geradores.cache.FactionGeradoresCache;
import com.github.merelysnow.geradores.data.FactionGeradores;
import com.github.merelysnow.geradores.database.FactionGeradoresDataBase;
import com.github.merelysnow.geradores.inventory.GeradoresView;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import lombok.RequiredArgsConstructor;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

@RequiredArgsConstructor
public class GeradoresCommandListener implements Listener {

    private final FactionGeradoresCache factionGeradoresCache;
    private final FactionGeradoresDataBase factionGeradoresDataBase;
    private final ViewFrame viewFrame;

    @EventHandler
    private void onCommand(PlayerCommandPreprocessEvent e) {

        final Player player = e.getPlayer();
        final MPlayer mPlayer = MPlayer.get(player);
        final String message = e.getMessage();

        if(message.startsWith("/f geradores")) {
            e.setCancelled(true);

            if(!mPlayer.hasFaction()) {
                player.sendMessage("§cVocê precisa possuir uma facção.");
                return;
            }

            if(mPlayer.getRole().isLessThan(Rel.OFFICER)) {
                player.sendMessage("§cVocê precisa ser capitão ou superior para executar esse comando.");
                return;
            }

            final Faction faction = mPlayer.getFaction();
            final Map<EntityType, Integer> a = Maps.newHashMap();

            a.put(EntityType.COW, 250); //apenas para testes
            a.put(EntityType.IRON_GOLEM, 500); //apenas para testes

            FactionGeradores factionGeradores = factionGeradoresCache.get(faction.getTag());

            if(factionGeradores == null) {
                factionGeradores = new FactionGeradores(faction.getTag(), a, false);

                factionGeradoresDataBase.create(factionGeradores);
                factionGeradoresCache.put(faction.getTag(), factionGeradores);
            }

            if(factionGeradores.isOpen()) {
                player.sendMessage("§cOops! Já possui um jogador gerenciando os geradores armazenados.");
                return;
            }

            viewFrame.open(GeradoresView.class, player, ImmutableMap.of("factionGeradores", factionGeradores));
        }
    }
}
