import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.serialize(new BigDecimal("123.45"), generator, provider);

        generator.flush();
        String output = generator.getOutputStr();
        Assert.assertEquals("123.45", output);
    }
}