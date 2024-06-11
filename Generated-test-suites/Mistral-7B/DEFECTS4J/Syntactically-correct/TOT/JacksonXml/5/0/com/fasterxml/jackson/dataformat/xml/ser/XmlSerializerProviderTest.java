package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.*;

public class XmlSerializerProviderTest {

    private SerializerProvider originalProvider;
    private XmlSerializerProvider xmlProvider;
    private XmlMapper mapper;

    @BeforeMethod
    public void setup() {
        originalProvider = new DefaultSerializerProvider();
        xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        mapper = new XmlMapper(xmlProvider);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(null);
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root><null/></root>", xml);
    }

    @Test
    public void testSerializePrimitiveTypes() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(Arrays.asList(1, 2.0F, 'A', "abc"));
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root><int>1</int><float>2.0</float><string>abc</string><string>abc</string></root>", xml);
    }

    @Test
    public void testSerializeArray() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(new int[]{1, 2, 3});
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></root>", xml);
    }

    @Test
    public void testSerializeCustomType() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        StdSerializer<String> stringSerializer = new StdSerializer<String>(String.class);
        xmlProvider.setSerializerInstance(String.class, stringSerializer);
        ObjectNode rootNode = mapper.writeValueAsTree("test");
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root><string>test</string></root>", xml);
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSerializeWithoutRootName() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(1);
        mapper.writeValueAsString(rootNode);
        fail("Expected JsonMappingException: No root name configured");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testSerializeRootNameFromConfig() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setRootValueSeparator("");
        ObjectNode rootNode = mapper.writeValueAsTree(1);
        String xml = mapper.writerWithRootName("").writeValueAsString(rootNode);
        fail("Expected JsonMappingException: Root name cannot be empty string");
    }

    @Test
    public void testSerializeEmptyList() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(Arrays.asList());
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root><item/></root>", xml);
    }

    @Test
    public void testSerializeEmptyMap() throws JsonGenerationException, JsonMappingException, IOException, XMLStreamException {
        ObjectNode rootNode = mapper.writeValueAsTree(Object.class.newInstance());
        String xml = mapper.writerWithRootName("root").writeValueAsString(rootNode);
        assertEquals("<root/>", xml);
    }
}