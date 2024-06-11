import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.IOContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    private JsonGenerator jsonGenerator;
    private IOContext ioContext;

    @Before
    public void setup() {
        ioContext = new IOContext();
        jsonGenerator = new JsonGeneratorImpl(ioContext, 0, null);
    }

    @Test
    public void testWriteFieldName() throws IOException {
        String fieldName = "testFieldName";
        jsonGenerator.writeFieldName(fieldName);
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals(fieldName, actual);
    }

    @Test
    public void testWriteString() throws IOException {
        String value = "testStringValue";
        jsonGenerator.writeString(value);
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals(value, actual);
    }

    // You would need to add similar test cases for other methods in the class
}