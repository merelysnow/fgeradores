/*
 * Copyright (c) 2022.
 *
 * Developers: Gabriel Santos, Leo Freitas
 *
 * Spring, Inc (github.com/rede-spring)
 */

package com.github.merelysnow.geradores.utils.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Atomics;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ItemStackBuilder implements ItemStackBase<ItemStackBuilder> {

    private static final String ANTIDUPE_ID = "factions:antidupe";
    protected ItemStack itemStack;
    protected ItemMeta itemMeta;

    protected NBTTagCompound nbt;

    public ItemStackBuilder() {
    }

    public ItemStackBuilder(Material material) {
        final ItemStack itemStack = new ItemStack(material);

        this.nbt = new NBTTagCompound();
        this.itemMeta = itemStack.getItemMeta();
        this.itemStack = itemStack;
    }

    public ItemStackBuilder(ItemStack itemStack) {
        itemMeta = itemStack.getItemMeta();

        final net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        nbt = (nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());

        nmsItem.setTag(nbt);

        this.itemStack = CraftItemStack.asBukkitCopy(nmsItem);
    }

    public ItemStackBuilder copy(ItemStack itemStack) {
        return new ItemStackBuilder(itemStack);
    }

    @Override
    public ItemStackBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    @Override
    public ItemStackBuilder glow(boolean state) {
        return state ? glow() : this;
    }

    @Override
    public String displayName() {
        return itemMeta.getDisplayName();
    }

    @Override
    public ItemStackBuilder markAntiDupeId() {
        return getNBTBuilder().setBoolean(ANTIDUPE_ID, true)
                .apply();
    }

    @Override
    public ItemStackBuilder displayName(String name) {
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        return this;
    }

    @Override
    public ItemStackBuilder loreWithPlaceholder(List<String> lore, ImmutableMap<String, String> placeholders) {
        final AtomicReference<List<String>> atomicReference = Atomics.newReference(lore);

        placeholders.forEach((key, placeholder) -> atomicReference.getAndUpdate(get -> get.stream()
                .map(line -> line.replace(key, placeholder).replace("&", "§"))
                .collect(Collectors.toList())));

        itemMeta.setLore(atomicReference.get());

        return this;
    }

    @Override
    public ItemStackBuilder allFlags() {
        itemMeta.addItemFlags(ItemFlag.values());
        return this;
    }

    @Override
    public ItemStackBuilder itemFlags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);

        return this;
    }

    @Override
    public ItemStackBuilder glow() {
        nbt.set("ench", new NBTTagList());

        return this;
    }

    @Override
    public ItemStackBuilder enchantment(Enchantment enchant, int level) {
        itemMeta.addEnchant(enchant, level, true);

        return this;
    }

    @Override
    public ItemStackBuilder removeEnchantment(Enchantment enchant) {
        itemMeta.removeEnchant(enchant);

        return this;
    }

    @Override
    public ItemStackBuilder setNbt(NBTTagCompound nbt) {
        this.nbt = nbt;
        return this;
    }

    @Override
    public NBTItemBuilder getNBTBuilder() {
        return new NBTItemBuilder(nbt, this);
    }

    @Override
    public ItemStackBuilder addNbt(String key, String value) {
        nbt.setString(key, value);

        return this;
    }

    @Override
    public List<String> getLore() {
        return itemMeta.getLore();
    }

    @Override
    public ItemStackBuilder lore(List<String> lore) {
        itemMeta.setLore(lore.stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()));

        return this;
    }

    @Override
    public ItemStackBuilder addLore(String... lines) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.addAll(Arrays.asList(lines));
        itemMeta.setLore(lore);
        return this;
    }

    @Override
    public ItemStackBuilder lore(String... lines) {
        itemMeta.setLore(
                Arrays.stream(lines).map(line -> line.replace("&", "§")).collect(Collectors.toList()));

        return this;
    }

    @Override
    public NBTTagCompound getNbtCompound() {
        return nbt;
    }

    @Override
    public String getNbt(String key) {
        return nbt.getString(key);
    }

    @Override
    public boolean hasNbt(String key) {
        return nbt.hasKey(key);
    }

    @Override
    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    @Override
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);

        return addFinalAtributes();
    }

    protected ItemStack addFinalAtributes() {
        final net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        nmsItem.setTag(nbt);

        final ItemStack finalItem = CraftItemStack.asBukkitCopy(nmsItem);
        final ItemMeta finalMeta = finalItem.getItemMeta();

        finalMeta.setDisplayName(itemMeta.getDisplayName());
        finalMeta.setLore(itemMeta.getLore());
        finalMeta.spigot().setUnbreakable(itemMeta.spigot().isUnbreakable());

        itemMeta.getEnchants().forEach((enchant, level) -> finalMeta.addEnchant(enchant, level, true));
        itemMeta.getItemFlags().forEach(finalMeta::addItemFlags);

        finalItem.setItemMeta(finalMeta);

        return finalItem;
    }
}
