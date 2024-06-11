package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSubtypeResolverTest {

    private static final Class<?> BASE_CLASS = TestBase.class;
    private static final Class<?> SUBTYPE1 = TestSubtype1.class;
    private static final Class<?> SUBTYPE2 = TestSubtype2.class;
    private static final Class<?> SUBTYPE3 = TestSubtype3.class;

    private MapperConfig<?> createMapperConfig() {
        MapperConfig<?> config = new MapperConfig<>();
        config.disable(MapperFeature.FAIL_ON_EMPTY_BEANS);
        return config;
    }

    private AnnotationIntrospector createAnnotationIntrospector() {
        return mock(AnnotationIntrospector.class);
    }

    private AnnotatedClass createAnnotatedClass(Class<?> cls) {
        AnnotatedClass ac = mock(AnnotatedClass.class);
        when(ac.getType()).thenReturn(cls);
        return ac;
    }

    private AnnotatedMember createAnnotatedMember(String propertyName) {
        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getName()).thenReturn(propertyName);
        return am;
    }

    private AnnotatedMethod createAnnotatedMethod(String methodName, Class<?>[] paramTypes) {
        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getName()).thenReturn(methodName);
        when(am.getParameterTypes()).thenReturn(paramTypes);
        return am;
    }

    private NamedType createNamedType(Class<?> clazz, String name) {
        if (name == null) {
            return new NamedType(clazz);
        }
        return new NamedType(clazz, name);
    }

    private void registerSubtypes(SubtypeResolver resolver, Class<?>... types) {
        resolver.registerSubtypes(types);
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Registration of subtypes
        registerSubtypes(resolver, SUBTYPE1, SUBTYPE2, SUBTYPE3);

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();

        // Test with base class
        AnnotatedClass baseClass = createAnnotatedClass(BASE_CLASS);
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, baseClass);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertEquals(SUBTYPE1, result.get(0).getType());
        assertNotNull(result.get(1));
        assertEquals(SUBTYPE2, result.get(1).getType());
        assertNotNull(result.get(2));
        assertEquals(SUBTYPE3, result.get(2).getType());

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByClass(config, baseClass);
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(0));
        assertEquals(result.get(1), result.get(1));
        assertEquals(result.get(2), result.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass_Property() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Registration of subtypes
        registerSubtypes(resolver, SUBTYPE1, SUBTYPE2, SUBTYPE3);

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();
        config.setAnnotationIntrospector(ai);

        AnnotatedClass baseClass = createAnnotatedClass(BASE_CLASS);
        AnnotatedMember property = createAnnotatedMember("property");

        // Test with base class
        List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, baseClass, property);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertEquals(SUBTYPE1, result.get(0).getType());
        assertNotNull(result.get(1));
        assertEquals(SUBTYPE2, result.get(1).getType());
        assertNotNull(result.get(2));
        assertEquals(SUBTYPE3, result.get(2).getType());

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByClass(config, baseClass, property);
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(0));
        assertEquals(result.get(1), result.get(1));
        assertEquals(result.get(2), result.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Registration of subtypes
        registerSubtypes(resolver, SUBTYPE1, SUBTYPE2, SUBTYPE3);

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();

        // Test with base class
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, BASE_CLASS);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertEquals(SUBTYPE1, result.get(0).getType());
        assertNotNull(result.get(1));
        assertEquals(SUBTYPE2, result.get(1).getType());
        assertNotNull(result.get(2));
        assertEquals(SUBTYPE3, result.get(2).getType());

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByTypeId(config, BASE_CLASS);
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(0));
        assertEquals(result.get(1), result.get(1));
        assertEquals(result.get(2), result.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_Property() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Registration of subtypes
        registerSubtypes(resolver, SUBTYPE1, SUBTYPE2, SUBTYPE3);

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();
        config.setAnnotationIntrospector(ai);

        AnnotatedClass baseClass = createAnnotatedClass(BASE_CLASS);
        AnnotatedMember property = createAnnotatedMember("property");

        // Test with base class
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass, property);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertEquals(SUBTYPE1, result.get(0).getType());
        assertNotNull(result.get(1));
        assertEquals(SUBTYPE2, result.get(1).getType());
        assertNotNull(result.get(2));
        assertEquals(SUBTYPE3, result.get(2).getType());

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass, property);
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(0));
        assertEquals(result.get(1), result.get(1));
        assertEquals(result.get(2), result.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_EmptyList() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();
        config.setAnnotationIntrospector(ai);

        AnnotatedClass baseClass = createAnnotatedClass(BASE_CLASS);

        // Test with empty list
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass);

        assertEquals(0, result.size());
        assertNull(result);

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass);
        assertEquals(0, result.size());
        assertEquals(result, result);
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId_NullProperty() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Registration of subtypes
        registerSubtypes(resolver, SUBTYPE1, SUBTYPE2, SUBTYPE3);

        AnnotationIntrospector ai = createAnnotationIntrospector();
        MapperConfig<?> config = createMapperConfig();
        config.setAnnotationIntrospector(ai);

        AnnotatedClass baseClass = createAnnotatedClass(BASE_CLASS);

        // Test with null property
        List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass, null);

        assertEquals(3, result.size());
        assertNotNull(result.get(0));
        assertEquals(SUBTYPE1, result.get(0).getType());
        assertNotNull(result.get(1));
        assertEquals(SUBTYPE2, result.get(1).getType());
        assertNotNull(result.get(2));
        assertEquals(SUBTYPE3, result.get(2).getType());

        // Test second call with same setup should return same result
        result = resolver.collectAndResolveSubtypesByTypeId(config, baseClass, null);
        assertEquals(3, result.size());
        assertEquals(result.get(0), result.get(0));
        assertEquals(result.get(1), result.get(1));
        assertEquals(result.get(2), result.get(2));
    }

    static class TestBase {
        // Empty base class
    }

    @JsonTypeInfo(as = As.PROPERTY, use = Id.CLASS)
    static class TestSubtype1 {
        // Empty subtype
    }

    @JsonTypeInfo(as = As.PROPERTY, use = Id.CLASS, property = "subtype")
    static class TestSubtype2 {
        // Empty subtype with an explicit name
    }

    static class TestSubtype3 {
        // Empty subtype without an explicit name
    }
}