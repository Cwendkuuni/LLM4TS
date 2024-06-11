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
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotNull(cloned);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned); // checking for deep cloning
    }

    @Test(expected = SerializationException.class)
    public void testCloneNull() {
        SerializationUtils.clone(null);
    }

    // Test for serialize method

    @Test
    public void testSerializeToOutputStream() {
        String original = "Hello World";
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);
        byte[] serializedData = ((ByteArrayOutputStream) outputStream).toByteArray();
        String deserializedData = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(original, deserializedData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInputStream() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    // Test for serialize method to byte array

    @Test
    public void testSerializeToByteArray() {
        String original = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(original);
        String deserializedData = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        Assert.assertEquals(original, deserializedData);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeToByteArrayNullObject() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method

    @Test
    public void testDeserializeFromInputStream() {
        String original = "Hello World";
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String deserializedData = (String) SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(original, deserializedData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    // Test for deserialize method from byte array

    @Test
    public void testDeserializeFromByteArray() {
        String original = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(original);
        String deserializedData = (String) SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(original, deserializedData);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    // Test for ClassLoaderAwareObjectInputStream

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        try {
            Map<String, Integer> original = new HashMap<>();
            original.put("one", 1);
            original.put("two", 2);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            OutputStream outputStream = new ClassLoaderAwareObjectOutputStream(byteArrayOutputStream, SerializationUtils.class.getClassLoader());
            SerializationUtils.serialize(original, outputStream);
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            Object deserializedData = SerializationUtils.deserialize(inputStream);
            Map<String, Integer> deserializedMap = (Map<String, Integer>) deserializedData;
            Assert.assertEquals(original, deserializedMap);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClassLoaderAwareObjectOutputStream extends ObjectOutputStream {
        public ClassLoaderAwareObjectOutputStream(OutputStream outputStream, ClassLoader classLoader) throws IOException {
            super(outputStream);
            this.defaultReadObjectHandler = new ClassLoaderAwareObjectInputStream.ClassResolver();
            this.writeStream = new ClassLoaderAwareByteArrayOutputStream(outputStream, classLoader);
        }
    }

    static class ClassLoaderAwareByteArrayOutputStream extends ByteArrayOutputStream {
        private final OutputStream outputStream;
        private final ClassLoader classLoader;

        public ClassLoaderAwareByteArrayOutputStream(OutputStream outputStream, ClassLoader classLoader) {
            super(1024);
            this.outputStream = outputStream;
            this.classLoader = classLoader;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            outputStream.writeTo(out);
        }

        @Override
        public byte[] toByteArray() {
            return outputStream.toByteArray();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }
    }
}