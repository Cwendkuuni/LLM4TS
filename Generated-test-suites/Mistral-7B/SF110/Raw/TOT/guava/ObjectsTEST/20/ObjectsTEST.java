import static org.junit.Assert.*;

import com.google.common.base.Objects;

public class ObjectsTest {
    @org.junit.Test
    public void testEqual() {
        // Test when objects are equal
        assertTrue(Objects.equal(new Object(), new Object()));

        // Test when first object is null
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));

        // Test when second object is null
        obj1 = new Object();
        obj2 = null;
        assertFalse(Objects.equal(obj1, obj2));

        // Test when both objects are null
        obj1 = null;
        obj2 = null;
        assertTrue(Objects.equal(obj1, obj2));

        // Test when first object is an instance of a class and second is not
        Object obj1Instance = new MyClass();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1Instance, obj2));

        // Test when first object is an instance of a class and second is an instance of the same class
        MyClass obj1Instance2 = new MyClass();
        assertTrue(Objects.equal(obj1Instance, obj1Instance2));
    }
}