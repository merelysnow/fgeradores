package com.github.merelysnow.geradores.api;

import com.github.merelysnow.geradores.data.FactionGenerators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface FactionGeneratorsApi {

    @Nullable
    FactionGenerators getFactionByTag(@NotNull String factionTag);
}
