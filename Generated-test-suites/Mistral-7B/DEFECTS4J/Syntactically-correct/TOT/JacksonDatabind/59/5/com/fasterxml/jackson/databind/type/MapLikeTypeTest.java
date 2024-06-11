import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializerSupplier;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test constructing MapLikeType instances with various arguments

        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);

        JavaType baseType = TypeFactory.defaultInstance().constructType(MapLikeType.class);
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType2 = MapLikeType.upgradeFrom(baseType, keyType1, valueType1);

        JavaType keyT, valueT;
        JavaType mapType2 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType mapLikeType3 = new MapLikeType(mapType2, TypeBindings.emptyBindings(), _bogusSuperClass(mapType2), null, keyT = TypeFactory.defaultInstance().constructType(String.class), valueT = TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(keyType, keyT);
        assertEquals(valueType, valueT);

        MapLikeType mapLikeType4 = mapLikeType1.withKeyType(keyType1);
        MapLikeType mapLikeType5 = mapLikeType1.withContentType(valueType1);
        MapLikeType mapLikeType6 = mapLikeType1.withTypeHandler(new TypeDeserializer.FromString());
        MapLikeType mapLikeType7 = mapLikeType1.withContentTypeHandler(new TypeSerializerSupplier.Std() {
            @Override
            public JavaType getTypeSerializer(TypeSerializerSupplier tcs, JavaType typeHint) {
                return TypeFactory.defaultInstance().constructType(Integer[].class);
            }
        });
        MapLikeType mapLikeType8 = mapLikeType1.withValueHandler(new Object() {});
        MapLikeType mapLikeType9 = mapLikeType1.withContentValueHandler(new Object() {});
        MapLikeType mapLikeType10 = mapLikeType1.withStaticTyping();
        MapLikeType mapLikeType11 = mapLikeType1.refine(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null);
    }

    @Test
    public void testMapLikeTypeMethods() {
        // Test various methods on MapLikeType instance

        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), mapLikeType.getKeyType());
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.hasHandlers());
        assertFalse(mapLikeType.isTrueMapType());

        assertNotNull(mapLikeType.getErasedSignature());
        assertNotNull(mapLikeType.getGenericSignature());
        assertNotNull(mapLikeType.toString());

        assertTrue(mapLikeType.equals(mapLikeType));
        assertFalse(mapLikeType.equals(null));
        assertFalse(mapLikeType.equals(""));
        assertFalse(mapLikeType.equals(new Object()));
        assertFalse(mapLikeType.equals(new ArrayList<>()));
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        // Test MapLikeType instance with keyTypeHandler

        JavaType keyTypeHandler = new TypeDeserializer.FromString();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));
        MapLikeType mapLikeTypeWithKeyTypeHandler = mapLikeType.withKeyTypeHandler(keyTypeHandler);
        assertTrue(mapLikeTypeWithKeyTypeHandler.hasHandlers());
        assertNotNull(mapLikeTypeWithKeyTypeHandler.getValueHandler());
        assertNull(mapLikeTypeWithKeyTypeHandler.getTypeHandler());
        assertEquals(keyTypeHandler, mapLikeTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        // Test MapLikeType instance with keyValueHandler

        JavaType keyValueHandler = new Object() {
            public Object readValue(String value) {
                return null;
            }
        };
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));
        MapLikeType mapLikeTypeWithKeyValueHandler = mapLikeType.withKeyValueHandler(keyValueHandler);
        assertTrue(mapLikeTypeWithKeyValueHandler.hasHandlers());
        assertNull(mapLikeTypeWithKeyValueHandler.getValueHandler());
        assertNull(mapLikeTypeWithKeyValueHandler.getTypeHandler());
        assertEquals(keyValueHandler, mapLikeTypeWithKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        // Test MapLikeType instance isTrueMapType

        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType baseType = TypeFactory.defaultInstance().constructType(MapLikeType.class);
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType2 = MapLikeType.upgradeFrom(baseType, keyType1, valueType1);
        assertTrue(mapLikeType2.isTrueMapType());

        JavaType keyT, valueT;
        JavaType mapType3 = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType mapLikeType3 = new MapLikeType(mapType3, TypeBindings.emptyBindings(), _bogusSuperClass(mapType3), null, keyT = TypeFactory.defaultInstance().constructType(String.class), valueT = TypeFactory.defaultInstance().constructType(Integer.class));
        assertTrue(mapLikeType3.isTrueMapType());
    }
}