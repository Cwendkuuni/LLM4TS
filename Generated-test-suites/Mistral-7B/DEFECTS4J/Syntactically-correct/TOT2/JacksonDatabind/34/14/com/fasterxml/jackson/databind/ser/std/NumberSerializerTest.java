import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws IOException {
        JsonGenerator generator = null; // Initialize JSON generator mock or real instance
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Number number = 10;
        serializer.serialize(number, generator, null); // Test the serialize method with an integer
    }

    // Add test cases for other data types
}