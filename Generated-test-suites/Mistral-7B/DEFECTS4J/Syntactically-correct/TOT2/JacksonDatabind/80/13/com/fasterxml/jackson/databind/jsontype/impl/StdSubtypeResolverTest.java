package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for {@link StdSubtypeResolver} class.
 */
public class StdSubtypeResolverTest {

    @Test
    public void testRegisterSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<Dog> dogClass = Dog.class;
        Class<Cat> catClass = Cat.class;
        Class<Bird> birdClass = Bird.class;

        // Register subtypes by class
        resolver.registerSubtypes(dogClass, catClass, birdClass);

        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Object.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Dog.class)));
        assertTrue(subtypes.contains(new NamedType(Cat.class)));
        assertTrue(subtypes.contains(new NamedType(Bird.class)));

        // Test registerSubtypes method with the same classes again
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(dogClass, catClass, birdClass);

        // Test collectionAndResolveSubtypesByClass method again
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Dog.class)));
        assertTrue(subtypes.contains(new NamedType(Cat.class)));
        assertTrue(subtypes.contains(new NamedType(Bird.class)));
    }

    @Test
    public void testRegisterSubtypesByName() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<Dog> dogClass = Dog.class;
        Class<Cat> catClass = Cat.class;
        Class<Bird> birdClass = Bird.class;

        // Register subtypes by name
        resolver.registerSubtypes(new NamedType(Dog.class), new NamedType(Cat.class), new NamedType(Bird.class));

        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Object.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Dog.class)));
        assertTrue(subtypes.contains(new NamedType(Cat.class)));
        assertTrue(subtypes.contains(new NamedType(Bird.class)));

        // Test registerSubtypes method with the same classes again
        resolver = new StdSubtypeResolver();
        resolver.registerSubtypes(new NamedType(Dog.class), new NamedType(Cat.class), new NamedType(Bird.class));

        // Test collectionAndResolveSubtypesByClass method again
        subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(3, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(Dog.class)));
        assertTrue(subtypes.contains(new NamedType(Cat.class)));
        assertTrue(subtypes.contains(new NamedType(Bird.class)));
    }

    @Test
    public void testNoSubtypesRegistered() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Object.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method when no subtypes are registered
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method with null baseType
        resolver.collectAndResolveSubtypesByClass(config, null, null);
    }

    @Test
    public void testEmptyTypeList() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Object.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method with an empty type list
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testSameClassAsBaseType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Object.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method with the same class as base type
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testBaseTypeIsAnInterface() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Animal.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method with an interface as base type
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subtypes.size());
    }

    @Test
    public void testAbstractClassAsBaseType() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(AbstractAnimal.class);
        MapperConfig<?> config = new MapperConfig.DefaultConfig();

        // Test collectionAndResolveSubtypesByClass method with an abstract class as base type
        Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subtypes.size());
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static class Animal {
        // Empty class
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static abstract class AbstractAnimal {
        // Empty abstract class
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static class Dog extends Animal {
        // Empty class
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static class Cat extends Animal {
        // Empty class
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
    static class Bird extends Animal {
        // Empty class
    }
}