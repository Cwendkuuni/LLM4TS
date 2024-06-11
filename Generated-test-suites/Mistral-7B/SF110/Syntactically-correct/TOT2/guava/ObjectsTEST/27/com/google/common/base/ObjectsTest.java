// ObjectsTest.java - Expert 1

import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        Object obj3 = new Object();

        Assert.assertTrue(Objects.equal(obj1, obj2));
        Assert.assertFalse(Objects.equal(obj1, obj3));
        Assert.assertFalse(Objects.equal(null, obj1));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        int hashCode1 = Objects.hashCode(obj1);
        int hashCode2 = Objects.hashCode(obj2);

        Assert.assertNotSame(hashCode1, hashCode2);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullSelf() {
        Objects.toStringHelper(null);
    }

    @Test
    public void testToStringHelperEmpty() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Empty");
        String expected = "Empty{}";

        String actual = helper.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringHelperSingleValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("SingleValue");
        String expected = "SingleValue{}";

        helper.add("key", "value");

        String actual = helper.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringHelperMultipleValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("MultipleValues");

        helper.omitNullValues();
        helper.add("key1", "value1");
        helper.add("key2", null);
        helper.add("key3", "value3");

        String expected = "MultipleValues{key1=value1, key3=value3}";

        String actual = helper.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFirstNonNullNullFirst() {
        String first = null;
        String second = "second";

        String result = Objects.firstNonNull(first, second);

        String expected = second;

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFirstNonNullFirstNotNull() {
        String first = "first";
        String second = null;

        String result = Objects.firstNonNull(first, second);

        String expected = first;

        Assert.assertEquals(expected, result);
    }
}