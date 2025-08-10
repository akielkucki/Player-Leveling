package com.gungens.misc.services;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Service {
    private static final Logger log = LoggerFactory.getLogger(Base64Service.class);

    public static String encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] decode(String encoded) {
        return java.util.Base64.getDecoder().decode(encoded);
    }
    public static ItemStack decodeItemStack(String encoded) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decode(encoded));
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                return (ItemStack) dataInput.readObject();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decode ItemStack from Base64", e);
        }
    }
    public static String encodeItemStack(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                dataOutput.writeObject(item);
            }
            return encode(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to encode ItemStack to Base64", e);
        }
    }
}
