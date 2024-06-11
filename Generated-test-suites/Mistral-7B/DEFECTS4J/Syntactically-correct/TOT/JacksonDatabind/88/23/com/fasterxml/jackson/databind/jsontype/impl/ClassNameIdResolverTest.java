package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
        String id = classNameIdResolver.idFromValue(String.class);
        assertEquals("java.lang.String", id);

        id = classNameIdResolver.idFromValue(List.class);
        assertEquals("java.util.List", id);

        id = classNameIdResolver.idFromValue(Map.class);
        assertEquals("java.util.Map", id);

        id = classNameIdResolver.idFromValue(EnumSet.class);
        assertEquals("java.util.EnumSet", id);

        id = classNameIdResolver.idFromValue(EnumMap.class);
        assertEquals("java.util.EnumMap", id);

        id = classNameIdResolver.idFromValue(ArrayList.class);
        assertEquals("java.util.ArrayList", id);

        id = classNameIdResolver.idFromValue(HashMap.class);
        assertEquals("java.util.HashMap", id);

        id = classNameIdResolver.idFromValue(MyCustomClass.class);
        assertEquals("com.example.MyCustomClass", id);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
        String id = classNameIdResolver.idFromValueAndType(new ArrayList<>(), ArrayList.class);
        assertEquals("java.util.ArrayList", id);

        id = classNameIdResolver.idFromValueAndType(new HashMap<>(), HashMap.class);
        assertEquals("java.util.HashMap", id);

        id = classNameIdResolver.idFromValueAndType(new EnumSet<>(EnumSet.allOf(MyEnum.class)), EnumSet.class);
        assertEquals("java.util.EnumSet", id);

        id = classNameIdResolver.idFromValueAndType(new EnumMap<>(Map.of(MyEnum.ONE, "Value1")), EnumMap.class);
        assertEquals("java.util.EnumMap", id);

        id = classNameIdResolver.idFromValueAndType(new MyCustomClass(), MyCustomClass.class);
        assertEquals("com.example.MyCustomClass", id);

        // Test for null value
        id = classNameIdResolver.idFromValueAndType(null, Object.class);
        assertEquals(JsonTypeInfo.Id.NULL.getId(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver classNameIdResolver = new ClassNameIdResolver(Object.class.getTypeName(), objectMapper.getTypeFactory());
        JavaType javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.lang.String");
        assertEquals(String.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.List");
        assertEquals(List.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.Map");
        assertEquals(Map.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.EnumSet");
        assertEquals(EnumSet.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.EnumMap");
        assertEquals(EnumMap.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(ArrayList.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "java.util.HashMap");
        assertEquals(HashMap.class, javaType.getClass());

        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "com.example.MyCustomClass");
        assertEquals(MyCustomClass.class, javaType.getClass());

        // Test for unknown type id
        javaType = classNameIdResolver.typeFromId(objectMapper.getDeserializationContext(), "com.unknown.UnknownClass");
        assertEquals(null, javaType);
    }

    private static class MyCustomClass {
    }

    private enum MyEnum {
        ONE,
        TWO
    }
}