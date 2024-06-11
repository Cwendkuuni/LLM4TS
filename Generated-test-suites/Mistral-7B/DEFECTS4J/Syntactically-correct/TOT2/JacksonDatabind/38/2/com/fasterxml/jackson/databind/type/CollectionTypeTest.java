import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {
    @Test
    public void testToString() {
        JavaType elemType = new SimpleType("java.lang.Integer");
        CollectionType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
        String expected = "[collection type; class List, contains java.lang.Integer]";
        String actual = collectionType.toString();
        assertEquals(expected, actual);
    }
}