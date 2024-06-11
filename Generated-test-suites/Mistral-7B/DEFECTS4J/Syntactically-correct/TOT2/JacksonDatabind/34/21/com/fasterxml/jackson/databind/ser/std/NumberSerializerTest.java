import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        serializer.serialize(1, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        serializer.serialize(1L, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        serializer.serialize(1.0, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        serializer.serialize(1.0f, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        BigDecimal bd = new BigDecimal("123.45");
        serializer.serialize(bd, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = null; // Initialize JsonGenerator in your setup method
        BigInteger bi = new BigInteger("123456789012345678901234567890");
        serializer.serialize(bi, gen, new SerializerProvider());
        // Add assertions for the serialized output here
    }

    // Add test cases for exceptional scenarios like null value, invalid input, etc.
}