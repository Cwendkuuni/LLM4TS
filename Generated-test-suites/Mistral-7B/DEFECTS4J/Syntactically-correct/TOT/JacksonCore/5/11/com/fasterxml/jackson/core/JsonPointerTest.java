import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertEquals(-1, empty.getMatchingIndex());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/test/property", pointer.toString());
        JsonPointer tail = pointer.tail();
        assertNull(tail);
    }

    @Test
    public void testPointerWithElement() {
        String input = "/test/0";
        JsonPointer pointer = null;
        try {
            pointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            fail("Should have parsed input: " + input);
        }
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithSlash() {
        JsonPointer.compile("/invalid");
    }

    @Test
    public void testPointerWithQuotes() {
        String input = "/test/property/~0/~1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/property/~0/~1", pointer.toString());
    }

    @Test
    public void testTailMethod() {
        String input = "/test/property/element";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/test/property", tail.toString());
        assertNull(tail.tail());
    }

    @Test
    public void testMatchesProperty() {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchProperty = new JsonPointer("/test/property");
        JsonPointer notMatchProperty = new JsonPointer("/test/anotherProperty");
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertTrue(matchProperty.matches());
        assertTrue(pointer.matches(matchProperty));
        assertFalse(pointer.matches(notMatchProperty));
        assertFalse(pointer.matches(empty));
    }

    @Test
    public void testMatchesElement() {
        String input = "/test/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchElement = new JsonPointer("/test/0");
        JsonPointer notMatchElement = new JsonPointer("/test/1");
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(pointer.mayMatchElement());
        assertTrue(matchElement.mayMatchElement());
        assertTrue(pointer.matchesElement(0));
        assertTrue(matchElement.matchesElement(0));
        assertFalse(pointer.matchesElement(1));
        assertFalse(matchElement.matchesElement(1));
        assertFalse(pointer.matchesElement(-1));
        assertFalse(matchElement.matchesElement(-1));
        assertFalse(pointer.matchesElement(Integer.MAX_VALUE));
        assertFalse(matchElement.matchesElement(Integer.MAX_VALUE));
        assertFalse(pointer.matches(empty));
        assertFalse(matchElement.matches(empty));
    }
}