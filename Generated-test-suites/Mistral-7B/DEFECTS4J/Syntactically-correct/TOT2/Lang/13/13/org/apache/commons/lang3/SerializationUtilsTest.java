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

    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
        Assert.assertNotSame(originalMap, clonedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test
    public void testDeserializeObject() throws IOException, ClassNotFoundException {
        String testString = "Test Object";
        Serializable serializableObject = new String(testString);
        byte[] objectData = SerializationUtils.serialize(serializableObject);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
        Assert.assertEquals(serializableObject, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserializeEmptyByteArray() {
        byte[] emptyByteArray = new byte[0];
        Object object = SerializationUtils.deserialize(new ByteArrayInputStream(emptyByteArray));
        Assert.assertNull(object);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[512]);
        bais.close();
        Object object = SerializationUtils.deserialize(bais);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Object testSerializableObject = new TestSerializableClass();
        byte[] objectData = SerializationUtils.serialize(testSerializableObject);

        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, this.getClass().getClassLoader());

        Object readObject = ois.readObject();
        Assert.assertEquals(testSerializableObject, readObject);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassLoaderAwareObjectInputStreamUnknownClass() throws IOException, ClassNotFoundException {
        Object testSerializableObject = new TestSerializableClass();
        byte[] objectData = SerializationUtils.serialize(testSerializableObject);

        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("UnknownClassName");
            }
        });

        Object readObject = ois.readObject();
        Assert.assertNull(readObject);
    }
}

class TestSerializableClass implements Serializable {
    private static final long serialVersionUID = 1L;
}