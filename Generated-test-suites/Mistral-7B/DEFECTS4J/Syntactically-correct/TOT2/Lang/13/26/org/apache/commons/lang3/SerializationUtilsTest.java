import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException {
        String original = new String("Original String");
        String cloned = SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertEquals(original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeString() throws SerializationException, IOException {
        String original = new String("Original String");
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeHashMap() throws SerializationException, IOException {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);
        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() throws SerializationException {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() throws SerializationException, IOException {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidClass() throws SerializationException, IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(new Object() {});
        byte[] objectData = baos.toByteArray();
        Object deserialized = SerializationUtils.deserialize(objectData);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException {
        String original = new String("Original String");
        byte[] serialized = SerializationUtils.serialize(original);
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(serialized));
        Object deserialized = inputStream.readObject();
        assertEquals(original, deserialized);
    }
}