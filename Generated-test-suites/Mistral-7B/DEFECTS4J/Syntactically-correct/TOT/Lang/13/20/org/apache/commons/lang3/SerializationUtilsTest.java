package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.testutils.SerializationTestUtils;
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

/**
 * Tests for SerializationUtils class.
 */
public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(original);

        Assert.assertNotNull(cloned);
        Assert.assertEquals(original.size(), cloned.size());
        for (Map.Entry<String, Integer> entry : original.entrySet()) {
            Assert.assertEquals(entry.getKey(), cloned.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), cloned.get(entry.getKey()));
        }
    }

    @Test
    public void testSerialAndDeserial() throws IOException, ClassNotFoundException {
        SerializableSerializableObject serializableObject = new SerializableSerializableObject("Test");
        byte[] serialized = SerializationUtils.serialize(serializableObject);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(serializableObject.getValue(), deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerialAndDeserialWithException() throws IOException, ClassNotFoundException {
        SerializableSerializableObject serializableObject = new SerializableSerializableObject(null);
        byte[] serialized = SerializationUtils.serialize(serializableObject);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
    }

    @Test(expected = SerializationException.class)
    public void testSerialAndDeserialWithInvalidClass() throws IOException, ClassNotFoundException {
        SerializableSerializableObject serializableObject = new SerializableSerializableObject("Test");
        byte[] serialized = SerializationUtils.serialize(serializableObject);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized), null);
    }

    @Test
    public void testSerialAndDeserialWithCustomClassLoader() throws IOException, ClassNotFoundException {
        MyCustomClass customClass = new MyCustomClass();
        byte[] serialized = SerializationUtils.serialize(customClass, SerializationTestUtils.getClassLoaderForTest());
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized), SerializationTestUtils.getClassLoaderForTest());
        Assert.assertEquals(customClass.getValue(), deserialized);
    }

    private static class SerializableSerializableObject implements Serializable {
        private String value;

        public SerializableSerializableObject(String value) {
            this.value = value;
        }

        private Object readResolve() throws ObjectStreamException {
            return new SerializableSerializableObject(value);
        }

        public String getValue() {
            return value;
        }
    }

    private static class MyCustomClass implements Serializable {
        private int value;

        public MyCustomClass() {
            this.value = 0;
        }

        public int getValue() {
            return value;
        }

        private Object readResolve() throws ObjectStreamException {
            return new MyCustomClass();
        }
    }
}