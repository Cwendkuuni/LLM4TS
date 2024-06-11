package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Before
    public void setup() {
        // Set up ObjectMapper for tests
        MAPPER.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                context.addDeserializer(EnumSet.class, new EnumSetDeserializer());
                context.addDeserializer(EnumMap.class, new EnumMapDeserializer());
            }
        });
    }

    @org.junit.Test
    public void testIdFromValue() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), MAPPER.getTypeFactory());

        // Test with simple classes
        Object simpleValue = "hello";
        String id = resolver.idFromValue(simpleValue);
        assertEquals("java.lang.String", id);

        // Test with arrays
        Object arrayValue = new Object[]{"hello", "world"};
        id = resolver.idFromValue(arrayValue);
        assertEquals("java.lang.Object[]", id);

        // Test with collections
        EnumSet<String> enumSetValue = EnumSet.of("hello", "world");
        id = resolver.idFromValue(enumSetValue);
        assertEquals("java.util.EnumSet", id);

        EnumMap<String, String> enumMapValue = new EnumMap<String, String>(String.class, Arrays.asList("hello", "world"));
        id = resolver.idFromValue(enumMapValue);
        assertEquals("java.util.EnumMap", id);

        // Test with custom classes
        CustomClass customValue = new CustomClass();
        id = resolver.idFromValue(customValue);
        assertEquals("com.custom.CustomClass", id);

        // Test with inner classes
        InnerClass innerValue = new OuterClass().new InnerClass();
        id = resolver.idFromValue(innerValue);
        assertEquals("com.outer.OuterClass$InnerClass", id);
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), MAPPER.getTypeFactory());

        // Test with simple classes
        Object simpleValue = "hello";
        Class<?> simpleType = String.class;
        String id = resolver.idFromValueAndType(simpleValue, simpleType);
        assertEquals("java.lang.String", id);

        // Test with arrays
        Object arrayValue = new Object[]{"hello", "world"};
        Class<?> arrayType = Object[].class;
        id = resolver.idFromValueAndType(arrayValue, arrayType);
        assertEquals("java.lang.Object[]", id);

        // Test with collections
        EnumSet<String> enumSetValue = EnumSet.of("hello", "world");
        Class<?> enumSetType = EnumSet.class;
        id = resolver.idFromValueAndType(enumSetValue, enumSetType);
        assertEquals("java.util.EnumSet", id);

        EnumMap<String, String> enumMapValue = new EnumMap<String, String>(String.class, Arrays.asList("hello", "world"));
        Class<?> enumMapType = EnumMap.class;
        id = resolver.idFromValueAndType(enumMapValue, enumMapType);
        assertEquals("java.util.EnumMap", id);

        // Test with custom classes
        CustomClass customValue = new CustomClass();
        Class<?> customType = CustomClass.class;
        id = resolver.idFromValueAndType(customValue, customType);
        assertEquals("com.custom.CustomClass", id);

        // Test with inner classes
        InnerClass innerValue = new OuterClass().new InnerClass();
        Class<?> innerType = InnerClass.class;
        id = resolver.idFromValueAndType(innerValue, innerType);
        assertEquals("com.outer.OuterClass$InnerClass", id);
    }

    @org.junit.Test
    public void testTypeFromId() throws IOException {
        TypeIdResolver resolver = new ClassNameIdResolver(Object.class.getTypeName(), MAPPER.getTypeFactory());

        // Test with simple classes
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(String.class, javaType.getRawClass());

        // Test with arrays
        id = "java.lang.Object[]";
        JavaType javaTypeArray = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(Object[].class, javaTypeArray.getRawClass());

        // Test with collections
        id = "java.util.EnumSet";
        JavaType javaTypeEnumSet = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(EnumSet.class, javaTypeEnumSet.getRawClass());

        id = "java.util.EnumMap";
        JavaType javaTypeEnumMap = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(EnumMap.class, javaTypeEnumMap.getRawClass());

        // Test with custom classes
        id = "com.custom.CustomClass";
        JavaType javaTypeCustom = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(CustomClass.class, javaTypeCustom.getRawClass());

        // Test with inner classes
        id = "com.outer.OuterClass$InnerClass";
        JavaType javaTypeInner = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertEquals(InnerClass.class, javaTypeInner.getRawClass());

        // Test unknown type id
        id = "com.unknown.UnknownClass";
        JavaType javaTypeUnknown = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertNull(javaTypeUnknown);
    }

    private static class CustomClass {
        // Empty class for testing
    }

    private static class InnerClass {
        // Inner class for testing
    }

    private static class EnumSetDeserializer extends EnumSetSerializer {
        @Override
        public EnumSet<Object> deserialize(Map<String, Object> ns, DeserializationContext ctxt) throws IOException {
            // Deserialize EnumSet for testing
            Class<?> enumClass = (Class<?>) ns.get("class");
            Object[] values = (Object[]) ns.get("values");
            return new EnumSet<Object>(enumClass, Arrays.asList(values));
        }
    }

    private static class EnumMapDeserializer extends EnumMapSerializer {
        @Override
        public EnumMap<Object, Object> deserialize(Map<String, Object> ns, DeserializationContext ctxt) throws IOException {
            // Deserialize EnumMap for testing
            Class<?> enumClass = (Class<?>) ns.get("classKey");
            Object[] keyValues = (Object[]) ns.get("keyValues");
            Object[] valueValues = (Object[]) ns.get("valueValues");
            return new EnumMap<Object, Object>(enumClass, Arrays.asList(keyValues), Arrays.asList(valueValues));
        }
    }
}