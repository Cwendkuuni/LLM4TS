// Test file for Same class

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjects() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with an object
        // When: The method matches is called with an object equal to the wanted object
        // Then: The method should return true
        Same sameObjectMatcher = new Same(new Object());
        Object actualObject = new Object();
        boolean result = sameObjectMatcher.matches(actualObject);
        // Assertion: The result should be true
        assertTrue(result);
    }

    @Test
    public void testDifferentObjects() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with an object
        // When: The method matches is called with an object not equal to the wanted object
        // Then: The method should return false
        Same sameObjectMatcher = new Same(new Object());
        Object differentObject = new Object();
        boolean result = sameObjectMatcher.matches(differentObject);
        // Assertion: The result should be false
        assertFalse(result);
    }

    @Test
    public void testNullWantedObject() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with a null wanted object
        // When: The method matches is called with a non-null object
        // Then: The method should return false
        Same sameObjectMatcher = new Same(null);
        Object actualObject = new Object();
        boolean result = sameObjectMatcher.matches(actualObject);
        // Assertion: The result should be false
        assertFalse(result);
    }

    @Test
    public void testNullActualObject() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with a non-null wanted object
        // When: The method matches is called with a null object
        // Then: The method should return false
        Same sameObjectMatcher = new Same(new Object());
        Object nullActualObject = null;
        boolean result = sameObjectMatcher.matches(nullActualObject);
        // Assertion: The result should be false
        assertFalse(result);
    }

    @Test
    public void testSameString() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with a String
        // When: The method matches is called with the same String
        // Then: The method should return true
        Same sameStringMatcher = new Same("testString");
        String actualString = "testString";
        boolean result = sameStringMatcher.matches(actualString);
        // Assertion: The result should be true
        assertTrue(result);
    }

    @Test
    public void testDifferentStrings() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with a String
        // When: The method matches is called with a different String
        // Then: The method should return false
        Same sameStringMatcher = new Same("testString");
        String differentString = "differentString";
        boolean result = sameStringMatcher.matches(differentString);
        // Assertion: The result should be false
        assertFalse(result);
    }

    @Test
    public void testSameCharacter() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher with a Character
        // When: The method matches is called with the same Character
        // Then: The method should return true
        Same sameCharacterMatcher = new Same('a');
        Character actualCharacter = 'a';
        boolean result = sameCharacterMatcher.matches(actualCharacter);
        // Assertion: The result should be true
        assertTrue(result);
    }

    @Test
    public void testDifferentCharacters() {
        // Test case for method: matches(Object)
        // Given: An instance of Same argument matcher
        // with a Character
        // When: The method matches is called with a different Character
        // Then: The method should return false
        Same sameCharacterMatcher = new Same('a');
        Character differentCharacter = 'b';
        boolean result = sameCharacterMatcher.matches(differentCharacter);
        // Assertion: The result should be false
        assertFalse(result);
    }
}