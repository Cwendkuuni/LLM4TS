import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
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
    public void testClone() throws SerializationException, CloneNotSupportedException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotSame(originalMap, clonedMap);
        Assert.assertEquals(originalMap, clonedMap);
    }

    // Test for serialize method with OutputStream
    @Test
    public void testSerializeOutputStream() throws SerializationException {
        String testObject = "testString";
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        Assert.assertEquals(testObject, deserializedObject);
    }

    // Test for serialize method with byte array
    @Test
    public void testSerializeByteArray() throws SerializationException {
        String testObject = "testString";
        byte[] serializedObject = SerializationUtils.serialize(testObject);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        Assert.assertEquals(testObject, deserializedObject);
    }

    // Test for deserialize method with InputStream
    @Test
    public void testDeserializeInputStream() throws SerializationException, IOException, ClassNotFoundException {
        String testObject = "testString";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(testObject);
        objectOutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        Assert.assertEquals(testObject, deserializedObject);
    }

    // Test for deserialize method with byte array
    @Test
    public void testDeserializeByteArray() throws SerializationException, IOException, ClassNotFoundException {
        String testObject = "testString";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(testObject);
        objectOutputStream.close();

        byte[] serializedObject = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        Assert.assertEquals(testObject, deserializedObject);
    }

    // Test for ClassLoaderAwareObjectInputStream
    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException, ClassNotFoundException {
        String testObject = "testString";
        byte[] serializedObject = SerializationUtils.serialize(testObject);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);

        ObjectInputStream objectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(inputStream, SerializationUtils.class);
        Object deserializedObject = objectInputStream.readObject();

        Assert.assertEquals(testObject, deserializedObject);
    }

    // Test for exception handling when passing null to OutputStream or InputStream
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionHandlingOutputStream() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionHandlingInputStream() {
        SerializationUtils.deserialize(null);
    }
}