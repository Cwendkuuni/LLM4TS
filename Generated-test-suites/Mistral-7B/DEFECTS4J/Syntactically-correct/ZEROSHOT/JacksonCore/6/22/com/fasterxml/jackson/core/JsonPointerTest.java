import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() throws JsonProcessingException {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testConstructPointer() throws JsonProcessingException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws JsonProcessingException {
        String input = "123";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchProperty() throws JsonProcessingException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(null, pointer.matchProperty(null));
        assertEquals(pointer, pointer.matchProperty("foo"));
        assertEquals(null, pointer.matchProperty("baz"));
    }

    @Test
    public void testMatchElement() throws JsonProcessingException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(null, pointer.matchElement(-1));
        assertEquals(pointer, pointer.matchElement(0));
        assertEquals(null, pointer.matchElement(1));
    }

    @Test
    public void testTail() throws JsonProcessingException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(null, pointer.tail());
        input = "/foo/0/bar/baz";
        pointer = JsonPointer.compile(input);
        assertEquals(new JsonPointer("/foo/0/bar"), pointer.tail());
    }

    @Test
    public void testToString() throws JsonProcessingException {
        String input = "/foo/0/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() throws JsonProcessingException {
        String input1 = "/foo/0/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/0/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        String input3 = "/foo/0/baz";
        JsonPointer pointer3 = JsonPointer.compile(input3);
        String input4 = "/foo/1/bar";
        JsonPointer pointer4 = JsonPointer.compile(input4);

        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertEquals(pointer1, pointer1);
        assertEquals(pointer1, pointer2);
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer4));
    }

    @Test
    public void testParseTail() throws IOException, JsonProcessingException {
        String input = "/foo/bar~1/baz/qux~0/quux";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals(new JsonPointer("/foo/bar/baz"), pointer.tail());
        assertEquals(new JsonPointer("/qux"), pointer);
    }

    @Test
    public void testParseQuotedTail() throws IOException, JsonProcessingException {
        String input = "/foo/bar~1/baz~0/quux";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 11);
        assertEquals(new JsonPointer("/baz"), pointer.tail());
        assertEquals(new JsonPointer("/quux"), pointer);
    }

    @Test
    public void testParseIndex() {
        assertEquals(-1, JsonPointer._parseIndex(null));
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(1, JsonPointer._parseIndex("1"));
        assertEquals(2, JsonPointer._parseIndex("2"));
        assertEquals(-1, JsonPointer._parseIndex("3"));
        assertEquals(-1, JsonPointer._parseIndex("10"));
        assertEquals(-1, JsonPointer._parseIndex("100"));
        assertEquals(-1, JsonPointer._parseIndex("1000"));
        assertEquals(-1, JsonPointer._parseIndex("10000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIndexLong() {
        JsonPointer._parseIndex("10000000000");
    }
}