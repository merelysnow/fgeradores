package com.github.merelysnow.geradores.cache;

import com.github.merelysnow.geradores.data.FactionGenerators;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;

import java.util.Map;

public class FactionGeneratorsCache {

    @Delegate
    private final Map<String, FactionGenerators> cache = Maps.newHashMap();
}
