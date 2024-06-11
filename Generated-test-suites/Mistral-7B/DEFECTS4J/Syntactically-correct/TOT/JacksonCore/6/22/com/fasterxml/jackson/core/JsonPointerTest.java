import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.mayMatchProperty());
 assertFalse(emptyPointer.mayMatchElement());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Expected valid pointer to be constructed");
 }
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String input = "~invalid";
 JsonPointer pointer = JsonPointer.compile(input);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerWithSlash() {
 String input = "/~invalid";
 JsonPointer pointer = JsonPointer.compile(input);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testPointerWithQuotedEscape() {
 String input = "/foo/bar~1/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerWithMultipleSegments() {
 String input = "/foo/bar/baz";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("baz", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 assertEquals(input, pointer.toString());
 }

 @Test
 public void testPointerFactory() {
 String input = "/foo/bar";
 JsonPointer pointer1 = JsonPointer.compile(input);
 JsonPointer pointer2 = JsonPointer.valueOf(input);
 assertEquals(pointer1, pointer2);
 }
}