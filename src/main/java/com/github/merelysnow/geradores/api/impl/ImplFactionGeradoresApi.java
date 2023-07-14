package com.github.merelysnow.geradores.api.impl;

import com.github.merelysnow.geradores.api.FactionGeradoresApi;
import com.github.merelysnow.geradores.cache.FactionGeradoresCache;
import com.github.merelysnow.geradores.data.FactionGeradores;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@RequiredArgsConstructor
public class ImplFactionGeradoresApi implements FactionGeradoresApi {

    private final FactionGeradoresCache factionGeradoresCache;

    @Override
    public @Nullable Optional<FactionGeradores> getFactionByTag(@NotNull String factionTag) {
        return Optional.ofNullable(factionGeradoresCache.get(factionTag));
    }
}
