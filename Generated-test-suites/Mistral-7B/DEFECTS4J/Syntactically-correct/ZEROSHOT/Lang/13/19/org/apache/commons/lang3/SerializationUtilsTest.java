import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws Exception {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test(expected = SerializationException.class)
    public void testCloneNull() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserialize() throws Exception {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeNull() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws Exception {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        InputStream inputStream = new ByteArrayInputStream(serialized);
        ClassLoader classLoader = this.getClass().getClassLoader();
        Object deserialized = SerializationUtils.deserialize(new ClassLoaderAwareObjectInputStream(inputStream, classLoader));
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeObjectStreamClass() throws Exception {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(serialized));
        ObjectStreamClass objectStreamClass = inputStream.readObject();
        Object deserialized = inputStream.readObject();
        Assert.assertEquals(original, deserialized);
        Assert.assertEquals(ObjectStreamClass.lookup(original.getClass()), objectStreamClass);
        inputStream.close();
    }
}