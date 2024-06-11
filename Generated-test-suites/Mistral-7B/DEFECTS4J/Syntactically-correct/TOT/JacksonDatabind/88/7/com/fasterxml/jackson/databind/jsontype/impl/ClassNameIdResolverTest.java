package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;
    private TypeFactory typeFactory;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
    }

    @Test
    public void testIdFromValue_SimpleClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        String value = "hello";
        String expectedId = "java.lang.String";
        assertEquals(expectedId, resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValue_ComplexClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(HashMap.class), typeFactory);
        HashMap<String, Integer> value = new HashMap<>();
        value.put("key", 1);
        String expectedId = "java.util.HashMap<java.lang.String, java.lang.Integer>";
        assertEquals(expectedId, resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType_SimpleClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        String value = "hello";
        Class<?> type = String.class;
        String expectedId = "java.lang.String";
        assertEquals(expectedId, resolver.idFromValueAndType(value, type));
    }

    @Test
    public void testIdFromValueAndType_ListClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);
        List<String> value = Arrays.asList("hello", "world");
        Class<?> type = List.class;
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        String expectedId = "java.util.ArrayList<java.lang.String>";
        assertEquals(expectedId, resolver.idFromValueAndType(value, typeRef.getType()));
    }

    @Test
    public void testTypeFromId_KnownClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, mapper.getFactory());
        String id = "java.lang.String";
        JavaType expectedType = mapper.getTypeFactory().constructType(String.class);
        assertEquals(expectedType, resolver.typeFromId(mapper.getDeserializationContext(), id));
    }

    @Test
    public void testTypeFromId_UnknownClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, mapper.getFactory());
        String id = "com.unknown.UnknownClass";
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), id));
    }

    @Test
    public void testTypeFromId_EnumClass() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, mapper.getFactory());
        String id = "java.lang.Enum";
        JavaType expectedType = mapper.getTypeFactory().constructType(Enum.class);
        assertEquals(expectedType, resolver.typeFromId(mapper.getDeserializationContext(), id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_InvalidId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, mapper.getFactory());
        String id = "invalid_id";
        resolver.typeFromId(mapper.getDeserializationContext(), id);
    }
}