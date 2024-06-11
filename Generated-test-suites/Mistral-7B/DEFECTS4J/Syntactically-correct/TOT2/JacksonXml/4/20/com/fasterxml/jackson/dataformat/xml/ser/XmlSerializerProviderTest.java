package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlMapper xmlMapper;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        xmlSerializerProvider = xmlMapper.getSerializerProvider();
    }

    // Test case for serializeValue method with Object as argument
    @Test
    public void testSerializeValueWithObject() throws JsonProcessingException, IOException {
        Object value = new Object();
        String expectedXml = "<rootElement><object>{</object></rootElement>";

        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("rootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with JavaType as argument
    @Test
    public void testSerializeValueWithJavaType() throws JsonProcessingException, IOException {
        JavaType javaType = xmlMapper.getTypeFactory().constructType(ArrayNode.class);
        Object value = Arrays.asList("element1", "element2").toString();
        String expectedXml = "<rootElement><array><item>element1</item><item>element2</item></array></rootElement>";

        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(javaType, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("rootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with JsonSerializer provided as argument
    @Test
    public void testSerializeValueWithJsonSerializer() throws JsonProcessingException, IOException {
        JsonSerializer<Object> jsonSerializer = new StdSerializer<Object>(Object.class) {};
        Object value = new Object();
        String expectedXml = "<rootElement><object>{</object></rootElement>";

        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("rootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with null value
    @Test
    public void testSerializeValueWithNull() throws JsonProcessingException, IOException {
        String expectedXml = "<null/>";

        JsonSerializer<Object> jsonSerializer = xmlMapper.findNullValueSerializer(Object.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(null, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("").writeValueAsString(null);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with custom root name
    @Test
    public
    void testSerializeValueWithCustomRootName() throws JsonProcessingException, IOException {
        Object value = new Object();
        String expectedXml = "<myRootElement><object>{</object></myRootElement>";

        SerializationConfig config = xmlMapper.getSerializationConfig();
        config.setRootValueSeparator("myRootElement");
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null, config, null);
        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("myRootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with empty value
    @Test
    public void testSerializeValueWithEmpty() throws JsonProcessingException, IOException {
        Object value = new Object();
        String expectedXml = "<rootElement></rootElement>";

        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("rootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for serializeValue method with exception
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithException() throws JsonProcessingException, IOException {
        Object value = new Object();
        JsonSerializer<Object> jsonSerializer = new ExceptionThrowingSerializer();
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
    }

    // Test case for _startRootArray method
    @Test
    public void testStartRootArray() throws JsonProcessingException, IOException {
        Object value = new Object();
        String expectedXml = "<rootElement><array><item/></array></rootElement>";

        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(ArrayNode.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("rootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for _initWithRootName method
    @Test
    public void testInitWithRootName() throws JsonProcessingException, IOException, XMLStreamException {
        Object value = new Object();
        String expectedXml = "<myRootElement><object>{</object></myRootElement>";

        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null, null, null) {
            @Override
            protected void _initWithRootName(ToXmlGenerator xgen, QName rootName) throws IOException {
                super._initWithRootName(xgen, rootName);
                xgen.writeStartElement("myRootElement");
            }
        };
        JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object.class, null);
        ObjectNode jsonNode = jsonSerializer.serialize(value, new ObjectNode(xmlMapper.getFactory()), xmlSerializerProvider);
        String jsonString = jsonNode.getValueAsString();

        String xmlString = xmlMapper.writerWithRootName("myRootElement").writeValueAsString(value);

        Assert.assertEquals(expectedXml, xmlString);
        Assert.assertEquals(expectedXml, jsonString);
    }

    // Test case for _rootNameFromConfig method
    @Test
    public void testRootNameFromConfig() {
        PropertyName name = xmlMapper.getSerializationConfig().getFullRootName();

        Assert.assertEquals("root", name.getSimpleName());
        Assert.assertNull(name.getNamespace());
    }

    // Test case for _asXmlGenerator method
    @Test
    public void testAsXmlGenerator() throws JsonProcessingException, IOException {
        Object value = new Object();
        JsonGenerator gen = xmlMapper.writerForTree(value);

        ToXmlGenerator toXmlGenerator = (ToXmlGenerator) xmlMapper.getSerializerProvider().findGenerator(gen);
        QName rootName = _rootNameFromConfig();
        _initWithRootName(toXmlGenerator, rootName);

        Assert.assertNotNull(toXmlGenerator);
        Assert.assertEquals("root", toXmlGenerator.getCurrentName());
    }

    private QName _rootNameFromConfig() {
        return xmlMapper.getSerializationConfig().getFullRootName() == null ? null : new QName("root", null, null);
    }

    // Inner class for JsonSerializer that throws an exception
    public static class ExceptionThrowingSerializer extends StdSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            throw new JsonMappingException("Expected exception");
        }
    }
}