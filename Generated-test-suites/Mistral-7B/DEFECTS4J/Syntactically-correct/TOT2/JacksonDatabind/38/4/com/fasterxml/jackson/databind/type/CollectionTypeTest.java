import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {
    // 

    @Test
    public void testToString() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = IntType.getInstance();
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(collectionClass, bindings, superClass, superInterfaces, elemT, valueHandler, typeHandler, asStatic);

        String expected = "[collection type; class " + collectionClass.getName() + ", contains " + elemT + "]";
        String actual = collectionType.toString();

        assertEquals(expected, actual);
    }

    // 
}