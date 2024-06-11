package org.apache.commons.lang3.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SerializationUtils}
 */
public class SerializationUtilsTest {

    private Serializable serializableObject;
    private Map<String, Integer> map;

    @Before
    public void setup() {
        serializableObject = new SerializedObject();
        map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
    }

    @Test
    public void testClone() throws SerialException {
        Serializable clone = SerializationUtils.clone(serializableObject);
        assertEquals(serializableObject.toString(), clone.toString());
    }

    @Test
    public void testSerializeDeserialize() throws IOException, SerialException {
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);
        Serializable deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        assertEquals(serializableObject.toString(), deserializedObject.toString());
    }

    @Test
    public void testSerializeDeserializeMap() throws IOException, SerialException {
        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));
        assertEquals(map, deserializedMap);
    }

    @Test(expected = SerialException.class)
    public void testSerializeFail() throws IOException {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerialException.class)
    public void testDeserializeFail() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream os = outputStream;
        SerializationUtils.serialize(serializableObject, os);
        byte[] serializedObject = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertNull(deserializedObject);
    }

    private static class SerializedObject implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "SerializedObject{}";
        }
    }

    private static class SerialException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SerialException(String message, Throwable cause) {
            super(message, cause);
        }

        public SerialException(Throwable cause) {
            super(cause);
        }
    }

}