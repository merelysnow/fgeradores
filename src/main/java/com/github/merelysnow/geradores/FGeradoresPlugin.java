package com.github.merelysnow.geradores;

import com.github.merelysnow.geradores.api.FactionGeneratorsApi;
import com.github.merelysnow.geradores.api.impl.ImplFactionGeneratorsApi;
import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.database.FactionGeneratorsDataBase;
import com.github.merelysnow.geradores.inventory.GeneratorsView;
import com.github.merelysnow.geradores.listeners.GeneratorsCommandListener;
import com.github.merelysnow.geradores.listeners.GeneratorsFactionListener;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class FGeradoresPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final FactionGeneratorsCache factionGeneratorsCache = new FactionGeneratorsCache();
        final FactionGeneratorsDataBase factionGeneratorsDataBase = new FactionGeneratorsDataBase(this);
        final ViewFrame viewFrame = ViewFrame.of(this, new GeneratorsView(factionGeneratorsDataBase)).register();

        factionGeneratorsDataBase.selectMany().forEach(geradores -> factionGeneratorsCache.put(geradores.getFactionTag(), geradores));

        getServer().getPluginManager().registerEvents(new GeneratorsCommandListener(factionGeneratorsCache, factionGeneratorsDataBase, viewFrame), this);
        getServer().getPluginManager().registerEvents(new GeneratorsFactionListener(factionGeneratorsCache, factionGeneratorsDataBase), this);

        Bukkit.getServicesManager().register(FactionGeneratorsApi.class, new ImplFactionGeneratorsApi(factionGeneratorsCache), this, ServicePriority.Highest);
    }
}
