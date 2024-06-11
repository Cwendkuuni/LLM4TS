package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.databind.JsonTestUtils.MAPPER_ANNOTATED;
import static org.junit.Assert.assertEquals;

/**
 * Test for {@link ClassNameIdResolver}.
 */
public class ClassNameIdResolverTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = MAPPER_ANNOTATED;
    }

    /**
     * Test method idFromValue.
     */
    @Test
    public void testIdFromValue() throws Exception {
        // Test with String
        Object value = "Hello World";
        String id = mapper.readerForRawValue(String.class).readValueAs(String.class, mapper.writeValueAsString(value));
        assertEquals("java.lang.String", idFromValue(value, mapper));

        // Test with Integer
        value = 123;
        id = mapper.readerForRawValue(int.class).readValueAs(int.class, mapper.writeValueAsString(value));
        assertEquals("java.lang.Integer", idFromValue(value, mapper));

        // Test with List
        Object listValue = Arrays.asList(1, 2, 3);
        id = mapper.readerForValue(new TypeReference<List<Integer>>(){}.getType()).readValueAs(List.class, mapper.writeValueAsString(listValue));
        assertEquals("java.util.ArrayList", idFromValue(listValue, mapper));

        // Test with Enum
        Enum valueEnum = Enum.valueOf(MyEnum.class, "VALUE_ONE");
        id = mapper.readerForValue(MyEnum.class).readValueAs(MyEnum.class, mapper.writeValueAsString(valueEnum));
        assertEquals("com.example.MyEnum", idFromValue(valueEnum, mapper));
    }

    /**
     * Test method idFromValueAndType.
     */
    @Test
    public void testIdFromValueAndType() throws Exception {
        // Test with String
        Object value = "Hello World";
        String type = "java.lang.String";
        String id = mapper.readerForValue(String.class).readValueAs(String.class, mapper.writeValueAsString(value), type);
        assertEquals("java.lang.String", id);

        // Test with Integer
        value = 123;
        type = "java.lang.Integer";
        id = mapper.readerForValue(int.class).readValueAs(int.class, mapper.writeValueAsString(value), type);
        assertEquals("java.lang.Integer", id);

        // Test with List
        Object listValue = Arrays.asList(1, 2, 3);
        type = "java.util.ArrayList";
        id = mapper.readerForValue(new TypeReference<List<Integer>>(){}.getType()).readValueAs(List.class, mapper.writeValueAsString(listValue), type);
        assertEquals("java.util.ArrayList", id);

        // Test with Enum
        Enum valueEnum = Enum.valueOf(MyEnum.class, "VALUE_ONE");
        type = "com.example.MyEnum";
        id = mapper.readerForValue(MyEnum.class).readValueAs(MyEnum.class, mapper.writeValueAsString(valueEnum), type);
        assertEquals("com.example.MyEnum", id);
    }

    /**
     * Test method typeFromId.
     */
    @Test
    public void testTypeFromId() throws Exception {
        // Test with String
        String id = "java.lang.String";
        JavaType javaType = new ClassNameIdResolver(Object.class, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
        assertEquals(String.class, javaType.getValueClass());

        // Test with Integer
        id = "java.lang.Integer";
        javaType = new ClassNameIdResolver(Object.class, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
        assertEquals(Integer.class, javaType.getValueClass());

        // Test with List
        id = "java.util.ArrayList";
        javaType = new ClassNameIdResolver(Object.class, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
        TypeFactory typeFactory = mapper.getTypeFactory();
        assertEquals(typeFactory.constructCollectionType(ArrayList.class, Integer.class), javaType);

        // Test with Enum
        id = "com.example.MyEnum";
        javaType = new ClassNameIdResolver(Object.class, mapper.getTypeFactory()).typeFromId(mapper.getContext(), id);
        assertEquals(MyEnum.class, javaType.getValueClass());
    }

    private String idFromValue(Object value, ObjectMapper mapper) throws IOException {
        String id = mapper.writerWithView(new CustomSerializerModifier()).writeValueAsString(value);
        return new ClassNameIdResolver(Object.class, mapper.getTypeFactory()).idFromValue(value);
    }
}

enum MyEnum {
    VALUE_ONE, VALUE_TWO
}