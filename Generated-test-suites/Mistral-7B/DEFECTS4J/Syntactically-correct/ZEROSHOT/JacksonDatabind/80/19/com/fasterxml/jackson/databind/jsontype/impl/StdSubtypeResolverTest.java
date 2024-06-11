package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;

/**
 * Unit tests for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for method {@link StdSubtypeResolver#registerSubtypes(Class...)}.
     */
    @Test
    public void testRegisterSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        resolver.registerSubtypes(subtype1, subtype2);
        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    /**
     * Test for method {@link StdSubtypeResolver#registerSubtypes(NamedType...)}.
     */
    @Test
    public void testRegisterSubtypesByName() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        AnnotationIntrospector introspector = mapper.getAnnotationIntrospector();
        NamedType namedSubtype1 = new NamedType(subtype1, introspector.findTypeName(subtype1));
        NamedType namedSubtype2 = new NamedType(subtype2, introspector.findTypeName(subtype2));

        resolver.registerSubtypes(namedSubtype1, namedSubtype2);
        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), null, baseTypeType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(namedSubtype1));
        assertTrue(subtypes.contains(namedSubtype2));
    }

    /**
     * Test for method {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        Class<?> propertyType = Animal.class;
        Object propertyValue = new Dog();
        AnnotatedMember property = new JacksonAnnotationIntrospector().findProperty(propertyType, "animal");

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(mapper.getConfig(), property, baseTypeType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    /**
     * Test for method {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassAnnotated() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        JsonTypeInfo annotation = new JsonTypeInfo()
                .use(As.CLASS)
                .id(Id.NAME)
                .property("animalType");

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        config.setRootValueFactory(DefaultValueFactory.instance());
        config.setSerializerProvider(mapper.getSerializerProvider());
        config.setDeserializerProvider(mapper.getDeserializerProvider());
        config.addAnnotation(baseType, annotation);

        AnnotatedClass baseTypeAnnotated = AnnotatedClass.construct(baseType, config, null, null, null);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, baseTypeAnnotated);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    /**
     * Test for method {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        JsonTypeInfo annotation = new JsonTypeInfo()
                .use(As.EXTERNAL_PROPERTY)
                .id(Id.NAME)
                .name("animalType")
                .property("animalType");

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        config.setRootValueFactory(DefaultValueFactory.instance());
        config.setSerializerProvider(mapper.getSerializerProvider());
        config.setDeserializerProvider(mapper.getDeserializerProvider());
        config.addAnnotation(baseType, annotation);

        Object writerValue = new Animal();
        ObjectReader baseTypeReader = mapper.readerForValue(baseType);
        ObjectWriter baseTypeWriter = mapper.writerForValue(writerValue);
        String json = baseTypeWriter.writeValueAsString(writerValue);

        ObjectMapper deserializer = new ObjectMapper();
        ObjectReader jsonReader = deserializer.readerForValue(json);
        ObjectReader reader = mapper.readerForType(baseType);

        Object propertyValue = jsonReader.readValue(JsonNode.class);
        AnnotatedMember property = new JacksonAnnotationIntrospector().findProperty(baseType, "animal");

        JavaType baseTypeType = mapper.getTypeFactory().constructType(baseType);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseTypeType);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    /**
     * Test for method {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdAnnotated() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        JsonTypeInfo annotation = new JsonTypeInfo()
                .use(As.EXTERNAL_PROPERTY)
                .id(Id.NAME)
                .name("animalType")
                .property("animalType");

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        config.setRootValueFactory(DefaultValueFactory.instance());
        config.setSerializerProvider(mapper.getSerializerProvider());
        config.setDeserializerProvider(mapper.getDeserializerProvider());
        config.addAnnotation(baseType, annotation);

        AnnotatedClass baseTypeAnnotated = AnnotatedClass.construct(baseType, config, null, null, null);
        List<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeAnnotated);
        assertEquals(2, subtypes.size());
        assertTrue(subtypes.contains(new NamedType(subtype1)));
        assertTrue(subtypes.contains(new NamedType(subtype2)));
    }

    /**
     * Test for methods {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)} and
     * {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
     */
    @Test
    public void testCollectAndResolveSubtypesByClassOrTypeIdAnnotated() throws Exception {
        StdSubtypeResolver resolver = new StdSubtypeResolver();
        Class<?> baseType = Animal.class;
        Class<?> subtype1 = Dog.class;
        Class<?> subtype2 = Cat.class;

        JsonTypeInfo annotation = new JsonTypeInfo()
                .use(As.EXTERNAL_PROPERTY)
                .id(Id.NAME)
                .name("animalType")
                .property("animalType");

        MapperConfig config = new MapperConfig(mapper.getFactory());
        config.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        config.setRootValueFactory(DefaultValueFactory.instance());
        config.setSerializerProvider(mapper.getSerializerProvider());
        config.setDeserializerProvider(mapper.getDeserializerProvider());
        config.addAnnotation(baseType, annotation);

        AnnotatedClass baseTypeAnnotated = AnnotatedClass.construct(baseType, config, null, null, null);
        List<NamedType> subtypesClass = resolver.collectAndResolveSubtypesByClass(config, baseTypeAnnotated);
        List<NamedType> subtypesTypeId = resolver.collectAndResolveSubtypesByTypeId(config, baseTypeAnnotated);

        assertEquals(subtypesClass, subtypesTypeId);
        assertEquals(2, subtypesClass.size());
        assertEquals(2, subtypesTypeId.size());
        assertTrue(subtypesClass.containsAll(subtypesTypeId));
        assertTrue(subtypesTypeId.containsAll(subtypesClass));
    }

    private static class Animal {
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "animalType")
        public String animalType;

        public static final JsonTypeInfo ANNOTATION = new JsonTypeInfo()
                .use(JsonTypeInfo.Id.NAME)
                .property("animalType");
    }

    private static class Dog extends Animal {}

    private static class Cat extends Animal {}
}