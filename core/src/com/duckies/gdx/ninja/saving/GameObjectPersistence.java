package com.duckies.gdx.ninja.saving;

import com.duckies.gdx.ninja.pojo.PlayerInstance;
import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class GameObjectPersistence {

    private static final Kryo kryo = new Kryo();

    static {
        kryo.register(PlayerInstance.class);
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


}
