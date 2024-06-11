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
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertTrue(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo", pointer.toString());
 }

 @Test
 public void testPointerWithProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertEquals("/foo/bar", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 fail("Should have been able to parse input: " + input);
 }
 assertTrue(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertTrue(pointer.mayMatchElement());
 assertEquals("/foo/0", pointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String input = "~foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected
 }
 fail("Should have thrown IllegalArgumentException: " + input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointerWithSlash() {
 String input = "/~foo";
 JsonPointer pointer = null;
 try {
 pointer = JsonPointer.compile(input);
 } catch (IllegalArgumentException e) {
 // expected
 }
 fail("Should have thrown IllegalArgumentException: " + input);
 }

}