package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final XmlRootNameLookup ROOT_NAME_LOOKUP = mock(XmlRootNameLookup.class);

    @Test
    public void testXmlSerializerProviderCreation() throws Exception {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = MAPPER.getSerializerProvider().getFactory();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        SerializerProvider defaultProvider = xmlProvider.getProvider();

        assertEquals(XmlSerializerProvider.class, xmlProvider.getClass());
        assertEquals(DefaultSerializerProvider.class, defaultProvider.getClass());
        assertSame(ROOT_NAME_LOOKUP, xmlProvider._rootNameLookup);
        assertNull(xmlProvider._config);
        assertSame(config, xmlProvider.getConfig());
        assertEquals(factory, xmlProvider.getFactory());
    }

    @Test
    public void testXmlSerializerProviderConfiguration() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.setRootValueSeparator(":");
        SerializerFactory factory = MAPPER.getSerializerProvider().getFactory();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP, config, factory);

        assertEquals(":", xmlProvider.getRootValueSeparator());
        assertEquals(config, xmlProvider.getConfig());
        assertEquals(factory, xmlProvider.getFactory());
        assertSame(ROOT_NAME_LOOKUP, xmlProvider._rootNameLookup);
    }

    @Test
    public void testSerializationWithDefaultRoot() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object());

        QName expectedRoot = new QName("root");
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null)).thenReturn(expectedRoot);

        OutputStream output = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);

        serializer.serialize(node, new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(output)), xmlProvider);
        output.flush();

        String result = output.toString();
        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><field/></root>".getBytes()});
    }

    @Test
    public void testSerializationWithCustomRoot() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object());

        QName expectedRoot = new QName("root", "ns");
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null))
                .thenReturn(new QName("default"));
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null, "ns"))
                .thenReturn(expectedRoot);

        OutputStream output = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, "ns");

        serializer.serialize(node, new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(output)), xmlProvider);
        output.flush();

        String result = output.toString();
        assertTrue(result.startsWith("<ns:root>"));
        assertTrue(result.endsWith("</ns:root>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<ns:root><field/></ns:root>".getBytes()});
    }

    @Test
    public void testSerializationWithNullValue() throws Exception {
        QName expectedRoot = new QName("root");
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null))
                .thenReturn(expectedRoot);

        OutputStream output = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);

        serializer.serialize(null, new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(output)), xmlProvider);
        output.flush();

        String result = output.toString();
        assertTrue(result.startsWith("<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"xsi\">"));
        assertTrue(result.endsWith("</root>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"xsi\"><xsi:null/></root>".getBytes()});
    }

    @Test
    public void testSerializationWithArray() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object[0]);

        QName expectedRoot = new QName("root");
        when(ROOT_NAME_LOOKUP.findRootName(Object[].class, null))
                .thenReturn(expectedRoot);

        OutputStream output = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object[].class, null);

        serializer.serializeValue(new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(output)), node.get("field"));
        output.flush();

        String result = output.toString();
        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertTrue(result.contains("<item/>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><item/></root>".getBytes()});
    }

    @Test
    public void testSerializationWithArrayAndCustomRoot() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object[0]);

        QName expectedRoot = new QName("root", "ns");
        when(ROOT_NAME_LOOKUP.findRootName(Object[].class, null))
                .thenReturn(new QName("default"));
        when(ROOT_NAME_LOOKUP.findRootName(Object[].class, null, "ns"))
                .thenReturn(expectedRoot);

        OutputStream output = new ByteArrayOutputStream();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object[].class, "ns");

        serializer.serializeValue(new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(output)), node.get("field"));
        output.flush();

        String result = output.toString();
        assertTrue(result.startsWith("<ns:root>"));
        assertTrue(result.endsWith("</ns:root>"));
        assertTrue(result.contains("<item/>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<ns:root><item/></ns:root>".getBytes()});
    }

    @Test
    public void testFindTypedValueSerializer() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object());

        QName expectedRoot = new QName("root");
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null))
                .thenReturn(expectedRoot);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);

        ArgumentCaptor<JavaType> typeCaptor = ArgumentCaptor.forClass(JavaType.class);
        ArgumentCaptor<Boolean> useBinderCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<JsonSerializer<?>> serializerCaptor = ArgumentCaptor.forClass(JsonSerializer.class);

        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(Object.class, true, null);
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null)).thenReturn(expectedRoot);

        xmlProvider.serializeValue(new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(new ByteArrayOutputStream())), node.get("field"), serializer);

        // First call
        xmlProvider.findTypedValueSerializer(Object.class, true, null);
        verify(xmlProvider).findValueSerializer(Object.class, null);

        // Second call
        xmlProvider.findTypedValueSerializer(Object.class, true, null);
        verify(xmlProvider, times(2)).findValueSerializer(Object.class, null);

        // Call with a binder
        serializer = xmlProvider.findTypedValueSerializer(Object.class, true, null);
        ObjectWriter writer = MAPPER.writerWithRootValueSerializer(serializer);
        String result = writer.writeValueAsString(node.get("field"));

        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><field/></root>".getBytes()});

        // Call with a type
        JavaType javaType = MAPPER.getTypeFactory().constructType(new TypeReference<List<Object>>(){}.getType());
        serializer = xmlProvider.findTypedValueSerializer(javaType, true, null);

        // First call
        xmlProvider.findTypedValueSerializer(javaType, true, null);
        verify(xmlProvider).findValueSerializer(javaType.getRawClass(), null);

        // Second call
        xmlProvider.findTypedValueSerializer(javaType, true, null);
        verify(xmlProvider, times(2)).findValueSerializer(javaType.getRawClass(), null);

        ObjectWriter arrayWriter = MAPPER.writerWithRootValueSerializer(serializer);
        result = arrayWriter.writeValueAsString(node.get("field"));

        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertTrue(result.contains("<item/>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><item/></root>".getBytes()});
    }

    @Test
    public void testSerializationWithTypeSerializer() throws Exception {
        ObjectNode node = MAPPER.createObjectNode();
        node.putPOJOProperty("field", new Object());

        QName expectedRoot = new QName("root");
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null))
                .thenReturn(expectedRoot);

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
        SimpleBeanPropertySerializerMapper mapper = new SimpleBeanPropertySerializerMapper();
        mapper.setSerializerProvider(xmlProvider);

        ArgumentCaptor<JavaType> typeCaptor = ArgumentCaptor.forClass(JavaType.class);
        ArgumentCaptor<JsonSerializer<?>> serializerCaptor = ArgumentCaptor.forClass(JsonSerializer.class);
        ArgumentCaptor<TypeSerializer> typeSerializerCaptor = ArgumentCaptor.forClass(TypeSerializer.class);

        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(Object.class, true, null);
        when(ROOT_NAME_LOOKUP.findRootName(Object.class, null)).thenReturn(expectedRoot);

        ObjectWriter writer = new ObjectMapper(mapper).writerWithRootValueSerializer(serializer);
        String result = writer.writeValueAsString(node.get("field"));

        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><field/></root>".getBytes()});

        // Call with a type
        JavaType javaType = MAPPER.getTypeFactory().constructType(new TypeReference<List<Object>>(){}.getType());
        serializer = xmlProvider.findTypedValueSerializer(javaType, true, null);
        TypeSerializer typeSerializer = MAPPER.getTypeSerializer();

        ObjectWriter arrayWriter = new ObjectMapper(mapper).writerWithRootValueSerializer(serializer);
        result = arrayWriter.writeValueAsString(node.get("field"));

        assertTrue(result.startsWith("<root>"));
        assertTrue(result.endsWith("</root>"));
        assertTrue(result.contains("<item/>"));
        assertArrayEquals(result.getBytes(), new byte[]{"<root><item/></root>".getBytes()});

        verify(xmlProvider).findValueSerializer(Object.class, null);
        verify(xmlProvider).findValueSerializer(Object[].class, null);
        verify(mapper).findBeanSerializer(Object.class, null);
        verify(mapper).findBeanSerializer(Object[].class, null);
        verify(mapper, times(2)).findBeanSerializer(Object[].class, new TypeReference<List<Object>>(){}.getType());

        verify(serializer).serialize(node.get("field"), new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(new ByteArrayOutputStream())), xmlProvider);
        verify(serializer).serializeValue(new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(new ByteArrayOutputStream())), node.get("field"));

        verify(typeSerializer).serializeValue(node.get("field"), new TokenBuffer(MAPPER.getFactory(), new OutputStreamWriter(new ByteArrayOutputStream())), xmlProvider);
    }
}