package com.github.merelysnow.geradores.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.EntityType;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Data
public class GeneratorsLogs {

    private final UUID uuid;
    private final String factionTag;
    private EntityType entityType;
    private int amount;
    private String player;
    private String action;
    private Date date;
}
