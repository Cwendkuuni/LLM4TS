import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = new String("Hello World");
        String cloned = SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertNotNull(cloned.getClass());
        assertNotSame(original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeString() {
        String original = new String("Hello World");
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertNotNull(deserialized);
        assertNotNull(deserialized.getClass());
        assertNotSame(original, deserialized);
        assertEqual(original, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeCustomClass() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        byte[] serialized = SerializationUtils.serialize(originalMap);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serialized);
        assertNotNull(deserializedMap);
        assertNotNull(deserializedMap.getClass());
        assertNotSame(originalMap, deserializedMap);
        assertTrue(deserializedMap.equals(originalMap));
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullObject() {
        SerializationUtils.deserialize(new byte[0]);
    }

    private void assertEqual(Object expected, Object actual) {
        if (expected == actual) {
            return;
        }
        if (expected instanceof String && actual instanceof String) {
            String exStr = (String) expected;
            String acStr = (String) actual;
            if (exStr.equals(acStr)) {
                return;
            }
        }
        throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }

}