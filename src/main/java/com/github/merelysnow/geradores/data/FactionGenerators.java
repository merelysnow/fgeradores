package com.github.merelysnow.geradores.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.EntityType;

import java.util.Map;

@AllArgsConstructor
@Data
public class FactionGenerators {

    private final String factionTag;
    private Map<EntityType, Integer> storagedSpawners;
    private boolean open;

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
