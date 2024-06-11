package com.fasterxml.jackson.databind.module.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleAbstractTypeResolverTest {

    private SimpleAbstractTypeResolver resolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(Collection.class, LinkedList.class);
        resolver.addMapping(List.class, LinkedList.class);
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testFindTypeMapping_CollectionToLinkedList() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(new TypeReference<List<Integer>>() {});
        JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_ListToLinkedList() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNotNull(mappedType);
        assertEquals(LinkedList.class, mappedType.getRawClass());
    }

    @Test
    public void testFindTypeMapping_UnknownType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mappedType = resolver.findTypeMapping(new DeserializationConfig(), javaType);
        assertNull(mappedType);
    }

    @Test
    public void testResolveAbstractType_UnknownType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mappedType = resolver.resolveAbstractType(new DeserializationConfig(), javaType);
        assertNull(mappedType);
    }
}