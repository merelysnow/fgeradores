package com.github.merelysnow.geradores.api.impl;

import com.github.merelysnow.geradores.api.FactionGeneratorsApi;
import com.github.merelysnow.geradores.cache.FactionGeneratorsCache;
import com.github.merelysnow.geradores.data.FactionGenerators;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@RequiredArgsConstructor
public class ImplFactionGeneratorsApi implements FactionGeneratorsApi {

    private final FactionGeneratorsCache factionGeneratorsCache;

    @Override
    public @Nullable Optional<FactionGenerators> getFactionByTag(@NotNull String factionTag) {
        return Optional.ofNullable(factionGeneratorsCache.get(factionTag));
    }
}
