package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final TypeIdResolver CLASS_NAME_ID_RESOLVER = new ClassNameIdResolver(Object.class.getTypeName(), new TypeFactory());

    @org.junit.BeforeClass
    public static void setup() {
        // setup
    }

    @org.junit.AfterClass
    public static void teardown() {
        // teardown
    }

    @org.junit.Test
    public void testIdFromValue() throws IOException {
        Object value = new Object();
        String id = CLASS_NAME_ID_RESOLVER.idFromValue(value);
        assertNotNull(id);
        assertEquals(value.getClass().getName(), id);

        value = Arrays.asList(1, 2, 3);
        id = CLASS_NAME_ID_RESOLVER.idFromValue(value);
        assertNotNull(id);
        assertEquals("java.util.ArrayList", id);

        value = EnumSet.allOf(TestEnum.class);
        id = CLASS_NAME_ID_RESOLVER.idFromValue(value);
        assertNotNull(id);
        assertEquals("java.util.EnumSet", id);

        value = new HashMap<>();
        id = CLASS_NAME_ID_RESOLVER.idFromValue(value);
        assertNotNull(id);
        assertEquals("java.util.HashMap", id);
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws IOException {
        Object value = new Object();
        Class<?> type = String.class;
        String id = CLASS_NAME_ID_RESOLVER.idFromValueAndType(value, type);
        assertNotNull(id);
        assertEquals("java.lang.String", id);

        value = Arrays.asList(1, 2, 3);
        type = Integer[].class;
        id = CLASS_NAME_ID_RESOLVER.idFromValueAndType(value, type);
        assertNotNull(id);
        assertEquals("java.lang.Integer[]", id);

        value = EnumSet.allOf(TestEnum.class);
        type = EnumSet.class;
        id = CLASS_NAME_ID_RESOLVER.idFromValueAndType(value, type);
        assertNotNull(id);
        assertEquals("java.util.EnumSet", id);
    }

    @org.junit.Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JavaType javaType = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());

        id = "java.util.ArrayList";
        javaType = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());

        id = "java.util.EnumSet";
        javaType = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaType);
        assertEquals(EnumSet.class, javaType.getRawClass());

        id = "java.util.Map";
        JavaType javaTypeMap = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaTypeMap);
        assertTrue(Map.class.isAssignableFrom(javaTypeMap.getRawClass()));

        id = "java.util.LinkedHashSet";
        JavaType javaTypeLinkedHashSet = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaTypeLinkedHashSet);
        assertEquals(LinkedHashSet.class, javaTypeLinkedHashSet.getRawClass());

        id = "java.lang.Enum";
        JavaType javaTypeEnum = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaTypeEnum);
        assertTrue(Enum.class.isAssignableFrom(javaTypeEnum.getRawClass()));

        id = "java.util.AbstractMap.SimpleEntry";
        JavaType javaTypeSimpleEntry = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaTypeSimpleEntry);
        assertEquals(SimpleEntry.class, javaTypeSimpleEntry.getRawClass());

        id = "java.util.AbstractList$ArrayListDecorator";
        JavaType javaTypeArrayListDecorator = CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
        assertNotNull(javaTypeArrayListDecorator);
        assertEquals(ArrayList.class, javaTypeArrayListDecorator.getRawClass());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testInvalidIdFromValue() throws IOException {
        Object value = new Object();
        String id = CLASS_NAME_ID_RESOLVER.idFromValue(value);
        Class<?> clazz = Class.forName("com.fasterxml.jackson.databind.jsontype.impl."); // invalid class name
        id = CLASS_NAME_ID_RESOLVER.idFromValueAndType(value, clazz);
        CLASS_NAME_ID_RESOLVER.typeFromId(new DatabindContext(), id);
    }

    @org.junit.Test
    public void testGetDescForKnownTypeIds() {
        String desc = CLASS_NAME_ID_RESOLVER.getDescForKnownTypeIds();
        assertEquals("class name used as type id", desc);
    }
}

class TestEnum {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    enum TestEnum {
        VALUE_1, VALUE_2, VALUE_3
    }
}

class SimpleEntry<K, V> {
    K key;
    V value;
}