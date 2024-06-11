import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider xmlSerializerProvider;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        xmlSerializerProvider = objectMapper.getSerializerProvider().findValueSerializer(String.class);
    }

    @Test
    public void testSerializeValue_String() throws Exception {
        String value = "Hello, World!";
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(stringWriter);
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        jsonGenerator.close();

        String actualXml = stringWriter.toString();
        String expectedXml = "<string>Hello, World!</string>";

        assertEquals(expectedXml, actualXml);
    }
}