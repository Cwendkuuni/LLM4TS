import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add assertions here if needed
    }

    @Test
    public void testSerializeWithBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeNumber(bigDecimal);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeWithNullJsonGenerator() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(bigDecimal, null, serializerProvider);
    }

    // Test other methods and their edge cases and exceptions similarly

    // ...
}