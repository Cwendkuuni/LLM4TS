package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapperFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;

    public XmlSerializerProviderTest() {
        objectMapper = new ObjectMapper(new XmlMapperFactory());
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method.
     */
    @Test
    public void testSerializeValueNull() throws JsonGenerationException, JsonMappingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, null);
        String actual = tokenBuffer.asXml();
        String expected = "<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><null/></null>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method.
     */
    @Test
    public void testSerializeValueWithCustomSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        class TestBean {
            private int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        class TestSerializer extends StdSerializer<TestBean> {
            public TestSerializer() {
                this(null);
            }

            public TestSerializer(Class<TestBean> t) {
                super(t);
            }

            @Override
            public void serialize(TestBean value, JsonGenerator gen, SerializerProvider serProvider) throws IOException {
                gen.writeNumber(value.getValue());
            }
        }

        JavaType javaType = objectMapper.getTypeFactory().constructType(TestBean.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("value");
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(filter);

        TestBean testBean = new TestBean();
        testBean.setValue(42);

        provider.setSerializationConfig(objectMapper.getSerializationConfig());
        provider.setSerializerFactory(objectMapper.getSerializerFactory());
        provider.setDefaultSerializerProvider(provider);

        JsonSerializer<TestBean> testSerializer = objectMapper.findValueSerializer(TestBean.class, null, null);
        provider.setBeanPropertyFilter(filter);

        provider.serializeValue(tokenBuffer, testBean, javaType, testSerializer);
        String actual = tokenBuffer.asXml();

        String expected = "<value xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><int>42</int></value>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method with empty list.
     */
    @Test
    public void testSerializeValueEmptyList() throws JsonGenerationException, JsonMappingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JavaType elementType = objectMapper.getTypeFactory().constructType(String.class);

        provider.serializeValue(tokenBuffer, Collections.emptyList(), javaType, null);
        String actual = tokenBuffer.asXml();
        String expected = "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><arrayName/></item>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method with empty list.
     */
    @Test
    public void testSerializeValueEmptyListWithCustomSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(List.class);
        JavaType elementType = objectMapper.getTypeFactory().constructType(String.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, Collections.emptyList(), javaType, null);
        String actual = tokenBuffer.asXml();
        String expected = "<item xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><arrayName/></item>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method with empty map.
     */
    @Test
    public void testSerializeValueEmptyMap() throws JsonGenerationException, JsonMappingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        JavaType keyType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType valueType = objectMapper.getTypeFactory().constructType(String.class);

        provider.serializeValue(tokenBuffer, Collections.emptyMap(), javaType, null);
        String actual = tokenBuffer.asXml();
        String expected = "<map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><entry name=\"key\"/></map>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method with empty map.
     */
    @Test
    public void testSerializeValueEmptyMapWithCustomSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Map.class);
        JavaType keyType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType valueType = objectMapper.getTypeFactory().constructType(String.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, Collections.emptyMap(), javaType, null);
        String actual = tokenBuffer.asXml();
        String expected = "<map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\"><entry name=\"key\"/></map>";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method with invalid object.
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueInvalidObject() throws JsonGenerationException, JsonMappingException, IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, new Object(), null, null);
        tokenBuffer.close();
    }

    /**
     * Test for {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method with invalid object.
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueInvalidObjectWithCustomSerializer() throws JsonGenerationException, JsonMappingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Object.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootNameLookup.DefaultRootNames()));
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, new Object(), javaType, null);
        tokenBuffer.close();
    }
}