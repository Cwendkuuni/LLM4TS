import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

        Assert.assertNotSame(originalMap, clonedMap);
        Assert.assertEquals(originalMap, clonedMap);

        for (Map.Entry<String, Integer> entry : originalMap.entrySet()) {
            Assert.assertEquals(entry.getKey(), clonedMap.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), clonedMap.get(entry.getKey()));
        }
    }

    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedData = SerializationUtils.serialize(originalMap);

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        Assert.assertNotSame(originalMap, deserializedMap);
        Assert.assertEquals(originalMap, deserializedMap);

        for (Map.Entry<String, Integer> entry : originalMap.entrySet()) {
            Assert.assertEquals(entry.getKey(), deserializedMap.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), deserializedMap.get(entry.getKey()));
        }
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        Object obj = new Object() {
            private static final long serialVersionUID = 1L;
        };
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(obj, bos);
        } catch (SerializationException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationException() {
        byte[] serializedData = SerializationUtils.serialize(new Object());
        InputStream inputStream = new ByteArrayInputStream(serializedData);
        try {
            SerializationUtils.deserialize(inputStream);
        } catch (IOException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        SerializationUtils.serialize(new Object(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        SerializationUtils.deserialize(null);
    }
}