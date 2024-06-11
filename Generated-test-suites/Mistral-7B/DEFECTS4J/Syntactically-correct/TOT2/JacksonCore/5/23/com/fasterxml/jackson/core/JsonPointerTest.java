import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSegment() {
        JsonPointer jsonPointer = new JsonPointer("/segment", JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertEquals("/segment", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("invalid", JsonPointer.EMPTY);
    }
}