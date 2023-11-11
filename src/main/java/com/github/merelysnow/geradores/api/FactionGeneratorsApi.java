package com.github.merelysnow.geradores.api;

import com.github.merelysnow.geradores.data.FactionGenerators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FactionGeneratorsApi {

    /**
     * Returns the FactionGenerators corresponding to the provided faction tag.
     *
     * @param factionTag The faction tag for which the FactionGenerators is desired. Must not be null.
     * @return The FactionGenerators corresponding to the provided faction tag. Can be null if there is no corresponding FactionGenerators.
     */
    @Nullable
    FactionGenerators getFactionByTag(@NotNull String factionTag);

    /**
     * Saves the provided FactionGenerators instance, ensuring its data is persisted.
     * The FactionGenerators object represents a collection of generators used to create factions.
     *
     * @param factionGenerators The FactionGenerators object to be saved. Must not be null.
     * @throws IllegalArgumentException If the provided factionGenerators parameter is null.
     */
    void saveFactionGenerator(@NotNull FactionGenerators factionGenerators);
}
