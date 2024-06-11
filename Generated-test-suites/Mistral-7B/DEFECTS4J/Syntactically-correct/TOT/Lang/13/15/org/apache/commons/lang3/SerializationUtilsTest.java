package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.exception.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap.size(), clonedMap.size());
        Assert.assertEquals(originalMap, clonedMap); // using deep equals
        Assert.assertNotSame(originalMap, clonedMap);

        // Test cloning null object
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        byte[] serializedData = SerializationUtils.serialize(originalMap);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(inputStream);

        Assert.assertNotNull(deserializedMap);
        Assert.assertEquals(originalMap.size(), deserializedMap.size());
        Assert.assertEquals(originalMap, deserializedMap); // using deep equals

        // Test serialization and deserialization of null object
        SerializationUtils.serialize(null);
        Object deserializedNullObject = SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
        Assert.assertNull(deserializedNullObject);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        byte[] objectData = new byte[0];
        SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(1024));
    }
}