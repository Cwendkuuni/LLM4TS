package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ClassNameIdResolverTest {

    private ObjectMapper mapper;
    private TypeIdResolver resolver;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        resolver = new ClassNameIdResolver(Object.class.getTypeName(), mapper.getTypeFactory());
        mapper.registerTypeIdResolver(resolver);
    }

    @Test
    public void testIdFromValue() throws IOException {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);

        Enum enumValue = Enum.valueOf(String.class, "VALUE");
        id = resolver.idFromValue(enumValue);
        assertEquals(enumValue.getClass().getName(), id);

        List<String> list = Arrays.asList("ONE", "TWO", "THREE");
        id = resolver.idFromValue(list);
        assertEquals("java.util.List", id);

        Map<String, Integer> map = new HashMap<>();
        map.put("ONE", 1);
        map.put("TWO", 2);
        id = resolver.idFromValue(map);
        assertEquals("java.util.Map", id);

        EnumMap<Enum, Integer> enumMap = new EnumMap<>(Enum.class);
        enumMap.put(Enum.valueOf(String.class, "VALUE"), 1);
        id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap", id);

        EnumSet<Enum> enumSet = EnumSet.allOf(Enum.class);
        id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet", id);

        // Test for special cases
        String[] array = {"ONE", "TWO", "THREE"};
        id = resolver.idFromValue(array);
        assertEquals("java.lang.String[]", id);

        Class<?> innerClass = String.class.getDeclaredClass();
        id = resolver.idFromValue(new Object() {}.getClass().getEnclosingClass());
        assertEquals("java.lang.Object", id); // Should be innerClass, but as per comment no way to handle that
        id = resolver.idFromValue(innerClass);
        assertEquals(innerClass.getName(), id);

        id = resolver.idFromValue(Arrays.asList(new Object()));
        assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValue(Collections.singletonMap("key", new Object()));
        assertEquals("java.util.HashMap", id);

        // Test for error cases
        String idFromNull = resolver.idFromValue(null);
        assertNull(idFromNull);

        String idFromWrongType = resolver.idFromValue(1);
        assertNull(idFromWrongType);
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        Object value = new Object();
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        assertEquals("java.lang.Object", id);

        Enum enumValue = Enum.valueOf(String.class, "VALUE");
        id = resolver.idFromValueAndType(enumValue, Enum.class);
        assertEquals(enumValue.getClass().getName(), id);

        List<String> list = Arrays.asList("ONE", "TWO", "THREE");
        id = resolver.idFromValueAndType(list, List.class);
        assertEquals("java.util.List", id);

        Map<String, Integer> map = new HashMap<>();
        map.put("ONE", 1);
        map.put("TWO", 2);
        id = resolver.idFromValueAndType(map, Map.class);
        assertEquals("java.util.Map", id);

        EnumMap<Enum, Integer> enumMap = new EnumMap<>(Enum.class);
        enumMap.put(Enum.valueOf(String.class, "VALUE"), 1);
        id = resolver.idFromValueAndType(enumMap, EnumMap.class);
        assertEquals("java.util.EnumMap", id);

        EnumSet<Enum> enumSet = EnumSet.allOf(Enum.class);
        id = resolver.idFromValueAndType(enumSet, EnumSet.class);
        assertEquals("java.util.EnumSet", id);

        String[] array = {"ONE", "TWO", "THREE"};
        id = resolver.idFromValueAndType(array, String[].class);
        assertEquals("java.lang.String[]", id);

        Class<?> innerClass = String.class.getDeclaredClass();
        id = resolver.idFromValueAndType(new Object() {}.getClass().getEnclosingClass(), innerClass);
        assertEquals(innerClass.getName(), id);

        id = resolver.idFromValueAndType(Arrays.asList(new Object()), ArrayList.class);
        assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValueAndType(Collections.singletonMap("key", new Object()), HashMap.class);
        assertEquals("java.util.HashMap", id);

        // Test for error cases
        id = resolver.idFromValueAndType(null, String.class);
        assertNull(id);

        id = resolver.idFromValueAndType(new Object(), String[].class);
        assertNull(id);

        id = resolver.idFromValueAndType(new Object(), Integer.class);
        assertNull(id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.Object";
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertSame(Object.class, type.getRawClass());

        id = "java.lang.String";
        type = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertSame(String.class, type.getRawClass());

        id = "java.util.List";
        JavaType javaListType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<List<String>> listTypeRef = new TypeReference<List<String>>() {};
        TypeReference<List<String>> expectedListTypeRef = new TypeReference<List<String>>() {};
        assertEquals(listTypeRef, expectedListTypeRef.wrap(javaListType));

        id = "java.util.Map";
        JavaType javaMapType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<Map<String, Integer>> mapTypeRef = new TypeReference<Map<String, Integer>>() {};
        TypeReference<Map<String, Integer>> expectedMapTypeRef = new TypeReference<Map<String, Integer>>() {};
        assertEquals(mapTypeRef, expectedMapTypeRef.wrap(javaMapType));

        id = "java.util.EnumSet";
        JavaType javaEnumSetType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<EnumSet<Enum>> enumSetTypeRef = new TypeReference<EnumSet<Enum>>() {};
        TypeReference<EnumSet<Enum>> expectedEnumSetTypeRef = new TypeReference<EnumSet<Enum>>() {};
        assertEquals(enumSetTypeRef, expectedEnumSetTypeRef.wrap(javaEnumSetType));

        id = "java.util.ArrayList";
        JavaType javaArrayListType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<List<String>> javaArrayListTypeRef = new TypeReference<List<String>>() {};
        TypeReference<List<String>> expectedJavaArrayListTypeRef = new TypeReference<List<String>>() {};
        assertEquals(javaArrayListTypeRef, expectedJavaArrayListTypeRef.wrap(javaArrayListType));

        id = "java.util.HashMap";
        JavaType javaHashMapType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<Map<String, Integer>> javaHashMapTypeRef = new TypeReference<Map<String, Integer>>() {};
        TypeReference<Map<String, Integer>> expectedJavaHashMapTypeRef = new TypeReference<Map<String, Integer>>() {};
        assertEquals(javaHashMapTypeRef, expectedJavaHashMapTypeRef.wrap(javaHashMapType));

        id = "java.lang.String[]";
        JavaType javaStringArrayType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<String[]> javaStringArrayTypeRef = new TypeReference<String[]>() {};
        TypeReference<String[]> expectedJavaStringArrayTypeRef = new TypeReference<String[]>() {};
        assertEquals(javaStringArrayTypeRef, expectedJavaStringArrayTypeRef.wrap(javaStringArrayType));

        id = "com.example.MyClass";
        JavaType javaMyClassType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        assertNull(javaMyClassType);

        id = "com.example.MyClass<java.util.List<java.lang.String>>";
        JavaType javaMyClassWithGenericTypeType = resolver.typeFromId(mapper.getDeserializationContext(), id);
        TypeReference<MyClass<List<String>>> javaMyClassWithGenericTypeRef = new TypeReference<MyClass<List<String>>>() {};
        TypeReference<MyClass<List<String>>> expectedJavaMyClassWithGenericTypeRef = new TypeReference<MyClass<List<String>>>() {};
        assertEquals(javaMyClassWithGenericTypeRef, expectedJavaMyClassWithGenericTypeRef.wrap(javaMyClassWithGenericTypeType));
    }
}