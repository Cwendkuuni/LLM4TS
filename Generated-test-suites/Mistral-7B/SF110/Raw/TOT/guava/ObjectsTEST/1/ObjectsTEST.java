import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test with null objects
        assertEquals(Objects.equal(null, null), true);

        // Test with same non-null object
        String str1 = new String("Test");
        assertEquals(Objects.equal(str1, str1), true);

        // Test with non-equal null objects
        assertEquals(Objects.equal(null, new String("Test")), false);

        // Test with non-equal non-null objects
        String str1_1 = new String("Test");
        String str2 = new String("Different");
        assertEquals(Objects.equal(str1, str2), false);
    }

    @Test
    public void testHashCode() {
        // Test with empty array
        Object[] emptyArray = new Object[0];
        int hashCode = Objects.hashCode(emptyArray);
        int expectedHashCode = 0;
        assertEquals(hashCode, expectedHashCode);

        // Test with array containing null and non-null objects
        Object[] array = {null, new String("Test"), new Integer(10)};
        hashCode = Objects.hashCode(array);
        expectedHashCode = 21 * 31 + nullHashCode() + "Test".hashCode() + 10;
        assertEquals(hashCode, expectedHashCode);
    }

    @Test
    public void testToStringHelper() {
        // Test with basic types
        Objects.ToStringHelper helper = new Objects.ToStringHelper("MyClass")
                .add("name", "John Doe")
                .add("age", 30)
                .add("isMarried", true);
        String expectedToString = "MyClass{name=John Doe, age=30, isMarried=true}";
        String actualToString = helper.toString();
        assertEquals(expectedToString, actualToString);

        // Test with omitting null values
        Objects.ToStringHelper helper_omitNull = new Objects.ToStringHelper("MyClass")
                .add("name", "John Doe")
                .add("age", 30)
                .add("isMarried", null)
                .omitNullValues();
        String expectedToString_omitNull = "MyClass{age=30}";
        String actualToString_omitNull = helper_omitNull.toString();
        assertEquals(expectedToString_omitNull, actualToString_omitNull);
    }

    private int nullHashCode() {
        return 0;
    }
}