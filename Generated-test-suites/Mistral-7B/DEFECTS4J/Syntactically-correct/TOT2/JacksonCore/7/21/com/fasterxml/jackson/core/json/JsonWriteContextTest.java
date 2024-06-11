import org.junit.Test;
import static org.junit.Assert.*;

public class TestJsonWriteContextConstructor {
    @Test
    public void testJsonWriteContextConstructor() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertNotNull(jsonWriteContext);
    }
}