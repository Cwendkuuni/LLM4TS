package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlSerializerProvider.class)
public class XmlSerializerProviderTest {

    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;

    @Test
    public void testSerializeValueNull() throws Exception {
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        xmlSerializerProvider.serializeValue(genMock, null);
        PowerMockito.verifyStatic();
        StaxUtil.writeXmlNull(genMock, false);
    }

    @Test
    public void testSerializeValueWithoutRootName() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(SimpleBeanPropertySerializerBase.class, ValueInstantiator.noOpValueInstanter(), null, null).thenReturn(Mockito.mock(JsonSerializer.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        xmlSerializerProvider.serializeValue(genMock, value);
        PowerMockito.verifyStatic();
        serMock.serialize(value, genMock, xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(SimpleBeanPropertySerializerBase.class, ValueInstantiator.noOpValueInstanter(), null, null).thenReturn(Mockito.mock(JsonSerializer.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        QName rootName = new QName("rootName");
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(new PropertyName("rootName"));
        xmlSerializerProvider.serializeValue(genMock, value, rootName);
        PowerMockito.verifyStatic();
        serMock.serialize(value, genMock, xmlSerializerProvider);
        PowerMockito.verifyStatic(StaxUtil.class);
        StaxUtil.writeRootName(genMock, rootName);
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(SimpleBeanPropertySerializerBase.class, ValueInstantiator.noOpValueInstanter(), null, null).thenReturn(Mockito.mock(JsonSerializer.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        XmlRootNameLookup rootNameLookupMock = Mockito.mock(XmlRootNameLookup.class);
        when(xmlSerializerProvider._rootNameLookup).thenReturn(rootNameLookupMock);
        when(rootNameLookupMock.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(new QName("rootName"));
        xmlSerializerProvider.serializeValue(genMock, value);
        PowerMockito.verifyStatic();
        serMock.serialize(value, genMock, xmlSerializerProvider);
        PowerMockito.verifyStatic(StaxUtil.class);
        StaxUtil.writeRootName(genMock, new QName("rootName"));
    }

    @Test
    public void testInitializeWithRootName() throws Exception {
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(ToXmlGenerator.class, genMock, null, null, null).thenReturn(Mockito.mock(ToXmlGenerator.class));
        QName rootName = new QName("rootName");
        xmlSerializerProvider._initWithRootName(Mockito.mock(ToXmlGenerator.class), rootName);
        PowerMockito.verifyStatic();
        StaxUtil.writeRootName(genMock, rootName);
    }

    @Test
    public void testSerializeValueWithRootNameAsArray() throws Exception {
        Object value = new Object[3];
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(ToXmlGenerator.class, genMock, null, null, null).thenReturn(Mockito.mock(ToXmlGenerator.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(JavaType.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        xmlSerializerProvider.serializeValue(genMock, value, Arrays.<JavaType>asList(JavaType.constructParametricType(Arrays.class, Integer[].class), JavaType.constructParametricType(Integer[].class, Integer.class)));
        PowerMockito.verifyStatic();
        serMock.serialize(value, genMock, xmlSerializerProvider);
        PowerMockito.verify(genMock).writeStartArray();
        PowerMockito.verify(genMock).writeEndArray();
    }

    @Test
    public void testSerializeValueWithRootNameAndNamespace() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(ToXmlGenerator.class, genMock, null, null, null).thenReturn(Mockito.mock(ToXmlGenerator.class));
        PowerMockito.whenNew(SerializationConfig.class).withArguments().thenReturn(new SerializationConfig());
        PowerMockito.whenNew(XmlRootNameLookup.class).withArguments(any(SerializationConfig.class)).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        when(xmlSerializerProvider._config).thenReturn(new SerializationConfig());
        when(xmlSerializerProvider._rootNameLookup).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        QName rootName = new QName("rootName", "namespace");
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(new PropertyName("rootName"));
        when(xmlSerializerProvider._rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(rootName);
        xmlSerializerProvider.serializeValue(genMock, value);
        PowerMockito.verifyStatic();
        StaxUtil.writeRootName(genMock, rootName);
        PowerMockito.verifyStatic(StaxUtil.class);
        StaxUtil.writeNamespace(genMock, "namespace");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithInvalidRootName() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(ToXmlGenerator.class, genMock, null, null, null).thenReturn(Mockito.mock(ToXmlGenerator.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        PowerMockito.whenNew(SerializationConfig.class).withArguments().thenReturn(new SerializationConfig());
        PowerMockito.whenNew(XmlRootNameLookup.class, any(SerializationConfig.class)).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        when(xmlSerializerProvider._config).thenReturn(new SerializationConfig());
        when(xmlSerializerProvider._rootNameLookup).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        QName rootName = new QName("rootName", "namespace");
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(new PropertyName("rootName"));
        when(xmlSerializerProvider._rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(null);
        xmlSerializerProvider.serializeValue(genMock, value);
        fail("Expected JsonMappingException to be thrown");
    }

    @Test
    public void testSerializeValueWithNullRootName() throws Exception {
        Object value = new Object();
        JsonGenerator genMock = Mockito.mock(JsonGenerator.class);
        PowerMockito.whenNew(TokenBuffer.class).withArguments(xmlSerializerProvider).thenReturn(new TokenBuffer(xmlSerializerProvider));
        PowerMockito.whenNew(ToXmlGenerator.class, genMock, null, null, null).thenReturn(Mockito.mock(ToXmlGenerator.class));
        JsonSerializer<Object> serMock = Mockito.mock(JsonSerializer.class);
        when(xmlSerializerProvider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(PropertyName.class))).thenReturn(serMock);
        PowerMockito.whenNew(SerializationConfig.class).withArguments().thenReturn(new SerializationConfig());
        PowerMockito.whenNew(XmlRootNameLookup.class, any(SerializationConfig.class)).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        when(xmlSerializerProvider._config).thenReturn(new SerializationConfig());
        when(xmlSerializerProvider._rootNameLookup).thenReturn(Mockito.mock(XmlRootNameLookup.class));
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(null);
        xmlSerializerProvider.serializeValue(genMock, value);
        PowerMockito.verifyStatic();
        StaxUtil.writeRootName(genMock, XmlSerializerProvider.ROOT_NAME_FOR_NULL);
    }

}