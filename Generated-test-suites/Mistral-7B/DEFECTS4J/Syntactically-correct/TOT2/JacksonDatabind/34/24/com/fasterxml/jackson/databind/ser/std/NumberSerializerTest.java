import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.fasterxml.jackson.core.version.Versions.VERSION_2_13;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    void testSerialize_Integer() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new AnnotationIntrospector());
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        ObjectNode result = mapper.writerWithView(SerializationFeature.FAST_SERIALIZATION_FEATURE).writeValueAsTree(new ObjectMapper(), 1);
        JsonGenerator g = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), VERSION_2_13);
        serializer.serialize(1, g, new SerializerProvider());
        assertEquals(result.toString(), g.getOutputContent().toString());
        g.close();
    }

    // Add similar test cases for other types: BigDecimal, BigInteger, Integer, Long, Double, Float, Byte, and Short
}