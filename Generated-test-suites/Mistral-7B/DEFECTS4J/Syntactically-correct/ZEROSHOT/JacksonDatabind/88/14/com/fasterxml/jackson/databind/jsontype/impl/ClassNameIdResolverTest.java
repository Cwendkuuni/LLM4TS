package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

    private ObjectMapper mapper;

    public ClassNameIdResolverTest() throws IOException {
        mapper = new ObjectMapper();
    }

    @Test
    public void testIdFromValue() throws JsonMappingException, IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.valueOf(Integer.class), mapper.getTypeFactory());
        String id = resolver.idFromValue(new Integer(1));
        assertEquals("java.lang.Integer", id);

        id = resolver.idFromValue(Collections.singletonList("Hello"));
        assertEquals("java.util.List", id);

        id = resolver.idFromValue(Collections.singletonMap("Key", "Value"));
        assertEquals("java.util.Map", id);

        EnumSet<DaysOfWeek> set = EnumSet.allOf(DaysOfWeek.class);
        id = resolver.idFromValue(set);
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(EnumSet.class, DaysOfWeek.class);
        assertEquals(javaType.getCanonicalName(), id);

        String unexpectedId = resolver.idFromValue(new Object());
        assertNull(unexpectedId);
    }

    @Test
    public void testIdFromValueAndType() throws JsonMappingException, IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.valueOf(Integer.class), mapper.getTypeFactory());
        String id = resolver.idFromValueAndType(new Integer(1), Integer.class);
        assertEquals("java.lang.Integer", id);

        id = resolver.idFromValueAndType(new String("Hello"), String.class);
        assertEquals("java.lang.String", id);

        id = resolver.idFromValueAndType(new ArrayList<>(), List.class);
        assertEquals("java.util.ArrayList", id);

        id = resolver.idFromValueAndType(new HashMap<>(), Map.class);
        assertEquals("java.util.HashMap", id);

        EnumSet<DaysOfWeek> set = EnumSet.allOf(DaysOfWeek.class);
        id = resolver.idFromValueAndType(set, EnumSet.class);
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(EnumSet.class, DaysOfWeek.class);
        assertEquals(javaType.getCanonicalName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        JavaType baseType = JavaType.valueOf(String[].class);
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, mapper.getTypeFactory());
        JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        JavaType expectedJavaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        assertEquals(expectedJavaType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.LinkedHashMap");
        JavaType expectedLinkedHashMapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        expectedLinkedHashMapType.putQuoted(Map.Entry.class, "java.util.Map.Entry");
        assertEquals(expectedLinkedHashMapType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.EnumSet");
        JavaType expectedEnumSetType = mapper.getTypeFactory().constructCollectionType(EnumSet.class, DaysOfWeek.class);
        assertEquals(expectedEnumSetType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.List");
        JavaType expectedListType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class);
        assertEquals(expectedListType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
        JavaType expectedStringType = JavaType.constructType(String.class);
        assertEquals(expectedStringType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Object");
        JavaType expectedObjectType = JavaType.constructType(Object.class);
        assertEquals(expectedObjectType, javaType);

        javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Class");
        JavaType expectedClassType = JavaType.constructType(Class.class);
        assertEquals(expectedClassType, javaType);

        // Test unregistered class
        JavaType unregisteredType = resolver.typeFromId(mapper.getDeserializationContext(), "com.example.MyClass");
        assertNull(unregisteredType);
    }

    private enum DaysOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}