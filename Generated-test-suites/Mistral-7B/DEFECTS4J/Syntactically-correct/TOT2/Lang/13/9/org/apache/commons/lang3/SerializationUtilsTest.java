package org.apache.commons.lang3;

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
 * Test cases for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    /**
     * Test method for {@link SerializationUtils#clone(Object)}.
     */
    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}.
     */
    @Test
    public void testSerializeToOutputStream() throws IOException {
        Serializable serializableObject = new HashMap<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableObject, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        Assert.assertEquals(serializableObject, deserializedObject);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Serializable)} and {@link SerializationUtils#deserialize(byte[])}.
     */
    @Test
    public void testSerializeToByteArray() throws IOException, ClassNotFoundException {
        Map<String, Integer> serializableObject = new HashMap<>();
        serializableObject.put("one", 1);
        serializableObject.put("two", 2);

        byte[] serializedObjectInBytes = SerializationUtils.serialize(serializableObject);
        Object deserializedObject = SerializationUtils.deserialize(serializedObjectInBytes);

        Assert.assertEquals(serializableObject, deserializedObject);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with an empty input stream.
     */
    @Test(expected = SerializationException.class)
    public void testEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(emptyInputStream);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with a corrupted input stream.
     */
    @Test(expected = IOException.class)
    public void testCorruptedInputStream() throws IOException {
        ByteArrayInputStream corruptedInputStream = new ByteArrayInputStream(new byte[]{'A'});
        SerializationUtils.deserialize(corruptedInputStream);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with a null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with a null byte array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullByteArray() {
        SerializationUtils.deserialize(null);
    }
}