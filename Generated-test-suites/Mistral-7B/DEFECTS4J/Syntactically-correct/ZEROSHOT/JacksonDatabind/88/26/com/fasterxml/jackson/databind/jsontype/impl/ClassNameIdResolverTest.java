package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeFactory TYPE_FACTORY = MAPPER.getTypeFactory();

    @Test
    public void testIdFromValue() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, TYPE_FACTORY);
        Class<?> clazz = String[].class;
        String idFromValue = resolver.idFromValue(new String[0]);
        assertEquals("java.lang.String[]", idFromValue);

        clazz = Integer[].class;
        idFromValue = resolver.idFromValue(new Integer[0]);
        assertEquals("java.lang.Integer[]", idFromValue);

        clazz = Collections.class;
        idFromValue = resolver.idFromValue(Collections.emptyList());
        assertEquals("java.util.Collections", idFromValue);

        clazz = Arrays.class;
        idFromValue = resolver.idFromValue(Arrays.asList(1, 2, 3).toArray());
        assertEquals("java.util.Arrays", idFromValue);

        clazz = EnumSet.class;
        EnumSet<Integer> enumSet = EnumSet.allOf(EnumSet.class);
        idFromValue = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet", idFromValue);

        clazz = Map.class;
        JavaType javaType = TYPE_FACTORY.constructType(new TypeReference<Map<String, Integer>>() {}.getType());
        idFromValue = resolver.idFromValue(javaType);
        assertEquals("java.util.Map", idFromValue);

        // Test cases for classes not found
        clazz = Object.class;
        assertNull(resolver.idFromValue(null));
        idFromValue = resolver.idFromValue(new Object());
        assertNull(idFromValue);

        clazz = Integer.class;
        assertNull(resolver.idFromValue(new Object()));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, TYPE_FACTORY);
        String idFromValueAndType = resolver.idFromValueAndType(new String[0], String[].class);
        assertEquals("java.lang.String[]", idFromValueAndType);

        idFromValueAndType = resolver.idFromValueAndType(new String[0], Integer[].class);
        assertNull(idFromValueAndType);
    }

    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, TYPE_FACTORY);
        JavaType typeFromId = resolver.typeFromId(MAPPER.getContext(), "java.lang.String[]");
        assertEquals(String[].class, typeFromId.getRawClass());

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.lang.Integer[]");
        assertEquals(Integer[].class, typeFromId.getRawClass());

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.util.Collections");
        assertEquals(Collections.class, typeFromId.getRawClass());

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.util.Arrays");
        assertEquals(Arrays.class, typeFromId.getRawClass());

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.util.EnumSet");
        assertEquals(EnumSet.class, typeFromId.getRawClass());

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.util.Map");
        assertEquals(Map.class, typeFromId.getRawClass());

        // Test cases for classes not found
        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.lang.Object[]");
        assertNull(typeFromId);

        typeFromId = resolver.typeFromId(MAPper.getContext(), "java.lang.Integer");
        assertNull(typeFromId);
    }

    @Test
    public void test_idFrom() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, TYPE_FACTORY);
        String id = resolver._idFrom(new String[0], String[].class, TYPE_FACTORY);
        assertEquals("java.lang.String[]", id);

        id = resolver._idFrom(new Integer[0], Integer[].class, TYPE_FACTORY);
        assertNull(id);

        id = resolver._idFrom(Collections.emptyList(), Collections.class, TYPE_FACTORY);
        assertEquals("java.util.Collections", id);

        id = resolver._idFrom(Arrays.asList(1, 2, 3).toArray(), Arrays.class, TYPE_FACTORY);
        assertEquals("java.util.Arrays", id);

        id = resolver._idFrom(EnumSet.allOf(EnumSet.class), EnumSet.class, TYPE_FACTORY);
        assertEquals("java.util.EnumSet", id);

        id = resolver._idFrom(new TypeReference<Map<String, Integer>>() {}.getRawType(), Map.class, TYPE_FACTORY);
        assertEquals("java.util.Map", id);
    }
}