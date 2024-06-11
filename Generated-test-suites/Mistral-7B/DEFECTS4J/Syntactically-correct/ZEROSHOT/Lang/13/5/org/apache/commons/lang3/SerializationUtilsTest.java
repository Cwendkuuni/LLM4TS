package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws SerializationException, InstantiationException, IllegalAccessException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void serializeDeserializeMap() throws SerializationException, IOException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);

        byte[] objectData = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(objectData);

        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void serializeDeserializeSimpleObject() throws SerializationException, IOException {
        SimpleObject original = new SimpleObject();
        original.setName("Original");
        byte[] objectData = SerializationUtils.serialize(original);
        SimpleObject deserialized = (SimpleObject) SerializationUtils.deserialize(objectData);

        Assert.assertEquals(original.getName(), deserialized.getName());
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws SerializationException {
        throw new SerializationException("Test SerializationException");
    }

    @Test
    public void testSerializationExceptionWithMessage() throws SerializationException {
        throw new SerializationException("Test SerializationException with message: serialization failed");
    }

    // Add other test methods as needed for the other methods in SerializationUtils class

    private static class SimpleObject implements Serializable {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}