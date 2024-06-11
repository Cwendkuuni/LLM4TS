package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.exception.SerializationException;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    private static final Object SERIALIZABLE_OBJECT = new HashMap<>();

    @Test
    public void testClone() throws SerializationException, IOException {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);

        // Test cloning a null object
        Object nullObject = null;
        try {
            SerializationUtils.clone(nullObject);
            Assert.fail("Expected SerializationException when cloning a null object.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerializeDeserialize() throws SerializationException, IOException {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);

        // Test serializing and deserializing a null object
        Object nullObject = null;
        try {
            byte[] serializedNull = SerializationUtils.serialize(nullObject);
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedNull));
            Assert.fail("Expected SerializationException when serializing a null object.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testIOExceptionWhileReadingClonedObjectData() {
        Object original = new HashMap<>();
        try {
            byte[] objectData = SerializationUtils.serialize(original);
            InputStream inputStream = new ByteArrayInputStream(objectData);
            throw new IOException("IOException while reading cloned object data");
            Object cloned = SerializationUtils.deserialize(inputStream);
        } catch (SerializationException e) {
            Assert.assertEquals("IOException while reading cloned object data", e.getMessage());
        }
    }

    @Test
    public void testClassNotFoundExceptionWhileReadingClonedObjectData() {
        Object original = new HashMap<>();
        try {
            byte[] objectData = SerializationUtils.serialize(original);
            InputStream inputStream = new ByteArrayInputStream(objectData);
            Class<?> clazz = Class.forName("non.existent.class");
            ObjectInputValidationMonitor monitor = new ObjectInputValidationMonitor();
            ObjectInputStream in = new ObjectInputStream(inputStream) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    return clazz;
                }
            };
            in.setValidationNewAndProtected(monitor);
            throw new ClassNotFoundException("ClassNotFoundException while reading cloned object data");
            Object cloned = in.readObject();
        } catch (SerializationException e) {
            Assert.assertEquals("ClassNotFoundException while reading cloned object data", e.getCause().getMessage());
        }
    }

    @Test
    public void testIOExceptionOnClosingClonedObjectDataInputStream() throws IOException {
        Object original = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(original);
        InputStream inputStream = new ByteArrayInputStream(objectData);
        try {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            in.close();
            Object cloned = in.readObject();
        } catch (IOException e) {
            Assert.assertEquals("IOException on closing cloned object data InputStream.", e.getMessage());
        }
    }

    @Test
    public void testSerializationException() {
        Object original = new HashMap<>();
        try {
            byte[] objectData = SerializationUtils.serialize(original);
            throw new SerializationException("SerializationException");
            Object cloned = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
        } catch (SerializationException e) {
            // Expected
        }
    }
}