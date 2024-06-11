import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
    }
}