import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals("", emptyPointer.toString());
 assertEquals(emptyPointer, emptyPointer.tail());
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 }

 @Test
 public void testBasicPointer() {
 JsonPointer pointer = JsonPointer.compile("/foo/bar");
 assertEquals("/foo/bar", pointer.toString());
 assertNull(pointer.tail());
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testPointerWithMatchingElement() {
 JsonPointer pointer = JsonPointer.compile("/foo/0");
 assertEquals("/foo/0", pointer.toString());
 assertNull(pointer.tail());
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("baz");
 }

 @Test
 public void testTailMethod() {
 JsonPointer pointer = JsonPointer.compile("/foo/0/baz");
 assertEquals("/foo/0", pointer.tail().toString());
 assertEquals("/baz", pointer.toString());
 }

 @Test
 public void testMatchingProperty() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/0");
 JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
 assertNull(pointer1.matchProperty("bar"));
 assertEquals(JsonPointer.EMPTY, pointer2.matchProperty("foo"));
 }

 @Test
 public void testMatchingElement() {
 JsonPointer pointer1 = JsonPointer.compile("/foo/0");
 JsonPointer pointer2 = JsonPointer.compile("/foo/1");
 assertNull(pointer1.matchElement(1));
 assertEquals(JsonPointer.EMPTY, pointer2.matchElement(0));
 }
}