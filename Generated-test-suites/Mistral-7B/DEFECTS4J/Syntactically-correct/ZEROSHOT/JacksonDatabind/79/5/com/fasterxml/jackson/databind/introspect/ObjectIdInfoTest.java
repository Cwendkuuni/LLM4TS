package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import org.junit.Test;
import org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> objectIdResolver = MyCustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(objectIdGenerator, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> objectIdResolver = MyCustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithResolver() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;
        Class<MyCustomObjectIdResolver> objectIdResolver = MyCustomObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, objectIdGenerator, null, objectIdResolver);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false, resolverType=class com.example.MyCustomObjectIdResolver", objectIdInfo.toString());
        assertEquals(objectIdResolver, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoWithDeprecatedConstructors() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<MyCustomObjectIdGenerator> objectIdGenerator = MyCustomObjectIdGenerator.class;

        ObjectIdInfo objectIdInfoWithName = new ObjectIdInfo(propertyName.getName(), scope, objectIdGenerator);
        ObjectIdInfo objectIdInfoWithNameAndResolver = new ObjectIdInfo(propertyName.getName(), scope, objectIdGenerator, null, SimpleObjectIdResolver.class);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfoWithName.toString());
        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false, resolverType=class com.fasterxml.jackson.databind.introspect.SimpleObjectIdResolver", objectIdInfoWithNameAndResolver.toString());
    }

    @Test
    public void testObjectIdInfoAnnotations() {
        JsonIdentityInfo jsonIdentityInfo = mock(JsonIdentityInfo.class);
        when(jsonIdentityInfo.property("testProperty")).thenReturn(new PropertyName("testProperty"));
        when(jsonIdentityInfo.scope()).thenReturn(String.class);
        when(jsonIdentityInfo.generator(ObjectIdGenerator.class)).thenReturn(MyCustomObjectIdGenerator.class);
        when(jsonIdentityInfo.resolver(ObjectIdResolver.class)).thenReturn(MyCustomObjectIdResolver.class);

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(jsonIdentityInfo);

        assertEquals("ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.MyCustomObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertEquals("testProperty", objectIdInfo.getPropertyName().getName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyCustomObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyCustomObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    static class MyCustomObjectIdGenerator implements ObjectIdGenerator<Object> {
    }

    static class MyCustomObjectIdResolver implements ObjectIdResolver {
    }
}