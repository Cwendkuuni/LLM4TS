import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType actual = mapType.getRawClass();
        JavaType expected = rawType;
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructWithDeprecatedMethod() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(rawType, keyType, valueType);
        JavaType actual = mapType.getRawClass();
        JavaType expected = rawType;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetValueType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType expected = valueType;
        JavaType actual = mapType.getValueType();
        assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        Class<Map<String, String>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(String.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        JavaType expected = new MapType(Map.class, bindings, null, null, JavaType.construct(String.class), JavaType.construct(String.class));
        JavaType actual = mapType.refine(Map.class, bindings, null, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        String expected = "[map type; class Map<java.lang.String, java.lang.Integer>, " +
                "java.lang.String -> java.lang.Integer]";
        String actual = mapType.toString();
        assertEquals(expected, actual);
    }
}