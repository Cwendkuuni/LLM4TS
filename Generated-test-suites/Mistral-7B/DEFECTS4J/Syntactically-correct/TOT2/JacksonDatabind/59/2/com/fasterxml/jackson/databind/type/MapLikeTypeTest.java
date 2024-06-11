package com.fasterxml.jackson.databind.type; // import statements go here

public class MapLikeTypeTest {

    

    @org.junit.Test
    public void testMapLikeTypeCreation() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = BasicTypeFactory.constructType(String.class);
        JavaType valueType = BasicTypeFactory.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(JavaType.construct(mapType), keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @org.junit.Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = BasicTypeFactory.constructType(String.class);
        JavaType valueType = BasicTypeFactory.constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidType() {
        JavaType baseType = BasicTypeFactory.constructType(String.class);
        JavaType keyType = BasicTypeFactory.constructType(String.class);
        JavaType valueType = BasicTypeFactory.constructType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    
}