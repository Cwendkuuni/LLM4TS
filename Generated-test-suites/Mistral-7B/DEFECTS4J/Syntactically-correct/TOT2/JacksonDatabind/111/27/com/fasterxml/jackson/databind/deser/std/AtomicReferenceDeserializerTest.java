import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testReferenceValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object value = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                new TypeReference<AtomicReference<Object>>() {}.getType(), null, null, null);
        AtomicReference<Object> reference = deserializer.referenceValue(value);
        assertNotNull(reference);
    }
}