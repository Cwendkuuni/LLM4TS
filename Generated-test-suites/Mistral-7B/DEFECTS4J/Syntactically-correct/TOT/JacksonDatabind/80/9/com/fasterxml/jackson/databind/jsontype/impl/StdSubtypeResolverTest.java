package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

/**
 * Test class for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

    private MapperConfig<?> config;
    private ObjectMapper objectMapper;
    private SubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        config = new MapperConfig(new AnnotationIntrospector());
        objectMapper = new ObjectMapper(config);
        subtypeResolver = new StdSubtypeResolver();
        config.enable(MapperFeature.AUTO_DETECT_TYPES);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}
     * method when passed valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        AnnotatedMember property = new AnnotatedMember(String.class, "propertyName", new PropertyName("name"));
        AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, String.class);

        List<NamedType> expected = Arrays.asList(
                new NamedType(Integer.class, "java.lang.Integer"),
                new NamedType(Character.class, "java.lang.Character"),
                new NamedType(Byte.class, "java.lang.Byte"),
                new NamedType(Short.class, "java.lang.Short"),
                new NamedType(Long.class, "java.lang.Long"),
                new NamedType(Float.class, "java.lang.Float"),
                new NamedType(Double.class, "java.lang.Double"),
                new NamedType(Boolean.class, "java.lang.Boolean"),
                new NamedType(BigInteger.class, "java.math.BigInteger"),
                new NamedType(BigDecimal.class, "java.math.BigDecimal"));

        List<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}
     * method when passed valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassAnnotation() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
        AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);

        List<NamedType> expected = Arrays.asList(
                new NamedType(MySubclass1.class, "com.example.MySubclass1"),
                new NamedType(MySubclass2.class, "com.example.MySubclass2"));

        List<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}
     * method when passed valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(MyClass.class);
        AnnotatedMember property = new AnnotatedMember(MyClass.class, "propertyName", new PropertyName("name"));

        List<NamedType> expected = Arrays.asList(
                new NamedType(MySubclass1.class, "com.example.MySubclass1"),
                new NamedType(MySubclass2.class, "com.example.MySubclass2"));

        List<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}
     * method when passed valid arguments.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotation() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(MyClass.class);
        AnnotatedClass type = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass.class);

        List<NamedType> expected = Arrays.asList(
                new NamedType(MySubclass1.class, "com.example.MySubclass1"),
                new NamedType(MySubclass2.class, "com.example.MySubclass2"));

        List<NamedType> actual = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertEquals(expected, actual);
    }

    /**
     * Test for handling empty and null cases.
     */
    @Test
    public void testEmptyAndNullCases() {
        JavaType baseType = null;
        AnnotatedMember property = null;
        AnnotatedClass type = null;

        List<NamedType> emptyList = Collections.emptyList();
        List<NamedType> expectedEmpty = Collections.emptyList();

        List<NamedType> actualEmpty = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertEquals(expectedEmpty, actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertEquals(expectedEmpty, actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertEquals(expectedEmpty, actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertEquals(expectedEmpty, actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        assertNull(actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
        assertNull(actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByClass(config, type);
        assertNull(actualEmpty);

        actualEmpty = subtypeResolver.collectAndResolveSubtypesByTypeId(config, type);
        assertNull(actualEmpty);
    }

    /**
     * Test for handling incorrect input types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectTypes() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(Object.class);
        AnnotatedMember property = new AnnotatedMember(String.class, "propertyName", new PropertyName("name"));

        AnnotatedClass notAClass = new AnnotatedClass() {};
        subtypeResolver.collectAndResolveSubtypesByClass(config, notAClass);

        baseType = null;
        subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);

        baseType = objectMapper.getTypeFactory().constructType(MyClass.class);
        subtypeResolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(String.class, "propertyName", new PropertyName("name")), baseType);

        baseType = null;
        subtypeResolver.collectAndResolveSubtypesByTypeId(config, new AnnotatedMember(String.class, "propertyName", new PropertyName("name")), baseType);

        subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);

        subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, null);
    }

    /**
     * Test for handling circular references.
     */
    @Test(expected = StackOverflowError.class)
    public void testCircularReferences() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(CircularClass.class);
        AnnotatedMember property = new AnnotatedMember(CircularClass.class, "propertyName", new PropertyName("name"));

        subtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
        subtypeResolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
    }

    static class MyClass {
        @JsonTypeInfo(use = Id.CLASS, include = As.EXISTING_PROPERTY)
        String propertyName;
    }

    static class MySubclass1 extends MyClass {
        // Empty class
    }

    static class MySubclass2 extends MyClass {
        // Empty class
    }

    static class CircularClass {
        CircularClass propertyName;
    }
}