package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    /**
     * Test cloning an object using serialization
     */
    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("Key1", "Value1");
        originalMap.put("Key2", "Value2");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap.size(), clonedMap.size());
        Assert.assertEquals(originalMap.get("Key1"), clonedMap.get("Key1"));
        Assert.assertEquals(originalMap.get("Key2"), clonedMap.get("Key2"));
    }

    /**
     * Test serializing and deserializing an object
     */
    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("Key1", "Value1");
        originalMap.put("Key2", "Value2");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, bos);
        byte[] serializedData = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(bis);

        Assert.assertNotNull(deserializedMap);
        Assert.assertEquals(originalMap.size(), deserializedMap.size());
        Assert.assertEquals(originalMap.get("Key1"), deserializedMap.get("Key1"));
        Assert.assertEquals(originalMap.get("Key2"), deserializedMap.get("Key2"));
    }

    /**
     * Test serializing and deserializing an object with custom class loader
     */
    @Test
    public void testCustomClassLoader() throws IOException, ClassNotFoundException {
        CustomSerializable customObject = new CustomSerializable("Test");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(customObject, bos);
        byte[] serializedData = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(bis);

        Assert.assertEquals(customObject.getValue(), deserializedObject);
    }

    /**
     * Test exception handling when serializing null object
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    /**
     * Test exception handling when deserializing null stream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Custom serializable class
     */
    static class CustomSerializable implements Serializable {
        private String value;

        public CustomSerializable(String value) {
            this.value = value;
        }

        private Object writeReplace() throws ObjectStreamException {
            return new SerializableProxy(this.getClass(), this);
        }

        private Object readResolve() throws ObjectStreamException {
            return this;
        }

        public String getValue() {
            return value;
        }
    }
}