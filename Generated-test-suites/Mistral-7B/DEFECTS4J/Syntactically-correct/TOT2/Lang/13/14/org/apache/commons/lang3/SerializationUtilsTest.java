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

public class SerializationUtilsTest {

    // Test for clone method

    @Test
    public void testClone() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("One", 1);
        originalMap.put("Two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap, clonedMap);

        // Test for null input
        Assert.assertNull(SerializationUtils.clone(null));
    }

    // Test for serialize method

    @Test
    public void testSerializeToOutputStream() {
        String testObject = "Test Object";

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream)) {
            SerializationUtils.serialize(testObject, out);
        }

        byte[] serializedObject = ((ByteArrayOutputStream) byteArrayOutputStream).toByteArray();

        Assert.assertNotNull(serializedObject);
        Assert.assertNotNull(SerializationUtils.deserialize(serializedObject));

        // Test for null input
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testSerializeToByteArray() {
        String testObject = "Test Object";

        byte[] serializedObject = SerializationUtils.serialize(testObject);
        Object deserializedObject = SerializationUtils.deserialize(serializedObject);

        Assert.assertNotNull(serializedObject);
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);

        // Test for null input
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null));
    }

    // Test for deserialize method

    @Test
    public void testDeserializeFromInputStream() throws IOException, ClassNotFoundException {
        String testObject = "Test Object";
        byte[] serializedObject = SerializationUtils.serialize(testObject);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);

        Object deserializedObject = SerializationUtils.deserialize(byteArrayInputStream);

        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);

        // Test for null input
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeFromByteArray() throws IOException, ClassNotFoundException {
        String testObject = "Test Object";
        byte[] serializedObject = SerializationUtils.serialize(testObject);

        Object deserializedObject = SerializationUtils.deserialize(serializedObject);

        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);

        // Test for empty byte array
        byte[] emptyByteArray = new byte[0];
        Assert.assertNull(SerializationUtils.deserialize(emptyByteArray));

        // Test for invalid byte array
        byte[] invalidByteArray = {(byte) 0x01, 0x02, 0x03, 0xFF};
        Assert.assertNull(SerializationUtils.deserialize(invalidByteArray));
    }
}