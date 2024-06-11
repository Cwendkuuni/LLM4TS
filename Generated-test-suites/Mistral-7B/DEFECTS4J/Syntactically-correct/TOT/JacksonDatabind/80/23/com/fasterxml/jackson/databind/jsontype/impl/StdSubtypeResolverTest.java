package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdSubtypeResolverTest {

    private ObjectMapper mapper;
    private SubtypeResolver subtypeResolver;

    @Before
    public void setUp() {
        MapperFactory factory = new MapperFactory();
        mapper = factory.getObjectMapper();
        subtypeResolver = mapper.getTypeFactory().constructValueType(SubtypeResolver.class);
    }

    @Test
    public void testRegisterSubtypesByClass() {
        // Register subtypes by class
        Class<?> baseType = Object.class;
        Class<?> subtype1 = Integer.class;
        Class<?> subtype2 = String.class;

        subtypeResolver.registerSubtypes(subtype1, subtype2);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);

        assertEquals(2, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertEquals(subtype1, subtypes.iterator().next().getType());
        assertNotNull(subtypes.iterator().next());
        assertEquals(subtype2, subtypes.iterator().next().getType());
    }

    @Test
    public void testRegisterSubtypesByAnnotation() {
        // Register subtypes by annotation
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
        @JsonTypeName("MyClass")
        static class MyBaseClass {
            // Empty base class
        }

        @JsonTypeName("Subtype1")
        static class Subtype1 extends MyBaseClass {
        }

        @JsonTypeName("Subtype2")
        static class Subtype2 extends MyBaseClass {
        }

        subtypeResolver.registerSubtypes(MyBaseClass.class);

        JavaType baseTypeType = mapper.getTypeFactory().constructType(MyBaseClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);

        assertEquals(2, subtypes.size());
        assertNotNull(subtypes.iterator().next());
        assertEquals(Subtype1.class, subtypes.iterator().next().getType());
        assertNotNull(subtypes.iterator().next());
        assertEquals(Subtype2.class, subtypes.iterator().next().getType());
    }

    @Test
    public void testEmptySubtypesCollection() {
        // Test empty subtypes collection
        Class<?> baseType = Object.class;

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testNoSubtypes() {
        // Test no subtypes
        Class<?> baseType = String.class;

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);

        assertEquals(0, subtypes.size());
    }

    @Test
    public void testErrorWhenSubtypeIsNotAssignable() {
        // Test error when subtype is not assignable
        Class<?> baseType = Object.class;
        Class<?> subtype = String[].class;

        try {
            subtypeResolver.registerSubtypes(subtype);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("A class [Ljava.lang.String; cannot be a subtype of base class java.lang.Object", e.getMessage());
        }

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);

        assertEquals(0, subtypes.size());
    }
}