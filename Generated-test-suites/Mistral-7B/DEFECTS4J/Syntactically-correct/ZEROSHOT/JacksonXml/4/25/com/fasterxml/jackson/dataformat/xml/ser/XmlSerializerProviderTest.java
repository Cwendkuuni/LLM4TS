package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new XmlMapper();

    @Test
    public void testXmlSerializerProvider() {
        // Test default constructor
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNotNull(provider);

        // Test constructor with XmlRootNameLookup
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig(MAPPER.getFactory());
        SerializerFactory factory = MAPPER.getSerializerProvider().getFactory();
        XmlSerializerProvider providerWithRootNameLookup = new XmlSerializerProvider(rootNameLookup, config, factory);
        assertNotNull(providerWithRootNameLookup);

        // Test constructor with source provider, config, and factory
        SerializerProvider srcProvider = MAPPER.getSerializerProvider();
        SerializationConfig config2 = new SerializationConfig(MAPPER.getFactory());
        SerializerFactory factory2 = MAPPER.getSerializerProvider().getFactory();
        XmlSerializerProvider providerWithSrc = new XmlSerializerProvider(srcProvider, config2, factory2);
        assertNotNull(providerWithSrc);
        assertEquals(srcProvider, providerWithSrc.getSerializers());
        assertEquals(config2, providerWithSrc.getConfig());
        assertEquals(factory2, providerWithSrc.getFactory());
        assertEquals(rootNameLookup, providerWithRootNameLookup._rootNameLookup);
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TokenBuffer tokenBuffer = new TokenBuffer();
        provider.serializeValue(tokenBuffer, null);
        String xml = tokenBuffer.asXml();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value.toUpperCase());
            }
        });
        mapper.registerModule(module);

        JsonNode rootNode = mapper.readTree("{\"name\":\"John\"}");
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        provider.setRootNameLookup(rootNameLookup);
        ObjectNode node = mapper.createObjectNode();
        node.put("rootName", "MyRoot");
        provider.setConfig(new SerializationConfig(mapper.getFactory()));
        provider.setConfig(node);

        String input = "{\"name\":\"John\"}";
        JsonParser parser = mapper.getFactory().createParser(input);
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, parser, null);
        String xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name>JOHN</name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, null);
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name>JOHN</name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, new StdSerializer<Object>(Object.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name>JOHN</name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, ObjectNode.class, new StdSerializer<Object>(ObjectNode.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name>JOHN</name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, Object.class, new StdSerializer<Object>(Object.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name>JOHN</name></MyRoot>", xml);
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws IOException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value.toUpperCase());
            }
        });
        mapper.registerModule(module);

        JsonNode rootNode = mapper.readTree("{\"name\":\"John\"}");
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        provider.setRootNameLookup(rootNameLookup);
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "MyRoot");
        provider.setConfig(new SerializationConfig(mapper.getFactory()));
        provider.setConfig(node);

        String input = "{\"name\":\"John\"}";
        JsonParser parser = mapper.getFactory().createParser(input);
        TokenBuffer tokenBuffer = new TokenBuffer();

        String className = String.class.getName();
        QName rootName = rootNameLookup.findRootName(className, provider.getConfig());
        provider.serializeValue(tokenBuffer, parser, null, rootName);
        String xml = tokenBuffer.asXml();
        assertEquals("<" + rootName.getLocalPart() + "><name>JOHN</name></" + rootName.getLocalPart() + ">", xml);

        provider.serializeValue(tokenBuffer, parser, null, rootName, null);
        xml = tokenBuffer.asXml();
        assertEquals("<" + rootName.getLocalPart() + "><name>JOHN</name></" + rootName.getLocalPart() + ">", xml);

        provider.serializeValue(tokenBuffer, parser, null, rootName, new StdSerializer<Object>(Object.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<" + rootName.getLocalPart() + "><name>JOHN</name></" + rootName.getLocalPart() + ">", xml);

        provider.serializeValue(tokenBuffer, parser, ObjectNode.class, rootName, new StdSerializer<Object>(ObjectNode.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<" + rootName.getLocalPart() + "><name>JOHN</name></" + rootName.getLocalPart() + ">", xml);

        provider.serializeValue(tokenBuffer, parser, Object.class, rootName, new StdSerializer<Object>(Object.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<" + rootName.getLocalPart() + "><name>JOHN</name></" + rootName.getLocalPart() + ">", xml);
    }

    @Test
    public void testSerializeValueWithArray() throws IOException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value.toUpperCase());
            }
        });
        mapper.registerModule(module);

        JsonNode rootNode = mapper.readTree("{\"name\":[\"John\",\"Jane\"]}");
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        provider.setRootNameLookup(rootNameLookup);
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "MyRoot");
        provider.setConfig(new SerializationConfig(mapper.getFactory()));
        provider.setConfig(node);

        String input = "{\"name\":[\"John\",\"Jane\"]}";
        JsonParser parser = mapper.getFactory().createParser(input);
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, parser, null);
        String xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, null);
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, ObjectNode.class, new StdSerializer<Object>(ObjectNode.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, Object[].class, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, Object.class, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><name><name>JOHN</name></name><name><name>JANE</name></name></MyRoot>", xml);
    }

    @Test
    public void testSerializeValueWithRootNameAndArray() throws IOException {
        ObjectMapper mapper = new XmlMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value.toUpperCase());
            }
        });
        mapper.registerModule(module);

        JsonNode rootNode = mapper.readTree("{\"name\":[{\"value\":\"John\"},{\"value\":\"Jane\"}]}");
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        provider.setRootNameLookup(rootNameLookup);
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "MyRoot");
        node.put("arrayElementName", "MyElement");
        provider.setConfig(new SerializationConfig(mapper.getFactory()));
        provider.setConfig(node);

        String input = "{\"name\":[{\"value\":\"John\"},{\"value\":\"Jane\"}]}";
        JsonParser parser = mapper.getFactory().createParser(input);
        TokenBuffer tokenBuffer = new TokenBuffer();

        provider.serializeValue(tokenBuffer, parser, null);
        String xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, null);
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, null, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, ObjectNode.class, new StdSerializer<Object>(ObjectNode.class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, Object[].class, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);

        provider.serializeValue(tokenBuffer, parser, Object.class, new StdSerializer<Object>(Object[].class) {});
        xml = tokenBuffer.asXml();
        assertEquals("<MyRoot><MyElement><name><name>JOHN</name></name></MyElement><MyElement><name><name>JANE</name></name></MyElement></MyRoot>", xml);
    }
}