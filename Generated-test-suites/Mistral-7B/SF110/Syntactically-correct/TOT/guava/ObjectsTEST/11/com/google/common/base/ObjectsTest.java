import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    @Test
    public void testEqual() {
        // Test with null objects
        assertFalse(Objects.equal(null, null));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));

        // Test with same object
        Object obj1 = new Object();
        assertTrue(Objects.equal(obj1, obj1));

        // Test with non-null and equal objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with non-null and not equal objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[10];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object() {
                @Override
                public int hashCode() {
                    return i;
                }
            };
        }

        int hashCode = Objects.hashCode(objects);
        int expectedHashCode = 0;
        for (Object obj : objects) {
            expectedHashCode ^= obj.hashCode();
        }
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object() {
            String name = "Test Object";
            int value = 42;
        };

        Objects.ToStringHelper helper = Objects.toStringHelper(obj);
        helper.add("name", obj.name);
        helper.add("value", obj.value);

        String expectedToString = "TestObject{name=Test Object, value=42}";
        String actualToString = helper.toString();
        assertEquals(expectedToString, actualToString);

        helper.omitNullValues();
        helper.add("value", null);

        expectedToString = "TestObject{name=Test Object}";
        actualToString = helper.toString();
        assertEquals(expectedToString, actualToString);
    }
}