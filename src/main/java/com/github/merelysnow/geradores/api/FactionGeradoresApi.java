package com.github.merelysnow.geradores.api;

import com.github.merelysnow.geradores.data.FactionGeradores;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface FactionGeradoresApi {

    @Nullable
    Optional<FactionGeradores> getFactionByTag(@NotNull String factionTag);
}
