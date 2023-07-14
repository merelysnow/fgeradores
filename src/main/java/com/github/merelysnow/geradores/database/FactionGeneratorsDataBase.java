package com.github.merelysnow.geradores.database;

import com.github.merelysnow.geradores.data.FactionGenerators;
import com.github.merelysnow.geradores.database.adapter.FactionGeneratorsAdapter;
import com.github.merelysnow.geradores.database.connection.RepositoryProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FactionGeneratorsDataBase extends RepositoryProvider {

    private static final String TABLE_NAME = "fgeradores_table";
    private static final Gson GSON = new GsonBuilder().create();
    private SQLExecutor executor;

    public FactionGeneratorsDataBase(Plugin instance) {
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
                "factionTag VARCHAR(4) NOT NULL PRIMARY KEY," +
                "storagedSpawners TEXT NOT NULL" +
                ");");
    }

    public Set<FactionGenerators> selectMany() {
        return executor.resultManyQuery(
                "SELECT * FROM " + TABLE_NAME, simpleStatement -> {
                }, FactionGeneratorsAdapter.class);
    }

    public void save(FactionGenerators factionGenerators) {
        CompletableFuture.runAsync(() -> {
            executor.updateQuery("UPDATE " + TABLE_NAME + " SET storagedSpawners = ? WHERE factionTag = ?", simpleStatement -> {
                simpleStatement.set(1, GSON.toJson(factionGenerators.getStoragedSpawners()));
                simpleStatement.set(2, factionGenerators.getFactionTag());
            });
        });
    }

    public void create(FactionGenerators factionGenerators) {
        CompletableFuture.runAsync(() -> {
            executor.updateQuery("INSERT INTO " + TABLE_NAME + " VALUES(?,?)", simpleStatement -> {
                simpleStatement.set(1, factionGenerators.getFactionTag());
                simpleStatement.set(2, GSON.toJson(factionGenerators.getStoragedSpawners()));
            });
        });
    }

    public FactionGenerators load(String factionID) {
        return executor.resultOneQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE factionTag = ?",
                simpleStatement -> simpleStatement.set(1, factionID),
                FactionGeneratorsAdapter.class);
    }

    public void delete(FactionGenerators factionGenerators) {
        CompletableFuture.runAsync(() ->
                executor.updateQuery("DELETE FROM " + TABLE_NAME + " WHERE factionTag = ?", simpleStatement -> {
                    simpleStatement.set(1, factionGenerators.getFactionTag());
                }));
    }
}
