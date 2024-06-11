package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    /**
     * Test cloning an object using serialization.
     */
    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        Serializable serializedMap = SerializationUtils.serialize(map);
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        assertEquals(map, clonedMap);
    }

    /**
     * Test serializing and deserializing an object.
     */
    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(inputStream);

        assertEquals(map, deserializedMap);
    }

    /**
     * Test serializing an object with an exception.
     */
    @Test(expected = SerializationException.class)
    public void testSerializeWithException() throws IOException {
        Object obj = new Object() {
            @Override
            public void method() throws IOException {
                throw new IOException();
            }
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
    }

    /**
     * Test deserializing an object with an exception.
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeWithException() throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);

        SerializationUtils.deserialize(inputStream);
    }
}