package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws Exception {
        Object original = new Object() {
            private static final long serialVersionUID = -1L;

            @SuppressWarnings("unused")
            private transient int ignoredField;

            public Object clone() throws CloneNotSupportedException {
                throw new CloneNotSupportedException();
            }
        };

        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getClass(), cloned.getClass());

        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        Object clonedMap = SerializationUtils.clone(map);
        Assert.assertEquals(map, clonedMap);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws Exception {
        Object original = new Object() {
            private static final long serialVersionUID = 1L;

            public String toString() {
                return "SerializedObject";
            }
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);
        byte[] serializedData = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(original, deserializedObject);
    }

    @Test
    public void testSerializeAndDeserializeMap() throws Exception {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("foo", 1);
        originalMap.put("bar", 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, outputStream);
        byte[] serializedData = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(serializedData);
        @SuppressWarnings("unchecked")
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws Exception {
        Object obj = new Object() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            private transient int ignoredField;

            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException();
            }

            private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
                throw new IOException();
            }
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
    }
}