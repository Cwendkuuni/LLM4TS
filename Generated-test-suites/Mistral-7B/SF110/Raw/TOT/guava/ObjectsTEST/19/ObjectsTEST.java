import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // equal objects
        assertTrue(Objects.equal(new Object(), new Object()));
        // null objects
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal(new Object(), null));
        // null and same object
        assertTrue(Objects.equal(null, null));
        // object with equal values
        String str1 = new String("test");
        String str2 = new String("test");
        assertTrue(Objects.equal(str1, str2));
        // object with different classes but equal values
        Integer int1 = 1;
        Integer int2 = 1;
        assertTrue(Objects.equal(int1, int2));
        // object with different values
        Integer int1_1 = 1;
        Integer int2_1 = 2;
        assertFalse(Objects.equal(int1_1, int2_1));
        // object with one null and one not null
        Object obj1 = new Object();
        Object obj2 = null;
        assertFalse(Objects.equal(obj1, obj2));
        // object with one null and one null
        Object obj3 = null;
        Object obj4 = null;
        assertTrue(Objects.equal(obj3, obj4));
    }

    @Test
    public void testHashCode() {
        Object[] objects = new Object[]{new Object(), new Object(), null};
        int hashCode = Objects.hashCode(objects);
        int expectedHashCode = 32 * 3 + Arrays.hashCode(objects);
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper(new Object());
        toStringHelper.add("field1", "value1");
        toStringHelper.add("field2", null);
        toStringHelper.add("field3", "value3");
        String expectedToString = "Object{field1=value1, field3=value3}";
        String actualToString = toStringHelper.toString();
        assertEquals(expectedToString, actualToString);

        toStringHelper.omitNullValues();
        expectedToString = "Object{field1=value1, field3=value3}";
        actualToString = toStringHelper.toString();
        assertEquals(expectedToString, actualToString);
    }
}