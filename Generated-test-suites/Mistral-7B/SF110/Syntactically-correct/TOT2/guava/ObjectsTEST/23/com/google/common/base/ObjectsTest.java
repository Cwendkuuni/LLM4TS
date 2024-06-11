import org.junit.Assert;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Basic test case
        Assert.assertTrue(Objects.equal(null, null));
        Assert.assertTrue(Objects.equal(new Object(), new Object()));
        Assert.assertFalse(Objects.equal(null, new Object()));
        Assert.assertFalse(Objects.equal(new Object(), null));
        Assert.assertFalse(Objects.equal(new Object(), new Object())); // different instances

        // Edge cases
        Assert.assertFalse(Objects.equal(null, new String("")));
        Assert.assertFalse(Objects.equal(new Object(), new String("")));

        Object[] arrays1 = new Object[0];
        Object[] arrays2 = new Object[1];
        Object[] arrays3 = new Object[2];
        arrays1[0] = new Object();
        arrays2[0] = new Object();
        arrays3[0] = new Object();
        arrays3[1] = new Object();

        Assert.assertEquals(Arrays.hashCode(new Object[0]), Objects.hashCode());
        Assert.assertEquals(Arrays.hashCode(new Object[]{new Object()}), Objects.hashCode(new Object[]{new Object()}));
        Assert.assertEquals(Arrays.hashCode(new Object[]{new Object(), new Object()}), Objects.hashCode(new Object[]{new Object(), new Object()}));
        Assert.assertNotEquals(Arrays.hashCode(arrays1), Arrays.hashCode(arrays2));
        Assert.assertNotEquals(Arrays.hashCode(arrays2), Arrays.hashCode(arrays3));
        Assert.assertNotEquals(Arrays.hashCode(arrays3), Arrays.hashCode(arrays1));

        // Exception handling
        try {
            Objects.toStringHelper(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            Objects.toStringHelper((Object) null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            Objects.toStringHelper(Objects.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Objects.toStringHelper("").add("name", null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    // Test cases for other methods can be added similarly

    @Test
    public void testToStringHelper() {
        // Test for empty ToStringHelper
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        Assert.assertEquals("", helper.toString());

        // Test for ToStringHelper with various types of values
        Objects.ToStringHelper helperWithString = Objects.toStringHelper("test");
        Objects.ToStringHelper helperWithBoolean = Objects.toStringHelper(true);
        Objects.ToStringHelper helperWithInteger = Objects.toStringHelper(123);
        Objects.ToStringHelper helperWithLong = Objects.toStringHelper(123L);
        Objects.ToStringHelper helperWithDouble = Objects.toStringHelper(123.0);
        Objects.ToStringHelper helperWithFloat = Objects.toStringHelper(123.0f);
        Objects.ToStringHelper helperWithArray = Objects.toStringHelper(new Object[]{1, 2, 3});

        // Test for omitNullValues()
        Objects.ToStringHelper helperWithNullValue = Objects.toStringHelper("test").add("nullValue", null);
        Objects.ToStringHelper helperWithOmittedNullValue = Objects.toStringHelper("test").omitNullValues().add("nullValue", null);
        Assert.assertNotEquals(helperWithNullValue.toString(), helperWithOmittedNullValue.toString());

        // Test for multiple additions
        Objects.ToStringHelper helperWithMultipleAdditions = Objects.toStringHelper("test")
                .add("name1", "value1")
                .add("name2", "value2")
                .add("name3", "value3")
                .add("name4", "value4");
        Assert.assertTrue(helperWithMultipleAdditions.toString().contains("name1=value1"));
        Assert.assertTrue(helperWithMultipleAdditions.toString().contains("name2=value2"));
        Assert.assertTrue(helperWithMultipleAdditions.toString().contains("name3=value3"));
        Assert.assertTrue(helperWithMultipleAdditions.toString().contains("name4=value4"));

        // Test for multiple addition types
        Objects.ToStringHelper helperWithMixedAdditions = Objects.toStringHelper("test")
                .add("name1", "value1")
                .addValue(123)
                .addValue('A')
                .addValue(123.0)
                .add("name2", "value2");
        Assert.assertTrue(helperWithMixedAdditions.toString().contains("name1=value1"));
        Assert.assertTrue(helperWithMixedAdditions.toString().contains("123"));
        Assert.assertTrue(helperWithMixedAdditions.toString().contains("A"));
        Assert.assertTrue(helperWithMixedAdditions.toString().contains("name2=value2"));

        // Test for exceptions
        try {
            Objects.ToStringHelper.ValueHolder valueHolder = new Objects.ToStringHelper.ValueHolder();
            valueHolder.name = null;
            valueHolder.value = null;
            Objects.ToStringHelper helperWithNullValueHolder = Objects.toStringHelper("test").addHolder(valueHolder);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        try {
            Objects.ToStringHelper helperWithInvalidType = Objects.toStringHelper("test").add("invalidType", new Runnable() {});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}