package com.github.merelysnow.geradores.database;

import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.data.GeneratorsLogs;
import com.github.merelysnow.geradores.database.adapter.FactionGeneratorsAdapter;
import com.github.merelysnow.geradores.database.adapter.GeneratorsLogsAdapter;
import com.github.merelysnow.geradores.database.connection.RepositoryProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GeneratorsLogsDataBase extends RepositoryProvider {

    private static final String TABLE_NAME = "logs_table";
    private SQLExecutor executor;

    public GeneratorsLogsDataBase(Plugin instance) {
        super(instance);
        prepare();
        createTable();
    }

    @Override
    public SQLConnector prepare() {
        final SQLConnector prepare = super.prepare();
        executor = new SQLExecutor(prepare);
        return prepare;
    }

    public void createTable() {
        executor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "uuid VARCHAR(37) NOT NULL," +
                "factionTag VARCHAR(4) NOT NULL," +
                "entityType VARCHAR(16) NOT NULL," +
                "amount INTEGER NOT NULL," +
                "player VARCHAR(32) NOT NULL," +
                "action VARCHAR(5) NOT NULL," +
                "date TIMESTAMP NOT NULL" +
                ");");
    }

    public Set<GeneratorsLogs> selectMany(String factionTag) {
        return executor.resultManyQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE factionTag = ?", simpleStatement -> {
                simpleStatement.set(1, factionTag); }, GeneratorsLogsAdapter.class);
    }

    public void create(GeneratorsLogs generatorsLogs) {
        CompletableFuture.runAsync(() -> {
            executor.updateQuery("INSERT INTO " + TABLE_NAME + " VALUES(?,?,?,?,?,?,?)", simpleStatement -> {
                simpleStatement.set(1, generatorsLogs.getUuid().toString());
                simpleStatement.set(2, generatorsLogs.getFactionTag());
                simpleStatement.set(3, generatorsLogs.getEntityType().name());
                simpleStatement.set(4, generatorsLogs.getAmount());
                simpleStatement.set(5, generatorsLogs.getPlayer());
                simpleStatement.set(6, generatorsLogs.getAction());
                simpleStatement.set(7, generatorsLogs.getDate().getTime());
            });
        });
    }
}
