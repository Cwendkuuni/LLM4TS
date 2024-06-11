package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SerializationUtilsTest {

    private Serializable testObject;

    @Before
    public void setUp() {
        testObject = new HashMap<Object, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
    }

    @Test
    public void testClone() throws SerializationException, CloneNotSupportedException {
        Serializable original = testObject;
        Serializable clone = SerializationUtils.clone(original);

        assertEquals(original, clone); // Objects are equal, not same instance
        assertEquals(original.getClass(), clone.getClass());

        Map clonedMap = (Map) clone;
        assertNotNull(clonedMap);
        assertEquals(original.size(), clonedMap.size());

        for (Object key : original.keySet()) {
            Object originalValue = original.get(key);
            Object clonedValue = clonedMap.get(key);
            assertEquals(originalValue, clonedValue);
        }
    }

    @Test
    public void testSerializeDeserialize() throws SerializationException, IOException {
        byte[] serialized = SerializationUtils.serialize(testObject);
        Serializable deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(testObject, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeException() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, outputStream); // Does not throw exception

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object deserialized = SerializationUtils.deserialize(inputStream);

        throw new SerializationException("Unexpected deserialization succeeded");
    }

    @After
    public void tearDown() throws IOException {
        if (testObject instanceof Closeable) {
            ((Closeable) testObject).close();
        }
    }
}