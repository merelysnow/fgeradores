package com.github.merelysnow.geradores;

import com.github.merelysnow.geradores.cache.FactionGeradoresCache;
import com.github.merelysnow.geradores.database.FactionGeradoresDataBase;
import com.github.merelysnow.geradores.inventory.GeradoresView;
import com.github.merelysnow.geradores.listeners.GeradoresCommandListener;
import com.github.merelysnow.geradores.listeners.GeradoresFactionListener;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.plugin.java.JavaPlugin;

public class FGeradoresPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final FactionGeradoresCache factionGeradoresCache = new FactionGeradoresCache();
        final FactionGeradoresDataBase factionGeradoresDataBase = new FactionGeradoresDataBase(this);
        final ViewFrame viewFrame = ViewFrame.of(this, new GeradoresView(factionGeradoresDataBase)).register();

        factionGeradoresDataBase.selectMany().forEach(geradores -> factionGeradoresCache.put(geradores.getFactionTag(), geradores));

        getServer().getPluginManager().registerEvents(new GeradoresCommandListener(factionGeradoresCache, factionGeradoresDataBase, viewFrame), this);
        getServer().getPluginManager().registerEvents(new GeradoresFactionListener(factionGeradoresCache, factionGeradoresDataBase), this);
    }
}
