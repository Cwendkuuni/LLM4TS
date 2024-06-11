import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertEquals(emptyPointer, emptyPointer.tail());
 assertNull(emptyPointer.matches());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.toString());
 assertNull(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 }

 @Test
 public void testMatchedPointer() {
 String input = "/foo/0";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected
 }
 JsonPointer matchedPointer = new JsonPointer("/foo", "0", JsonPointer.EMPTY);
 assertEquals(matchedPointer.toString(), pointer.toString());
 assertSame(matchedPointer, pointer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer.compile("invalid");
 }

 @Test
 public void testLongInput() {
 String input = "/foo/bar/baz/123/qux";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(input, pointer.toString());
 assertNull(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(123, pointer.getMatchingIndex());
 assertNull(pointer.tail());
 }

 @Test
 public void testNullPointer() {
 JsonPointer nullPointer = null;
JsonPointer pointer = new JsonPointer("/foo", "bar", nullPointer);
 assertEquals("/foo/bar", pointer.toString());
 assertNull(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertSame(nullPointer, pointer.tail());
 }

 @Test
 public void testMatchProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 String property = "bar";
 JsonPointer matchingPointer = JsonPointer.compile("/foo/bar");
 JsonPointer matchedPointer = pointer.matchProperty(property);
 assertEquals(matchingPointer, matchedPointer);
 assertNull(pointer.tail());
 }

 @Test
 public void testMatchElement() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer matchingPointer = JsonPointer.compile("/foo/0");
 JsonPointer matchedPointer = pointer.matchElement(0);
 assertEquals(matchingPointer, matchedPointer);
 assertNull(pointer.tail());
 }

 @Test
 public void testNoMatchProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 String property = "baz";
 assertNull(pointer.matchProperty(property));
 }

 @Test
 public void testNoMatchElement() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer matchingPointer = JsonPointer.compile("/foo/1");
 assertNull(pointer.matchElement(0));
 assertEquals(matchingPointer, pointer.tail());
 }
}