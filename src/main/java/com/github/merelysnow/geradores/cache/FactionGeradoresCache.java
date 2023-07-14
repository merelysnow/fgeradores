package com.github.merelysnow.geradores.cache;

import com.github.merelysnow.geradores.data.FactionGeradores;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;

import java.util.Map;

public class FactionGeradoresCache {

    @Delegate
    private final Map<String, FactionGeradores> cache = Maps.newHashMap();
}
