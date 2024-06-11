package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionLikeTypeTest {

    @Test
    public void testCollectionLikeTypeConstruction() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(int.class));

        assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
        assertEquals(javaType.getRawClass(), collectionLikeType._class);
        assertEquals(TypeBindings.emptyBindings(), collectionLikeType._bindings);
        assertEquals(null, collectionLikeType._superClass);
        assertEquals(null, collectionLikeType._superInterfaces);
        assertEquals(JavaType.constructType(int.class), collectionLikeType._elementType);
        assertEquals(null, collectionLikeType._valueHandler);
        assertEquals(null, collectionLikeType._typeHandler);
        assertFalse(collectionLikeType._asStatic);

        JavaType javaTypeWithTypeVariable = TypeFactory.defaultInstance()
                .constructType(Collection.class, Integer[].class);
        TypeVariable<?> typeVariable = javaTypeWithTypeVariable.getActualTypeArguments()[0];
        CollectionLikeType collectionLikeTypeWithTypeVariable = CollectionLikeType.construct(javaTypeWithTypeVariable.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Integer.class));
        assertEquals(Integer.TYPE, ((TypeVariable<?>) collectionLikeTypeWithTypeVariable._elementType.getTypeParameters()[0]).getType());
    }

    @Test
    public void testCollectionLikeTypeUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));
        JavaType upgradedCollectionLikeType = CollectionLikeType.upgradeFrom(collectionLikeType, Integer.TYPE);

        assertEquals(CollectionLikeType.class, upgradedCollectionLikeType.getClass());
        assertEquals(javaType.getRawClass(), upgradedCollectionLikeType._class);
        assertEquals(TypeBindings.emptyBindings(), upgradedCollectionLikeType._bindings);
        assertEquals(null, upgradedCollectionLikeType._superClass);
        assertEquals(null, upgradedCollectionLikeType._superInterfaces);
        assertEquals(JavaType.constructType(Integer.TYPE), upgradedCollectionLikeType._elementType);
        assertEquals(null, upgradedCollectionLikeType._valueHandler);
        assertEquals(null, upgradedCollectionLikeType._typeHandler);
        assertFalse(upgradedCollectionLikeType._asStatic);
    }

    @Test
    public void testCollectionLikeTypeGetters() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        assertEquals(JavaType.constructType(String.class), collectionLikeType.getContentType());
        assertEquals(JavaType.constructType(String.class).getValueHandler(), collectionLikeType.getContentValueHandler());
        assertEquals(JavaType.constructType(String.class).getTypeHandler(), collectionLikeType.getContentTypeHandler());
        assertTrue(collectionLikeType.hasHandlers());

        JavaType javaTypeWithValueHandler = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class, TypeDeserializer.class);
        CollectionLikeType collectionLikeTypeWithValueHandler = CollectionLikeType.construct(javaTypeWithValueHandler.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class), null, null, false);
        assertEquals(JavaType.constructType(String.class), collectionLikeTypeWithValueHandler.getContentType());
        assertNotNull(collectionLikeTypeWithValueHandler.getContentValueHandler());
        assertNull(collectionLikeTypeWithValueHandler.getContentTypeHandler());
        assertTrue(collectionLikeTypeWithValueHandler.hasHandlers());

        JavaType javaTypeWithTypeHandler = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class, TypeFactory.DefaultSerializers.StringSerializer.class);
        CollectionLikeType collectionLikeTypeWithTypeHandler = CollectionLikeType.construct(javaTypeWithTypeHandler.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class), null, javaTypeWithTypeHandler.getTypeHandler(), false);
        assertEquals(JavaType.constructType(String.class), collectionLikeTypeWithTypeHandler.getContentType());
        assertEquals(JavaType.constructType(String.class).getValueHandler(), collectionLikeTypeWithTypeHandler.getContentValueHandler());
        assertEquals(javaTypeWithTypeHandler.getTypeHandler(), collectionLikeTypeWithTypeHandler.getContentTypeHandler());
        assertTrue(collectionLikeTypeWithTypeHandler.hasHandlers());
    }

    @Test
    public void testCollectionLikeTypeIsCollectionLikeTypeAndIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        assertTrue(collectionLikeType.isCollectionLikeType());
        assertTrue(collectionLikeType.isContainerType());

        JavaType javaTypeWithStaticTyping = TypeFactory.defaultInstance().constructType(ArrayList.class, String[].class);
        CollectionLikeType collectionLikeTypeWithStaticTyping = CollectionLikeType.construct(javaTypeWithStaticTyping.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String[].class));
        assertTrue(collectionLikeTypeWithStaticTyping.isCollectionLikeType());
        assertTrue(collectionLikeTypeWithStaticTyping.isContainerType());

        JavaType javaTypeWithTypeVariable = TypeFactory.defaultInstance().constructType(Collection.class, Integer[].class);
        CollectionLikeType collectionLikeTypeWithTypeVariable = CollectionLikeType.construct(javaTypeWithTypeVariable.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Integer.class));
        assertTrue(collectionLikeTypeWithTypeVariable.isCollectionLikeType());
        assertTrue(collectionLikeTypeWithTypeVariable.isContainerType());

        JavaType javaTypeNotCollection = TypeFactory.defaultInstance().constructType(String[].class);
        TypeBase typeBaseNotCollection = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(CollectionLikeType.class.isAssignableFrom(typeBaseNotCollection.getClass()));
        assertFalse(typeBaseNotCollection.isCollectionLikeType());
        assertFalse(typeBaseNotCollection.isContainerType());
    }

    @Test
    public void testCollectionLikeTypeIsTrueCollectionType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType javaTypeWithTypeVariable = TypeFactory.defaultInstance().constructType(Collection.class, Integer[].class);
        CollectionLikeType collectionLikeTypeWithTypeVariable = CollectionLikeType.construct(javaTypeWithTypeVariable.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Integer.class));

        assertTrue(collectionLikeTypeWithTypeVariable.isTrueCollectionType());

        JavaType javaTypeNotCollection = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(CollectionLikeType.class.isAssignableFrom(javaTypeNotCollection.getRawClass()));
        assertFalse(CollectionLikeType.isTrueCollectionType(javaTypeNotCollection));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        CollectionLikeType.upgradeFrom(collectionLikeType, String[].class);
    }

    @Test
    public void testCollectionLikeTypeRefine() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        JavaType javaTypeRefined = TypeFactory.defaultInstance().constructType(LinkedList.class, String.class);
        CollectionLikeType collectionLikeTypeRefined = collectionLikeType.refine(javaTypeRefined.getRawClass(), TypeBindings.emptyBindings(), null, null);

        assertEquals(LinkedList.class, collectionLikeTypeRefined._class);
        assertEquals(TypeBindings.emptyBindings(), collectionLikeTypeRefined._bindings);
        assertEquals(null, collectionLikeTypeRefined._superClass);
        assertEquals(null, collectionLikeTypeRefined._superInterfaces);
        assertEquals(JavaType.constructType(String.class), collectionLikeTypeRefined._elementType);
        assertEquals(null, collectionLikeTypeRefined._valueHandler);
        assertEquals(null, collectionLikeTypeRefined._typeHandler);
        assertFalse(collectionLikeTypeRefined._asStatic);
    }

    @Test
    public void testCollectionLikeTypeEquals() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertTrue(collectionLikeType1.equals(collectionLikeType1));

        CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));
        assertFalse(collectionLikeType1.equals(collectionLikeType3));

        JavaType javaTypeDifferentElementType = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        CollectionLikeType collectionLikeType4 = CollectionLikeType.construct(javaTypeDifferentElementType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Integer.class));
        assertFalse(collectionLikeType1.equals(collectionLikeType4));
    }

    @Test
    public void testCollectionLikeTypeCanonicalName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.String]", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testCollectionLikeTypeGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        StringBuilder output = new StringBuilder();
        collectionLikeType.getGenericSignature(output);

        assertEquals("[collection-like type; class java.util.ArrayList<T>, contains java.lang.String]", output.toString());
    }

    @Test
    public void testCollectionLikeTypeErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        StringBuilder output = new StringBuilder();
        collectionLikeType.getErasedSignature(output);

        assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.Object]", output.toString());
    }

    @Test
    public void testCollectionLikeTypeIsAssignableFrom() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(javaType1.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        JavaType javaType2 = TypeFactory.defaultInstance().constructType(LinkedList.class, String.class);
        CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(javaType2.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        JavaType javaType3 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        TypeBase typeBase3 = CollectionLikeType.construct(javaType3.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Integer.class));

        JavaType javaType4 = TypeFactory.defaultInstance().constructType(String[].class);
        TypeBase typeBase4 = CollectionLikeType.construct(javaType4.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(String.class));

        JavaType javaType5 = TypeFactory.defaultInstance().constructType(ArrayList.class, Double.class);
        CollectionLikeType collectionLikeType5 = CollectionLikeType.construct(javaType5.getRawClass(), TypeBindings.emptyBindings(), null, null, JavaType.constructType(Double.class));

        JavaType javaType6 = TypeFactory.defaultInstance().constructType(LinkedHashSet.class, String.class);
        assertFalse(collectionLikeType1.isAssignableFrom(javaType6));

        JavaType javaType7 = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, String.class);
        assertFalse(collectionLikeType1.isAssignableFrom(javaType7));

        JavaType javaType8 = TypeFactory.defaultInstance().constructType(ArrayList.class, String[].class);
        assertFalse(collectionLikeType1.isAssignableFrom(javaType8));

        JavaType javaType9 = TypeFactory.defaultInstance().constructType(Collection.class, Integer[].class);
        assertFalse(collectionLikeType1.isAssignableFrom(javaType9));

        JavaType javaType10 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        assertTrue(collectionLikeType1.isAssignableFrom(javaType10));

        JavaType javaType11 = TypeFactory.defaultInstance().constructType(LinkedList.class, String.class);
        assertTrue(collectionLikeType1.isAssignableFrom(javaType11));

        JavaType javaType12 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        assertTrue(collectionLikeType2.isAssignableFrom(javaType12));

        JavaType javaType13 = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        assertTrue(typeBase3.isAssignableFrom(javaType13));

        JavaType javaType14 = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        assertTrue(collectionLikeType5.isAssignableFrom(javaType14));
    }
}