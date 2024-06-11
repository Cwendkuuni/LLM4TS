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

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Serializable originalMap = map;
        Serializable clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
        Assert.assertNotSame(originalMap, clonedMap);

        // test cloning a null object
        Serializable nullObject = null;
        Serializable clonedNullObject = SerializationUtils.clone(nullObject);
        Assert.assertNull(clonedNullObject);

        // test cloning an empty map
        Map<String, Integer> emptyMap = new HashMap<>();
        Serializable emptyMapClone = SerializationUtils.clone(emptyMap);
        Assert.assertEquals(emptyMap, emptyMapClone);
        Assert.assertNotSame(emptyMap, emptyMapClone);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws IOException, ClassNotFoundException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Serializable serializableMap = map;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableMap, byteArrayOutputStream);
        byte[] serializedData = byteArrayOutputStream.toByteArray();

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        Assert.assertEquals(map, deserializedMap);

        // test serialization and deserialization of null object
        Serializable nullObject = null;
        SerializationUtils.serialize(nullObject, byteArrayOutputStream);
        byte[] serializedNullData = byteArrayOutputStream.toByteArray();
        Serializable deserializedNullObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedNullData));
        Assert.assertNull(deserializedNullObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        Serializable objectToSerialize = new Object() {
        };
        SerializationUtils.serialize(objectToSerialize, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        Serializable nullObject = null;
        SerializationUtils.clone(nullObject);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeFailure() throws IOException {
        Object obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException();
            }
        };
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, byteArrayOutputStream);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeFailure() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(byteArrayInputStream);
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        SerializationUtils.deserialize(byteArrayInputStream);
    }
}