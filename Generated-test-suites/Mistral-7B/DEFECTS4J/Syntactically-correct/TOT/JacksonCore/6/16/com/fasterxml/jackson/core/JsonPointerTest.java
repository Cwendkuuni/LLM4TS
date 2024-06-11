import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

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
 public void testBasicPointer() {
 String pointerString = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String invalidPointerString = "~bar/baz";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(invalidPointerString);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(pointer);
 }

 @Test
 public void testPropertyMatching() {
 String pointerString = "/foo/bar/baz/0";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }

 @Test
 public void testElementMatching() {
 String pointerString = "/foo/bar[0]";
 JsonPointer pointer = JsonPointer.compile(pointerString);
 assertEquals("bar", pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 }

 @Test
 public void testTailMatching() {
 JsonPointer pointer = new JsonPointer("/foo/bar", "/bar");
 assertTrue(pointer.matches());
 assertEquals("", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 }
}