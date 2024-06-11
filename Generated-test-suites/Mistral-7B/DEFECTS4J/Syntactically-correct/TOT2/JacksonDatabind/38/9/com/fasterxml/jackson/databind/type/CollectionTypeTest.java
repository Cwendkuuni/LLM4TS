import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CollectionTypeTest {
    @Test
    public void testToString() {
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, IntegerType.getInstance());
        String actual = collectionType.toString();
        String expected = "[collection type; class ArrayList, contains class java.lang.Integer]";
        assertEquals(expected, actual);
    }
}