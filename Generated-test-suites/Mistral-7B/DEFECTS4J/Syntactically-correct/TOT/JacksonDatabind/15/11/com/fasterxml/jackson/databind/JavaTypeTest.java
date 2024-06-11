package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testCreateSimpleType() {
        TypeFactory tf = new TypeFactory();
        JavaType simpleType = tf.constructType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals("java.lang.String", simpleType.toString());
    }

    @Test
    public void testCreateAbstractType() {
        TypeFactory tf = new TypeFactory();
        JavaType abstractType = tf.constructType(AbstractClass.class);
        assertTrue(abstractType.isAbstract());
        assertEquals("com.example.AbstractClass", abstractType.toString());
    }

    @Test
    public void testCreateInterfaceType() {
        TypeFactory tf = new TypeFactory();
        JavaType interfaceType = tf.constructType(InterfaceType.class);
        assertTrue(interfaceType.isInterface());
        assertEquals("com.example.InterfaceType", interfaceType.toString());
    }

    @Test
    public void testCreateEnumType() {
        TypeFactory tf = new TypeFactory();
        JavaType enumType = tf.constructType(MyEnum.class);
        assertTrue(enumType.isEnumType());
        assertEquals("com.example.MyEnum", enumType.toString());
    }

    @Test
    public void testCreateArrayType() {
        TypeFactory tf = new TypeFactory();
        JavaType arrayType = tf.constructType(int[].class);
        assertTrue(arrayType.isArrayType());
        assertEquals("int[]", arrayType.toString());
    }

    @Test
    public void testCreateCollectionType() {
        TypeFactory tf = new TypeFactory();
        JavaType collectionType = tf.constructType(java.util.ArrayList.class);
        assertTrue(collectionType.isCollectionLikeType());
        assertEquals("java.util.ArrayList", collectionType.toString());
    }

    @Test
    public void testCreateMapType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructType(java.util.HashMap.class);
        assertTrue(mapType.isMapLikeType());
        assertEquals("java.util.HashMap", mapType.toString());
    }

    @Test
    public void testCreateUnknownType() {
        TypeFactory tf = new TypeFactory();
        JavaType unknownType = tf.constructType(Object.class);
        assertFalse(unknownType.isContainerType());
        assertEquals("java.lang.Object", unknownType.toString());
    }

    @Test
    public void testCreateTypeWithTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType simpleType = tf.constructType(String.class);
        JavaType withHandler = simpleType.withValueHandler(new MyValueHandler());
        assertNotNull(withHandler.getValueHandler());
        assertEquals(String.class, withHandler.getRawClass());
        assertEquals("java.lang.String", withHandler.toString());
    }

    @Test
    public void testCreateTypeWithContentTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType collectionType = tf.constructType(java.util.ArrayList.class);
        JavaType withContentHandler = collectionType.withContentTypeHandler(new MyContentHandler());
        assertNotNull(withContentHandler.getContentTypeHandler());
        assertEquals("java.util.ArrayList", withContentHandler.toString());
    }

    @Test
    public void testCreateTypeWithTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType simpleType = tf.constructType(String.class);
        JavaType withHandler = simpleType.withTypeHandler(new MyTypeHandler());
        assertNotNull(withHandler.getTypeHandler());
        assertEquals(String.class, withHandler.getRawClass());
        assertEquals("java.lang.String", withHandler.toString());
    }

    @Test
    public void testCreateTypeWithContentValueHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType collectionType = tf.constructType(java.util.ArrayList.class);
        JavaType withContentValueHandler = collectionType.withContentValueHandler(new MyContentValueHandler());
        assertNotNull(withContentValueHandler.getContentValueHandler());
        assertEquals("java.util.ArrayList", withContentValueHandler.toString());
    }

    @Test
    public void testNarrowingBySubclass() {
        TypeFactory tf = new TypeFactory();
        JavaType superclass = tf.constructType(Superclass.class);
        JavaType subclass = superclass.narrowBy(Subclass.class);
        assertTrue(superclass.isAssignableFrom(subclass));
        assertEquals("com.example.Subclass", subclass.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public
    void testNarrowingByIncompatibleType() {
        TypeFactory tf = new TypeFactory();
        JavaType superclass = tf.constructType(Number.class);
        JavaType incompatible = superclass.narrowBy(String.class);
    }

    @Test
    public void testWideningBySuperclass() {
        TypeFactory tf = new TypeFactory();
        JavaType subclass = tf.constructType(Subclass.class);
        JavaType superclass = subclass.widenBy(Superclass.class);
        assertTrue(superclass.isAssignableFrom(subclass));
        assertEquals("com.example.Superclass", superclass.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWideningByIncompatibleType() {
        TypeFactory tf = new TypeFactory();
        JavaType subclass = tf.constructType(Number.class);
        JavaType superclass = subclass.widenBy(String.class);
    }

    @Test
    public void testNarrowingContentsBy() {
        TypeFactory tf = new TypeFactory();
        JavaType container = tf.constructType(Container.class);
        JavaType narrowedContents = container.narrowContentsBy(Content.class);
        assertEquals("com.example.Content", narrowedContents.getRawClass());
        assertEquals("com.example.Container<com.example.Content>", narrowedContents.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowingContentsByIncompatibleType() {
        TypeFactory tf = new TypeFactory();
        JavaType container = tf.constructType(Container.class);
        JavaType incompatible = container.narrowContentsBy(Number.class);
    }

    @Test
    public void testWideningContentsBy() {
        TypeFactory tf = new TypeFactory();
        JavaType container = tf.constructType(Container.class);
        JavaType widenedContents = container.widenContentsBy(Number.class);
        assertTrue(Number.class.isAssignableFrom(widenedContents.getRawClass()));
        assertEquals("com.example.Container<java.lang.Number>", widenedContents.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWideningContentsByIncompatibleType() {
        TypeFactory tf = new TypeFactory();
        JavaType container = tf.constructType(Container.class);
        JavaType incompatible = container.widenContentsBy(String.class);
    }

    private static class MyValueHandler implements JsonValueHandler {
        // Stub implementation
    }

    private static class MyContentHandler implements JsonSerializer, JsonDeserializer {
        // Stub implementation
    }

    private static class MyContentValueHandler implements JsonValueHandler {
        // Stub implementation
    }

    private static class MyTypeHandler implements JsonSerializer, JsonDeserializer {
        // Stub implementation
    }

    private static class MyEnum {}

    private static class AbstractClass { }

    private static class InterfaceType implements java.io.Serializable { }

    private static class Superclass { }

    private static class Subclass extends Superclass { }

    private static class Container<T> {
        T content;
    }

    private static class Content { }
}