import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonWriteContext;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext ctx = JsonWriteContext.createRootContext();
        int result = ctx.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }
}