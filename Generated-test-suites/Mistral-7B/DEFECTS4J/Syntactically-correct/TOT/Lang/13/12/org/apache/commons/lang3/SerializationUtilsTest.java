package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    private static final int TEST_OBJECT_SIZE = 1024;

    private byte[] serializedObject;
    private Serializable testObject;

    @Before
    public void setUp() {
        testObject = new HashMap<Object, Object>(TEST_OBJECT_SIZE);
        for (int i = 0; i < TEST_OBJECT_SIZE; i++) {
            testObject.put(Integer.valueOf(i), String.valueOf(i));
        }
        serializedObject = SerializationUtils.serialize(testObject);
    }

    @After
    public void tearDown() {
        testObject = null;
        serializedObject = null;
    }

    /**
     * Test for clone() method.
     */
    @Test
    public void testClone() {
        Serializable clonedObject = SerializationUtils.clone(testObject);
        assertNotNull(clonedObject);
        assertEquals(testObject, clonedObject);
    }

    /**
     * Test for clone() method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testCloneWithNullInput() {
        SerializationUtils.clone(null);
    }

    /**
     * Test for serialize() method.
     */
    @Test
    public void testSerialize() {
        byte[] deserializedObject = SerializationUtils.deserialize(serializedObject);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(deserializedObject));
        @SuppressWarnings("unchecked")
        Serializable deserializedTestObject = (Serializable) objectInputStream.readObject();
        assertEquals(testObject, deserializedTestObject);
    }

    /**
     * Test for serialize() method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testSerializeWithNullInput() {
        SerializationUtils.serialize(null, null);
    }

    /**
     * Test for deserialize() method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test for deserialize() method with empty byte array.
     */
    @Test
    public void testDeserializeWithEmptyByteArray() {
        byte[] emptyByteArray = new byte[0];
        Object deserializedObject = SerializationUtils.deserialize(emptyByteArray);
        assertNull(deserializedObject);
    }
}