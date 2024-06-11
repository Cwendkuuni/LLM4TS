package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("TestTypeVariable"));
    }

    @Test
    public void testCreateBindingsForSingleTypeParameter() {
        // Given
        JavaType javaType = new TypeReference<>("java.util.List<java.lang.String>").getType();

        // When
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);

        // Then
        assertEquals(1, bindings.size());
        String boundName = bindings.getBoundName(0);
        assertEquals("java.lang.String", boundName);
        JavaType boundType = bindings.findBoundType("java.lang.String");
        assertEquals(JavaType.constructUnboundType(String.class), boundType);
    }

    @Test
    public void testCreateBindingsForMultipleTypeParameters() {
        // Given
        JavaType javaType = new TypeReference<>("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>").getType();

        // When
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType);

        // Then
        assertEquals(2, bindings.size());
        String firstBoundName = bindings.getBoundName(0);
        assertEquals("java.lang.String", firstBoundName);
        JavaType firstBoundType = bindings.findBoundType("java.lang.String");
        assertEquals(JavaType.constructUnboundType(String.class), firstBoundType);

        String secondBoundName = bindings.getBoundName(1);
        assertEquals("java.util.List<java.lang.Integer>", secondBoundName);
        JavaType secondBoundType = bindings.findBoundType("java.util.List<java.lang.Integer>");
        assertEquals(JavaType.constructUnboundType(List.class.getTypeParameterTypes()[0]), secondBoundType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithMismatchedNumberOfTypeParameters() {
        // Given
        JavaType javaType = new TypeReference<>("java.util.List<java.lang.String>").getType();

        // When
        TypeBindings bindings = TypeBindings.create(javaType.getRawType(), javaType, new JavaType[1]);

        // Then
        // An exception should be thrown due to the mismatched number of type parameters
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBindingsWithMismatchedBoundTypes() {
        // Given
        JavaType erasedType = new TypeReference<>("java.util.List<String>").getType();

        // When
        TypeBindings bindings = TypeBindings.create(erasedType, JavaType.constructUnboundType(String[].class));

        // Then
        // An exception should be thrown due to the mismatched bound types
    }
}