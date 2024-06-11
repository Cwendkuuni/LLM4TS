package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class HashCodeBuilderTest {

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithInitialValueAndMultiplierProvider")
    public void testReflectionHashCodeWithInitialValueAndMultiplier(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, Object object) {
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object);
        int actualHashCode = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber).append(object).toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithTestTransientsProvider")
    public void testReflectionHashCodeWithTestTransients(Object object) {
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, true);
        int actualHashCode = new HashCodeBuilder().append(object).toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithExcludeFieldsProvider")
    public void testReflectionHashCodeWithExcludeFields(Object object, String... excludeFields) {
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, excludeFields);
        int actualHashCode = new HashCodeBuilder().append(object, excludeFields).toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithReflectUpToClassProvider")
    public void testReflectionHashCodeWithReflectUpToClass(Object object, Class reflectUpToClass) {
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, reflectUpToClass, false, null);
        int actualHashCode = new HashCodeBuilder().append(object, reflectUpToClass).toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithTestTransientsAndExcludeFieldsProvider")
    public void testReflectionHashCodeWithTestTransientsAndExcludeFields(Object object, boolean testTransients, String... excludeFields) {
        int expectedHashCode = HashCodeBuilder.reflectionHashCode(object, testTransients, null, excludeFields);
        int actualHashCode = new HashCodeBuilder().append(object, testTransients, excludeFields).toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @ParameterizedTest
    @MethodSource("testReflectionHashCodeWithNullObjectProvider")
    public void testReflectionHashCodeWithNullObject(Object object) {
        assertThrows(IllegalArgumentException.class, () -> HashCodeBuilder.reflectionHashCode(object, 17, 37));
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        assertNull(builder.build());
    }

    static Stream<Arguments> testReflectionHashCodeWithInitialValueAndMultiplierProvider() {
        return Stream.of(
                arguments(17, 37, "hello"),
                arguments(13, 3, new int[]{1, 2, 3}),
                arguments(19, 5, new double[]{1.1, 2.2, 3.3}),
                arguments(3, 7, new String[]{"hello", "world"})
        );
    }

    static Stream<Arguments> testReflectionHashCodeWithTestTransientsProvider() {
        return Stream.of(
                arguments(new Object(){
                    boolean transientField;
                }),
                arguments("Hello", new String[]{ "transient Field" })
        );
    }

    static Stream<Arguments> testReflectionHashCodeWithExcludeFieldsProvider() {
        return Stream.of(
                arguments("Hello", new String[]{"transientField"}),
                arguments(new Object(){
                    private int id;
                    private String name;
                    private transient String transientField;
                }, new String[]{"transientField", "id"})
        );
    }

    static Stream<Arguments> testReflectionHashCodeWithReflectUpToClassProvider() {
        return Stream.of(
                arguments(new Object(){
                    private String name;
                }, String.class),
                arguments(new Object(){
                    private String name;
                    private int age;
                }, String.class)
        );
    }

    static Stream<Arguments> testReflectionHashCodeWithTestTransientsAndExcludeFieldsProvider() {
        return Stream.of(
                arguments(new Object(){
                    private String name;
                    private transient int age;
                }, true, "age")
        );
    }

    @ParameterizedTest
    @MethodSource("testAppendProvider")
    public void testAppend(Object object) {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(object);
        assertNotNull(builder.build());
    }

    static Stream<Arguments> testAppendProvider() {
        return Stream.of(
                arguments("hello"),
                arguments(new int[]{1, 2, 3}),
                arguments(new double[]{1.1, 2.2, 3.3}),
                arguments(new String[]{"hello", "world"})
        );
    }
}