import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {

    @Test
    public void testToString() {
        Class<List<Integer>> listClass = List.class;
        JavaType listType = CollectionType.construct(listClass, null, null, null, JavaType.INT_TYPE);
        String expected = "[collection type; class List, contains class java.lang.Integer]";
        String actual = listType.toString();
        assertEquals(expected, actual);
    }
}