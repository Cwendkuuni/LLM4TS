import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testNumberSerializerWithBigDecimal() {
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");

        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(bigDecimal);
    }

    @Test
    public void testNumberSerializerWithBigInteger() {
        NumberSerializer numberSerializer = new NumberSerializer(BigInteger.class);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");

        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);

        verify(jsonGenerator, times(1)).writeNumber(bigInteger);
    }

    // Test getSchema and acceptJsonFormatVisitor methods here
}