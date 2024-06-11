import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValueWithString() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getRootNameLookup());

        String value = "Hello World";
        xmlProvider.serializeValue(gen, value);

        String actualXml = new String(gen.getOutputBuffer().asByteArray());
        String expectedXml = "<string>Hello World</string>";

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithInteger() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getRootNameLookup());

        int value = 42;
        xmlProvider.serializeValue(gen, value);

        String actualXml = new String(gen.getOutputBuffer().asByteArray());
        String expectedXml = "<int>42</int>";

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithNull() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getRootNameLookup());

        xmlProvider.serializeValue(gen, null);

        String actualXml = new String(gen.getOutputBuffer().asByteArray());
        String expectedXml = "<null></null>";

        assertEquals(expectedXml, actualXml);
    }

    @Test(expected = IOException.class)
    public void testSerializeValueWithUnsupportedType() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator gen = new JsonFactory(config).createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(mapper.getRootNameLookup());

        Object value = new Object(){};
        xmlProvider.serializeValue(gen, value);

        fail("Expected IOException but got none.");
    }
}