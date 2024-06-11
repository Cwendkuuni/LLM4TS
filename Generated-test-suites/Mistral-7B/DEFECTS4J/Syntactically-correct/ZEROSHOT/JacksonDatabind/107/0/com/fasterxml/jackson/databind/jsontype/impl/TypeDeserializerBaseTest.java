package com.example.jackson.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private DeserializationContext context;
    private TypeDeserializerBase testee;
    private TypeIdResolver idResolver;
    private BeanProperty property;
    private JavaType baseType;
    private JavaType defaultImpl;

    @Before
    public void setUp() throws Exception {
        idResolver = mock(TypeIdResolver.class);
        baseType = mock(JavaType.class);
        defaultImpl = mock(JavaType.class);
        property = mock(BeanProperty.class);
        context = mock(DeserializationContext.class);

        testee = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, defaultImpl) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                this.property = prop;
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer result = testee.forProperty(property);
        assertNotNull(result);
        assertEquals(testee.getClass(), result.getClass());
        assertEquals(testee.getPropertyName(), result.getPropertyName());
        assertEquals(testee.getTypeIdResolver(), result.getTypeIdResolver());
        assertEquals(testee.getDefaultImpl(), result.getDefaultImpl());
        assertNotSame(testee, result);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As result = testee.getTypeInclusion();
        assertSame(JsonTypeInfo.As.WRAP_INCLUDE, result);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        String result = testee.baseTypeName();
        assertNotNull(result);
        assertEquals(baseType.getRawClass().getName(), result);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        String result = testee.getPropertyName();
        assertNotNull(result);
        assertEquals("typePropertyName", result);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeIdResolver result = testee.getTypeIdResolver();
        assertSame(idResolver, result);
    }

    @Test
    public void testDefaultImpl() throws Exception {
        JavaType result = testee.defaultImpl();
        assertSame(defaultImpl, result);
    }

    @Test
    public void testBaseType() throws Exception {
        JavaType result = testee.baseType();
        assertSame(baseType, result);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "typeId";
        JsonDeserializer<Object> result = testee._findDeserializer(context, typeId);
        verify(idResolver).typeFromId(context, typeId);
        verifyNoMoreInteractions(idResolver);
        verifyNoInteractions(context);
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(context);
        assertNotNull(result);
        verifyNoMoreInteractions(context);
        verify(context).findContextualValueDeserializer(defaultImpl, property);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDeserializer_noDeserializer() throws Exception {
        String typeId = "typeId";
        testee._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDeserializer_nullTypeId() throws Exception {
        String typeId = null;
        JsonDeserializer<Object> result = testee._findDeserializer(context, typeId);
        verify(context).findContextualValueDeserializer(defaultImpl, property);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer_deserializerPresent() throws Exception {
        String typeId = "typeId";
        when(idResolver.typeFromId(context, typeId)).thenReturn(typeIdJavaType);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.findDeserializer(typeIdJavaType, context)).thenReturn(deserializer);

        JsonDeserializer<Object> result = testee._findDeserializer(context, typeId);
        verify(idResolver).typeFromId(context, typeId);
        verify(idResolver).findDeserializer(typeIdJavaType, context);
        verify(deserializer).deserialize(any(JsonParser.class), any(DeserializationContext.class));
        assertSame(deserializer, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_noTypeId() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        testee._deserializeWithNativeTypeId(jsonParser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_nonStringTypeId() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        Object typeId = new Object();
        testee._deserializeWithNativeTypeId(jsonParser, context, typeId);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_nullDeserializer() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        String typeId = "typeId";
        when(idResolver.findDeserializer(any(JavaType.class), eq(context))).thenReturn(null);
        testee._deserializeWithNativeTypeId(jsonParser, context, typeId);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getText()).thenReturn("typeId");
        when(idResolver.findDeserializer(any(JavaType.class), eq(context))).thenReturn(jsonDeserializer);

        Object result = testee._deserializeWithNativeTypeId(jsonParser, context, "typeId");
        verify(jsonDeserializer).deserialize(jsonParser, context);
        assertSame(jsonDeserializer.getNullValue(), result);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(idResolver.getDescForKnownTypeIds()).thenReturn("someTypeIds");

        JavaType result = testee._handleUnknownTypeId(context, "unknownTypeId");
        verify(context).handleUnknownTypeId(baseType, "unknownTypeId", idResolver, "known type ids = someTypeIds");
        assertSame(null, result);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        JavaType result = testee._handleMissingTypeId(context, "missingTypeId");
        verify(context).handleMissingTypeId(baseType, idResolver, "missingTypeId");
        assertSame(null, result);
    }
}

class typeIdJavaType extends SimpleImmutableJavaType {
    public typeIdJavaType(Class<?> rawClass, JavaType typeParameters) {
        super(ClassUtil.getRawClass(rawClass), typeParameters);
    }
}

class jsonDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }
}