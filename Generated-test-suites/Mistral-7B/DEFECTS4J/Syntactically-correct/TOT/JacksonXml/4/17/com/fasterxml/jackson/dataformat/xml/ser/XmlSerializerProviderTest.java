package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class XmlSerializerProviderTest {

    private ObjectMapper mapper;
    private XmlSerializerProvider serializerProvider;

    @Before
    public void setup() {
        mapper = new XmlMapper();
        serializerProvider = mapper.getSerializerProvider().findValueSerializerProviderInstance(XmlSerializerProvider.class);
    }

    @Test
    public void testSerializeNull() throws IOException {
        String expectedXml = "<null/>";

        // Test serializing a null value
        String actualXml = serializeAndReadBack(null);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeString() throws IOException {
        String value = "Hello World";
        String expectedXml = "<string>Hello World</string>";

        // Test serializing a String value
        String actualXml = serializeAndReadBack(value);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeObjectWithDefaultRootName() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.putPOJOInTextForm("testObject", new TestObject());
        String expectedXml = "<testObject><property1>Value1</property1><property2>Value2</property2></testObject>";

        // Test serializing an object with a default root name
        String actualXml = serializeAndReadBack(node);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeObjectWithCustomRootName() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.setRootName("customRootName");
        node.putPOJOInTextForm("testObject", new TestObject());
        String expectedXml = "<customRootName><testObject><property1>Value1</property1><property2>Value2</property2></testObject></customRootName>";

        // Test serializing an object with a custom root name
        String actualXml = serializeAndReadBack(node);
        Assert.assertEquals(expectedXml, actualXml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeObjectWithInvalidConfiguredRootName() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.setRootName("customRootName");
        node.putPOJOInTextForm("testObject", new TestObject());
        serializerProvider._initWithRootName((ToXmlGenerator) node.getSerializers().findValueSerializer(Object.class), new QName("notValidRootName"));
        String actualXml = serializeAndReadBack(node);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeObjectWithNullRootName() throws IOException {
        ObjectNode node = mapper.createObjectNode();
        node.putPOJOInTextForm("testObject", new TestObject());
        serializerProvider._initWithRootName((ToXmlGenerator) node.getSerializers().findValueSerializer(Object.class), null);
        String actualXml = serializeAndReadBack(node);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeArrayWithNullRootName() throws IOException {
        ObjectNode node = mapper.createArrayNode();
        String expectedXml = "<array><item/></array>";

        // Test serializing an array with a null root name
        serializerProvider._startRootArray((ToXmlGenerator) node.getSerializers().findValueSerializer(Object[].class), null);
        String actualXml = serializeAndReadBack(node);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeArrayWithInvalidRootName() throws IOException {
        ObjectNode node = mapper.createArrayNode();
        String expectedXml = "<array><item/></array>";

        // Test serializing an array with an invalid root name
        serializerProvider._startRootArray((ToXmlGenerator) node.getSerializers().findValueSerializer(Object[].class), new QName("notValidRootName"));
        String actualXml = serializeAndReadBack(node);
    }

    private String serializeAndReadBack(Object value) throws IOException {
        StringWriter writer = new StringWriter();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        xmlSerializerProvider.setSerializationConfig(mapper.getSerializationConfig());
        xmlSerializerProvider.serializeValue(new JsonFactory(), value);
        xmlSerializerProvider.getOutputFactory().releaseWriter(writer, null, null);
        return writer.toString();
    }

    private static class TestObject {
        private String property1 = "Value1";
        private String property2 = "Value2";
    }
}