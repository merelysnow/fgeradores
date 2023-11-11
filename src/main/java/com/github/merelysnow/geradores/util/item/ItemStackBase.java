/*
 * Copyright (c) 2022.
 *
 * Developers: Gabriel Santos, Leo Freitas
 *
 * Spring, Inc (github.com/rede-spring)
 */

package com.github.merelysnow.geradores.util.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemStackBase<T extends ItemStackBuilder> {

    T displayName(String name);

    T lore(List<String> lore);

    T loreWithPlaceholder(List<String> lore, ImmutableMap<String, String> placeholders);

    ItemStackBuilder addLore(String... lore);

    T lore(String... lines);

    T amount(int amount);

    T allFlags();

    T itemFlags(ItemFlag... flags);

    T glow(boolean state);

    T glow();

    T enchantment(Enchantment enchant, int level);

    T removeEnchantment(Enchantment enchant);

    T setNbt(NBTTagCompound nbt);

    NBTItemBuilder getNBTBuilder();

    T addNbt(String key, String value);

    T setUnbreakable(boolean unbreakable);

    List<String> getLore();

    String displayName();

    T markAntiDupeId();

    NBTTagCompound getNbtCompound();

    String getNbt(String key);

    boolean hasNbt(String key);

    ItemStack build();
}
