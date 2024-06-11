
package com.example.junit.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;
        SimpleAbstractTypeResolver expectedResolver = new SimpleAbstractTypeResolver();
        expectedResolver.addMapping(superType, subType);

        SimpleAbstractTypeResolver actualResolver = new SimpleAbstractTypeResolver();
        actualResolver.addMapping(superType, subType);

        assertEquals(expectedResolver, actualResolver);
    }

    // Test adding a mapping for a concrete type instead of an abstract type
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = ArrayList.class;
        Class<LinkedList> subType = LinkedList.class;
        resolver.addMapping(superType, subType);
    }

    // Test adding a mapping for a subtype that is not a subtype of the supertype
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingInvalidSubType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<List> superType = List.class;
        Class<Set<String>> subType = HashSet.class;
        resolver.addMapping(superType, subType);
    }

    // Test adding a mapping for a supertype that is not abstract
    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<Integer> superType = Integer.class;
        Class<Integer> subType = Integer.class;
        resolver.addMapping(superType, subType);
    }
}
