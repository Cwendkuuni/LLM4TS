package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
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
    public void testClone() throws Exception {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializationException() {
        try {
            SerializationUtils.serialize(null);
            Assert.fail();
        } catch (SerializationException e) {
            // expected
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws Exception {
        Map<String, Object> original = new HashMap<>();
        original.put("test", "test");

        byte[] serialized = SerializationUtils.serialize(original);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        Object deserialized = SerializationUtils.deserialize(new ClassLoaderAwareObjectInputStream(inputStream, getClass().getClassLoader()));

        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testClassLoaderAwareObjectInputStreamWithCustomClassLoader() throws Exception {
        Map<String, Object> original = new HashMap<>();
        original.put("test", "test");

        ClassLoader customClassLoader = getClass().getClassLoader().getParent();
        byte[] serialized = SerializationUtils.serialize(original);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        Object deserialized = SerializationUtils.deserialize(new ClassLoaderAwareObjectInputStream(inputStream, customClassLoader));

        Assert.assertEquals(original, deserialized);
    }
}