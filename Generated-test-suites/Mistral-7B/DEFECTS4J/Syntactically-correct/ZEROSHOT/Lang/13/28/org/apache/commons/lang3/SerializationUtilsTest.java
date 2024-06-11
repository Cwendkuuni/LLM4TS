package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Assert; import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    private static final byte[] SERIALIZED_OBJECT = new byte[] {
            // byte array representation of a serialized object
    };

    private Object testObject;
    private Map<String, String> map;

    @Before
    public void setUp() {
        testObject = new Object() {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return "Test Object";
            }
        };

        map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
    }

    @After
    public void tearDown() throws Exception {
        testObject = null;
        map = null;
    }

    @Test
    public void testClone() {
        // Test cloning an object using SerializationUtils
        SerializationUtils serializationUtils = new SerializationUtils();
        Object clonedObject = serializationUtils.clone(testObject);
        Assert.assertNotSame(testObject, clonedObject);
        Assert.assertEquals(testObject.toString(), clonedObject.toString());
    }

    @Test
    public void testSerialization() throws IOException {
        // Test serializing an object to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, baos);
        byte[] serializedByteArray = baos.toByteArray();
        Assert.assertArrayNotEquals(SERIALIZED_OBJECT, serializedByteArray);

        // Test serializing an object to an InputStream
        ByteArrayInputStream bais = new ByteArrayInputStream(SERIALIZED_OBJECT);
        Object deserializedObject = SerializationUtils.deserialize(bais);
        Assert.assertNotSame(testObject, deserializedObject);
        Assert.assertEquals(testObject.toString(), deserializedObject.toString());
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        // Test throwing a SerializationException when deserializing an invalid object
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(bais);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionWithIOException() throws IOException {
        // Test throwing a SerializationException when deserializing with an IOException
        ByteArrayInputStream bais = new ByteArrayInputStream(SERIALIZED_OBJECT);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject(); // throw an IOException
    }

    @Test
    public void testMapSerialization() throws IOException {
        // Test serializing and deserializing a Map
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, baos);
        byte[] serializedMapByteArray = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMapByteArray);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(bais);
        Assert.assertEquals(map, deserializedMap);
    }

}