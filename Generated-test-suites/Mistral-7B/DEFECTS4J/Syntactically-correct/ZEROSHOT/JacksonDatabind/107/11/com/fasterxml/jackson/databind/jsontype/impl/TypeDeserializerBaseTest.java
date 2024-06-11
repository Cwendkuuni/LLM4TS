package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private static final String TEST_TYPE_PROPERTY_NAME = "testTypePropertyName";
    private static final boolean TEST_TYPE_ID_VISIBLE = true;
    private static final JavaType TEST_BASE_TYPE = TypeFactory.defaultInstance().constructType(Object.class);
    private static final JavaType TEST_DEFAULT_IMPL = TypeFactory.defaultInstance().constructType(String.class);
    private static final String TEST_TYPE_ID = "testTypeId";
    private static final String TEST_OTHER_TYPE_ID = "testOtherTypeId";
    private static final TypeIdResolver TEST_TYPE_ID_RESOLVER = mock(TypeIdResolver.class);
    private static final BeanProperty TEST_PROPERTY = mock(BeanProperty.class);

    private TypeDeserializerBase testee;
    private DeserializationContext testContext;
    private JsonParser testParser;

    @Before
    public void setup() throws Exception {
        testContext = mock(DeserializationContext.class);
        testParser = mock(JsonParser.class);
        when(testParser.getCurrentName()).thenReturn(TEST_TYPE_ID);
        when(testParser.getText()).thenReturn(TEST_TYPE_ID);
        when(testParser.getTypeId()).thenReturn(TEST_TYPE_ID);
        when(testContext.getAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(testContext.getTypeFactory()).thenReturn(TypeFactory.defaultInstance());
        when(testContext.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);
        when(testContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(testContext.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)).thenReturn(false);
        when(testContext.isEnabled(DeserializationFeature.FAIL_ON_UNPARSED_VALUE)).thenReturn(false);

        testee = new TypeDeserializerBase(TEST_BASE_TYPE, TEST_TYPE_ID_RESOLVER, TEST_TYPE_PROPERTY_NAME, TEST_TYPE_ID_VISIBLE, TEST_DEFAULT_IMPL) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                assertEquals(TEST_PROPERTY, prop);
                return new TypeDeserializerBase(this, prop);
            }
        };
    }

    @After
    public void teardown() throws Exception {
        verifyNoMoreInteractions(testParser);
        verifyNoMoreInteractions(testContext);
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(testee);
        assertEquals(TEST_BASE_TYPE, testee.getBaseType());
        assertEquals(TEST_TYPE_ID_RESOLVER, testee.getTypeIdResolver());
        assertEquals(TEST_TYPE_PROPERTY_NAME, testee.getPropertyName());
        assertFalse(testee.isTypeIdVisible());
        assertNull(testee._property);
        assertNull(testee._defaultImpl);
    }

    @Test
    public void testSubClassConstructor() throws Exception {
        TypeDeserializerBase subTestee = new TypeDeserializerBase(testee, TEST_PROPERTY) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                fail();
            }
        };

        assertEquals(TEST_BASE_TYPE, subTestee.getBaseType());
        assertEquals(TEST_TYPE_ID_RESOLVER, subTestee.getTypeIdResolver());
        assertEquals(TEST_TYPE_PROPERTY_NAME, subTestee.getPropertyName());
        assertFalse(subTestee.isTypeIdVisible());
        assertSame(testee, subTestee._baseType);
        assertSame(testee, subTestee._idResolver);
        assertSame(TEST_PROPERTY, subTestee._property);
        assertSame(TEST_DEFAULT_IMPL, subTestee._defaultImpl);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_EXCEPTIONS, testee.getTypeInclusion());
    }

    @Test
    public void testToString() throws Exception {
        String expected = String.format("[%s; base-type:%s; id-resolver: %s]",
                TypeDeserializerBase.class.getName(), TEST_BASE_TYPE, TEST_TYPE_ID_RESOLVER);
        assertEquals(expected, testee.toString());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(TEST_BASE_TYPE.getRawClass().getName(), testee.baseTypeName());
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(TEST_DEFAULT_IMPL);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> result = testee._findDeserializer(testContext, TEST_TYPE_ID);
        assertNotNull(result);
        verify(testContext).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        assertNull(testee._findDeserializer(testContext, null));
    }

    @Test
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(null);

        assertNull(testee._findDeserializer(testContext, TEST_TYPE_ID));
        verifyZeroInteractions(testContext);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(testContext);
        assertNotNull(result);
        verify(testContext).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImpl() throws Exception {
        assertNull(testee._findDefaultImplDeserializer(testContext));
    }

    @Test
    public void testFindDeserializerWithCustomDeserializerRegistered() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(TEST_DEFAULT_IMPL);
        JsonDeserializer<Object> customDeserializer = mock(JsonDeserializer.class);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(customDeserializer);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(null);

        JsonDeserializer<Object> result = testee._findDeserializer(testContext, TEST_TYPE_ID);
        assertSame(customDeserializer, result);
        verify(testContext).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
        verify(testContext, times(2)).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
    }

    @Test
    public void testFindDeserializerWithMultipleCustomDeserializersRegistered() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(TEST_DEFAULT_IMPL);
        JsonDeserializer<Object> customDeserializer1 = mock(JsonDeserializer.class);
        JsonDeserializer<Object> customDeserializer2 = mock(JsonDeserializer.class);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(customDeserializer1);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(customDeserializer2);

        JsonDeserializer<Object> result = testee._findDeserializer(testContext, TEST_TYPE_ID);
        assertSame(customDeserializer2, result);
        verify(testContext, atLeast(2)).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(TEST_DEFAULT_IMPL);
        when(testContext.findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY)).thenReturn(mock(JsonDeserializer.class));

        Object result = testee._deserializeWithNativeTypeId(testParser, testContext);
        assertNotNull(result);
        verify(testParser).nextToken();
        verify(testParser).nextToken();
        verify(testParser).nextToken();
        verify(testParser).nextToken();
        verify(testParser).nextToken();
        verify(testParser).nextToken();
        verify(testContext).findContextualValueDeserializer(TEST_DEFAULT_IMPL, TEST_PROPERTY);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullDefaultImpl() throws Exception {
        assertNull(testee._deserializeWithNativeTypeId(testParser, testContext));
    }

    @Test
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(null);

        assertNull(testee._deserializeWithNativeTypeId(testParser, testContext));
        verifyZeroInteractions(testParser);
        verifyZeroInteractions(testContext);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(TEST_TYPE_ID_RESOLVER.typeFromId(testContext, TEST_TYPE_ID)).thenReturn(null);
        when(testContext.handleMissingTypeId(TEST_BASE_TYPE, TEST_ID_RESOLVER)).thenReturn(TEST_DEFAULT_IMPL);

        JavaType result = testee._handleUnknownTypeId(testContext, TEST_TYPE_ID);
        assertSame(TEST_DEFAULT_IMPL, result);
        verify(testContext).handleMissingTypeId(TEST_BASE_TYPE, TEST_ID_RESOLVER);
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        JavaType expected = TypeFactory.defaultInstance().constructType(HashMap.class);

        when(testContext.handleMissingTypeId(TEST_BASE_TYPE, TEST_ID_RESOLVER)).thenReturn(expected);

        JavaType result = testee._handleMissingTypeId(testContext, "test extra description");
        assertSame(expected, result);
        verify(testContext).handleMissingTypeId(TEST_BASE_TYPE, TEST_ID_RESOLVER);
    }

}