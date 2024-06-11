package org.apache.commons.lang3.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    /**
     * Test method for {@link SerializationUtils#clone(Object)}
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);

        // Clone the original map
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        // Check if cloned map is equal to the original one
        assertEquals(map, clonedMap);

        // Change a value in the cloned map
        clonedMap.put("three", 3);

        // Check if the original map is not affected
        assertNotNull(map.get("three"));
        assertNull(map.get("three"));

        // Test cloning null object
        SerializationUtils.clone(null);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}
     */
    @Test
    public void testSerialize() throws IOException {
        String originalString = "Hello, World!";

        // Serialize the string to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalString, outputStream);

        // Deserialize the string from the ByteArrayOutputStream
        String deserializedString = (String) SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));

        // Check if the deserialized string is equal to the original one
        assertEquals(originalString, deserializedString);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)}
     */
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        String originalString = "Hello, World!";

        // Serialize the string to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalString, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Deserialize the string from the ByteArrayInputStream
        String deserializedString = (String) SerializationUtils.deserialize(inputStream);

        // Check if the deserialized string is equal to the original one
        assertEquals(originalString, deserializedString);

        // Test deserializing null object
        SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])}
     */
    @Test
    public void testDeserializeByteArray() throws IOException, ClassNotFoundException {
        String originalString = "Hello, World!";

        // Serialize the string to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalString, outputStream);
        byte[] serializedData = outputStream.toByteArray();

        // Deserialize the string from the byte array
        String deserializedString = (String) SerializationUtils.deserialize(serializedData);

        // Check if the deserialized string is equal to the original one
        assertEquals(originalString, deserializedString);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with a custom
     * {@link ClassLoader}
     */
    @Test
    public void testDeserializeWithCustomClassLoader() throws IOException, ClassNotFoundException {
        MySerializableClass mySerializableClass = new MySerializableClass();
        MySerializableClass serializedObject = null;

        // Serialize the object to a ByteArrayOutputStream with a custom ClassLoader
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(mySerializableClass, outputStream, new MyClassLoader());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // Deserialize the object from the ByteArrayInputStream with the same custom ClassLoader
        serializedObject = (MySerializableClass) SerializationUtils.deserialize(inputStream, MyClassLoader.class);

        // Check if the deserialized object is equal to the original one
        assertEquals(mySerializableClass, serializedObject);
    }
}

class MySerializableClass implements Serializable {
    // Implement your serializable class here
}

class MyClassLoader extends ClassLoader {
    // Implement your custom ClassLoader here
}