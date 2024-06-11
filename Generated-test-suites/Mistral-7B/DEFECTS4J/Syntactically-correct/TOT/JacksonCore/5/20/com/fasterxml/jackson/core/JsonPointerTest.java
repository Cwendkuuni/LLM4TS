import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testPointerWithTail() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("bar", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertNull(tail.tail());
        assertEquals("/bar", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "~invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testPointerWithIndex() {
        String input = "/foo/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("1", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(1, tail.getMatchingIndex());
        assertNull(tail.tail());
        assertEquals("/1", tail.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/foo/0/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("bar", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(1, tail.getMatchingIndex());
        JsonPointer grandTail = tail.tail();
        assertNotNull(grandTail);
        assertEquals("", grandTail.getMatchingProperty());
        assertEquals(-1, grandTail.getMatchingIndex());
        assertNull(grandTail.tail());
        assertEquals("/1", tail.toString());
        assertEquals("/bar/1", grandTail.toString());
    }

    @Test
    public void testPointerWithEscapedCharacter() {
        String input = "/foo/~1/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertEquals("~1", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertEquals("", tail.getMatchingProperty());
        assertEquals(1, tail.getMatchingIndex());
        assertNull(tail.tail());
        assertEquals("/~1", tail.toString());
        JsonPointer grandTail = tail.tail();
        assertNotNull(grandTail);
        assertEquals("bar", grandTail.getMatchingProperty());
        assertFalse(grandTail.matches());
        assertEquals("", grandTail.getMatchingProperty());
        assertEquals(-1, grandTail.getMatchingIndex());
        assertNull(grandTail.tail());
        assertEquals("/bar", grandTail.toString());
    }
}