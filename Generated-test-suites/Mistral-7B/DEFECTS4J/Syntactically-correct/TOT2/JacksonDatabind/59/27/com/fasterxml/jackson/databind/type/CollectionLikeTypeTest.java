package com.fasterxml.jackson.databind.type; // import statements go here

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        // Basic testing
        JavaType elemType = new SimpleType(List.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, null, null, null, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getContentType());

        // Test with TypeBindings
        TypeVariable<?>[] typeParams = ArrayList.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(ArrayList.class, elemType);
        collectionLikeType = CollectionLikeType.construct(ArrayList.class, bindings, null, null, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getContentType());

        // Test with deprecated constructor
        collectionLikeType = CollectionLikeType.construct(ArrayList.class, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(ArrayList.class);
        JavaType elemType = new SimpleType(String.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(elemType, collectionLikeType.getContentType());
    }
}