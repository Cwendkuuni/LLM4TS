import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.base.TestUtils.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.math.*;

public class NumberSerializerTest {
    @Test
    public void testSerialize_Integer() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        Number number = 123;
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.intValue(), parser.getValueAsInt());
    }

    @Test
    public void testSerialize_Long() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        Number number = 987654321L;
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.longValue(), parser.getValueAsLong());
    }

    @Test
    public void testSerialize_Float() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        Number number = 123.45f;
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.floatValue(), parser.getValueAsFloat(), 0.001f);
    }

    @Test
    public void testSerialize_Double() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        Number number = 123.45;
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.doubleValue(), parser.getValueAsDouble(), 0.001);
    }

    @Test
    public void testSerialize_BigDecimal() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.doubleValue(), parser.getValueAsDouble(), 0.001);
    }

    @Test
    public void testSerialize_BigInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        BigInteger number = new BigInteger("987654321");
        serializer.serialize(number, gen, provider);

        JsonParser parser = createJsonParser(new ByteArrayInputStream(gen.getOutputBuffer().getBytes()));
        assertEquals(number.toString(), parser.getValueAsString());
    }

    @Test(expected = IOException.class)
    public void testSerialize_UnsupportedType() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = createJsonGenerator();
        SerializerProvider provider = new StandardSerializerProvider();

        Number number = new Object();
        serializer.serialize(number, gen, provider);
    }
}