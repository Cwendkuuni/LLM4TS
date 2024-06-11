package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactoryImpl;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig config;
    private SerializerFactory serializerFactory;
    private XmlRootNameLookup rootNameLookup;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        serializerFactory = new SerializerFactoryImpl();
        rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        config.setSerializerProvider(xmlSerializerProvider);
        serializerFactory.setSerializerProvider(xmlSerializerProvider);
    }

    @Test
    public void testCreateInstance() throws Exception {
        XmlSerializerProvider newProvider = xmlSerializerProvider.createInstance(config, serializerFactory);
        assertNotNull(newProvider);
        assertEquals(xmlSerializerProvider.getClass(), newProvider.getClass());
    }

    @Test
    public void testSerializeValueWithNull() throws Exception {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Mockito.when(generator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        xmlSerializerProvider._serializeXmlNull(generator);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(generator).writeStartObject();
        Mockito.verify(generator).writeFieldName("null");
        Mockito.verify(generator).writeEndObject();
    }

    @Test
    public void testSerializeValueWithoutRootName() throws Exception {
        Object value = new Object();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        Mockito.when(generator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(generator.asDeferredGenerator()).thenReturn(xgen);

        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(ser);

        xmlSerializerProvider.serializeValue(generator, value);

        Mockito.verify(ser).serializeValues(value, generator, xmlSerializerProvider);
        Mockito.verifyNoMoreInteractions(xgen);
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        Object value = new Object();
        String rootName = "testRootName";
        config.setRootValueSeparator(":");
        config.setRootName(new PropertyName(rootName));

        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        Mockito.when(generator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(generator.asDeferredGenerator()).thenReturn(xgen);

        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(ser);

        Mockito.when(xgen.setNextNameIfMissing(Mockito.any(QName.class))).thenReturn(false);
        Mockito.when(xgen.setNextName(Mockito.any(QName.class))).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                QName rootNameQName = (QName) invocation.getArguments()[0];
                assertEquals(rootName, rootNameQName.getLocalPart());
                return null;
            }
        });

        xmlSerializerProvider.serializeValue(generator, value);

        Mockito.verify(ser).serializeValues(value, generator, xmlSerializerProvider);
        Mockito.verify(xgen).initGenerator();
        Mockito.verifyNoMoreInteractions(xgen);
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws Exception {
        Object value = new Object();
        String rootName = "testRootName";

        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        Mockito.when(generator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(generator.asDeferredGenerator()).thenReturn(xgen);

        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(ser);

        Mockito.when(rootNameLookup.findRootName(Object.class, config)).thenReturn(new QName(rootName));

        xmlSerializerProvider.serializeValue(generator, value);

        Mockito.verify(ser).serializeValues(value, generator, xmlSerializerProvider);
        Mockito.verify(xgen).initGenerator();
        Mockito.verify(xgen).setNextName(Mockito.any(QName.class));
        Mockito.verify(xgen).writeStartObject();
        Mockito.verify(xgen).writeFieldName(rootName);
        Mockito.verify(xgen).writeEndObject();
    }

    @Test
    public void testSerializeValueAsArray() throws Exception {
        Object[] values = new Object[]{new Object(), new Object()};
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
        Mockito.when(generator.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(generator.asDeferredGenerator()).thenReturn(xgen);

        JsonSerializer<Object> ser = Mockito.mock(JsonSerializer.class);
        Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object[].class, true, null)).thenReturn(ser);

        Mockito.when(xgen.setNextNameIfMissing(Mockito.any(QName.class))).thenReturn(false);
        Mockito.when(xgen.setNextName(Mockito.any(QName.class))).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                QName rootNameQName = (QName) invocation.getArguments()[0];
                assertEquals("item", rootNameQName.getLocalPart());
                return null;
            }
        });

        Mockito.when(xgen.writeStartObject()).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                xgen.writeFieldName("item");
                return null;
            }
        });

        xmlSerializerProvider.serializeValue(generator, values);

        Mockito.verify(ser, Mockito.times(values.length)).serializeValues(Mockito.any(), Mockito.eq(generator), Mockito.eq(xmlSerializerProvider));
        Mockito.verify(xgen, Mockito.times(values.length + 1)).initGenerator();
        Mockito.verify(xgen, Mockito.times(values.length)).writeStartObject();
        Mockito.verify(xgen).writeEndObject();
        Mockito.verifyNoMoreInteractions(xgen);
    }

    @Test
    public void testFindTypedValueSerializer() throws Exception {
        SimpleBeanPropertySerializer serializer = Mockito.mock(SimpleBeanPropertySerializer.class);
        Mockito.when(serializer.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(serializer.getPropertyName()).thenReturn(new PropertyName("testProperty"));

        JsonSerializer<Object> returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(String.class, true, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(Object[].class, true, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(String[].class, true, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(int[].class, true, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(String.class, false, null);
        assertNull(returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(Object.class, false, null);
        assertNotNull(returnedSerializer);
        Mockito.verify(returnedSerializer).setSerializerProvider(xmlSerializerProvider);

        Mockito.reset(serializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(Object.class, true, serializer);
        Mockito.verify(serializer).setSerializerProvider(xmlSerializerProvider);
        assertEquals(serializer, returnedSerializer);

        returnedSerializer = xmlSerializerProvider.findTypedValueSerializer(Object.class, false, serializer);
        Mockito.verify(serializer, Mockito.never()).setSerializerProvider(xmlSerializerProvider);
        assertNull(returnedSerializer);
    }
}