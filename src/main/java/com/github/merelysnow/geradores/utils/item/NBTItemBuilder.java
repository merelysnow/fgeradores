/*
 * Copyright (c) 2022.
 *
 * Developers: Gabriel Santos, Leo Freitas
 *
 * Spring, Inc (github.com/rede-spring)
 */

package com.github.merelysnow.geradores.utils.item;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@RequiredArgsConstructor
public class NBTItemBuilder {

    protected final NBTTagCompound nbt;
    protected final ItemStackBuilder itemStackBuilder;

    public NBTItemBuilder setString(String key, String value) {
        nbt.setString(key, value);
        return this;
    }

    public NBTItemBuilder setLong(String key, long value) {
        nbt.setLong(key, value);
        return this;
    }

    public NBTItemBuilder setInt(String key, int value) {
        nbt.setInt(key, value);
        return this;
    }

    public NBTItemBuilder setByte(String key, byte value) {
        nbt.setByte(key, value);
        return this;
    }

    public NBTItemBuilder setShort(String key, short value) {
        nbt.setShort(key, value);
        return this;
    }

    public NBTItemBuilder setFloat(String key, float value) {
        nbt.setFloat(key, value);
        return this;
    }

    public NBTItemBuilder setDouble(String key, double value) {
        nbt.setDouble(key, value);
        return this;
    }

    public NBTItemBuilder setByteArray(String key, byte[] value) {
        nbt.setByteArray(key, value);
        return this;
    }

    public NBTItemBuilder setIntArray(String key, int[] value) {
        nbt.setIntArray(key, value);
        return this;
    }

    public NBTItemBuilder setBoolean(String key, boolean value) {
        nbt.setBoolean(key, value);
        return this;
    }

    public NBTItemBuilder setCompound(String key, NBTTagCompound value) {
        nbt.set(key, value);
        return this;
    }

    public NBTTagCompound getNbt() {
        return nbt;
    }

    public ItemStackBuilder apply() {
        itemStackBuilder.setNbt(nbt);
        return itemStackBuilder;
    }
}
