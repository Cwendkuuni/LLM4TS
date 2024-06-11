package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

public class SerializationUtilsTest {

    // Test Clone Method
    @org.junit.Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        assertNotNull(clonedMap);
        assertEquals(originalMap.size(), clonedMap.size());
        assertEquals(originalMap.get("one"), clonedMap.get("one"));
        assertEquals(originalMap.get("two"), clonedMap.get("two"));

        // Test cloning null object
        assertNull(SerializationUtils.clone(null));
    }

    // Test Serialize Method
    @org.junit.Test
    public void testSerialize() {
        Serializable serializableObject = new SerializableObject();

        byte[] serializedObject = SerializationUtils.serialize(serializableObject);

        assertNotNull(serializedObject);

        // Test serializing null object
        try {
            SerializationUtils.serialize(null);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    // Test Deserialize Method
    @org.junit.Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, bos);

        ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(bais);

        assertNotNull(deserializedMap);
        assertEquals(originalMap.size(), deserializedMap.size());
        assertEquals(originalMap.get("one"), deserializedMap.get("one"));
        assertEquals(originalMap.get("two"), deserializedMap.get("two"));

        // Test deserializing null object
        try {
            SerializationUtils.deserialize(null);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    // SerializableObject is a simple Serializable class for testing
    static class SerializableObject implements Serializable {

    }

}