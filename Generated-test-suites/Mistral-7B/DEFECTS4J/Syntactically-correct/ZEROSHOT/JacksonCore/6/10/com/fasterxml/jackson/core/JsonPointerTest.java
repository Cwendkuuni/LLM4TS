import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertNull(pointer.tail());
 assertEquals("", pointer.toString());
 }

 @Test
 public void testConstructor() {
 String input = "/foo/bar[0]/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar[0]/baz", pointer.toString());
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNull(pointer.tail());

 JsonPointer tail = pointer.tail();
 assertEquals("/foo/bar", tail.toString());
 assertEquals("bar", tail.getMatchingProperty());
 assertEquals(-1, tail.getMatchingIndex());
 assertNull(tail.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 String input = "foo";
 JsonPointer.compile(input);
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer = new JsonPointer("/foo/bar", "bar", null);
 JsonPointer match = pointer.matchProperty("bar");
 assertEquals(pointer, match);
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer = new JsonPointer("/foo/bar[0]", "bar", null);
 JsonPointer match = pointer.matchElement(0);
 assertEquals(pointer, match);
 }

 @Test
 public void testNullProperties() {
 JsonPointer pointer = new JsonPointer("/foo/bar[0]", null, null);
 assertNull(pointer.getMatchingProperty());
 }

 @Test
 public void testNullTail() {
 JsonPointer pointer = new JsonPointer("/foo/bar", "bar", null);
 assertNull(pointer.tail());
 }
}