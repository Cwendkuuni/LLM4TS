import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);

        Object actual = wanted;
        boolean result = sameInstance.matches(actual);
        Assert.assertTrue("Expected same instance to match", result);
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);

        Object actual = new Object();
        boolean result = sameInstance.matches(actual);
        Assert.assertFalse("Expected different instances to not match", result);
    }

    @Test
    public void testNullWanted() {
        Object wanted = null;
        Same sameInstance = new Same(wanted);

        Object actual = new Object();
        boolean result = sameInstance.matches(actual);
        Assert.assertFalse("Expected null wanted to not match", result);
    }

    @Test
    public void testNullActual() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);

        Object actual = null;
        boolean result = sameInstance.matches(actual);
        Assert.assertFalse("Expected null actual to not match", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWantedIsSerializable() {
        Object wanted = new java.util.Date();
        Same sameInstance = new Same(wanted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testActualIsSerializable() {
        Object wanted = new Object();
        Same sameInstance = new Same(wanted);

        Object actual = new java.util.Date();
    }

    @Test
    public void testStringWanted() {
        String wanted = "hello";
        Same sameInstance = new Same(wanted);

        String actual = "hello";
        boolean result = sameInstance.matches(actual);
        Assert.assertTrue("Expected same string to match", result);
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "hello";
        Same sameInstance = new Same(wanted);

        String actual = "world";
        boolean result = sameInstance.matches(actual);
        Assert.assertFalse("Expected different strings to not match", result);
    }

    @Test
    public void testCharacterWanted() {
        char wanted = 'a';
        Same sameInstance = new Same(wanted);

        char actual = 'a';
        boolean result = sameInstance.matches(actual);
        Assert.assertTrue("Expected same character to match", result);
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        Same sameInstance = new Same(wanted);

        char actual = 'b';
        boolean result = sameInstance.matches(actual);
        Assert.assertFalse("Expected different characters to not match", result);
    }
}