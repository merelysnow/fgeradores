package com.github.merelysnow.geradores.repository.adapter;

import com.github.merelysnow.geradores.data.GeneratorsLogs;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import org.bukkit.entity.EntityType;

import java.util.Date;
import java.util.UUID;

public class GeneratorsLogsAdapter implements SQLResultAdapter<GeneratorsLogs> {

    @Override
    public GeneratorsLogs adaptResult(SimpleResultSet resultSet) {

        final UUID uuid = UUID.fromString(resultSet.get("uuid"));
        final String factionTag = resultSet.get("factionTag");
        final EntityType entityType = EntityType.valueOf(resultSet.get("entityType"));
        final int amount = resultSet.get("amount");
        final String player = resultSet.get("player");
        final String action = resultSet.get("action");
        final Date date = new Date((long) resultSet.get("date"));

        return new GeneratorsLogs(uuid, factionTag, entityType, amount, player, action, date);
    }
}
