package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        // Test with simple class
        assertEquals("java.lang.String", resolver.idFromValue("hello"));

        // Test with array
        Object array = new Object[]{"hello", "world"};
        assertEquals("java.lang.Object[]", resolver.idFromValue(array));

        // Test with ArrayList
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        assertEquals("java.util.ArrayList", resolver.idFromValue(list));

        // Test with Enum
        Enum e = Enum.valueOf(MyEnum.class, "VALUE_1");
        assertEquals("com.fasterxml.jackson.test.MyEnum", resolver.idFromValue(e));

        // Test with inner class
        MyClass outer = new MyClass();
        MyInnerClass inner = outer.new MyInnerClass();
        assertEquals("com.fasterxml.jackson.test.MyClass$MyInnerClass", resolver.idFromValue(inner));

        // Test with EnumMap
        Map<MyEnum, String> map = new EnumMap<>(MyEnum.class);
        map.put(MyEnum.VALUE_1, "hello");
        map.put(MyEnum.VALUE_2, "world");
        assertEquals("java.util.EnumMap<com.fasterxml.jackson.test.MyEnum, java.lang.String>", resolver.idFromValue(map));

        // Test with EnumSet
        Set<MyEnum> set = EnumSet.allOf(MyEnum.class);
        assertEquals("java.util.EnumSet<com.fasterxml.jackson.test.MyEnum>", resolver.idFromValue(set));

        // Test with complex class
        MyComplexClass complex = new MyComplexClass();
        assertEquals("com.fasterxml.jackson.test.MyComplexClass", resolver.idFromValue(complex));

        // Test with list of inner classes
        List<MyClass.MyInnerClass> listOfInner = Arrays.asList(outer.new MyInnerClass(), outer.new MyInnerClass());
        assertEquals("java.util.ArrayList", resolver.idFromValue(listOfInner));
    }

    @Test
    public void testIdFromValueAndType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        // Test with simple class
        assertEquals("java.lang.String", resolver.idFromValueAndType("hello", String.class));

        // Test with array
        Object array = new Object[]{"hello", "world"};
        assertEquals("java.lang.Object[]", resolver.idFromValueAndType(array, Object[].class));

        // Test with ArrayList
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("world");
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(list, List.class));

        // Test with Enum
        Enum e = Enum.valueOf(MyEnum.class, "VALUE_1");
        assertEquals("com.fasterxml.jackson.test.MyEnum", resolver.idFromValueAndType(e, MyEnum.class));

        // Test with inner class
        MyClass outer = new MyClass();
        MyInnerClass inner = outer.new MyInnerClass();
        assertEquals("com.fasterxml.jackson.test.MyClass$MyInnerClass", resolver.idFromValueAndType(inner, MyInnerClass.class));

        // Test with EnumMap
        Map<MyEnum, String> map = new EnumMap<>(MyEnum.class);
        map.put(MyEnum.VALUE_1, "hello");
        map.put(MyEnum.VALUE_2, "world");
        assertEquals("java.util.EnumMap<com.fasterxml.jackson.test.MyEnum, java.lang.String>", resolver.idFromValueAndType(map, Map.class));

        // Test with EnumSet
        Set<MyEnum> set = EnumSet.allOf(MyEnum.class);
        assertEquals("java.util.EnumSet<com.fasterxml.jackson.test.MyEnum>", resolver.idFromValueAndType(set, Set.class));

        // Test with complex class
        MyComplexClass complex = new MyComplexClass();
        assertEquals("com.fasterxml.jackson.test.MyComplexClass", resolver.idFromValueAndType(complex, MyComplexClass.class));

        // Test with list of inner classes
        List<MyClass.MyInnerClass> listOfInner = Arrays.asList(outer.new MyInnerClass(), outer.new MyInnerClass());
        assertEquals("java.util.ArrayList", resolver.idFromValueAndType(listOfInner, List.class));

        // Test with null value
        assertNull(resolver.idFromValueAndType(null, String.class));
    }

    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ClassNameIdResolver resolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());

        // Test with simple class
        JavaType type = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
        assertEquals(String.class, type.getRawClass());

        // Test with array
        JavaType arrayType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.Object[]");
        assertEquals(Object[].class, arrayType.getRawClass());

        // Test with ArrayList
        JavaType listType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(List.class, listType.getRawClass());

        // Test with Enum
        JavaType enumType = resolver.typeFromId(mapper.getDeserializationContext(), "com.fasterxml.jackson.test.MyEnum");
        assertEquals(MyEnum.class, enumType.getRawClass());

        // Test with inner class
        JavaType innerType = resolver.typeFromId(mapper.getDeserializationContext(), "com.fasterxml.jackson.test.MyClass$MyInnerClass");
        assertEquals(MyInnerClass.class, innerType.getRawClass());

        // Test with EnumMap
        JavaType mapType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumMap<com.fasterxml.jackson.test.MyEnum, java.lang.String>");
        assertEquals(Map.class, mapType.getRawClass());
        JavaType enumMapType = (JavaType) mapType.getGenericType();
        assertEquals(EnumMap.class, enumMapType.getRawClass());
        JavaType enumTypeMapType = enumMapType.getGenericType();
        assertEquals(MyEnum.class, enumTypeMapType.getRawClass());
        JavaType valueType = ((ParameterizedType) enumTypeMapType.getComponentType()).getActualTypeArguments().get(0);
        assertEquals(String.class, valueType.getRawClass());

        // Test with EnumSet
        JavaType setType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.EnumSet<com.fasterxml.jackson.test.MyEnum>");
        assertEquals(Set.class, setType.getRawClass());
        JavaType enumSetType = (JavaType) setType.getGenericType();
        assertEquals(EnumSet.class, enumSetType.getRawClass());
        JavaType enumTypeSetType = enumSetType.getGenericType();
        assertEquals(MyEnum.class, enumTypeSetType.getRawClass());

        // Test with complex class
        JavaType complexType = resolver.typeFromId(mapper.getDeserializationContext(), "com.fasterxml.jackson.test.MyComplexClass");
        assertEquals(MyComplexClass.class, complexType.getRawClass());

        // Test with list of inner classes
        JavaType listOfInnerType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
        assertEquals(List.class, listOfInnerType.getRawClass());
        JavaType listOfInnerListType = (JavaType) listOfInnerType.getGenericType();
        assertEquals(MyClass.MyInnerClass.class, listOfInnerListType.getRawClass());

        // Test with non-existent class
        assertNull(resolver.typeFromId(mapper.getDeserializationContext(), "com.fasterxml.jackson.nonExistentClass"));
    }
}