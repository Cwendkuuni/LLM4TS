package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.exception.SerializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test cases for SerializationUtils class.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SerializationUtilsTest {

    // Clone tests

    /**
     * Test cloning of a simple object.
     */
    @Test
    public void testCloneSimpleObject() {
        String original = "Test String";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    /**
     * Test cloning of a complex object.
     */
    @Test
    public void testCloneComplexObject() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("Key1", "Value1");
        originalMap.put("Key2", "Value2");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        assertEquals(originalMap.size(), clonedMap.size());

        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedValue = clonedMap.get(key);
            assertEquals(value, clonedValue);
        }
    }

    /**
     * Test cloning of a null object.
     */
    @Test(expected = NullPointerException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    // Serialize tests

    /**
     * Test serialization of a simple object.
     */
    @Test
    public void testSerializeSimpleObject() {
        String original = "Test String";
        byte[] serialized = SerializationUtils.serialize(original);

        String cloned = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(original, cloned);
    }

    /**
     * Test serialization of a complex object.
     */
    @Test
    public void testSerializeComplexObject() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("Key1", "Value1");
        originalMap.put("Key2", "Value2");

        byte[] serialized = SerializationUtils.serialize(originalMap);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(originalMap.size(), deserializedMap.size());

        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String deserializedValue = deserializedMap.get(key);
            assertEquals(value, deserializedValue);
        }
    }

    /**
     * Test serialization of a null object.
     */
    @Test(expected = NullPointerException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    // Deserialize tests

    /**
     * Test deserialization of a simple object.
     */
    @Test
    public void testDeserializeSimpleObject() {
        String original = "Test String";
        byte[] serialized = SerializationUtils.serialize(original);

        String cloned = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(original, cloned);
    }

    /**
     * Test deserialization of a complex object.
     */
    @Test
    public void testDeserializeComplexObject() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("Key1", "Value1");
        originalMap.put("Key2", "Value2");

        byte[] serialized = SerializationUtils.serialize(originalMap);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(originalMap.size(), deserializedMap.size());

        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String deserializedValue = deserializedMap.get(key);
            assertEquals(value, deserializedValue);
        }
    }

    /**
     * Test deserialization of a null object.
     */
    @Test(expected = NullPointerException.class)
    public void testDeserializeNullObject() {
        SerializationUtils.deserialize(null);
    }

    // SerializationException tests

    /**
     * Test SerializationException when deserializing a null object.
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeNullObjectSerializationException() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test SerializationException when deserializing invalid data.
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidDataSerializationException() {
        InputStream invalidDataStream = new ByteArrayInputStream(new byte[]{"Invalid Data"});
        SerializationUtils.deserialize(invalidDataStream);
    }
}