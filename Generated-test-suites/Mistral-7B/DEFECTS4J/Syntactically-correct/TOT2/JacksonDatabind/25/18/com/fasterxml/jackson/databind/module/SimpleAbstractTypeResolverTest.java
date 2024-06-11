package com.example;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Test;

import java.lang.reflect.IllegalArgumentException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SimpleAbstractTypeResolverTest {

    @Test
    public void testAddMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        JavaType type = JavaType.constructParametricType(LinkedList.class, Integer.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = JavaType.constructParametricType(ArrayList.class, Integer.class);
        JavaType mappedType = resolver.findTypeMapping(null, type);
        assertEquals(ArrayList.class, mappedType.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType type = JavaType.constructParametricType(List.class, Integer.class);
        JavaType resolvedType = resolver.resolveAbstractType(null, type);
        assertEquals(null, resolvedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingConcreteClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingNonAbstractClass() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Integer.class, LinkedList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSuperclassToItself() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMappingSelfToSelf() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(SimpleAbstractTypeResolver.class, SimpleAbstractTypeResolver.class);
    }

    @Test
    public void testFindTypeMappingWithNestedTypes() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, ArrayList.class);
        JavaType listType = JavaType.constructParametricType(List.class, Integer.class);
        JavaType arrayListType = resolver.findTypeMapping(null, listType);
        assertEquals(ArrayList.class, arrayListType.getRawClass());
    }

    @Test(expected = NullPointerException.class)
    public void testFindTypeMappingWithNullJavaType() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType nullType = null;
        JavaType mappedType = resolver.findTypeMapping(null, nullType);
    }
}