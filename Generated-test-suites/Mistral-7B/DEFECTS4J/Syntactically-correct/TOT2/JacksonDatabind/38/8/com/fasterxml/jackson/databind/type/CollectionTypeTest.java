package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    @Test
    public void testConstruct() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings,
                superClass, superInts, elemT);

        assertEquals("java.util.ArrayList", collectionType.getRawClass().getName());
        assertEquals(Collection.class, collectionType.getSuperClass());
        assertEquals(Integer.TYPE, collectionType.getElementType());
    }

    @Test
    public void testConstructWithOneArgument() {
        Class<?> listClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, elemT);

        assertEquals("java.util.ArrayList", collectionType.getRawClass().getName());
        assertEquals(Collection.class, collectionType.getSuperClass());
        assertEquals(Integer.TYPE, collectionType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        JavaType elemT = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(null, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullElementType() {
        Class<?> listClass = ArrayList.class;
        CollectionType collectionType = CollectionType.construct(listClass, null);
    }

    @Test
    public void testConstructWithInvalidClass() {
        Class<?> invalidClass = Object.class;
        JavaType elemT = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(invalidClass, elemT);
        assertFalse(invalidClass.isInstance(collectionType.getRawClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNegativeSize() {
        Class<?> listClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(listClass, elemT, -1);
    }

    @Test
    public void testToString() {
        Class<?> listClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(listClass, elemT);
        String toString = collectionType.toString();
        assertTrue(toString.contains("collection type"));
        assertTrue(toString.contains("java.util.ArrayList"));
        assertTrue(toString.contains("contains " + Integer.TYPE.getName()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstruct() {
        Class<?> listClass = ArrayList.class;
        JavaType elemT = Integer.TYPE;
        CollectionType collectionType = CollectionType.construct(listClass, elemT);
        CollectionType collectionType2 = new CollectionType(listClass, elemT);
    }

    @Test
    public void testRefine() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = Integer.TYPE;

        CollectionType collectionType = CollectionType.construct(listClass, bindings,
                superClass, superInterfaces, elemT);

        JavaType refinedType = collectionType.refine(HashSet.class, bindings, null, null);
        assertEquals(HashSet.class, refinedType.getRawClass());
    }
}