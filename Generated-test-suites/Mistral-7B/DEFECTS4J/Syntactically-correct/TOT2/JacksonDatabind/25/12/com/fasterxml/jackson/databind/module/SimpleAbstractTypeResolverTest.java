package com.fasterxml.jackson.databind.module;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;

public class SimpleAbstractTypeResolverTest {
    

    @Test
    public void addMappingTest1() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        assertSame(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
    }

    @Test
    public void findTypeMappingTest1() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, LinkedList.class);
        JavaType javaType = JavaType.construct(List.class, Integer.class);
        JavaType expected = JavaType.construct(LinkedList.class, Integer.class);
        JavaType actual = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertSame(expected, actual);
    }

    @Test
    public void resolveAbstractTypeTest1() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(List.class);
        assertNull(resolver.resolveAbstractType(new DeserializationConfig(), javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMappingTest2() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(List.class, List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMappingTest3() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ArrayList.class, LinkedList.class);
    }

    @Test
    public void addMappingTest4() {
        SimpleAbstractTypeResolver resolver1 = new SimpleAbstractTypeResolver();
        resolver1.addMapping(List.class, ArrayList.class);
        SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
        resolver2.addMapping(ArrayList.class, LinkedList.class);
        resolver1.addMapping(resolver2);
        resolver1.addMapping(List.class, LinkedList.class);
        assertSame(LinkedList.class, resolver1._mappings.get(new ClassKey(List.class)));
    }

    @Test(expected = NullPointerException.class)
    public void findTypeMappingTest2() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;
        resolver.findTypeMapping(new DeserializationConfig(), javaType);
    }

    @Test
    public void findTypeMappingTest3() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(ArrayList.class);
        assertNull(resolver.findTypeMapping(new DeserializationConfig(), javaType));
    }

    @Test(expected = NullPointerException.class)
    public void findTypeMappingTest4() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        SimpleAbstractTypeResolver resolver2 = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(List.class, new ClassKey(ArrayList.class));
        assertNull(resolver.findTypeMapping(new DeserializationConfig(), javaType));
    }

    @Test(expected = NullPointerException.class)
    public void resolveAbstractTypeTest2() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = null;
        resolver.resolveAbstractType(new DeserializationConfig(), javaType);
    }

    @Test
    public void resolveAbstractTypeTest3() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        JavaType javaType = JavaType.construct(ArrayList.class);
        assertNull(resolver.resolveAbstractType(new DeserializationConfig(), javaType));
    }

    
}