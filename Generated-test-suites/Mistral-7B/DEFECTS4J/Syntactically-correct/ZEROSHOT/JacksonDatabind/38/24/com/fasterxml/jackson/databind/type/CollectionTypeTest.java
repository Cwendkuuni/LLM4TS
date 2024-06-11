package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link CollectionType} class.
 */
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Test valid construction
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elementType);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertFalse(collectionType.isStatic());

        // Test with TypeBindings
        TypeBindings bindings = TypeBindings.empty();
        collectionType = CollectionType.construct(listClass, bindings, null, null, elementType);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(bindings, collectionType.getTypeBindings());

        // Test with superclass and superinterfaces
        Class<?> superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[]{Collection.class};
        collectionType = CollectionType.construct(listClass, null, superClass, superInterfaces, elementType);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(superClass, collectionType.getSuperclass());
        assertArrayEquals(superInterfaces, collectionType.getSuperinterfaces());
    }

    @Test
    public void testConstructDeprecated() {
        // Test deprecated construction with elemType only
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = CollectionType.construct(listClass, elementType);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertFalse(collectionType.isStatic());

        // Test deprecated construction with bindings, superclass and superinterfaces
        TypeBindings bindings = TypeBindings.empty();
        collectionType = new CollectionType(listClass, bindings, _bogusSuperClass(listClass), null, elementType);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elementType, collectionType.getContentType());
        assertNull(collectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(bindings, collectionType.getTypeBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullRawType() {
        CollectionType.construct(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullElementType() {
        CollectionType.construct(List.class, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowDeprecated() {
        Class<List<Integer>> listClass = List.class;
        JavaType collectionType = TypeFactory.defaultInstance().constructType(List.class);
        CollectionType originalCollectionType = new CollectionType(listClass, null, _bogusSuperClass(listClass), null, TypeFactory.defaultInstance().constructType(Integer.class));
        CollectionType newCollectionType = originalCollectionType.withSuperclass(ArrayList.class);
        CollectionType narrowedCollectionType = originalCollectionType._narrow(ArrayList.class);
        assertNotSame(newCollectionType, narrowedCollectionType);
    }

    @Test
    public void testWithContentType() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Double.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertNotSame(collectionType, newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionType.getTypeBindings());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        Object typeHandler = new Object(){};
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertNotSame(collectionType, newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elementType, newCollectionType.getContentType());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionType.getTypeBindings());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        Object contentTypeHandler = new Object(){};
        Object typeHandler = new Object(){};
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        CollectionType newCollectionTypeWithTypeHandler = newCollectionType.withTypeHandler(typeHandler);
        assertNotSame(collectionType, newCollectionTypeWithTypeHandler);
        assertEquals(List.class, newCollectionTypeWithTypeHandler.getRawClass());
        assertEquals(elementType.withTypeHandler(contentTypeHandler), newCollectionTypeWithTypeHandler.getContentType());
        assertEquals(typeHandler, newCollectionTypeWithTypeHandler.getTypeHandler());
        assertNull(newCollectionTypeWithTypeHandler.getValueHandler());
        assertFalse(newCollectionTypeWithTypeHandler.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionTypeWithTypeHandler.getTypeBindings());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        Object valueHandler = new Object(){};
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotSame(collectionType, newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elementType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionType.getTypeBindings());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        Object contentValueHandler = new Object(){};
        Object valueHandler = new Object(){};
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        CollectionType newCollectionTypeWithValueHandler = newCollectionType.withValueHandler(valueHandler);
        assertNotSame(collectionType, newCollectionTypeWithValueHandler);
        assertEquals(List.class, newCollectionTypeWithValueHandler.getRawClass());
        assertEquals(elementType.withValueHandler(contentValueHandler), newCollectionTypeWithValueHandler.getContentType());
        assertEquals(valueHandler, newCollectionTypeWithValueHandler.getValueHandler());
        assertFalse(newCollectionTypeWithValueHandler.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionTypeWithValueHandler.getTypeBindings());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotSame(collectionType, newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(elementType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertTrue(newCollectionType.isStatic());
        assertEquals(collectionType.getTypeBindings(), newCollectionType.getTypeBindings());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefineWithSameRawType() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        CollectionType refinedCollectionType = collectionType.refine(listClass, null, null, null);
        assertSame(collectionType, refinedCollectionType);
    }

    @Test
    public void testRefineWithDifferentRawType() {
        Class<List<Integer>> listClass = List.class;
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(listClass, null, null, null, elementType);
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        CollectionType refinedCollectionType = collectionType.refine(arrayListClass, null, null, null);
        assertNotSame(collectionType, refinedCollectionType);
        assertEquals(ArrayList.class, refinedCollectionType.getRawClass());
        assertEquals(elementType, refinedCollectionType.getContentType());
        assertNull(refinedCollectionType.getTypeHandler());
        assertNull(refinedCollectionType.getValueHandler());
        assertFalse(refinedCollectionType.isStatic());
        assertEquals(collectionType.getTypeBindings(), refinedCollectionType.getTypeBindings());
    }
}