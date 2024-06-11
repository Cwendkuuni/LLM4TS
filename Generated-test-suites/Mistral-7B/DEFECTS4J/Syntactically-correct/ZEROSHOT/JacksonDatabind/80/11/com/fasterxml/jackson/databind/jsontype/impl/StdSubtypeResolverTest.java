package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.NamedType;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

public class StdSubtypeResolverTest {

    private MapperConfig<?> config;
    private AnnotationIntrospector annotationIntrospector;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setUp() throws Exception {
        annotationIntrospector = mock(AnnotationIntrospector.class);
        config = new MapperFactoryBuilder().build(AnnotationIntrospector.defaultInstance())
                .getConfig();
        config.setAnnotationIntrospector(annotationIntrospector);
        subtypeResolver = new StdSubtypeResolver();
    }

    @After
    public void tearDown() {
        subtypeResolver = null;
        config = null;
        annotationIntrospector = null;
    }

    @Test
    public void testRegisterSubtypes() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        subtypeResolver.registerSubtypes(new NamedType(subtype1), new NamedType(subtype2));

        List<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertEquals(2, registeredSubtypes.size());
        assertNamedTypesEqual(new NamedType(subtype1), registeredSubtypes.get(0));
        assertNamedTypesEqual(new NamedType(subtype2), registeredSubtypes.get(1));

        subtypeResolver.registerSubtypes(new NamedType(baseType));
        registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertEquals(3, registeredSubtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), registeredSubtypes.get(2));
    }

    @Test
    public void testRegisterSubtypesClassArray() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        subtypeResolver.registerSubtypes(new Class<?>[] { subtype1, subtype2 });

        List<NamedType> registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertEquals(2, registeredSubtypes.size());
        assertNamedTypesEqual(new NamedType(subtype1), registeredSubtypes.get(0));
        assertNamedTypesEqual(new NamedType(subtype2), registeredSubtypes.get(1));

        subtypeResolver.registerSubtypes(new Class<?>[] { baseType });
        registeredSubtypes = subtypeResolver.getRegisteredSubtypes();
        assertEquals(3, registeredSubtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), registeredSubtypes.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClass() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        subtypeResolver.registerSubtypes(new NamedType(subtype1));
        subtypeResolver.registerSubtypes(new NamedType(subtype2));

        AnnotatedClass annotatedType = mock(AnnotatedClass.class);
        when(annotatedType.getType()).thenReturn(baseType);

        JavaType baseTypeJavaType = new ClassType(baseType);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config,
                mock(AnnotatedMember.class), baseTypeJavaType);

        assertEquals(3, subtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), subtypes.get(0));
        assertNamedTypesEqual(new NamedType(subtype1), subtypes.get(1));
        assertNamedTypesEqual(new NamedType(subtype2), subtypes.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByClassEmpty() {
        Class<?> baseType = Object.class;
        AnnotatedClass annotatedType = mock(AnnotatedClass.class);
        when(annotatedType.getType()).thenReturn(baseType);

        JavaType baseTypeJavaType = new ClassType(baseType);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config,
                annotatedType);

        assertEquals(1, subtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), subtypes.get(0));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        subtypeResolver.registerSubtypes(new NamedType(subtype1));
        subtypeResolver.registerSubtypes(new NamedType(subtype2));

        AnnotatedClass annotatedType = mock(AnnotatedClass.class);
        when(annotatedType.getType()).thenReturn(baseType);

        JavaType baseTypeJavaType = new ClassType(baseType);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config,
                mock(AnnotatedMember.class), baseTypeJavaType);

        assertEquals(3, subtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), subtypes.get(0));
        assertNamedTypesEqual(new NamedType(subtype1), subtypes.get(1));
        assertNamedTypesEqual(new NamedType(subtype2), subtypes.get(2));
    }

    @Test
    public void testCollectAndResolveSubtypesByTypeIdEmpty() {
        Class<?> baseType = Object.class;
        AnnotatedClass annotatedType = mock(AnnotatedClass.class);
        when(annotatedType.getType()).thenReturn(baseType);

        JavaType baseTypeJavaType = new ClassType(baseType);

        List<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config,
                annotatedType);

        assertEquals(1, subtypes.size());
        assertNamedTypesEqual(new NamedType(baseType), subtypes.get(0));
    }

    private void assertNamedTypesEqual(NamedType expected, NamedType actual) {
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getName(), actual.getName());
    }
}