import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testPointerWithElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertNull(pointer.tail());
        assertEquals("/test/0", pointer.toString());
    }

    @Test
    public void testPointerWithPropertyAndElement() {
        JsonPointer pointer = JsonPointer.compile("/test/property/0");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/property/0", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/test/property1/property2/property3/0");
        assertTrue(pointer.matches());
        assertEquals("property3", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/property1/property2/property3/0", pointer.toString());
    }

    @Test
    public void testPointerMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        JsonPointer matchingPointer = new JsonPointer("/test", "property", null);
        JsonPointer result = pointer.matchProperty("property");
        assertEquals(matchingPointer, result);
    }

    @Test
    public void testPointerMatchingElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        JsonPointer matchingPointer = new JsonPointer("/test", "", new JsonPointer("/", "0", null));
        JsonPointer result = pointer.matchElement(0);
        assertEquals(matchingPointer, result);
    }

    @Test
    public void testPointerTail() {
        JsonPointer pointer = JsonPointer.compile("/test/property/0");
        JsonPointer tail = pointer.tail();
        JsonPointer expectedTail = new JsonPointer("/test/property", "", null);
        assertEquals(expectedTail, tail);
    }
}