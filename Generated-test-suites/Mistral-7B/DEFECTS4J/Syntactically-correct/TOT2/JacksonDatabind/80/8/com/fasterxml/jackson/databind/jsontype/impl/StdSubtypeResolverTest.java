package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    private ObjectMapper mapper;
    private AnnotationIntrospector annotationIntrospector;
    private SubtypeResolver subtypeResolver;

    @Before
    public void setup() {
        MapperFactoryBuilder builder = new MapperFactoryBuilder();
        mapper = builder.build();
        annotationIntrospector = mapper.getAnnotationIntrospector();
        subtypeResolver = new StdSubtypeResolver();
    }

    @Test
    public void testEmptyRegistration() {
        // Given: an empty registration
        subtypeResolver.registerSubtypes();

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: No subtypes are registered
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testSimpleRegistration() {
        // Given: A simple registration
        subtypeResolver.registerSubtypes(new NamedType(String[].class, "ArrayOfString"));

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(String[].class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: The registered subtype is present
        assertEquals(1, subtypes.size());
        NamedType subtype = subtypes.iterator().next();
        assertEquals("ArrayOfString", subtype.getName());
    }

    @Test
    public void testMultipleRegistration() {
        // Given: Multiple registrations
        subtypeResolver.registerSubtypes(new NamedType(String[].class, "ArrayOfString"));
        subtypeResolver.registerSubtypes(new NamedType(List.class, "List"));
        subtypeResolver.registerSubtypes(new NamedType(Map.class, "Map"));

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(List.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: All registered subtypes are present
        assertEquals(3, subtypes.size());
        List<NamedType> registeredSubtypes = new ArrayList<>(subtypes);
        assertTrue(registeredSubtypes.remove(new NamedType(Map.class, "Map")));
        assertTrue(registeredSubtypes.remove(new NamedType(String[].class, "ArrayOfString")));
        assertTrue(registeredSubtypes.isEmpty());

        // When: Resolving subtypes by type id
        baseType = mapper.constructType(Map.class);
        subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);

        // Then: All registered subtypes are present
        assertEquals(3, subtypes.size());
        registeredSubtypes = new ArrayList<>(subtypes);
        assertTrue(registeredSubtypes.remove(new NamedType(Map.class, "Map")));
        assertTrue(registeredSubtypes.remove(new NamedType(String[].class, "ArrayOfString")));
        assertTrue(registeredSubtypes.isEmpty());
    }

    @Test
    public void testRegistrationsWithNameConflict() {
        // Given: Two registrations with name conflict
        subtypeResolver.registerSubtypes(new NamedType(Map.class, "Map"));
        subtypeResolver.registerSubtypes(new NamedType(List.class, "Map"));

        // When: Resolving subtypes by type id
        JavaType baseType = mapper.constructType(List.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);

        // Then: Only one registration is present (with the later registration taking precedence)
        assertEquals(1, subtypes.size());
        assertEquals(List.class, subtypes.iterator().next().getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegistrationOfInvalidType() {
        // Given: A registration of invalid type
        subtypeResolver.registerSubtypes(new NamedType(null, "InvalidType"));

        // When: Calling method to register subtypes
        subtypeResolver.registerSubtypes();

        // Then: An IllegalArgumentException is thrown
    }

    @Test
    public void testRegistrationOfBaseType() {
        // Given: A registration of a base type
        subtypeResolver.registerSubtypes(new NamedType(Object.class, "Object"));

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: The base type itself is not a subtype
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testRegistrationOfAbstractType() {
        // Given: A registration of an abstract type
        subtypeResolver.registerSubtypes(new NamedType(AbstractClass.class, "AbstractClass"));

        // When: Resolving subtypes by type id
        JavaType baseType = mapper.constructType(AbstractClass.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(mapper.getConfig(), null, baseType);

        // Then: The abstract type is not a subtype
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testRegistrationOfUnreachableType() {
        // Given: A registration of an unreachable type
        subtypeResolver.registerSubtypes(new NamedType(UnreachableClass.class, "UnreachableClass"));

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(String.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: The unreachable type is not a subtype
        assertTrue(subtypes.isEmpty());
    }

    @Test
    public void testRegistrationOfRootType() {
        // Given: A registration of the root type
        subtypeResolver.registerSubtypes(new NamedType(Object.class, "Object"));

        // When: Resolving subtypes by class
        JavaType baseType = mapper.constructType(Object.class);
        Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseType);

        // Then: The root type is the only subtype
        assertEquals(1, subtypes.size());
        assertEquals(Object.class, subtypes.iterator().next().getType());
    }

    // Add more test cases here for other methods and edge cases, such as:
    // - Registration of subtypes with same name but different base types
    // - Registration of cyclic types
    // - Registration of types with base type not assignable from Object
    // - Registration of types with base type already registered as a subtype
    // - Registration of types with base type not found
}