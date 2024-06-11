package com.fasterxml.jackson.databind.jsontype.impl;

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
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase testee;
    private DeserializationContext context;
    private TypeIdResolver typeIdResolver;
    private BeanProperty property;

    @Before
    public void setUp() {
        context = mock(DeserializationContext.class);
        typeIdResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);

        testee = new TypeDeserializerBase(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
        property = mock(BeanProperty.class);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
        when(testee.getTypeInclusion()).thenReturn(expectedTypeInclusion);
        assertEquals(expectedTypeInclusion, testee.getTypeInclusion());
    }

    @Test
    public void testToString() {
        String expectedString = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:[Ljava.lang.Class;@5b64d62b; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@15db9742]";
        when(testee.toString()).thenReturn(expectedString);
        assertEquals(expectedString, testee.toString());
    }

    @Test
    public void testFindDeserializerWithExistingDeserializer() throws IOException {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeIdResolver.typeFromId(context, typeId)).thenReturn(testee.getDefaultType());
        when(_testee.findDeserializer(context, typeId)).thenReturn(deserializer);

        assertEquals(deserializer, testee.findDeserializer(context, typeId));
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithMissingDeserializer() throws IOException {
        String typeId = "testTypeId";
        when(typeIdResolver.typeFromId(context, typeId)).thenReturn(null);

        testee.findDeserializer(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> expectedDeserializer = NullifyingDeserializer.instance;
        when(testee.findDefaultImplDeserializer(context)).thenReturn(expectedDeserializer);

        assertEquals(expectedDeserializer, testee.findDefaultImplDeserializer(context));
    }

    @Test
    public void testHandleUnknownTypeIdWithKnownTypeIdResolver() throws IOException {
        String typeId = "testTypeId";
        String extraDesc = "known type ids = com.fasterxml.jackson.core.type.TypeReference[class java.util.HashMap<java.lang.String, java.lang.String>]";
        JavaType expectedType = mock(JavaType.class);

        when(typeIdResolver.getTypeIdResolverBase()).thenReturn(mock(TypeIdResolverBase.class));
        when(testee._handleUnknownTypeId(context, typeId, typeIdResolver, testee.getDefaultType())).thenReturn(expectedType);

        assertEquals(expectedType, testee._handleUnknownTypeId(context, typeId, typeIdResolver, testee.getDefaultType()));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithUnknownTypeIdResolver() throws IOException {
        String typeId = "testTypeId";

        when(typeIdResolver.getTypeIdResolverBase()).thenReturn(null);

        testee._handleUnknownTypeId(context, typeId, typeIdResolver, testee.getDefaultType());
    }
}