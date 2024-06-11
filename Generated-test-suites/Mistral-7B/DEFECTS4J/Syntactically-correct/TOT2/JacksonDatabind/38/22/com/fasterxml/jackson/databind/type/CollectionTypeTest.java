
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testToString() {
        JavaType listType = CollectionType.construct(ArrayList.class, null, null, null, String.class);
        String expectedResult = "[collection type; class ArrayList, contains java.lang.String]";
        String actualResult = listType.toString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString_nullJavaType() {
        String actualResult = toString((JavaType) null);
        assertNull(actualResult);
    }

    @Test
    public void testToString_emptyCollectionType() {
        CollectionType emptyCollectionType = new CollectionType(Object.class, null, null, null, null, null, null, false);
        String expectedResult = "[collection type; class java.lang.Object, contains null]";
        String actualResult = emptyCollectionType.toString();
        assertEquals(expectedResult, actualResult);
    }

    // Add more test cases for other methods here

    private String toString(JavaType javaType) {
        return javaType.toString();
    }
}