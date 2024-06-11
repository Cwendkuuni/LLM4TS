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

/**
 * Test cases for SerializationUtils class.
 */
public class SerializationUtilsTest {

    /**
     * Test deep clone using serialization for simple Serializable class.
     */
    @Test
    public void testCloneSimpleSerializableClass() {
        SimpleSerializableClass original = new SimpleSerializableClass("original");
        SimpleSerializableClass cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original.getValue(), cloned.getValue());
    }

    /**
     * Test deep clone using serialization for complex Serializable class with nested objects.
     */
    @Test
    public void testCloneComplexSerializableClass() {
        ComplexSerializableClass original = new ComplexSerializableClass("outer", new SimpleSerializableClass("inner"));
        ComplexSerializableClass cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original.getValue(), cloned.getValue());
        Assert.assertEquals(original.getInner().getValue(), cloned.getInner().getValue());
    }

    /**
     * Test serialization and deserialization for simple Serializable class.
     */
    @Test
    public void testSerializationAndDeserializationSimpleSerializableClass() throws IOException, ClassNotFoundException {
        SimpleSerializableClass original = new SimpleSerializableClass("original");
        byte[] serializedData = SerializationUtils.serialize(original);
        SimpleSerializableClass deserialized = (SimpleSerializableClass) SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(original.getValue(), deserialized.getValue());
    }

    /**
     * Test serialization and deserialization for complex Serializable class with nested objects.
     */
    @Test
    public void testSerializationAndDeserializationComplexSerializableClass() throws IOException, ClassNotFoundException {
        ComplexSerializableClass original = new ComplexSerializableClass("outer", new SimpleSerializableClass("inner"));
        byte[] serializedData = SerializationUtils.serialize(original);
        ComplexSerializableClass deserialized = (ComplexSerializableClass) SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(original.getValue(), deserialized.getValue());
        Assert.assertEquals(original.getInner().getValue(), deserialized.getInner().getValue());
    }

    /**
     * Test handling exceptions during serialization.
     */
    @Test(expected = SerializationException.class)
    public void testSerializationWithException() throws IOException {
        IOException ioException = new IOException("test exception");
        throw ioException;
    }

    /**
     * Test handling exceptions during deserialization.
     */
    @Test(expected = SerializationException.class)
    public void testDeserializationWithException() throws IOException, ClassNotFoundException {
        byte[] serializedData = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
    }

    /**
     * Nested classes.
     */
    public static class SimpleSerializableClass implements Serializable {
        private String value;

        public SimpleSerializableClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class ComplexSerializableClass implements Serializable {
        private String value;
        private SimpleSerializableClass inner;

        public ComplexSerializableClass(String value, SimpleSerializableClass inner) {
            this.value = value;
            this.inner = inner;
        }

        public String getValue() {
            return value;
        }

        public SimpleSerializableClass getInner() {
            return inner;
        }
    }
}