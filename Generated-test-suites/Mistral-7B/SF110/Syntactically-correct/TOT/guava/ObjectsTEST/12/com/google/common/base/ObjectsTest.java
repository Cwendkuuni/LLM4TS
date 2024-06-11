import static org.junit.Assert.*; // for assertions
import static com.google.common.base.Objects.*; // for the method under test

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertTrue(equal(null, null));
        assertFalse(equal(null, new Object()));
        assertFalse(equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(equal(obj1, obj1));

        // Test with non-null equal objects
        Object obj2 = new Object();
        assertTrue(equal(obj1, obj2));
        assertTrue(equal(obj2, obj1));

        // Test with non-equal objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        assertFalse(equal(obj1, obj3));
        assertFalse(equal(obj3, obj1));
    }
}