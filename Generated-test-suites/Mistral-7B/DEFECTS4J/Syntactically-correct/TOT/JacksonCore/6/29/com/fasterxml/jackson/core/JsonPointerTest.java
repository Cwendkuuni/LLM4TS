import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testBasicPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar", tail.toString());
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("~foo");
    }

    @Test
    public void testQuotedPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar~1/baz");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar~1/baz", tail.toString());
        assertEquals("/foo/bar~1/baz", jsonPointer.toString());
    }

    @Test
    public void testIndexPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        assertFalse(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/foo", tail.toString());
        assertEquals("/foo/0", jsonPointer.toString());
    }

    @Test
    public void testNestedPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0/bar");
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/foo/0", tail.toString());
        assertEquals("/foo/0/bar", jsonPointer.toString());
    }

    @Test
    public void testMatchesProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer matched = JsonPointer.compile("/foo/bar");
        assertTrue(jsonPointer.matchesProperty(matched.getMatchingProperty()));
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/bar", tail.toString());
    }

    @Test
    public void testMatchesElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/0");
        JsonPointer matched = JsonPointer.compile("/foo/1");
        assertFalse(jsonPointer.matchesElement(matched.getMatchingIndex()));
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/foo", tail.toString());
    }
}