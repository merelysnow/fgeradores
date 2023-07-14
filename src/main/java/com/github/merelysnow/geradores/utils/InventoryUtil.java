package com.github.merelysnow.geradores.utils;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public final class InventoryUtil {

    public void addItems(Player player, Collection<ItemStack> itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) continue;
            addItem(player, itemStack);
        }
    }

    public void addItems(Player player, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            addItem(player, itemStack);
        }
    }

    public boolean addItem(Player player, ItemStack itemStack) {
        final PlayerInventory inventory = player.getInventory();
        if (!isFull(player)) {
            inventory.addItem(itemStack);
            return true;
        }

        player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        return false;
    }

    public boolean isFull(Player player) {
        final PlayerInventory playerInventory = player.getInventory();
        return playerInventory.firstEmpty() == -1;
    }

    public boolean isFullWithArmor(Player player) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack[][] itemStacks = new ItemStack[][] {inventory.getContents(), inventory.getArmorContents()};

        final List<ItemStack> itemStackList =
                Stream.of(itemStacks).flatMap(Stream::of).collect(Collectors.toList());

        return itemStackList.stream()
                .anyMatch(itemStack -> itemStack != null && !itemStack.getType().equals(Material.AIR));
    }

    public void mergeItems(Player player) {
        final PlayerInventory inventory = player.getInventory();
        final Map<Integer, ItemStack> itemStackMap = Maps.newHashMap();

        Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .forEach(itemStack -> itemStackMap.put(itemStackMap.size(), itemStack.clone()));

        inventory.clear();

        itemStackMap.values().forEach(inventory::addItem);
    }

    public int count(Player player, ItemStack itemStack) {
        final PlayerInventory inventory = player.getInventory();
        final AtomicInteger count = new AtomicInteger(0);

        for (ItemStack content : inventory.getContents()) {
            if (!content.isSimilar(itemStack)) {
                continue;
            }

            count.getAndAdd(itemStack.getAmount());
        }

        return count.get();
    }

    public long getAmountOfSlotsEmpty(Player player) {
        return Arrays.stream(player.getInventory().getContents())
                .filter(itemStack -> itemStack == null || itemStack.getType() == Material.AIR)
                .count();
    }

    public void removeItemFromHand(Player player, int amount) {
        final ItemStack itemInHand = player.getItemInHand();
        final int handAmount = itemInHand.getAmount();

        if (handAmount > amount) {
            itemInHand.setAmount(handAmount - amount);
            return;
        }

        player.setItemInHand(null);
    }

    public void removeItemFromHand(Player player) {
        removeItemFromHand(player, 1);
    }

    public void removeItemFromCursor(Player player, int amount) {
        final ItemStack itemOnCursor = player.getItemOnCursor();
        final int cursorAmount = itemOnCursor.getAmount();

        if (cursorAmount > amount) {
            itemOnCursor.setAmount(cursorAmount - amount);
            return;
        }

        player.setItemOnCursor(null);
    }

    public void removeItem(Player player, ItemStack itemStack, int amount) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack[] contents = inventory.getContents();

        for (int slot = 0; slot < contents.length; slot++) {
            final ItemStack content = contents[slot];

            if (content == null) {
                continue;
            }

            if (!content.isSimilar(itemStack)) {
                continue;
            }

            if (amount == -1) {
                inventory.setItem(slot, null);
                continue;
            }

            if (amount < content.getAmount()) {
                content.setAmount(content.getAmount() - amount);
                continue;
            }

            inventory.setItem(slot, null);
            amount -= content.getAmount();
        }
    }
}