import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Given
        String original = "Hello World";
        String expected = "Hello World";

        // When
        String cloned = SerializationUtils.clone(original);

        // Then
        assertEquals(expected, cloned);
    }

    @Test(expected = SerializationException.class)
    public void testCloneNullInput() {
        // Given
        String input = null;

        // When, Then
        SerializationUtils.clone(input);
    }

    @Test
    public void testSerializeDeserializeString() {
        // Given
        String original = "Hello World";

        // When
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);

        // Then
        assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullInput() {
        // Given
        String input = null;

        // When, Then
        SerializationUtils.serialize(input, new ByteArrayOutputStream());
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        // Given
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key1", new Object() {});
        byte[] serialized = SerializationUtils.serialize(inputMap);

        // When
        Map<String, Object> deserialized = (Map<String, Object>) SerializationUtils.deserialize(serialized);

        // Then
        assertEquals(inputMap, deserialized);
    }
}