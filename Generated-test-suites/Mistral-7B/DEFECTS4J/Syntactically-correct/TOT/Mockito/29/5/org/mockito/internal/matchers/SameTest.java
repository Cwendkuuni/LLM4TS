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

        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testDifferentObjects() {
        Object wantedObject = new Object();
        Object actualObject = new Object(); // different instance

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullAndSameObject() {
        Object wantedObject = new Object();
        Object actualObject = wantedObject;

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        // null should not match
        assertFalse(sameMatcher.matches(null));

        // same object should match
        assertTrue(sameMatcher.matches(actualObject));
    }

    @Test
    public void testNullAndDifferentObject() {
        Object wantedObject = new Object();
        Object actualObject = new Object(); // different instance

        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        // null should not match
        assertFalse(sameMatcher.matches(null));

        // different objects should not match
        assertFalse(sameMatcher.matches(actualObject));
    }

    @Test
    public void testStringSameObjects() {
        String wantedString = "same string";
        String actualString = wantedString;

        ArgumentMatcher<Object> sameMatcher = new Same(wantedString);

        assertTrue(sameMatcher.matches(actualString));
    }

    @Test
    public void testStringDifferentObjects() {
        String wantedString = "same string";
        String actualString = "different string";

        ArgumentMatcher<Object> sameMatcher = new Same(wantedString);

        assertFalse(sameMatcher.matches(actualString));
    }

    @Test
    public void testCharacterSameObjects() {
        Character wantedCharacter = 'same';
        Character actualCharacter = wantedCharacter;

        ArgumentMatcher<Object> sameMatcher = new Same(wantedCharacter);

        assertTrue(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testCharacterDifferentObjects() {
        Character wantedCharacter = 'same';
        Character actualCharacter = 'different';

        ArgumentMatcher<Object> sameMatcher = new Same(wantedCharacter);

        assertFalse(sameMatcher.matches(actualCharacter));
    }

    @Test
    public void testSerializableSameObjects() {
        Serializable wantedSerializable = new Serializable() {};
        Serializable actualSerializable = wantedSerializable;

        ArgumentMatcher<Object> sameMatcher = new Same(wantedSerializable);

        assertTrue(sameMatcher.matches(actualSerializable));
    }

    @Test
    public void testSerializableDifferentObjects() {
        Serializable wantedSerializable = new Serializable() {};
        Serializable actualSerializable = new Serializable() {}; // different instance

        ArgumentMatcher<Object> sameMatcher = new Same(wantedSerializable);

        assertFalse(sameMatcher.matches(actualSerializable));
    }
}