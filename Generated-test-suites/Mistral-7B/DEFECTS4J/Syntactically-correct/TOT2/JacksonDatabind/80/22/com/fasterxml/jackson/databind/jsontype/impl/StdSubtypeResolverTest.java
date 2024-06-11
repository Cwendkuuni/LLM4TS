package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;

/**
 * Tests for {@link StdSubtypeResolver}.
 */
public class StdSubtypeResolverTest {

    /**
     * Test for {@link StdSubtypeResolver#registerSubtypes(Class...)} method.
     */
    @Test
    public void testRegisterSubtypes() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty registration
        resolver.registerSubtypes();
        assertEquals(0, resolver.size());

        // Test registration of single type
        resolver.registerSubtypes(new Class[] { SubClass1.class });
        assertEquals(1, resolver.size());
        NamedType subType = resolver.findDeserializer(SubClass1.class);
        assertNotNull(subType);
        assertEquals(SubClass1.class, subType.getType());

        // Test registration of multiple types
        resolver.registerSubtypes(new Class[] { SubClass1.class, SubClass2.class });
        assertEquals(2, resolver.size());
        NamedType subType1 = resolver.findDeserializer(SubClass1.class);
        assertNotNull(subType1);
        assertEquals(SubClass1.class, subType1.getType());
        NamedType subType2 = resolver.findDeserializer(SubClass2.class);
        assertNotNull(subType2);
        assertEquals(SubClass2.class, subType2.getType());
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}
     * method.
     */
    @Test
    public void testCollectAndResolveSubtypesByClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty registration
        resolver.registerSubtypes();
        MapperConfig config = new MapperFactoryBuilder().configure(StdSubtypeResolverTest.class, new AnnotationIntrospector());
        JavaType baseType = config.getTypeFactory().constructType(BaseClass.class);
        List<NamedType> subTypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(0, subTypes.size());

        // Test registration of single type
        resolver.registerSubtypes(new Class[] { SubClass1.class });
        subTypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(1, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());

        // Test registration of multiple types
        resolver.registerSubtypes(new Class[] { SubClass1.class, SubClass2.class });
        subTypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
        assertEquals(2, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());
        assertEquals(SubClass2.class, subTypes.get(1).getType().getRawClass());
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}
     * method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeId() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty registration
        resolver.registerSubtypes();
        MapperConfig config = new MapperFactoryBuilder().configure(StdSubtypeResolverTest.class, new AnnotationIntrospector());
        ObjectMapper mapper = config.getMapper();
        List<NamedType> subTypes = mapper.getTypeFactory().constructValueType(List.class, NamedType.class).getValue(mapper);
        assertEquals(0, subTypes.size());

        // Test registration of single type
        resolver.registerSubtypes(new Class[] { SubClass1.class });
        subTypes = mapper.getTypeFactory().constructValueType(List.class, NamedType.class).getValue(mapper);
        assertEquals(1, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());

        // Test registration of multiple types
        resolver.registerSubtypes(new Class[] { SubClass1.class, SubClass2.class });
        subTypes = mapper.getTypeFactory().constructValueType(List.class, NamedType.class).getValue(mapper);
        assertEquals(2, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());
        assertEquals(SubClass2.class, subTypes.get(1).getType().getRawClass());
    }

    /**
     * Test for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}
     * method.
     */
    @Test
    public void testCollectAndResolveSubtypesByTypeIdClass() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test empty registration
        resolver.registerSubtypes();
        MapperConfig config = new MapperFactoryBuilder().configure(StdSubtypeResolverTest.class, new AnnotationIntrospector());
        List<NamedType> subTypes = resolver.collectAndResolveSubtypesByTypeId(config, null, BaseClass.class);
        assertEquals(0, subTypes.size());

        // Test registration of single type
        resolver.registerSubtypes(new Class[] { SubClass1.class });
        subTypes = resolver.collectAndResolveSubtypesByTypeId(config, null, BaseClass.class);
        assertEquals(1, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());

        // Test registration of multiple types
        resolver.registerSubtypes(new Class[] { SubClass1.class, SubClass2.class });
        subTypes = resolver.collectAndResolveSubtypesByTypeId(config, null, BaseClass.class);
        assertEquals(2, subTypes.size());
        assertEquals(SubClass1.class, subTypes.get(0).getType().getRawClass());
        assertEquals(SubClass2.class, subTypes.get(1).getType().getRawClass());
    }

    /**
     * Test for edge cases and exception scenarios.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionScenarios() {
        StdSubtypeResolver resolver = new StdSubtypeResolver();

        // Test null input
        resolver.registerSubtypes(null);

        // Test null input in collectAndResolveSubtypesByClass method
        MapperConfig config = new MapperFactoryBuilder().configure(StdSubtypeResolverTest.class, new AnnotationIntrospector());
        JavaType baseType = config.getTypeFactory().constructType(BaseClass.class);
        resolver.collectAndResolveSubtypesByClass(config, null, null);

        // Test null input in collectAndResolveSubtypesByTypeId method
        ObjectMapper mapper = new ObjectMapper();
        List<NamedType> subTypes = mapper.getTypeFactory().constructValueType(List.class, NamedType.class).getValue(mapper);
        resolver.collectAndResolveSubtypesByTypeId(null, null, BaseClass.class);

        // Test null input in collectAndResolveSubtypesByTypeId method with AnnotatedClass
        MapperConfig config2 = new MapperFactoryBuilder().configure(StdSubtypeResolverTest.class, new AnnotationIntrospector());
        resolver.collectAndResolveSubtypesByTypeId(config2, null, null);
    }

    /**
     * Base class for testing.
     */
    static class BaseClass {
    }

    /**
     * Subclass1 for testing.
     */
    static class SubClass1 extends BaseClass {
    }

    /**
     * Subclass2 for testing.
     */
    static class SubClass2 extends BaseClass {
    }
}