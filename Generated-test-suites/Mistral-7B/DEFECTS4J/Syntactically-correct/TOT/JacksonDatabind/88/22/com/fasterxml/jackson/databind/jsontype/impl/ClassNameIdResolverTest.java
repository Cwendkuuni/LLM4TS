package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

    @Test
    public void testIdFromValue() throws Exception {
        TypeFactory typeFactory = new TypeFactoryBuilder()
                .base(Object.class)
                .build();

        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getType(TypeReference.UNBOUND_TYPE), typeFactory);

        // Test with simple class
        Object value1 = new Object();
        String id1 = resolver.idFromValue(value1);
        assertEquals("java.lang.Object", id1);

        // Test with array
        Object[] value2 = new Object[1];
        String id2 = resolver.idFromValue(value2);
        assertEquals("java.lang.Object[]", id2);

        // Test with collection
        List<String> value3 = new ArrayList<>();
        String id3 = resolver.idFromValue(value3);
        assertEquals("java.util.ArrayList", id3);

        // Test with enum
        MyEnum value4 = MyEnum.VALUE1;
        String id4 = resolver.idFromValue(value4);
        assertEquals("com.example.MyEnum", id4);

        // Test with inner class
        MyOuterClass.InnerClass value5 = new MyOuterClass().new InnerClass();
        String id5 = resolver.idFromValue(value5);
        assertEquals("com.example.MyOuterClass$InnerClass", id5);

        // Test with generic class
        List<MyEnum> value6 = new ArrayList<>();
        String id6 = resolver.idFromValue(value6);
        assertEquals("java.util.ArrayList<com.example.MyEnum>", id6);

        // Test with custom class name
        MyCustomClass value7 = new MyCustomClass();
        String id7 = resolver.idFromValue(value7);
        assertEquals("com.example.MyCustomClass", id7);

        // Test with unknown class
        Object unknownValue = new Object() {
            @Override
            public String toString() {
                return "UnknownClass";
            }
        };
        String idUnknown = resolver.idFromValue(unknownValue);
        assertNull(idUnknown);
    }

    @Test
    public void testIdFromValueAndType() throws Exception {
        TypeFactory typeFactory = new TypeFactoryBuilder()
                .base(Object.class)
                .build();

        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class.getType(TypeReference.UNBOUND_TYPE), typeFactory);

        // Test with simple class
        Object value1 = new Object();
        Class<?> type1 = value1.getClass();
        String id1 = resolver.idFromValueAndType(value1, type1);
        assertEquals("java.lang.Object", id1);

        // Test with array
        Object[] value2 = new Object[1];
        Class<?> type2 = value2.getClass();
        String id2 = resolver.idFromValueAndType(value2, type2);
        assertEquals("java.lang.Object[]", id2);

        // Test with collection
        List<String> value3 = new ArrayList<>();
        Class<?> type3 = value3.getClass();
        String id3 = resolver.idFromValueAndType(value3, type3);
        assertEquals("java.util.ArrayList", id3);

        // Test with enum
        MyEnum value4 = MyEnum.VALUE1;
        Class<?> type4 = value4.getClass();
        String id4 = resolver.idFromValueAndType(value4, type4);
        assertEquals("com.example.MyEnum", id4);

        // Test with inner class
        MyOuterClass.InnerClass value5 = new MyOuterClass().new InnerClass();
        Class<?> type5 = value5.getClass();
        String id5 = resolver.idFromValueAndType(value5, type5);
        assertEquals("com.example.MyOuterClass$InnerClass", id5);

        // Test with generic class
        List<MyEnum> value6 = new ArrayList<>();
        Class<?> type6 = value6.getClass();
        String id6 = resolver.idFromValueAndType(value6, type6);
        assertEquals("java.util.ArrayList<com.example.MyEnum>", id6);

        // Test with custom class name
        MyCustomClass value7 = new MyCustomClass();
        Class<?> type7 = value7.getClass();
        String id7 = resolver.idFromValueAndType(value7, type7);
        assertEquals("com.example.MyCustomClass", id7);

        // Test with unknown class
        Object unknownValue = new Object() {
            @Override
            public String toString() {
                return "UnknownClass";
            }
        };
        Class<?> unknownType = unknownValue.getClass();
        String idUnknown = resolver.idFromValueAndType(unknownValue, unknownType);
        assertNull(idUnknown);
    }

    @Test
    public void testTypeFromId() throws Exception {
        ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, new TypeFactoryBuilder()
                .build());

        // Test with simple class
        String idSimpleClass = "java.lang.String";
        JavaType javaTypeSimpleClass = resolver.typeFromId(null, idSimpleClass);
        assertEquals(String.class, javaTypeSimpleClass.getRawClass());

        // Test with array
        String idArray = "java.lang.Object[]";
        JavaType javaTypeArray = resolver.typeFromId(null, idArray);
        assertEquals(Object[].class, javaTypeArray.getRawClass());

        // Test with collection
        String idArrayList = "java.util.ArrayList";
        JavaType javaTypeArrayList = resolver.typeFromId(null, idArrayList);
        assertEquals(ArrayList.class, javaTypeArrayList.getRawClass());

        // Test with enum
        String idMyEnum = "com.example.MyEnum";
        JavaType javaTypeMyEnum = resolver.typeFromId(null, idMyEnum);
        assertEquals(MyEnum.class, javaTypeMyEnum.getRawClass());

        // Test with inner class
        String idInnerClass = "com.example.MyOuterClass$InnerClass";
        JavaType javaTypeInnerClass = resolver.typeFromId(null, idInnerClass);
        assertEquals(MyOuterClass.InnerClass.class, javaTypeInnerClass.getRawClass());

        // Test with generic class
        String idArrayListMyEnum = "java.util.ArrayList<com.example.MyEnum>";
        JavaType javaTypeArrayListMyEnum = resolver.typeFromId(null, idArrayListMyEnum);
        assertEquals(ArrayList.class, javaTypeArrayListMyEnum.getRawClass());
        assertEquals(MyEnum.class, javaTypeArrayListMyEnum.getType().getComponentType());

        // Test with custom class name
        String idMyCustomClass = "com.example.MyCustomClass";
        JavaType javaTypeMyCustomClass = resolver.typeFromId(null, idMyCustomClass);
        assertEquals(MyCustomClass.class, javaTypeMyCustomClass.getRawClass());

        // Test with unknown class
        String idUnknown = "com.example.UnknownClass";
        JavaType javaTypeUnknown = resolver.typeFromId(null, idUnknown);
        assertNull(javaTypeUnknown);

        // Test with custom deserializer
        StdDeserializer<MyCustomClass> deserializer = new StdDeserializer<MyCustomClass>() {
            @Override
            public MyCustomClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new MyCustomClass();
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addDeserializer(MyCustomClass.class, deserializer));
        JavaType javaTypeMyCustomClassDeserializer = mapper.getTypeFactory()
                .constructType(MyCustomClass.class);
        assertEquals(javaTypeMyCustomClassDeserializer, resolver.typeFromId(mapper.getDeserializationContext(), idMyCustomClass));
    }
}