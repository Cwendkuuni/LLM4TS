package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = CollectionLikeType.construct(listClass, elemType, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, elemType);
        assertEquals(CollectionType.class, collectionType.getClass());
        assertEquals(listClass, collectionType.getType());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(elemType, collectionType.getContentType());
        assertFalse(collectionType.isStatic());

        JavaType setType = HashSet.class;
        JavaType[] superInterfaces = {HashSet.class};
        CollectionType setCollectionType = CollectionType.construct(setType, null, null, superInterfaces, elemType);
        assertEquals(CollectionType.class, setCollectionType.getClass());
        assertEquals(setType, setCollectionType.getType());
        assertEquals(elemType, setCollectionType.getContentType());
        assertNull(setCollectionType.getBindings());
        assertNull(setCollectionType.getSuperClass());
        assertTrue(setCollectionType.isStatic());
    }

    @Test
    public void testConstructWithValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        TypeBindings bindings = new TypeBindings();
        Object valueHandler = new Object() {};
        JavaType superClass = CollectionLikeType.construct(listClass, elemType, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, elemType, valueHandler, null, false);
        assertEquals(CollectionType.class, collectionType.getClass());
        assertEquals(listClass, collectionType.getType());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(elemType, collectionType.getContentType());
        assertFalse(collectionType.isStatic());
        assertEquals(valueHandler, collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());

        JavaType setType = HashSet.class;
        JavaType[] superInterfaces = {HashSet.class};
        CollectionType setCollectionType = CollectionType.construct(setType, null, null, superInterfaces, elemType, null, new Object() {}, false);
        assertEquals(CollectionType.class, setCollectionType.getClass());
        assertEquals(setType, setCollectionType.getType());
        assertEquals(elemType, setCollectionType.getContentType());
        assertNull(setCollectionType.getBindings());
        assertNull(setCollectionType.getSuperClass());
        assertTrue(setCollectionType.isStatic());
        assertNull(setCollectionType.getValueHandler());
        assertNotNull(setCollectionType.getTypeHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        TypeBindings bindings = new TypeBindings();
        Object typeHandler = new Object() {};
        JavaType superClass = CollectionLikeType.construct(listClass, elemType, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, elemType, null, typeHandler, false);
        assertEquals(CollectionType.class, collectionType.getClass());
        assertEquals(listClass, collectionType.getType());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(elemType, collectionType.getContentType());
        assertFalse(collectionType.isStatic());
        assertNull(collectionType.getValueHandler());
        assertEquals(typeHandler, collectionType.getTypeHandler());

        JavaType setType = HashSet.class;
        JavaType[] superInterfaces = {HashSet.class};
        CollectionType setCollectionType = CollectionType.construct(setType, null, null, superInterfaces, elemType, null, null, false);
        assertEquals(CollectionType.class, setCollectionType.getClass());
        assertEquals(setType, setCollectionType.getType());
        assertEquals(elemType, setCollectionType.getContentType());
        assertNull(setCollectionType.getBindings());
        assertNull(setCollectionType.getSuperClass());
        assertTrue(setCollectionType.isStatic());
        assertNull(setCollectionType.getValueHandler());
        assertNull(setCollectionType.getTypeHandler());
    }

    @Test
    public void testConstructWithContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        TypeBindings bindings = new TypeBindings();
        Object contentTypeHandler = new Object() {};
        JavaType superClass = CollectionLikeType.construct(listClass, elemType, bindings);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, null, elemType, null, null, false).withContentTypeHandler(contentTypeHandler);
        assertEquals(CollectionType.class, collectionType.getClass());
        assertEquals(listClass, collectionType.getType());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(bindings, collectionType.getBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertEquals(elemType, collectionType.getContentType());
        assertFalse(collectionType.isStatic());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertEquals(contentTypeHandler, collectionType.getContentTypeHandler());

        JavaType setType = HashSet.class;
        JavaType[] superInterfaces = {HashSet.class};
        CollectionType setCollectionType = CollectionType.construct(setType, null, null, superInterfaces, elemType, null, null, false).withContentTypeHandler(contentTypeHandler);
        assertEquals(CollectionType.class, setCollectionType.getClass());
        assertEquals(setType, setCollectionType.getType());
        assertEquals(elemType, setCollectionType.getContentType());
        assertNull(setCollectionType.getBindings());
        assertNull(setCollectionType.getSuperClass());
        assertTrue(setCollectionType.isStatic());
        assertNull(setCollectionType.getValueHandler());
        assertNull(setCollectionType.getTypeHandler());
        assertEquals(contentTypeHandler, setCollectionType.getContentTypeHandler());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        assertEquals(CollectionType.class, collectionType.getClass());
        assertEquals(listClass, collectionType.getType());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertFalse(collectionType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullClass() {
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType.construct(null, elemType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullElementType() {
        Class<?> listClass = ArrayList.class;
        CollectionType.construct(listClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType, null, null, null, null, null, false);
        collectionType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType, null, null, null, null, null, false);
        collectionType.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullContentValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType, null, null, null, null, null, false);
        collectionType.withContentValueHandler(null);
    }

    @Test
    public void testNarrow() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        CollectionType subCollectionType = CollectionType.construct(LinkedList.class, elemType);
        JavaType narrowedType = collectionType.narrow(LinkedList.class);
        assertEquals(subCollectionType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowNullType() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, BasicTypeFactory.intType());
        collectionType.narrow(null);
    }

    @Test
    public void testWithContentType() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        JavaType contentType = BasicTypeFactory.longType();
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertEquals(CollectionType.class, newCollectionType.getClass());
        assertEquals(listClass, newCollectionType.getType());
        assertEquals(contentType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertEquals(CollectionType.class, newCollectionType.getClass());
        assertEquals(listClass, newCollectionType.getType());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getBindings());
        assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = BasicTypeFactory.intType();
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        JavaType newListType = ArrayList.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType newSuperClass = CollectionLikeType.construct(newListType, elemType, newBindings);
        CollectionType newCollectionType = collectionType.refine(newListType, newBindings, newSuperClass, null);
        assertEquals(CollectionType.class, newCollectionType.getClass());
        assertEquals(newListType, newCollectionType.getType());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(newBindings, newCollectionType.getBindings());
        assertEquals(newSuperClass, newCollectionType.getSuperClass());
        assertFalse(newCollectionType.isStatic());
    }
}