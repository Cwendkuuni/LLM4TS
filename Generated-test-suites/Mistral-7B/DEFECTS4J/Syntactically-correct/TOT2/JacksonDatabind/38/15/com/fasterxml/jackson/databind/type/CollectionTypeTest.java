import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {
    @Test
    public void testConstructor() {
        Class<?> listClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = ArrayList.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.INSTANCE;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        assertEquals("[collection type; class class java.util.ArrayList, contains class int]", collectionType.toString());
    }
}