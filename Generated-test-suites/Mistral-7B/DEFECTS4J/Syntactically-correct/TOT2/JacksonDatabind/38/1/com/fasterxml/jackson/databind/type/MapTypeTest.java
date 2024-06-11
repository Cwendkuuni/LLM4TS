import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.construct(Object.class, bindings);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.construct(String.class, bindings);
        JavaType valueT = JavaType.construct(Integer.class, bindings);
        MapType mapTypeObj = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapTypeObj);
    }
}