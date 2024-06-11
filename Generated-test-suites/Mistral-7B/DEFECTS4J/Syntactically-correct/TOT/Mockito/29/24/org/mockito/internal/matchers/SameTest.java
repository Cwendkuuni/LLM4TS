import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object wantedObject = new Object();
        Object actualObject = wantedObject;

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        // Test that same object matches itself
        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testDifferentObjects() {
        Object wantedObject = new Object();
        Object actualObject = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        // Test that different objects do not match
        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullWantedObject() {
        Object actualObject = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(null);

        // Test that null wanted object does not match any object
        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullActualObject() {
        Object wantedObject = new Object();

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        // Test that null actual object throws NullPointerException
        try {
            sameMatcher.matches(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testStringWantedObject() {
        String wantedString = "test";
        String actualString = "test";

        ArgumentMatcher<Object> sameMatcher = new Same(wantedString);

        // Test that same strings match
        assertTrue(sameMatcher.matches(actualString));
    }

    @Test
    public void testDifferentStringObjects() {
        String wantedString = "test";
        String actualString = "different";

        ArgumentMatcher<Object> sameMatcher = new Same(wantedString);

        // Test that different strings do not match
        assertFalse(sameMatcher.matches(actualString));
    }

    @Test
    public void testCharWantedObject() {
        char wantedChar = 'a';
        char actualChar = 'a';

        ArgumentMatcher<Object> sameMatcher = new Same(wantedChar);

        // Test that same characters match
        assertTrue(sameMatcher.matches(actualChar));
    }

    @Test
    public void testDifferentCharObjects() {
        char wantedChar = 'a';
        char actualChar = 'b';

        ArgumentMatcher<Object> sameMatcher = new Same(wantedChar);

        // Test that different characters do not match
        assertFalse(sameMatcher.matches(actualChar));
    }
}