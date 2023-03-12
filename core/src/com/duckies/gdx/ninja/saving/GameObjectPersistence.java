package com.duckies.gdx.ninja.saving;

import com.badlogic.gdx.math.Vector2;
import com.duckies.gdx.ninja.pojo.Inventory;
import com.duckies.gdx.ninja.pojo.ItemInstance;
import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class GameObjectPersistence {

    private static final Kryo kryo = new Kryo();

    static {
        kryo.register(PlayerInstance.class);
        kryo.register(Inventory.class);
        kryo.register(ItemInstance.class);
        kryo.register(Vector2.class);

    }

    public static void save(int id, Object object) {
        String saveFilename = String.format("save/%s_%s.bin", object.getClass().getSimpleName(), id);
        try (Output output = new Output(new FileOutputStream(saveFilename))) {
            kryo.writeObject(output, object);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T load(int id, Class<T> clazz) {
        String saveFilename = String.format("save/%s_%s.bin", clazz.getSimpleName(), id);
        try (Input input = new Input(new FileInputStream(saveFilename))) {
            return kryo.readObject(input, clazz);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public static <T> List<T> loadAll(Class<T> clazz) {

        List<T> results = new ArrayList<>();
        File directory = new File("save/");
        String pattern = clazz.getSimpleName() + "_.*\\.bin"; // pattern for files ending in ".txt"
        File[] files = directory.listFiles((dir, name) -> name.matches(pattern));

        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        for (File file : files) {
            results.add(load(Integer.parseInt(file.getName().substring(file.getName().indexOf("_") + 1,
                    file.getName().lastIndexOf("."))),clazz));
        }

        return results;
    }
}
