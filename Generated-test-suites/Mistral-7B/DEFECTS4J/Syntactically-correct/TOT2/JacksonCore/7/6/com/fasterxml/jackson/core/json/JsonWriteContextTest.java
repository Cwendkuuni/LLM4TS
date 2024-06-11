import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {
    @Test
    public void testWriteValue() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
}