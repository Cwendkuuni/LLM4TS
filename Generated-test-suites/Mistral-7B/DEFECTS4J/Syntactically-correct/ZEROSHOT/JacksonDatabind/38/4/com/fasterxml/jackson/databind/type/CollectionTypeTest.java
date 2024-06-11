package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = CollectionType.construct(listType, bindings, superClass, superInts, elemT);
        assertEquals(List.class, collectionType.getRawClass());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        JavaType elemT = String.class;

        CollectionType collectionType = CollectionType.construct(listType, elemT);
        assertEquals(List.class, collectionType.getRawClass());
    }

    @Test
    public void testRefine() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        JavaType refinedType = collectionType.refine(ArrayList.class, bindings, superClass, superInts);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        JavaType refinedType = collectionType.withContentType(Integer.TYPE);
        assertNotSame(elemT, refinedType.getContentType());
        assertEquals(Integer.TYPE, refinedType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        Object contentTypeHandler = new Object();
        JavaType refinedType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(refinedType.getContentTypeHandler());
        assertEquals(contentTypeHandler, refinedType.getContentTypeHandler());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        Object typeHandler = new Object();
        JavaType refinedType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(refinedType.getTypeHandler());
        assertEquals(typeHandler, refinedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        Object valueHandler = new Object();
        JavaType refinedType = collectionType.withValueHandler(valueHandler);
        assertNotNull(refinedType.getValueHandler());
        assertEquals(valueHandler, refinedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        Object contentValueHandler = new Object();
        JavaType refinedType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(refinedType.getContentValueHandler());
        assertEquals(contentValueHandler, refinedType.getContentValueHandler());
    }

    @Test
    public void testNarrow() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        Class<ArrayList<Integer>> arrayListIntType = ArrayList.class;

        CollectionType collectionType = new CollectionType(listType, null);
        CollectionType narrowedType = collectionType.narrow(arrayListIntType);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowUnsupported() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        Class<HashSet<Integer>> hashSetIntType = HashSet.class;

        CollectionType collectionType = new CollectionType(listType, null);
        CollectionType narrowedType = collectionType.narrow(hashSetIntType);
    }

    @Test
    public void testToString() {
        Class<List<String>> listType = new ArrayList<String>().getClass();
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listType, bindings);
        String stringRepresentation = collectionType.toString();
        assertTrue(stringRepresentation.startsWith("[collection type"));
    }
}