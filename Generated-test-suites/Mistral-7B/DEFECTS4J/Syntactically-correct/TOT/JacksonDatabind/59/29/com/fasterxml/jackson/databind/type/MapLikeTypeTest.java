package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        // Test construction with basic arguments
        JavaType keyType = new SimpleType("String").widen();
        JavaType valueType = new SimpleType("Integer").widen();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertEquals(mapLikeType.getClass(), MapLikeType.class);
        assertEquals(mapLikeType.getKeyType(), keyType);
        assertEquals(mapLikeType.getValueType(), valueType);

        // Test construction with baseType
        JavaType baseType = new SimpleType("HashMap").widen();
        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(upgradedType.getClass(), MapLikeType.class);
        assertEquals(upgradedType.getKeyType(), keyType);
        assertEquals(upgradedType.getValueType(), valueType);
    }

    @Test
    public void testMapLikeTypeMethods() {
        JavaType keyType = new SimpleType("String").widen();
        JavaType valueType = new SimpleType("Integer").widen();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        // Test isContainerType
        assertTrue(mapLikeType.isContainerType());
        assertFalse(new SimpleType("String").widen().isContainerType());

        // Test isMapLikeType
        assertTrue(mapLikeType.isMapLikeType());
        assertFalse(new SimpleType("String").widen().isMapLikeType());

        // Test getKeyType
        assertEquals(mapLikeType.getKeyType(), keyType);

        // Test getContentType
        assertEquals(mapLikeType.getContentType(), valueType);

        // Test hasHandlers
        assertFalse(mapLikeType.hasHandlers());
        JavaType handlerType = new TypeToken<MyHandler<String, Integer>>(){}.getType();
        mapLikeType = mapLikeType.withKeyTypeHandler(new MyHandler<String, Integer>(){}).withContentTypeHandler(new MyHandler<String, Integer>(){});
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("String").widen();
        JavaType valueType = new SimpleType("Integer").widen();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(staticMapLikeType.isMapLikeType());
        assertTrue(staticMapLikeType.isTrueMapType());
        mapLikeType = staticMapLikeType.withStaticTyping();
        assertSame(mapLikeType, staticMapLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType baseType = new TypeToken<List<String>>(){}.getType();
        MapLikeType.construct(List.class, new SimpleType("String").widen(), null);
    }

    // Add more test cases here as needed
}