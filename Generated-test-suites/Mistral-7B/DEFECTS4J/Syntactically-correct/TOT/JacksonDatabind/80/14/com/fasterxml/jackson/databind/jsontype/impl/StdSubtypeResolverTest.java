package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private MapperFactory mapperFactory;
    private AnnotationIntrospector annotationIntrospector;

    @BeforeEach
    public void setUp() {
        mapperFactory = new MapperFactory();
        mapperFactory.findAndRegisterModules();
        objectMapper = mapperFactory.getObjectMapper();
        annotationIntrospector = objectMapper.getAnnotationIntrospector();
    }

    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?> baseType = Integer.class;
        Class<?> subtype1 = Long.class;
        Class<?> subtype2 = Float.class;

        stdSubtypeResolver.registerSubtypes(baseType, subtype1, subtype2);
        AnnotatedClass baseTypeAnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, baseType);
        List<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(mapperFactory, baseTypeAnnotatedClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?> baseType = String.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = Long.class;

        stdSubtypeResolver.registerSubtypes(baseType, subtype1, subtype2);
        AnnotatedClass baseTypeAnnotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(mapperFactory, baseType);
        List<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(mapperFactory, baseTypeAnnotatedClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        // Test with annotation
        baseTypeAnnotatedClass = AnnotatedClassResolver.resolveAnnotatedWith(baseType, JsonTypeName.class);
        subtypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(mapperFactory, baseTypeAnnotatedClass);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    @Test
    public void testResolutionByTypeId() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?> baseType = Object[].class;
        Class<?> subtype1 = String[].class;
        Class<?> subtype2 = Integer[].class;

        stdSubtypeResolver.registerSubtypes(baseType, subtype1, subtype2);

        // Resolve by type id using an instance of the base type
        Object baseTypeInstance = new Object[1];
        JavaType javaType = objectMapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, javaType);

        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));

        // Resolve by type id using an instance of a subtype
        Object subtype1Instance = new String[1];
        JavaType subtype1JavaType = objectMapper.getTypeFactory().constructType(subtype1);
        List<NamedType> subtypesBySubtype = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, subtype1JavaType);

        assertEquals(1, subtypesBySubtype.size());
        assertTrue(subtypesBySubtype.contains(new NamedType(subtype1)));
    }

    @Test
    public void testResolutionByTypeIdError() {
        StdSubtypeResolver stdSubtypeResolver = new StdSubtypeResolver();
        Class<?> baseType = Object.class;
        Class<?> subtype = String[].class;

        stdSubtypeResolver.registerSubtypes(baseType, subtype);

        // Test with incorrect base type
        ObjectMapper incorrectBaseTypeMapper = mapperFactory.getObjectMapper();
        JavaType incorrectBaseTypeJavaType = incorrectBaseTypeMapper.getTypeFactory().constructType(int[].class);
        List<NamedType> incorrectBaseTypeSubtypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, incorrectBaseTypeJavaType);

        assertEquals(0, incorrectBaseTypeSubtypes.size());

        // Test with incorrect subtype
        ObjectMapper incorrectSubtypeMapper = mapperFactory.getObjectMapper();
        JavaType incorrectSubtypeJavaType = incorrectSubtypeMapper.getTypeFactory().constructType(double[].class);
        List<NamedType> incorrectSubtypeSubtypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(mapperFactory, null, incorrectSubtypeJavaType);

        assertEquals(0, incorrectSubtypeSubtypes.size());
    }
}