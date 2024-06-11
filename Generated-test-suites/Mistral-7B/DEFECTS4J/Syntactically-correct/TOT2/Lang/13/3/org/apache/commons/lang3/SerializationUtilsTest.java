import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    // Test for clone method
    @Test
    public void testClone() {
        String input = "Hello World";
        String cloned = SerializationUtils.clone(input);
        assertEquals(input, cloned);
    }

    // Test for serialize method with valid Input
    @Test
    public void testSerializeValidInput() {
        String input = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(input);
        String deserializedData = SerializationUtils.deserialize(serializedData);
        assertEquals(input, deserializedData);
    }

    // Test for serialize method with null Input
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method with valid Input
    @Test
    public void testDeserializeValidInput() {
        String input = "Hello World";
        byte[] serializedData = SerializationUtils.serialize(input);
        String deserializedData = SerializationUtils.deserialize(serializedData);
        assertEquals(input, deserializedData);
    }

    // Test for deserialize method with null Input
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }
}