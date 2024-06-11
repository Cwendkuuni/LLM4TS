import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.Serializable;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws InstantiationException, IllegalAccessException, CloneNotSupportedException, SerializationException {
        Cloneable objectToClone = new CloneableObject();
        Serializable serializedObject = SerializationUtils.clone(objectToClone);
        Object clonedObject = SerializationUtils.clone(serializedObject);
        Assert.assertNotSame(objectToClone, clonedObject);
        Assert.assertEquals(objectToClone.hashCode(), clonedObject.hashCode());
    }

    @Test
    public void testSerializeAndDeserializeObject() throws SerializationException, IOException {
        Object objectToSerialize = new ObjectToSerialize();
        byte[] serializedData = SerializationUtils.serialize(objectToSerialize);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(objectToSerialize, deserializedObject);
    }

    @Test
    public void testSerializeAndDeserializeMap() throws SerializationException, IOException {
        Map<String, Integer> mapToSerialize = new HashMap<>();
        mapToSerialize.put("key1", 1);
        mapToSerialize.put("key2", 2);
        byte[] serializedData = SerializationUtils.serialize(mapToSerialize);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(mapToSerialize, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionWhenNullInput() throws SerializationException {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionWhenNullOutputStream() throws SerializationException {
        SerializationUtils.serialize(new Object(), null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionWhenIOException() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        OutputStream outputStreamForSerializationException = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Simulated exception during writing to OutputStream.");
            }
        };
        try {
            SerializationUtils.serialize(new Object(), outputStreamForSerializationException);
        } catch (SerializationException e) {
            throw new AssertionError("Expected IOException, but received SerializationException.", e);
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationExceptionWhenNullInputStream() throws SerializationException, IOException {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationExceptionWhenIOException() throws SerializationException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        InputStream inputStreamForSerializationException = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated exception during reading from InputStream.");
            }
        };
        try {
            SerializationUtils.deserialize(inputStreamForSerializationException);
        } catch (SerializationException e) {
            throw new AssertionError("Expected IOException, but received SerializationException.", e);
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassNotFoundExceptionWhenDeserializingNonSerializableObject() throws SerializationException, IOException, ClassNotFoundException {
        byte[] serializedData = SerializationUtils.serialize(new NonSerializableObject());
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
            Assert.fail();
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ClassLoader customClassLoader = this.getClass().getClassLoader();
        Object serializedObject = new ObjectToSerialize();
        byte[] serializedData = SerializationUtils.serialize(serializedObject);
        Map<String, ObjectStreamClass> expectedObjectStreamClasses = new HashMap<>();
        expectedObjectStreamClasses.put(SerializationUtils.class.getName(), new ObjectStreamClass(SerializationUtils.class));
        expectedObjectStreamClasses.put(ObjectStreamClass.class.getName(), new ObjectStreamClass(ObjectStreamClass.class));
        expectedObjectStreamClasses.put(ObjectOutputStream.class.getName(), new ObjectStreamClass(ObjectOutputStream.class));
        expectedObjectStreamClasses.put(ObjectInputStream.class.getName(), new ObjectStreamClass(ObjectInputStream.class));
        expectedObjectStreamClasses.put(ClassLoader.class.getName(), new ObjectStreamClass(ClassLoader.class));
        expectedObjectStreamClasses.put(Object.class.getName(), new ObjectStreamClass(Object.class));
        Class<?>[] expectedClasses = new Class[expectedObjectStreamClasses.size()];
        expectedClasses = expectedObjectStreamClasses.values().toArray(expectedClasses);
        ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serializedData), customClassLoader);
        ClassLoaderAwareObjectInputStream deserializedObject = (ClassLoaderAwareObjectInputStream) classLoaderAwareObjectInputStream.readObject();
        Assert.assertEquals(SerializationUtils.class, deserializedObject.getClass());
        Assert.assertArrayEquals(expectedClasses, deserializedObject.getClasses());
    }

    private static class CloneableObject implements Cloneable, Serializable {
        private static final long serialVersionUID = 1L;
    }

    private static class ObjectToSerialize implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    private static class NonSerializableObject implements Cloneable {
        private static final long serialVersionUID = 1L;
    }
}