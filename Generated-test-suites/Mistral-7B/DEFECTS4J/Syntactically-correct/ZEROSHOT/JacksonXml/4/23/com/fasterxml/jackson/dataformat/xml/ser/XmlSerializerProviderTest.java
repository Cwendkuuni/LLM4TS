package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtilTest;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookupTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

/**
 * Test class for XmlSerializerProvider
 */
public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private ObjectMapper objectMapper;
    private XmlRootNameLookup xmlRootNameLookup;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlRootNameLookup = mock(XmlRootNameLookup.class);
        objectMapper = new ObjectMapper();
        objectMapper.setSerializerProvider(new DefaultSerializerProvider());
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
        objectMapper.setSerializerProvider(xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithNull() throws JsonMappingException, JsonProcessingException, IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName("null")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeNull()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        verifyXmlSerialization("{}", jsonGenerator);
    }

    @Test
    public void testSerializeValueWithEmptyList() throws JsonMappingException, JsonProcessingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonSerializer<List<Integer>> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serializeValues(ArgumentMatchers.isA(JsonGenerator.class), ArgumentMatchers.any(SerializerProvider.class), ArgumentMatchers.any(JavaType.class), ArgumentMatchers.anyList())).thenReturn(null);

        when(xmlSerializerProvider.findValueSerializer(javaType, null)).thenReturn(jsonSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName("item")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeStartArray()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndArray()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, Collections.emptyList(), javaType);
        verifyXmlSerialization("{}", jsonGenerator);
    }

    @Test
    public void testSerializeValueWithList() throws JsonMappingException, JsonProcessingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        JsonSerializer<List<Integer>> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serializeValues(ArgumentMatchers.isA(JsonGenerator.class), ArgumentMatchers.any(SerializerProvider.class), ArgumentMatchers.any(JavaType.class), ArgumentMatchers.anyList())).thenReturn(new Integer[]{1, 2, 3});

        when(xmlSerializerProvider.findValueSerializer(javaType, null)).thenReturn(jsonSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName("item")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeStartArray()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeNumber(1)).thenReturn(jsonGenerator);
        when(jsonGenerator.writeNumber(2)).thenReturn(jsonGenerator);
        when(jsonGenerator.writeNumber(3)).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndArray()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, Arrays.asList(1, 2, 3), javaType);
        verifyXmlSerialization("{\"item\":[1,2,3]}", jsonGenerator);
    }

    @Test
    public void testSerializeValueWithObject() throws JsonMappingException, JsonProcessingException, IOException {
        JsonSerializer<String> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serializeValues(ArgumentMatchers.isA(JsonGenerator.class), ArgumentMatchers.any(SerializerProvider.class), ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any())).thenReturn("test");

        when(xmlSerializerProvider.findValueSerializer(String.class, null)).thenReturn(jsonSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName("test")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeString("test")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, "test");
        verifyXmlSerialization("{\"test\":\"test\"}", jsonGenerator);
    }

    @Test
    public void testSerializeValueWithObjectAndRootName() throws JsonMappingException, JsonProcessingException, IOException {
        String rootName = "testRoot";
        JsonSerializer<String> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serializeValues(ArgumentMatchers.isA(JsonGenerator.class), ArgumentMatchers.any(SerializerProvider.class), ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any())).thenReturn("test");

        when(xmlSerializerProvider.findValueSerializer(String.class, null)).thenReturn(jsonSerializer);
        when(xmlRootNameLookup.findRootName(String.class, objectMapper.getSerializationConfig())).thenReturn(new QName(rootName));

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName(rootName)).thenReturn(jsonGenerator);
        when(jsonGenerator.writeString("test")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, "test");
        verifyXmlSerialization("{\"testRoot\":\"test\"}", jsonGenerator);
    }

    @Test
    public void testSerializeValueWithObjectAndRootNameFromConfig() throws JsonMappingException, JsonProcessingException, IOException {
        String rootName = "testRoot";
        PropertyName propertyName = PropertyName.construct(rootName);
        SerializationConfig config = objectMapper.getSerializationConfig();
        config.setRootName(propertyName);

        JsonSerializer<String> jsonSerializer = mock(JsonSerializer.class);
        when(jsonSerializer.serializeValues(ArgumentMatchers.isA(JsonGenerator.class), ArgumentMatchers.any(SerializerProvider.class), ArgumentMatchers.any(JavaType.class), ArgumentMatchers.any())).thenReturn("test");

        when(xmlSerializerProvider.findValueSerializer(String.class, null)).thenReturn(jsonSerializer);

        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        when(jsonGenerator.writeStartObject()).thenReturn(jsonGenerator);
        when(jsonGenerator.writeFieldName(rootName)).thenReturn(jsonGenerator);
        when(jsonGenerator.writeString("test")).thenReturn(jsonGenerator);
        when(jsonGenerator.writeEndObject()).thenReturn(jsonGenerator);

        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(jsonGenerator);

        xmlSerializerProvider.serializeValue(jsonGenerator, "test");
        verifyXmlSerialization("{\"testRoot\":\"test\"}", jsonGenerator);
    }

    private void verifyXmlSerialization(String expectedXml, JsonGenerator jsonGenerator) throws IOException {
        String actualXml = StaxUtilTest.writeValueAsString(jsonGenerator);
        assertEquals(expectedXml, actualXml);
    }
}