import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {
    @Test
    public void testSerializeNumber() throws Exception {
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper().getSerializerProvider());
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Number number = 123; // any Number type
        numberSerializer.serialize(number, generator, new ObjectMapper().getSerializerProvider());
        generator.flush();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) generator.getOutputShop();
        String output = byteArrayOutputStream.toString();
        // Add assertion here to check the expected JSON output
    }
}