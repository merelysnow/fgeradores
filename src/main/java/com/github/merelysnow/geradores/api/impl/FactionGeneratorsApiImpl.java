package com.github.merelysnow.geradores.api.impl;

import com.github.merelysnow.geradores.api.FactionGeneratorsApi;
import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.repository.FactionGeneratorsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class FactionGeneratorsApiImpl implements FactionGeneratorsApi {

    private final FactionGeneratorsCache factionGeneratorsCache;
    private final FactionGeneratorsRepository factionGeneratorsRepository;

    @Override
    public @Nullable FactionGenerators getFactionByTag(@NotNull String factionTag) {
        return factionGeneratorsCache.get(factionTag);
    }

    @Override
    public void saveFactionGenerator(@NotNull FactionGenerators factionGenerators) {
        factionGeneratorsRepository.save(factionGenerators);
    }
}
