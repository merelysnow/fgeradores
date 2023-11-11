package com.github.merelysnow.geradores;

import com.github.merelysnow.geradores.api.FactionGeneratorsApi;
import com.github.merelysnow.geradores.api.impl.FactionGeneratorsApiImpl;
import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.inventory.GeneratorsLogsView;
import com.github.merelysnow.geradores.inventory.GeneratorsView;
import com.github.merelysnow.geradores.listener.GeneratorsCommandListener;
import com.github.merelysnow.geradores.listener.GeneratorsFactionListener;
import com.github.merelysnow.geradores.repository.FactionGeneratorsRepository;
import com.github.merelysnow.geradores.repository.GeneratorsLogsRepository;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class FGeradoresPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        final FactionGeneratorsCache factionGeneratorsCache = new FactionGeneratorsCache();
        final FactionGeneratorsRepository factionGeneratorsRepository = new FactionGeneratorsRepository(this);
        final GeneratorsLogsRepository generatorsLogsRepository = new GeneratorsLogsRepository(this);

        final ViewFrame viewFrame = ViewFrame.of(this, new GeneratorsView(factionGeneratorsRepository, generatorsLogsRepository), new GeneratorsLogsView(generatorsLogsRepository)).register();
        factionGeneratorsRepository.selectMany().forEach(geradores -> factionGeneratorsCache.put(geradores.getFactionTag(), geradores));

        getServer().getPluginManager().registerEvents(new GeneratorsCommandListener(factionGeneratorsCache, factionGeneratorsRepository, viewFrame), this);
        getServer().getPluginManager().registerEvents(new GeneratorsFactionListener(factionGeneratorsCache, factionGeneratorsRepository), this);

        Bukkit.getServicesManager().register(FactionGeneratorsApi.class, new FactionGeneratorsApiImpl(factionGeneratorsCache, factionGeneratorsRepository), this, ServicePriority.Highest);
    }
}
