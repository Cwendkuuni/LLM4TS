import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameObjects() {
        Object obj1 = new Object();
        Same sameObj = new Same(obj1);

        Object obj2 = obj1;
        assertTrue(sameObj.matches(obj2));
    }

    @Test
    public void testDifferentObjects() {
        Object obj1 = new Object();
        Same sameObj = new Same(obj1);

        Object obj2 = new Object();
        assertFalse(sameObj.matches(obj2));
    }

    @Test
    public void testNullObjects() {
        Object obj1 = null;
        Same sameObj = new Same(obj1);

        Object obj2 = null;
        assertTrue(sameObj.matches(obj2));
    }

    @Test(expected = AssertionError.class)
    public void testStringObjectsWithDifferentContent() {
        Same sameObj = new Same("Hello");
        String actual = "World";
        assertFalse(sameObj.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testCharacterObjectsWithDifferentValues() {
        Same sameObj = new Same('A');
        Character actual = 'B';
        assertFalse(sameObj.matches(actual));
    }

    @Test
    public void testStringObjectsWithSameContent() {
        Same sameObj = new Same("Hello");
        String actual = "Hello";
        assertTrue(sameObj.matches(actual));
    }

    @Test
    public void testCharacterObjectsWithSameValue() {
        Same sameObj = new Same((char) 65);
        Character actual = (char) 65;
        assertTrue(sameObj.matches(actual));
    }

    @Test
    public void testSerializableObjects() {
        Integer obj1 = 1;
        Same sameObj = new Same(obj1);
        Integer obj2 = 1;
        assertTrue(sameObj.matches(obj2));
    }
}