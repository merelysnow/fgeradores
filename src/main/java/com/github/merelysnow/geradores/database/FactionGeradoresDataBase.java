package com.github.merelysnow.geradores.database;

import com.github.merelysnow.geradores.data.FactionGeradores;
import com.github.merelysnow.geradores.database.adapter.FactionGeradoresAdapter;
import com.github.merelysnow.geradores.database.connection.RepositoryProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FactionGeradoresDataBase extends RepositoryProvider {

    private static final String TABLE_NAME = "fgeradores_table";
    private static final Gson GSON = new GsonBuilder().create();
    private SQLExecutor executor;

    public FactionGeradoresDataBase(Plugin instance) {
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

    public Set<FactionGeradores> selectMany() {
        return executor.resultManyQuery(
                "SELECT * FROM " + TABLE_NAME, simpleStatement -> {
                }, FactionGeradoresAdapter.class);
    }

    public void save(FactionGeradores factionGeradores) {
        CompletableFuture.runAsync(() -> {
            executor.updateQuery("UPDATE " + TABLE_NAME + " SET storagedSpawners = ? WHERE factionTag = ?", simpleStatement -> {
                simpleStatement.set(1, GSON.toJson(factionGeradores.getStoragedSpawners()));
                simpleStatement.set(2, factionGeradores.getFactionTag());
            });
        });
    }

    public void create(FactionGeradores factionGeradores) {
        CompletableFuture.runAsync(() -> {
            executor.updateQuery("INSERT INTO " + TABLE_NAME + " VALUES(?,?)", simpleStatement -> {
                simpleStatement.set(1, factionGeradores.getFactionTag());
                simpleStatement.set(2, GSON.toJson(factionGeradores.getStoragedSpawners()));
            });
        });
    }

    public FactionGeradores load(String factionID) {
        return executor.resultOneQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE factionTag = ?",
                simpleStatement -> simpleStatement.set(1, factionID),
                FactionGeradoresAdapter.class);
    }

    public void delete(FactionGeradores factionGeradores) {
        CompletableFuture.runAsync(() ->
                executor.updateQuery("DELETE FROM " + TABLE_NAME + " WHERE factionTag = ?", simpleStatement -> {
                    simpleStatement.set(1, factionGeradores.getFactionTag());
                }));
    }
}
