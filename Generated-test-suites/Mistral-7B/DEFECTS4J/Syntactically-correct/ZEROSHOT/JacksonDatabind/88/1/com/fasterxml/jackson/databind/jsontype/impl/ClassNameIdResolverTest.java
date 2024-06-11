package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactoryBuilder;

/**
 * Test file for ClassNameIdResolver class.
 */
public class ClassNameIdResolverTest {

    private static ObjectMapper mapper;
    private static TypeFactory typeFactory;

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        mapper = new ObjectMapper();
        typeFactory = new TypeFactoryBuilder().build();
    }

    @org.junit.Test
    public void testIdFromValue() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.JAVA_LANG_STRING, typeFactory);

        Object value = new Object();

        String id = resolver.idFromValue(value);
        assertEquals("java.lang.Object", id);

        value = new String("test");
        id = resolver.idFromValue(value);
        assertEquals("java.lang.String", id);

        value = new Integer(1);
        id = resolver.idFromValue(value);
        assertEquals("java.lang.Integer", id);

        value = new Long(2L);
        id = resolver.idFromValue(value);
        assertEquals("java.lang.Long", id);

        value = new EnumSet.Of(Enum.values()[0]);
        id = resolver.idFromValue(value);
        String expectedEnumSetId = "java.util.EnumSet";
        String actualEnumSetId = id.substring(0, expectedEnumSetId.length());
        assertTrue(actualEnumSetId.equals(expectedEnumSetId)
                || id.startsWith(expectedEnumSetId + "$"));

        value = new HashMap<String, Object>();
        id = resolver.idFromValue(value);
        assertEquals("java.util.Map", id);

        value = Arrays.asList(new Integer(1));
        id = resolver.idFromValue(value);
        String expectedArrayListId = "java.util.ArrayList";
        String actualArrayListId = id.substring(0, expectedArrayListId.length());
        assertTrue(actualArrayListId.equals(expectedArrayListId)
                || id.startsWith(expectedArrayListId + "$"));
    }

    @org.junit.Test
    public void testIdFromValueAndType() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.JAVA_LANG_STRING, typeFactory);

        Object value = new Object();
        Class<?> valueType = value.getClass();

        String id = resolver.idFromValueAndType(value, valueType);
        assertEquals("java.lang.Object", id);

        value = new String("test");
        id = resolver.idFromValueAndType(value, String.class);
        assertEquals("java.lang.String", id);

        value = new Integer(1);
        id = resolver.idFromValueAndType(value, Integer.class);
        assertEquals("java.lang.Integer", id);
    }

    @org.junit.Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.JAVA_LANG_STRING, typeFactory);

        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
        assertEquals(JavaType.JAVA_LANG_STRING, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumSet");
        assertEquals(JavaType._valueClass(EnumSet.class), javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.HashMap");
        assertEquals(JavaType._valueClass(HashMap.class), javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(JavaType._valueClass(ArrayList.class), javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumMap");
        assertEquals(JavaType._valueClass(EnumMap.class), javaType);

        try {
            javaType = resolver.typeFromId(mapper.getDeserializationContext(), "com.test.TestClass");
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}