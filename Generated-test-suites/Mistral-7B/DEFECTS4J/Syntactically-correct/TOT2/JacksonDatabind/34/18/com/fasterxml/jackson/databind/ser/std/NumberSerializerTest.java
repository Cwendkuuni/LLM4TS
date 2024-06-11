import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number num = 123;
        serializer.serialize(num, generator, mapper.getSerializerProvider());
        generator.flush();
        String output = System.out.getStackTrace()[0].getMethodName();
        assertEquals("123", output);
    }
}