package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubtypeResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.*;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;
    private MapperConfig<?> config;
    private AnnotationIntrospector introspector;
    private StdSubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        MapperFactory factory = new MapperFactory();
        config = factory.getConfig();
        introspector = config.getAnnotationIntrospector();
        subtypeResolver = new StdSubtypeResolver();
        objectMapper = factory.builder()
                .withTypeResolver(new LaissezFaireSubtypeResolver())
                .withSubtypeResolver(subtypeResolver)
                .build();
    }

    /**
     * Test for registering and resolving subtypes by class for serialization.
     */
    @Test
    public void testSerializationSubtypesByClass() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        subtypeResolver.registerSubtypes(new NamedType(Bar.class));

        // Test resolution
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Foo.class)));
        assertTrue(subtypes.contains(new NamedType(Bar.class)));
    }

    /**
     * Test for registering and resolving subtypes by class for deserialization.
     */
    @Test
    public void testDeserializationSubtypesByClass() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        subtypeResolver.registerSubtypes(new NamedType(Bar.class));

        // Prepare JSON input
        String jsonInput = "{\"type\":\"com.fasterxml.jackson.databind.jsontype.impl.Foo\"}";

        // Deserialize
        Foo foo = objectMapper.readValue(jsonInput, Foo.class);
        assertNotNull(foo);

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Foo.class)));
        assertTrue(subtypes.contains(new NamedType(Bar.class)));
    }

    /**
     * Test for registering and resolving subtypes by type name for deserialization.
     */
    @Test
    public void testDeserializationSubtypesByName() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class, "FooType"));
        subtypeResolver.registerSubtypes(new NamedType(Bar.class, "BarType"));

        // Prepare JSON input
        String jsonInput = "{\"type\":\"FooType\"}";

        // Deserialize
        Foo foo = objectMapper.readValue(jsonInput, Foo.class);
        assertNotNull(foo);

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Foo.class, "FooType")));
        assertTrue(subtypes.contains(new NamedType(Bar.class, "BarType")));
    }

    /**
     * Test for handling missing subtypes registration.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingSubtypesRegistration() {
        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertNull(subtypes);
    }

    /**
     * Test for handling multiple registrations of the same subtype.
     */
    @Test
    public void testMultipleSubtypesRegistration() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(1, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Foo.class)));
    }

    /**
     * Test for handling empty subtypes registration.
     */
    @Test
    public void testEmptySubtypesRegistration() {
        // Register empty subtypes
        subtypeResolver.registerSubtypes(new NamedType[0]);

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(0, subtypes.size());
    }

    /**
     * Test for handling null subtypes registration.
     */
    @Test
    public void testNullSubtypesRegistration() {
        // Register null subtypes
        subtypeResolver.registerSubtypes(null);

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertEquals(0, subtypes.size());
    }

    /**
     * Test for handling invalid subtypes registration (non-NamedType).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubtypesRegistration() {
        // Register invalid subtypes
        subtypeResolver.registerSubtypes(new Object());

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertNull(subtypes);
    }

    /**
     * Test for handling registration of subtypes from the same class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationOfSameClassSubtypes() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));
        subtypeResolver.registerSubtypes(new NamedType(Foo.class));

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);
        assertNull(subtypes);
    }

    /**
     * Test for handling registration of invalid base types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationOfInvalidBaseType() {
        // Register subtypes
        subtypeResolver.registerSubtypes(new NamedType(Object.class, "InvalidBaseType"));

        // Test resolution
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);
        assertNull(subtypes);
    }

    static class Foo {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        public String type = "FooType";
    }

    static class Bar extends Foo {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
        public String type = "BarType";
    }
}