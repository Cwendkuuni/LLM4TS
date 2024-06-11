import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Test for clone method with valid input
    @Test
    public void testCloneValidInput() throws Exception {
        HashMap<String, Integer> originalMap = new HashMap<>();
        originalMap.put("key1", 1);
        originalMap.put("key2", 2);

        HashMap<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        assertEquals(originalMap, clonedMap);
    }

    // Test for clone method with null input
    @Test(expected = NullPointerException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    // Test for serialize method with valid input
    @Test
    public void testSerializeValidInput() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        byte[] serializedData = SerializationUtils.serialize(map);

        HashMap<String, Integer> deserializedMap = SerializationUtils.deserialize(serializedData);

        assertEquals(map, deserializedMap);
    }

    // Test for serialize method with null input
    @Test(expected = NullPointerException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method with valid input
    @Test
    public void testDeserializeValidInput() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        byte[] serializedData = SerializationUtils.serialize(map);

        HashMap<String, Integer> deserializedMap = SerializationUtils.deserialize(serializedData);

        assertEquals(map, deserializedMap);
    }

    // Test for deserialize method with null input
    @Test(expected = NullPointerException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }
}