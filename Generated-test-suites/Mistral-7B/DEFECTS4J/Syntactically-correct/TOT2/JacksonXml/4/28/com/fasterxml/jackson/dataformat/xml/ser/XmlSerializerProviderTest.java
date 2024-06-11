import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeNull() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        assertNull(provider.serializeValue(null, new JsonGenerator(new TokenBuffer())));
        XmlSerializerProvider xmlProvider = (XmlSerializerProvider) provider;
        JsonGenerator xmlGen = xmlProvider.createXmlGenerator();
        xmlProvider.serializeValue(null, xmlGen);
        String result = xmlGen.getOutputFactory().createWriter("XML").writeValueAsString(xmlGen);
        assertEquals("<null/>", result);
    }

    @Test
    public void testSerializeValue() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper jsonMapper = new ObjectMapper();
        SerializerProvider jsonProvider = jsonMapper.getSerializerProvider();
        SerializerProvider xmlProvider = (XmlSerializerProvider) provider;
        JsonSerializer<Object> jsonSerializer = jsonProvider.findValueSerializer(String.class, null);
        JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(String.class, null);

        String value = "Test Value";
        assertEquals(value, jsonSerializer.deserialize(jsonSerializer.serializeValue(value, new JsonGenerator(new TokenBuffer()))));
        assertEquals("<Test Value/>", xmlSerializer.serializeValue(value, new JsonGenerator(new TokenBuffer())).toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeException() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper jsonMapper = new ObjectMapper();
        SerializerProvider jsonProvider = jsonMapper.getSerializerProvider();
        SerializerProvider xmlProvider = (XmlSerializerProvider) provider;
        JsonSerializer<Object> jsonSerializer = jsonProvider.findValueSerializer(String[].class, null);
        try {
            xmlProvider.serializeValue(new Object() {
            }, new JsonGenerator(new TokenBuffer()));
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
        JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(String[].class, null);
        xmlSerializer.serializeValue(new String[]{"Test Value"}, new JsonGenerator(new TokenBuffer()));
    }

    @Test
    public void testSerializeArray() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper jsonMapper = new ObjectMapper();
        SerializerProvider jsonProvider = jsonMapper.getSerializerProvider();
        SerializerProvider xmlProvider = (XmlSerializerProvider) provider;
        JsonSerializer<Object> jsonSerializer = jsonProvider.findValueSerializer(String[].class, null);
        JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(String[].class, null);

        String[] value = {"Test Value 1", "Test Value 2"};
        assertEquals("[Test Value 1, Test Value 2]", jsonSerializer.deserialize(jsonSerializer.serializeValue(value, new JsonGenerator(new TokenBuffer()))).toString());
        assertEquals("<array><item>Test Value 1</item><item>Test Value 2</item></array>", xmlSerializer.serializeValue(value, new JsonGenerator(new TokenBuffer())).toString());
    }

    @Test
    public void testSerializeRootElement() throws Exception {
        XmlMapper mapper = new XmlMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        ObjectMapper jsonMapper = new ObjectMapper();
        SerializerProvider jsonProvider = jsonMapper.getSerializerProvider();
        SerializerProvider xmlProvider = (XmlSerializerProvider) provider;
        JsonSerializer<Object> jsonSerializer = jsonProvider.findValueSerializer(String.class, null);
        JsonSerializer<Object> xmlSerializer = xmlProvider.findValueSerializer(String.class, null);

        String value = "Test Value";
        JsonGenerator jsonGen = jsonProvider.getFactory().createGenerator(new File("test.json"), null);
        jsonSerializer.serializeValue(value, jsonGen);
        jsonGen.close();
        JsonGenerator xmlGen = xmlProvider.createXmlGenerator();
        xmlSerializer.serializeValue(value, xmlGen);
        String xmlResult = xmlGen.getOutputFactory().createWriter("XML").writeValueAsString(xmlGen);
        assertEquals("<Test Value/>", xmlResult);
    }

    @Test
    public void testSerializeConfiguredRootElement() throws Exception {
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializerProvider(new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("root"))));
        XmlSerializerProvider provider = (XmlSerializerProvider) mapper.getSerializerProvider();
        JsonSerializer<Object> xmlSerializer = provider.findValueSerializer(String.class, null);
        JsonGenerator xmlGen = provider.createXmlGenerator();
        xmlSerializer.serializeValue("Test Value", xmlGen);
        assertEquals("<root>Test Value</root>", xmlGen.getOutputFactory().createWriter("XML").writeValueAsString(xmlGen));
    }
}