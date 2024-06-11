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
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap); // check if the cloned map is deep cloned and equals the original map
    }

    @Test
    public void testSerializeDeserialize() throws IOException, SerializationException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        byte[] serializedData = SerializationUtils.serialize(originalMap);

        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedData);

        Assert.assertEquals(originalMap, deserializedMap); // check if the deserialized map is equal to the original map
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null); // testing for IllegalArgumentException when passing null object to serialize method
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() {
        InputStream nullInputStream = null;
        SerializationUtils.deserialize(nullInputStream); // testing for SerializationException when passing null InputStream to deserialize method
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeUnknownClass() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new Exception("UnknownClass"));
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Object obj = SerializationUtils.deserialize(bais); // testing for ClassNotFoundException when the deserialized class is not known
    }
}