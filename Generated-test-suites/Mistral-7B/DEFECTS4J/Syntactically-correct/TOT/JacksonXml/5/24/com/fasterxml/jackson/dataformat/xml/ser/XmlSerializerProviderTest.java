package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlSerializerProviderTest {

    private XmlMapper mapper;
    private XmlSerializerProvider serializerProvider;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFilterID("IncludeNullValues", SimpleBeanPropertyFilter.filterNull());
        mapper = objectMapper.setSerializerProvider(filterProvider.getSerializerProvider())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        serializerProvider = mapper.getSerializerProvider();
    }

    @Test
    public void testSerializeNullValue() throws JsonProcessingException {
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializerProvider.serializeValue(generator, null);
        generator.close();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>"));
        ObjectNode rootNode = mapper.readTree(parser);
        assertEquals(rootNode.get("null").asText(), serializerProvider.ROOT_NAME_FOR_NULL.getLocalPart());
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("rootName", "MyRootElement");
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializerProvider.setRootName(rootNode);
        Object obj = new Object();
        serializerProvider.serializeValue(generator, obj);
        generator.close();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MyRootElement/>"));
        ObjectNode parsedObject = mapper.readTree(parser);
        assertEquals(parsedObject.get("rootName").asText(), rootNode.get("rootName").asText());
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("MyClass", new QName("http://example.com", "MyClass"));
        serializerProvider._rootNameLookup = new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> cls, SerializationConfig config) {
                return rootNode.get(cls.getName());
            }
        };
        Object obj = new MyClass();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializerProvider.serializeValue(generator, obj);
        generator.close();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MyClass xmlns=\"http://example.com\"/>"));
        ObjectNode parsedObject = mapper.readTree(parser);
        assertEquals(parsedObject.get("MyClass").get("local-name").asText(), "MyClass");
    }

    @Test
    public void testSerializeValueWithArray() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("items", new QName("http://example.com", "items"));
        serializerProvider._rootNameLookup = new XmlRootNameLookup() {
            @Override
            public QName findRootName(JavaType type, SerializationConfig config) {
                return rootNode.get(type.getRawClass().getSimpleName());
            }
        };
        Object obj = new Object[3];
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializerProvider.serializeValue(generator, obj);
        generator.close();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<items>\n<item/>\n<item/>\n<item/>\n</items>"));
        ObjectNode parsedObject = mapper.readTree(parser);
        assertEquals(parsedObject.get("items").get("local-name").asText(), "items");
    }

    @Test
    public void testSerializeValueWithEmptyRootName() throws JsonProcessingException {
        ObjectNode rootNode = mapper.createObjectNode();
        serializerProvider._rootNameLookup = new XmlRootNameLookup() {
            @Override
            public QName findRootName(Class<?> cls, SerializationConfig config) {
                return null;
            }
        };
        Object obj = new Object();
        JsonGenerator generator = new JsonFactory().createGenerator(System.out);
        serializerProvider.serializeValue(generator, obj);
        generator.close();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<rootElement xmlns=\"\">"));
        ObjectNode parsedObject = mapper.readTree(parser);
        assertNull(parsedObject.get("rootElement").get("local-name").asText());
    }

    private static class MyClass {
        // Empty class
    }
}