import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testNotSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameString() {
        String wanted = "hello";
        Same sameMatcher = new Same(wanted);

        String actual = "hello";
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testNotSameString() {
        String wanted = "hello";
        Same sameMatcher = new Same(wanted);

        String actual = "world";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        Same sameMatcher = new Same(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullActual() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        // Null actual should throw an IllegalArgumentException
        sameMatcher.matches(null);
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);

        Character actual = 'a';
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testNotSameCharacter() {
        Character wanted = 'a';
        Same sameMatcher = new Same(wanted);

        Character actual = 'b';
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSerializable() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);

        // Serialize and deserialize the instance of the matcher
        byte[] serialized = SerializationUtils.serialize(sameMatcher);
        Same deserialized = (Same) SerializationUtils.deserialize(serialized);

        Object actual = wanted;
        assertTrue(deserialized.matches(actual));
    }
}