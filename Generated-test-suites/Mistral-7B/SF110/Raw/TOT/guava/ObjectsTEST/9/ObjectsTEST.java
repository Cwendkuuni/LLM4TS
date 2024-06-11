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

        // Test with equal objects
        Object obj2 = new Object();
        assertTrue(Objects.equal(obj1, obj2));
        assertTrue(Objects.equal(obj2, obj1));

        // Test with non-equal objects
        Object obj3 = new Object() {
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        assertFalse(Objects.equal(obj1, obj3));
        assertFalse(Objects.equal(obj3, obj1));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[] {new Object(), new Object(), new Object()};
        int hashCode = Objects.hashCode(objects);
        int expectedHashCode = 16777619 * Arrays.hashCode(objects) ^ 0; // HashCode prime

        assertEquals(expectedHashCode, hashCode);

        // Test with empty array
        hashCode = Objects.hashCode(new Object[0]);
        expectedHashCode = 0;

        assertEquals(expectedHashCode, hashCode);

        // Test with single null element
        Object nullObject = null;
        objects[0] = nullObject;
        hashCode = Objects.hashCode(objects);
        expectedHashCode = 16777619 * Arrays.hashCode(objects) ^ System.identityHashCode(nullObject); // HashCode prime

        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass")
                .add("name", "John Doe")
                .add("age", 30)
                .add("isMarried", true)
                .addValue(new Object())
                .omitNullValues();

        String expectedToString = "TestClass{name=John Doe, age=30, isMarried=true}";
        String actualToString = helper.toString();

        assertEquals(expectedToString, actualToString);
    }
}