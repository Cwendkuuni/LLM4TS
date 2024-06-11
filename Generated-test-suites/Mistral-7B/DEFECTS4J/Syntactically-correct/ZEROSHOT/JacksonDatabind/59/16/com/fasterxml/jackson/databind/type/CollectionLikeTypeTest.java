package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Collection.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.emptyBindings();
        TypeBase base = new CollectionLikeType(javaType.getRawClass(), bindings);
        CollectionLikeType collectionLikeType = (CollectionLikeType) base;

        assertEquals(javaType.getRawClass(), collectionLikeType.getClass());
        assertEquals(Integer.TYPE, collectionLikeType.getContentType().getContentType());
    }

    @Test
    public void testCollectionLikeTypeUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        JavaType elementType = TypeFactory.defaultInstance().constructType(Integer.TYPE);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, elementType);

        assertEquals(javaType, collectionLikeType.getSuperClass());
        assertEquals(elementType, collectionLikeType.getContentType());
    }

    @Test
    public void testCollectionLikeTypeIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());

        assertTrue(collectionLikeType.isContainerType());
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings());

        assertTrue(collectionLikeType.isTrueCollectionType());
        assertFalse(collectionLikeType.isTrueCollectionType().getClass().isInstance(new ArrayList<>()[0].getClass()));
    }

    @Test
    public void testCollectionLikeTypeEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawClass(), bindings);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawClass(), bindings);

        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertTrue(collectionLikeType2.equals(collectionLikeType1));

        TypeBindings bindings2 = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(javaType1.getRawClass(), bindings2);

        assertFalse(collectionLikeType1.equals(collectionLikeType3));
        assertFalse(collectionLikeType3.equals(collectionLikeType1));

        JavaType javaType3 = TypeFactory.defaultInstance().constructType(LinkedList.class, Integer.TYPE);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(javaType1.getRawClass(), bindings);

        assertFalse(collectionLikeType1.equals(collectionLikeType4));
        assertFalse(collectionLikeType4.equals(collectionLikeType1));
    }

    @Test
    public void testCollectionLikeTypeHashCode() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType1.getRawClass(), bindings);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType2.getRawClass(), bindings);

        int hashCode1 = collectionLikeType1.hashCode();
        int hashCode2 = collectionLikeType2.hashCode();

        assertTrue(hashCode1 == hashCode2);

        TypeBindings bindings2 = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(javaType1.getRawClass(), bindings2);

        int hashCode3 = collectionLikeType3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        JavaType javaType3 = TypeFactory.defaultInstance().constructType(LinkedList.class, Integer.TYPE);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(javaType1.getRawClass(), bindings);

        int hashCode4 = collectionLikeType4.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testCollectionLikeTypeToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        String toString = collectionLikeType.toString();

        assertTrue(toString.contains("[collection-like type]"));
        assertTrue(toString.contains("class "+javaType.getRawClass().getName()));
        assertTrue(toString.contains("contains "+javaType.getContentType()));
    }

    @Test
    public void testCollectionLikeTypeGetCanonicalName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        String canonicalName = collectionLikeType.buildCanonicalName();

        assertTrue(canonicalName.contains(ArrayList.class.getName()));
        assertTrue(canonicalName.contains("Integer"));
    }

    @Test
    public void testCollectionLikeTypeGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        StringBuilder sb = new StringBuilder();
        collectionLikeType.getGenericSignature(sb);

        assertTrue(sb.toString().contains("ArrayList<Integer>"));
    }

    @Test
    public void testCollectionLikeTypeGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        StringBuilder sb = new StringBuilder();
        collectionLikeType.getErasedSignature(sb);

        assertTrue(sb.toString().contains("Collection<T>"));
    }

    @Test
    public void testCollectionLikeTypeIsInstance() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        assertTrue(CollectionLikeType.class.isInstance(collectionLikeType));
    }

    @Test
    public void testCollectionLikeTypeGetSuperInterfaces() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        JavaType[] superInterfaces = collectionLikeType.getSuperInterfaces();

        assertTrue(Arrays.stream(superInterfaces).anyMatch(i -> i.getClass().equals(Collection.class)));
    }

    @Test
    public void testCollectionLikeTypeWithContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String.class);

        CollectionLikeType collectionLikeType2 = collectionLikeType.withContentType(javaType2);

        assertEquals(javaType2, collectionLikeType2.getContentType());
    }

    @Test
    public void testCollectionLikeTypeWithTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        Object handler = new Object();
        CollectionLikeType collectionLikeType2 = collectionLikeType.withTypeHandler(handler);

        assertEquals(handler, collectionLikeType2.getTypeHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentTypeHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String.class);

        CollectionLikeType collectionLikeType2 = collectionLikeType.withContentTypeHandler(handler);

        assertEquals(javaType2, collectionLikeType2.getContentType().getTypeHandler(handler));
    }

    @Test
    public void testCollectionLikeTypeWithValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        Object handler = new Object();
        CollectionLikeType collectionLikeType2 = collectionLikeType.withValueHandler(handler);

        assertEquals(handler, collectionLikeType2.getValueHandler());
    }

    @Test
    public void testCollectionLikeTypeWithContentValueHandler() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);
        Object handler = new Object();
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String.class);

        CollectionLikeType collectionLikeType2 = collectionLikeType.withContentValueHandler(handler);

        assertEquals(javaType2, collectionLikeType2.getContentType().getValueHandler(handler));
    }

    @Test
    public void testCollectionLikeTypeWithStaticTyping() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        CollectionLikeType collectionLikeType2 = collectionLikeType.withStaticTyping();

        assertTrue(collectionLikeType2.isStaticTyping());
    }

    @Test
    public void testCollectionLikeTypeRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        JavaType javaType2 = TypeFactory.defaultInstance().constructType(ArrayList.class, Double.TYPE);

        CollectionLikeType collectionLikeType2 = collectionLikeType.refine(javaType2.getRawClass(), bindings, null, null);

        assertEquals(javaType2, collectionLikeType2.getSuperClass());
        assertEquals(Double.TYPE, collectionLikeType2.getContentType().getContentType());
    }

    @Test
    public void testCollectionLikeTypeGetContentTypeClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.TYPE);

        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), bindings);

        Class<?> contentTypeClass = collectionLikeType.getContentType().getContentType().getType();

        assertEquals(Integer.TYPE, contentTypeClass);
    }
}