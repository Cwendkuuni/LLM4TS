package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() throws Exception {
        typeIdResolver = mock(TypeIdResolver.class);
        deserializationContext = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser(getClass().getResourceAsStream("/test.json"));

        JavaType baseType = new ClassType(TypeDeserializerBase.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "propertyName", true, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return new TypeDeserializerBase(this, prop) {
                    @Override
                    public JsonTypeInfo.As getTypeInclusion() {
                        return JsonTypeInfo.As.EXISTING_PROPERTY;
                    }

                    @Override
                    protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                            throws IOException {
                        return NullifyingDeserializer.instance;
                    }

                    @Override
                    protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                            throws IOException {
                        return null;
                    }

                    @Override
                    protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId)
                            throws IOException {
                        return null;
                    }

                    @Override
                    protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                            throws IOException {
                        return null;
                    }
                };
            }
        };

        when(typeIdResolver.typeFromId(deserializationContext, "typeId1")).thenReturn(new ClassType(String.class));
        when(typeIdResolver.typeFromId(deserializationContext, "typeId2")).thenReturn(new ClassType(Integer.class));
        when(typeIdResolver.typeFromId(deserializationContext, null)).thenReturn(null);

        when(deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(deserializationContext.findContextualValueDeserializer(new ClassType(String.class), null)).thenReturn(new StringDeserializer());
        when(deserializationContext.findContextualValueDeserializer(new ClassType(Integer.class), null)).thenReturn(new IntDeserializer());

        when(deserializationContext.handleMissingTypeId(baseType, typeIdResolver, "test")).thenThrow(new IOException("MissingTypeId"));
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(new BeanProperty("propertyName", String.class));
        assertEquals(StringDeserializer.class, typeDeserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(TypeDeserializerBase.class.getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("propertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(deserializationContext, "typeId1");
        assertEquals(StringDeserializer.class, deserializer.getClass());

        deserializer = typeDeserializerBase._findDeserializer(deserializationContext, "typeId2");
        assertEquals(IntDeserializer.class, deserializer.getClass());

        deserializer = typeDeserializerBase._findDeserializer(deserializationContext, null);
        assertNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws Exception {
        typeDeserializerBase._handleMissingTypeId(deserializationContext, "test");
    }

    @Test
    public void testDeserializeNull() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
        Object result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        String json = "{\"typeId\":\"typeId1\", \"value\":\"test\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "typeId1");
        assertEquals("test", result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        typeDeserializerBase._handleUnknownTypeId(deserializationContext, "unknownTypeId");
    }

    private static class StringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }

    private static class IntDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsInt();
        }
    }
}

class ConverterMock implements Converter {
    @Override
    public Object readValue(String value, Class<?> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String writeValue(Object value) {
        throw new UnsupportedOperationException();
    }
}