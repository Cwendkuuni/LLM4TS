import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;
import org.hamcrest.Description;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstance() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testStringSame() {
        String wanted = "test";
        String actual = "test";

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testStringDifferent() {
        String wanted = "test";
        String actual = "anotherTest";

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterSame() {
        Character wanted = 'a';
        Character actual = 'a';

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterDifferent() {
        Character wanted = 'a';
        Character actual = 'b';

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullSame() {
        Object wanted = null;
        Object actual = null;

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDifferent() {
        Object wanted = null;
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        sameMatcher.matches(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSerializable() {
        Object wanted = new NonSerializableObject();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        // JUnit 4 tests does not support exception testing with annotated methods
        // so we have to use an assertion to check the exception is thrown
        Assert.assertThrows(IllegalArgumentException.class, () -> sameMatcher.matches(actual));
    }

    private static class NonSerializableObject implements Serializable {
    }

    @Test
    public void testSerializeAndDeserialize() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        // Serialize the object
        byte[] serialized = new java.io.ByteArrayOutputStream().toByteArray(new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(sameMatcher));

        // Deserialize the object
        Same deserialized = (Same) new ObjectInputStream(new ByteArrayInputStream(serialized)).readObject();

        // Compare the objects to check if they are the same
        Assert.assertEquals(sameMatcher, deserialized);
    }
}