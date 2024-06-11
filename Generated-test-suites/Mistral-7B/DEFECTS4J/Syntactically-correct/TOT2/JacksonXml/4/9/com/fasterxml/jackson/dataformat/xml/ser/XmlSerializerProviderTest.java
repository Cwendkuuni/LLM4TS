import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new XmlMapper();

    @Test
    public void testSerializeValueNull() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        String expectedXml = "<null/>";

        TokenBuffer tokenBuffer = new TokenBuffer(MAPPER.getSerializerProvider().getFactory().getXmlFactory());
        XmlSerializerProvider.ToXmlGenerator xmlGenerator = MAPPER.getSerializerProvider().getWriterFactory().getWriter(tokenBuffer);

        provider.serializeValue(xmlGenerator, null);
        xmlGenerator.flush();

        String actualXml = xmlGenerator.getXml().toString();

        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueString() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        String input = "Hello World";
        String expectedXml = "<string>Hello World</string>";

        provider.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String xml = provider.serializeValue(null, input);

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueObject() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();
        QName rootName = new QName("TestRoot");

        String expectedXml = "<TestRoot><item></item></TestRoot>";

        provider.setRootName(rootName);
        String xml = provider.serializeValue(null, input);

        assertEquals(expectedXml, xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueObjectWithNullRootName() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();

        provider.serializeValue(null, input);
    }

    @Test
    public void testSerializeValueObjectWithInvalidRootName() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();
        QName rootName = new QName("TestRoot", "TestPrefix");

        String expectedXml = "<TestPrefix:TestRoot><item></item></TestPrefix:TestRoot>";

        provider.setRootName(rootName);
        String xml = provider.serializeValue(null, input);

        assertEquals(expectedXml, xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueArrayNull() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object[] input = null;
        JavaType rootType = MAPPER.getTypeFactory().constructCollectionType(List.class, String.class);

        provider.serializeValue(null, input, rootType);
    }

    @Test
    public void testSerializeValueArrayEmpty() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object[] input = new Object[0];
        JavaType rootType = MAPPER.getTypeFactory().constructArrayType(String[].class);

        String expectedXml = "<array name=\"array\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\"></array>";

        provider.setRootName(new QName("array"));
        String xml = provider.serializeValue(null, input, rootType);

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueArrayOneElement() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object[] input = new Object[]{"Element1"};
        JavaType rootType = MAPPER.getTypeFactory().constructArrayType(String[].class);

        String expectedXml = "<array name=\"array\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<string>Element1</string>" +
                "</array>";

        provider.setRootName(new QName("array"));
        String xml = provider.serializeValue(null, input, rootType);

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueArrayMultipleElements() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object[] input = new Object[]{"Element1", "Element2"};
        JavaType rootType = MAPPER.getTypeFactory().constructArrayType(String[].class);

        String expectedXml = "<array name=\"array\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<string>Element1</string>" +
                "<string>Element2</string>" +
                "</array>";

        provider.setRootName(new QName("array"));
        String xml = provider.serializeValue(null, input, rootType);

        assertEquals(expectedXml, xml);
    }

    @Test(expected = IOException.class)
    public void testSerializeValueIOException() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();

        provider.serializeValue(null, input, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueJsonMappingException() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();

        provider.serializeValue(null, input, null);
    }

    @Test(expected = RuntimeException.class)
    public void testSerializeValueRuntimeException() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new Object();

        provider.serializeValue(null, input, null);
    }

    @Test
    public void testSerializeValueNestedObjects() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new NestedObjects();

        String expectedXml = "<outer><inner><item1>Value1</item1><item2>Value2</item2></inner></outer>";

        provider.setRootName(new QName("outer"));
        String xml = provider.serializeValue(null, input, null);

        assertEquals(expectedXml, xml);
    }

    @Test
    public void testSerializeValueNestedArrays() throws JsonMappingException, JsonGenerationException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(MAPPER.getRootValueSerializer());
        Object input = new NestedArrays();

        String expectedXml = "<outer><array name=\"array1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<array name=\"array2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"array\">" +
                "<string>Value1</string>" +
                "<string>Value2</string>" +
                "</array>" +
                "</array>" +
                "</outer>";

        provider.setRootName(new QName("outer"));
        String xml = provider.serializeValue(null, input, null);

        assertEquals(expectedXml, xml);
    }

    private static class NestedObjects {
        private Inner inner = new Inner();

        public Inner getInner() {
            return inner;
        }

        public void setInner(Inner inner) {
            this.inner = inner;
        }
    }

    private static class Inner {
        private String item1 = "Value1";
        private String item2 = "Value2";

        public String getItem1() {
            return item1;
        }

        public void setItem1(String item1) {
            this.item1 = item1;
        }

        public String getItem2() {
            return item2;
        }

        public void setItem2(String item2) {
            this.item2 = item2;
        }
    }

    private static class NestedArrays {
        private String[] array1;

        public String[] getArray1() {
            return array1;
        }

        public void setArray1(String[] array1) {
            this.array1 = array1;
        }

        private String[] array2;

        public String[] getArray2() {
            return array2;
        }

        public void setArray2(String[] array2) {
            this.array2 = array2;
        }
    }
}