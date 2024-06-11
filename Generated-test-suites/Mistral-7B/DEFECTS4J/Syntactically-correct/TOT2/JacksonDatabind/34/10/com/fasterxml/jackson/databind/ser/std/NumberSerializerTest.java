import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BigDecimal number = new BigDecimal("123.45");

        numberSerializer.serialize(number, mockGenerator, mockProvider);

        verify(mockGenerator, times(1)).writeNumber(number);
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BigInteger number = new BigInteger("12345678901234567890");

        numberSerializer.serialize(number, mockGenerator, mockProvider);

        verify(mockGenerator, times(1)).writeNumber(number);
    }

    // Add test cases for other primitive types

    @Test
    public void testSerialize_UnsupportedType() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(String.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        String number = "UnsupportedType";

        numberSerializer.serialize(number, mockGenerator, mockProvider);

        verify(mockGenerator, times(1)).writeNumber(number.toString());
    }
}