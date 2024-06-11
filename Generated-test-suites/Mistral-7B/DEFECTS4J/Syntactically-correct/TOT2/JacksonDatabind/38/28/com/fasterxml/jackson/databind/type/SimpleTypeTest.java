import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
    // Test case for constructor with Class arg
    @Test
    public void testSimpleTypeConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("java.lang.Integer", intType.getRawClass().getName());
    }

    // Test case for constructor with Class, TypeBindings, JavaType, JavaType[] args
    @Test
    public void testSimpleTypeConstructor2() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.lang.Integer", intType.getRawClass().getName());
    }

    // Test case for copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intType);
        assertEquals(intType, intType2);
    }

    // Test case for static factory method constructUnsafe
    @Test
    public void testConstructUnsafe() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.constructUnsafe(intClass);
        assertEquals("java.lang.Integer", intType.getRawClass().getName());
    }

    // Test case for deprecated static factory method construct
    @Test
    public void testConstructDeprecated() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = SimpleType.construct(intClass);
        assertEquals("java.lang.Integer", intType.getRawClass().getName());
    }
}