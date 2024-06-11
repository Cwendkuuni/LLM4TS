package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {

    private ObjectMapper objectMapper;
    private TypeFactory typeFactory;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        typeFactory = objectMapper.getTypeFactory();
    }

    @Test
    public void testIdFromValueWithString() throws IOException {
        // Given
        String inputString = "Hello World";
        String expectedId = "java.lang.String";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructType(String.class), typeFactory);

        // When
        String id = classResolver.idFromValue(inputString);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueWithInteger() throws IOException {
        // Given
        int inputInteger = 42;
        String expectedId = "java.lang.Integer";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructType(Integer.class), typeFactory);

        // When
        String id = classResolver.idFromValue(inputInteger);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueWithEnum() throws IOException {
        // Given
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        String expectedId = "java.lang.Enum";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructType(DayOfWeek.class), typeFactory);

        // When
        String id = classResolver.idFromValue(dayOfWeek);

        // Then
        assertEquals(expectedId, id.substring(0, expectedId.length() - 5));
    }

    @Test
    public void testIdFromValueWithArrayList() throws IOException {
        // Given
        List<String> inputList = Arrays.asList("Hello World", "Test");
        String expectedId = "java.util.ArrayList";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructType(List.class.getTypeName()), typeFactory);

        // When
        String id = classResolver.idFromValue(inputList);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueWithMap() throws IOException {
        // Given
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("Key1", "Value1");
        inputMap.put("Key2", "Value2");
        String expectedId = "java.util.HashMap";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructType(Map.class.getTypeName()), typeFactory);

        // When
        String id = classResolver.idFromValue(inputMap);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueWithEnumMap() throws IOException {
        // Given
        Map<DayOfWeek, String> inputMap = new EnumMap<>(DayOfWeek.class);
        inputMap.put(DayOfWeek.MONDAY, "Monday");
        inputMap.put(DayOfWeek.TUESDAY, "Tuesday");
        String expectedId = "java.util.EnumMap";
        ClassNameIdResolver classResolver =
                new ClassNameIdResolver(new TypeReference<Map<DayOfWeek, String>>() {}.getType(), objectMapper);

        // When
        String id = classResolver.idFromValue(inputMap);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void testTypeFromIdWithKnownType() throws IOException {
        // Given
        String id = "java.lang.String";
        String expectedClassName = "java.lang.String";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(null, objectMapper.getFactory());

        // When
        JavaType javaType = classResolver.typeFromId(null, id);

        // Then
        assertEquals(expectedClassName, javaType.getClassName());
    }

    @Test
    public void testTypeFromIdWithUnknownType() throws IOException {
        // Given
        String id = "MyUnknownClass";
        String expectedErrorMessage = "Invalid type id 'MyUnknownClass'";
        ClassNameIdResolver classResolver = new ClassNameIdResolver(null, objectMapper.getFactory());

        // When & Then
        assertNull(classResolver.typeFromId(null, id));
        assertEquals(expectedErrorMessage, objectMapper.getDeserializationConfig().getDeserializationErrorMessage(IOException.class, id));
    }
}