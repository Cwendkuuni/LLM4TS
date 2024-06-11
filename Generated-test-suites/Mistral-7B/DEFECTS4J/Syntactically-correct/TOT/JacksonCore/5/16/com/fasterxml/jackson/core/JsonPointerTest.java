import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testPointerWithIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/0/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/0/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithPropertyName() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        pointer.matchProperty("invalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerWithInvalidIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/-1/property");
    }

    @Test
    public void testPointerWithMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/test/0/property1/property2");
        assertTrue(pointer.matches());
        assertEquals("property2", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/test/0/property1/property2", pointer.toString());
    }

    @Test
    public void testPointerWithQuotedSegments() {
        JsonPointer pointer = JsonPointer.compile("/test/~0/property");
        assertTrue(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/test/~0/property", pointer.toString());
    }

    @Test
    public void testPointerTail() {
        JsonPointer pointer = JsonPointer.compile("/test/property/subproperty");
        JsonPointer tail = pointer.tail();
        assertEquals("/test/property", tail.toString());
        assertTrue(tail.matches());
        assertEquals(null, tail.getMatchingProperty());
        assertFalse(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
    }

    @Test
    public void testPointerMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        JsonPointer match = new JsonPointer("/test/testProperty");
        JsonPointer result = pointer.matchProperty("property");
        assertEquals(match, result);
    }

    @Test
    public void testPointerMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0/property");
        JsonPointer match = new JsonPointer("/test/list/0/property");
        JsonPointer result = pointer.matchElement(0);
        assertEquals(match, result);
    }
}