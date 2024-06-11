package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.jmock.lib.legacy.ClassImposterizer;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * Test class for {@link StdKeySerializer}.
 */
public class StdKeySerializerTest extends MockObjectTestCase {

    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private StdKeySerializer serializer;

    @Before
    public void setUp() {
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
        serializer = new StdKeySerializer();
    }

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName(date.toString());
        verify(mockJsonGenerator).defaultSerializeDateKey(eq(date), any(JsonGenerator.class));
    }

    @Test
    public void testSerializeString() throws IOException {
        String str = "testString";
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(str, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName(str);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expected = createObjectNode().putPOJO("type", "string");
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode actual = serializer.getSchema(mockSerializerProvider, Object.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = ObjectMapper.getDefaultInstance().getTypeFactory().constructValueType(String.class);
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(eq(typeHint));
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeNull() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        serializer.serialize(null, mockJsonGenerator, provider);
        verify(provider).reportMappingError(anyString(), any(JsonMappingException.class));
    }

    @Test
    public void testSerializeObject() throws IOException {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(obj, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("testObject");
    }

    @Test
    public void testSerializePrimitiveBoolean() throws IOException {
        boolean bool = true;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(bool, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("true");
    }

    @Test
    public void testSerializePrimitiveByte() throws IOException {
        byte byteValue = 127;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(byteValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("127");
    }

    @Test
    public void testSerializePrimitiveShort() throws IOException {
        short shortValue = Short.MIN_VALUE;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(shortValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("-32768");
    }

    @Test
    public void testSerializePrimitiveInt() throws IOException {
        int intValue = 123;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(intValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("123");
    }

    @Test
    public void testSerializePrimitiveLong() throws IOException {
        long longValue = Long.MAX_VALUE;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(longValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("9223372036854775807");
    }

    @Test
    public void testSerializePrimitiveFloat() throws IOException {
        float floatValue = 1.23f;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(floatValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("1.23");
    }

    @Test
    public void testSerializePrimitiveDouble() throws IOException {
        double doubleValue = 1.23;
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(doubleValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("1.23");
    }

    @Test
    public void testSerializePrimitiveCharacter() throws IOException {
        char charValue = 'A';
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(charValue, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("A");
    }

    @Test
    public void testSerializePrimitiveCharArray() throws IOException {
        char[] charArray = {'A', 'B'};
        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(charArray, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator).writeFieldName("[AB]");
    }

    @Before
    public void setUpMocks() {
        ClassImposterizer imposterizer = new ClassImposterizer();
        imposterizer.impose(JsonGenerator.class, mockJsonGenerator);
        imposterizer.impose(SerializerProvider.class, mockSerializerProvider);
    }
}