import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    @Test
    public void testSimpleTypeConstructor() {
        // Test the constructor with a valid class
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        assertEquals("java.lang.String", stringSimpleType.toString());

        // Test the constructor with an empty TypeBindings
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        SimpleType emptySimpleType = new SimpleType(String.class, emptyBindings);
        assertEquals("java.lang.String", emptySimpleType.toString());

        // Test the copy-constructor
        SimpleType stringSimpleType2 = new SimpleType(stringSimpleType);
        assertEquals(stringSimpleType, stringSimpleType2);

        // Test the pass-through constructor
        SimpleType stringSimpleType3 = new SimpleType(stringSimpleType);
        assertEquals(stringSimpleType, stringSimpleType3);
    }

    @Test
    public void testConstructUnsafe() {
        Class<Integer> intClass = Integer.class;
        SimpleType intSimpleType = SimpleType.constructUnsafe(intClass);
        assertEquals("class java.lang.Integer", intSimpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    // Add more test cases for other methods here
}