import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testCompileValidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "/foo/bar/baz[0]";
 JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
 assertTrue(jsonPointer.matches());
 assertEquals("foo", jsonPointer.getMatchingProperty());
 assertEquals(0, jsonPointer.getMatchingIndex());
 assertFalse(jsonPointer.mayMatchProperty());
 assertFalse(jsonPointer.mayMatchElement());
 JsonPointer tail = jsonPointer.tail();
 assertEquals("/bar/baz[0]", tail.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileInvalidJsonPointer() throws IllegalArgumentException {
 String jsonPointerString = "~foo/bar/baz[0]";
 JsonPointer.compile(jsonPointerString); //should throw IllegalArgumentException
 }

 @Test
 public void testMatchProperty() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar", null);
 assertTrue(jsonPointer.matchesProperty("bar"));
 assertNull(jsonPointer.tail());
 }

 @Test
 public void testMatchElement() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar[0]", null);
 assertTrue(jsonPointer.matchesElement(0));
 assertNull(jsonPointer.tail());
 }

 @Test
 public void testTail() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar/baz[0]", null);
 JsonPointer tail = jsonPointer.tail();
 assertEquals("/bar/baz[0]", tail.toString());
 }

 @Test
 public void testToString() {
 JsonPointer jsonPointer = new JsonPointer("/foo/bar/baz[0]", null);
 assertEquals("/foo/bar/baz[0]", jsonPointer.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/baz[0]", null);
 JsonPointer jsonPointer2 = new JsonPointer("/foo/bar/baz[0]", null);
 JsonPointer jsonPointer3 = new JsonPointer("/foo/bar/baz[1]", null);
 assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
 assertEquals(jsonPointer1, jsonPointer1);
 assertEquals(jsonPointer1, jsonPointer2);
 assertNotEquals(jsonPointer1, jsonPointer3);
 }

}