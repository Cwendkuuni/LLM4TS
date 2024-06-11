package com.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> superType = List.class;
        Class<LinkedList> subType = LinkedList.class;

        resolver.addMapping(superType, subType);

        Map<ClassKey, Class<?>> expectedMapping = new HashMap<>();
        expectedMapping.put(new ClassKey(superType), subType);

        assertEquals(expectedMapping, resolver._mappings);
    }

    @Test
    public void testAddMapping_ConcreteType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> superType = List.class;
        Class<ArrayList> subType = ArrayList.class;

        resolver.addMapping(superType, subType);

        Map<ClassKey, Class<?>> expectedMapping = new HashMap<>();

        assertFalse(expectedMapping.containsKey(new ClassKey(superType)));
    }

    @Test
    public void testAddMapping_InvalidSubtype() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> superType = List.class;
        Class<String> subType = String.class;

        resolver.addMapping(superType, subType);

        Map<ClassKey, Class<?>> expectedMapping = new HashMap<>();

        assertFalse(expectedMapping.containsKey(new ClassKey(superType)));
    }

    @Test
    public void testAddMapping_SelfMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> superType = List.class;

        resolver.addMapping(superType, superType);

        Map<ClassKey, Class<?>> expectedMapping = new HashMap<>();

        assertFalse(expectedMapping.containsKey(new ClassKey(superType)));
    }

    @Test
    public void testFindTypeMapping() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType collectionType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType expectedType = TypeFactory.defaultInstance().constructType(LinkedList.class);

        JavaType actualType = resolver.findTypeMapping(null, collectionType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMapping_NonExistentSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType nonExistentType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.findTypeMapping(null, nonExistentType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMapping_EmptyMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType emptyType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.findTypeMapping(null, emptyType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testFindTypeMapping_InterfaceType() throws Exception {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType interfaceType = TypeFactory.defaultInstance().constructType(Iterator.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.findTypeMapping(null, interfaceType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);

        JavaType actualType = resolver.resolveAbstractType(null, listType);
        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractType_EmptyMappings() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        JavaType abstractType = TypeFactory.defaultInstance().constructType(Collection.class);

        JavaType actualType = resolver.resolveAbstractType(null, abstractType);
        assertNull(actualType);
    }

    @Test
    public void testResolveAbstractType_NonExistentSuperType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType nonExistentType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.resolveAbstractType(null, nonExistentType);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testResolveAbstractType_ConcreteType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);

        JavaType concreteType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType expectedType = null;

        JavaType actualType = resolver.resolveAbstractType(null, concreteType);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMapping_InvalidMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        Class<List> superType = List.class;
        Class<ArrayList> subType = ArrayList.class;

        resolver.addMapping(superType, subType);
        resolver.addMapping(superType, ArrayList.class);
    }
}