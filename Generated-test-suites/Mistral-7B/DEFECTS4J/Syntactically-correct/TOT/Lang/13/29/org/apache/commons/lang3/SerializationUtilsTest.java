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
 * Test class for {@link SerializationUtils}
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

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap.size(), clonedMap.size());
        Assert.assertEquals(originalMap.get("one"), clonedMap.get("one"));
        Assert.assertEquals(originalMap.get("two"), clonedMap.get("two"));
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}.
     */
    @Test
    public void testSerialize() throws IOException {
        Serializable serializableObject = new SerializableObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableObject, baos);

        Assert.assertNotNull(baos.toByteArray());
        Assert.assertGreaterThan(baos.toByteArray().length, 0);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)}.
     */
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        SerializableObject serializableObject = new SerializableObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableObject, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        Object deserializedObject = SerializationUtils.deserialize(bais);

        Assert.assertNotNull(deserializedObject);
        Assert.assertTrue(deserializedObject instanceof SerializableObject);
        SerializableObject deserializedSerializableObject = (SerializableObject) deserializedObject;
        Assert.assertEquals(serializableObject.value, deserializedSerializableObject.value);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])}.
     */
    @Test
    public void testDeserializeByteArray() throws IOException, ClassNotFoundException {
        SerializableObject serializableObject = new SerializableObject();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableObject, baos);
        byte[] serializedData = baos.toByteArray();

        Object deserializedObject = SerializationUtils.deserialize(serializedData);

        Assert.assertNotNull(deserializedObject);
        Assert.assertTrue(deserializedObject instanceof SerializableObject);
        SerializableObject deserializedSerializableObject = (SerializableObject) deserializedObject;
        Assert.assertEquals(serializableObject.value, deserializedSerializableObject.value);
    }

    /**
     * Serializable test object for testing the {@link SerializationUtils} methods.
     */
    static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        int value = 42;
    }
}