package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * ClassNameIdResolverTest
 */
public class ClassNameIdResolverTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        MAPPER.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) throws Exception {
                context.addBean(ClassNameIdResolver.class);
            }
        });
    }

    @org.junit.Test
    public void testIdFromValue() throws Exception {
        Object testValue = new Object();
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        String id = resolver.idFromValue(testValue);
        assertNull(id);

        String className = "com.example.TestClass";
        TestClass testClass = new TestClass();
        id = resolver.idFromValue(testClass);
        assertEquals(className, id);
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws Exception {
        Object testValue = new Object();
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        String id = resolver.idFromValueAndType(testValue, Object.class);
        assertNull(id);

        String className = "com.example.TestClass";
        TestClass testClass = new TestClass();
        id = resolver.idFromValueAndType(testClass, TestClass.class);
        assertEquals(className, id);
    }

    @org.junit.Test
    public void testTypeFromId() throws Exception {
        String id = "com.example.TestClass";
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        JavaType type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertTrue(type.isJavaClass());
        assertEquals(TestClass.class, type.getRawClass());

        id = "java.util.ArrayList";
        type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertTrue(type.isJavaClass());
        assertEquals(ArrayList.class, type.getRawClass());

        id = "java.util.HashMap";
        type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertTrue(type.isMapType());
        MapType mapType = (MapType) type;
        assertEquals(HashMap.class, mapType.getRawKeyType().getRawClass());
        assertEquals(String.class, mapType.getRawValueType().getRawClass());

        id = "java.util.EnumSet<java.lang.String>";
        type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertTrue(type.isCollectionType());
        CollectionType collectionType = (CollectionType) type;
        assertEquals(EnumSet.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getComponentType().getRawClass());
    }

    @org.junit.Test
    public void testTypeFromIdWithUnknownClass() throws Exception {
        String id = "com.unknown.UnknownClass";
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        JavaType type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertNull(type);
    }

    @org.junit.Test
    public void testTypeFromIdWithGenerics() throws Exception {
        String id = "java.util.ArrayList<java.lang.String>";
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        JavaType type = resolver.typeFromId(MAPPER.getDeserializationContext(), id);
        assertTrue(type.isCollectionType());
        CollectionType collectionType = (CollectionType) type;
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(String.class, collectionType.getComponentType().getRawClass());
    }

    @org.junit.Test
    public void testIdFromEnumSet() throws Exception {
        EnumSet<String> enumSet = EnumSet.allOf(StringEnumClass.class);
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        String id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet", id);
    }

    @org.junit.Test
    public void testIdFromEnumMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        TypeIdResolver resolver = MAPPER.getTypeFactory().constructValueType( classNameIdResolverClass );
        String id = resolver.idFromValue(map);
        assertEquals("java.util.HashMap", id);
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="className")
    static class TestClass {
        private String className;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    static class StringEnumClass {
        public static final StringEnumClass VALUE1 = new StringEnumClass("VALUE1");
        public static final StringEnumClass VALUE2 = new StringEnumClass("VALUE2");

        private final String value;

        StringEnumClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static final Class<?> classNameIdResolverClass
            = ClassNameIdResolver.class;
}