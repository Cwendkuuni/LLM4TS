package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
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

    // Clone tests

    /**
     * Test cloning an empty HashMap
     */
    @Test
    public void testCloneEmptyHashMap() {
        Map<String, String> original = new HashMap<>();
        Map<String, String> cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    /**
     * Test cloning a non-empty HashMap
     */
    @Test
    public void testCloneNonEmptyHashMap() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");
        Map<String, String> cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    /**
     * Test cloning a null HashMap
     */
    @Test(expected = SerializationException.class)
    public void testCloneNullHashMap() {
        Map<String, String> original = null;
        SerializationUtils.clone(original);
    }

    // Serialize tests

    /**
     * Test serializing and deserializing an empty HashMap
     */
    @Test
    public void testSerializeAndDeserializeEmptyHashMap() {
        Map<String, String> original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, String> deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test serializing and deserializing a non-empty HashMap
     */
    @Test
    public void testSerializeAndDeserializeNonEmptyHashMap() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");
        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, String> deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test serializing and deserializing a null HashMap
     */
    @Test(expected = SerializationException.class)
    public void testSerializeAndDeserializeNullHashMap() {
        Map<String, String> original = null;
        byte[] serialized = SerializationUtils.serialize(original);
        SerializationUtils.deserialize(serialized);
    }

    // Deserialize tests

    /**
     * Test deserializing from an input stream
     */
    @Test
    public void testDeserializeFromInputStream() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Map<String, String> deserialized = SerializationUtils.deserialize(inputStream);
        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test deserializing from a byte array
     */
    @Test
    public void testDeserializeFromByteArray() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");

        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, String> deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test deserializing a null object
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeNullObject() {
        byte[] serialized = null;
        SerializationUtils.deserialize(serialized);
    }

    // Edge cases and error scenarios

    /**
     * Test cloning an infinite recursion object graph
     */
    @Test(expected = StackOverflowError.class)
    public void testCloneInfiniteRecursion() {
        class InfiniteRecursion {
            InfiniteRecursion next = this;
        }

        InfiniteRecursion original = new InfiniteRecursion();
        SerializationUtils.clone(original);
    }

    /**
     * Test serializing and deserializing an infinite recursion object graph
     */
    @Test(expected = StackOverflowError.class)
    public void testSerializeAndDeserializeInfiniteRecursion() {
        class InfiniteRecursion {
            InfiniteRecursion next = this;
        }

        InfiniteRecursion original = new InfiniteRecursion();
        SerializationUtils.serialize(original, new ByteArrayOutputStream());
    }

    /**
     * Test deserializing an object with a custom ObjectStreamClass
     */
    @Test
    public void testDeserializeCustomObjectStreamClass() {
        class CustomObjectStreamClass extends ObjectStreamClass {
            protected Class<?> resolvedClass = null;

            @Override
            public Class<?> forInstanceData() {
                return resolvedClass;
            }

            @Override
            public void setClassDesc(ObjectInput in) throws IOException, ClassNotFoundException {
                resolvedClass = in.readClassDescs()[0];
            }
        }

        class CustomSerializable implements Serializable {
            private static final long serialVersionUID = 1L;
            private String value;

            public CustomSerializable(String value) {
                this.value = value;
            }

            private Object writeReplace() throws ObjectStreamException {
                return new CustomObjectStreamClass();
            }
        }

        CustomSerializable original = new CustomSerializable("value");
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertSame(original.getClass(), deserialized.getClass());
        Assert.assertEquals(original.value, ((CustomSerializable) deserialized).value);
    }
}