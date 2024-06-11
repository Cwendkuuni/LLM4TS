
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        JsonPointer pointer1 = new JsonPointer("/test1");
        JsonPointer pointer2 = new JsonPointer("/test1/test2");

        assertTrue(emptyPointer.matches());
        assertFalse(pointer1.matches());
        assertFalse(pointer2.matches());

        JsonPointer matchedPointer = new JsonPointer("/test1/test2");
        assertTrue(matchedPointer.tail().matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        String invalidInput1 = "";
        String invalidInput2 = "test";
        String invalidInput3 = "//test1";

        try {
            JsonPointer.compile(invalidInput1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            JsonPointer.compile(invalidInput2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            JsonPointer.compile(invalidInput3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompileEmpty() {
        JsonPointer emptyPointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, emptyPointer);
    }

    @Test
    public void testCompileSingleSegment() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testCompileMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/test1/test2");
        assertEquals("/test1/test2", pointer.toString());
    }

    @Test
    public void testCompileIndex() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertEquals("/test/0", pointer.toString());
    }

    @Test
    public void testCompileEscaped() {
        JsonPointer pointer = JsonPointer.compile("/test~1/test2");
        assertEquals("/test/1/test2", pointer.toString());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test/property");
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/test/0");
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test/property");
        JsonPointer matchedPointer = new JsonPointer("/test/subtest/property");
        JsonPointer unmatchedPointer = new JsonPointer("/test/subtest/subproperty");

        assertEquals(matchedPointer, pointer.matchProperty("property"));
        assertNull(pointer.matchProperty("subproperty"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/test/0");
        JsonPointer matchedPointer = new JsonPointer("/test/subtest/0");
        JsonPointer unmatchedPointer = new JsonPointer("/test/subtest/1");

        assertEquals(matchedPointer, pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/property");
        assertEquals(new JsonPointer("/test/property"), pointer);
        assertEquals(new JsonPointer("/test/subtest/property"), pointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/test/property");
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/test/property");
        JsonPointer pointer2 = new JsonPointer("/test/property");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test/property");
        JsonPointer pointer2 = new JsonPointer("/test/property");
        JsonPointer pointer3 = new JsonPointer("/test/subproperty");

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }
}
