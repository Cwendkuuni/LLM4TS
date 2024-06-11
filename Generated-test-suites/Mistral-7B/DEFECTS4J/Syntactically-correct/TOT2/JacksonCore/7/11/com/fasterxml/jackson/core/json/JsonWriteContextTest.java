import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {
    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
        int result = jsonWriteContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
}