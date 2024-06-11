// GetGenericTypeTest.java
import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;

public class GetGenericTypeTest {

    @Test
    public void testGetGenericType() {
        Class clazz = MyClass.class;
        Field field = null; // replace with valid field
        Class expected = String.class;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }

    // Add edge case test for null field and null return type
    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeNullField() {
        Class clazz = MyClass.class;
        Field field = null;
        new GenericMaster().getGenericType(field);
    }

    // Add test for non-parametrized type
    @Test
    public void testGetGenericTypeNonParametrizedType() {
        Class clazz = MyClass.class;
        Field field = null; // replace with valid non-parametrized field
        Class expected = Object.class;
        Class actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }
}