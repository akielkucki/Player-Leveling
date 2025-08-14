package com.gungens.misc.libs;

import com.gungens.misc.models.Reward;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

public class Base64Utils {
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static ItemStack deserializeItem(String base64) throws IOException, ClassNotFoundException {
        byte[] decoded = decode(base64);
        BukkitObjectInputStream stream = new BukkitObjectInputStream(new ByteArrayInputStream(decoded));
        ItemStack itemStack = (ItemStack) stream.readObject();
        stream.close();
        return itemStack;

    }
    public static String serializeItem(ItemStack itemStack) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream stream = new BukkitObjectOutputStream(outputStream);
        stream.writeObject(itemStack);
        return encode(outputStream.toByteArray());
    }

}
