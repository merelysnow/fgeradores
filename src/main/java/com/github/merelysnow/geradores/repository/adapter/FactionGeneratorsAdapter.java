package com.github.merelysnow.geradores.repository.adapter;

import com.github.merelysnow.geradores.data.FactionGenerators;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FactionGeneratorsAdapter implements SQLResultAdapter<FactionGenerators> {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public FactionGenerators adaptResult(SimpleResultSet resultSet) {

        final Type type = new TypeToken<HashMap<EntityType, Integer>>() {
        }.getType();
        final String factionTag = resultSet.get("factionTag");
        final Map<EntityType, Integer> storagedSpawners = GSON.fromJson((String) resultSet.get("storagedSpawners"), type);

        return FactionGenerators.builder()
                .factionTag(factionTag)
                .storagedSpawners(storagedSpawners)
                .open(false)
                .build();
    }
}
