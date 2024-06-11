package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class StdSubtypeResolverTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        MapperFactoryBuilder mfb = new MapperFactoryBuilder()
                .findAndRegisterModules();
        this.objectMapper = mfb.build();
    }

    @Test
    public void testSubtypeRegistrationAndCollectionByClass() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        // Register subtypes by class
        subtypeResolver.registerSubtypes(subtype1, subtype2);

        // Test collection by class
        JavaType javaType = objectMapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, javaType);

        // Check if collected subtypes are correct
        Map<Class<?>, NamedType> expectedSubtypes = new LinkedHashMap<>();
        expectedSubtypes.put(subtype1, new NamedType(subtype1));
        expectedSubtypes.put(subtype2, new NamedType(subtype2));
        expectedSubtypes.put(baseType, new NamedType(baseType));

        for (NamedType collectedSubtype : collectedSubtypes) {
            Assert.assertTrue("Collected subtype class must be in the expected list",
                    expectedSubtypes.containsKey(collectedSubtype.getType()));
        }

        // Check if collected subtypes are unique
        Assert.assertEquals("Number of collected subtypes should be equal to the number of specified subtypes",
                expectedSubtypes.size(), collectedSubtypes.size());
    }

    @Test
    public void testSubtypeRegistrationAndCollectionByClassWithSuperclasses() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;
        Class<?> subtype3 = Subtype3.class;

        // Register subtypes by class
        subtypeResolver.registerSubtypes(subtype1, subtype2);

        // Test collection by class
        JavaType javaType = objectMapper.getTypeFactory().constructType(baseType);
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, javaType);

        // Check if collected subtypes are correct
        Map<Class<?>, NamedType> expectedSubtypes = new LinkedHashMap<>();
        expectedSubtypes.put(subtype1, new NamedType(subtype1));
        expectedSubtypes.put(subtype2, new NamedType(subtype2));
        expectedSubtypes.put(baseType, new NamedType(baseType));

        for (NamedType collectedSubtype : collectedSubtypes) {
            Assert.assertTrue("Collected subtype class must be in the expected list",
                    expectedSubtypes.containsKey(collectedSubtype.getType()));
        }

        // Check if collected subtypes are unique
        Assert.assertEquals("Number of collected subtypes should be equal to the number of specified subtypes",
                expectedSubtypes.size(), collectedSubtypes.size());

        // Test collection by class with subtype3 which is a superclass of subtype1
        subtypeResolver.registerSubtypes(subtype3);
        collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, javaType);

        // Check if collected subtypes are correct
        expectedSubtypes = new LinkedHashMap<>(expectedSubtypes);
        expectedSubtypes.put(subtype3, new NamedType(subtype3));
        expectedSubtypes.put(subtype1, new NamedType(subtype1));
        expectedSubtypes.put(subtype2, new NamedType(subtype2));
        expectedSubtypes.put(baseType, new NamedType(baseType));

        for (NamedType collectedSubtype : collectedSubtypes) {
            Assert.assertTrue("Collected subtype class must be in the expected list",
                    expectedSubtypes.containsKey(collectedSubtype.getType()));
        }

        // Check if collected subtypes are unique
        Assert.assertEquals("Number of collected subtypes should be equal to the number of specified subtypes",
                expectedSubtypes.size(), collectedSubtypes.size());
    }

    @Test
    public void testSubtypeRegistrationAndCollectionByTypeId() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;

        // Register subtypes by class
        subtypeResolver.registerSubtypes(subtype1, subtype2);

        // Test collection by type id
        JavaType javaType = objectMapper.getTypeFactory().constructType(BaseClass.class);
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(
                this.objectMapper.getConfig(), null, javaType);

        // Check if collected subtypes are correct
        Map<Class<?>, NamedType> expectedSubtypes = new LinkedHashMap<>();
        expectedSubtypes.put(subtype1, new NamedType(subtype1));
        expectedSubtypes.put(subtype2, new NamedType(subtype2));
        expectedSubtypes.put(baseType, new NamedType(baseType));

        for (NamedType collectedSubtype : collectedSubtypes) {
            Assert.assertTrue("Collected subtype class must be in the expected list",
                    expectedSubtypes.containsKey(collectedSubtype.getType()));
        }

        // Check if collected subtypes are unique
        Assert.assertEquals("Number of collected subtypes should be equal to the number of specified subtypes",
                expectedSubtypes.size(), collectedSubtypes.size());
    }

    @Test
    public void testSubtypeRegistrationAndCollectionByTypeIdWithSuperclasses() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        Class<?> baseType = BaseClass.class;
        Class<?> subtype1 = Subtype1.class;
        Class<?> subtype2 = Subtype2.class;
        Class<?> subtype3 = Subtype3.class;

        // Register subtypes by class
        subtypeResolver.registerSubtypes(subtype1, subtype2);

        // Test collection by type id
        JavaType javaType = objectMapper.getTypeFactory().constructType(BaseClass.class);
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(
                this.objectMapper.getConfig(), null, javaType);

        // Check if collected subtypes are correct
        Map<Class<?>, NamedType> expectedSubtypes = new LinkedHashMap<>(expectedSubtypes);
        expectedSubtypes.put(subtype1, new NamedType(subtype1));
        expectedSubtypes.put(subtype2, new NamedType(subtype2));
        expectedSubtypes.put(subtype3, new NamedType(subtype3));
        expectedSubtypes.put(baseType, new NamedType(baseType));

        for (NamedType collectedSubtype : collectedSubtypes) {
            Assert.assertTrue("Collected subtype class must be in the expected list",
                    expectedSubtypes.containsKey(collectedSubtype.getType()));
        }

        // Check if collected subtypes are unique
        Assert.assertEquals("Number of collected subtypes should be equal to the number of specified subtypes",
                expectedSubtypes.size(), collectedSubtypes.size());
    }

    @Test
    public void testNullBaseType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test collection by class with null base type
        JavaType javaType = null;
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, javaType);

        Assert.assertEquals("Expected empty list since base type is null", 0, collectedSubtypes.size());
    }

    @Test
    public void testNullConfig() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test collection by class with null config
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

        Assert.assertEquals("Expected empty list since config is null", 0, collectedSubtypes.size());
    }

    @Test
    public void testNullProperty() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test collection by class with null property
        JavaType baseType = objectMapper.getTypeFactory().constructType(BaseClass.class);
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, baseType);

        Assert.assertEquals("Expected empty list since property is null", 0, collectedSubtypes.size());
    }

    @Test
    public void testNullPropertyType() {
        StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();

        // Test collection by class with null property type
        Collection<NamedType> collectedSubtypes = subtypeResolver.collectAndResolveSubtypesByClass(
                this.objectMapper.getConfig(), null, null);

        Assert.assertEquals("Expected empty list since property type is null", 0, collectedSubtypes.size());
    }

    static class BaseClass {
    }

    static class Subtype1 extends BaseClass {
    }

    static class Subtype2 extends BaseClass {
    }

    static class Subtype3 extends BaseClass {
    }
}