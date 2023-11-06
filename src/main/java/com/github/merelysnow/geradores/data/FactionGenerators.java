package com.github.merelysnow.geradores.data;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.EntityType;

import java.util.Map;

@Builder
@Data
public class FactionGenerators {

    private final String factionTag;
    @Builder.Default
    private Map<EntityType, Integer> storagedSpawners = Maps.newHashMap();
    @Builder.Default
    private boolean open = false;

    public void addAmountPlaced(EntityType type, int amount) {
        storagedSpawners.merge(type, amount, Integer::sum);
    }

    public void withdrawAmountPlaced(EntityType type, int amount) {
        storagedSpawners.merge(type, amount, (oldValue, newValue) -> {
            if ((oldValue - newValue) <= 0) {
                return 0;
            }

            return oldValue - newValue;
        });
    }
}
