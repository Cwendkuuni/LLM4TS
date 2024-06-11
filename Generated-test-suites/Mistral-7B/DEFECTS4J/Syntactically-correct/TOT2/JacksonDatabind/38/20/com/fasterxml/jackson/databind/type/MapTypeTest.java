import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        MapType mapType = new MapType(MapType.class, null, null, null, keyType, valueType, valueHandler, typeHandler, false);

        assertNotNull(mapType);
        assertEquals(MapType.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }
}