import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyJsonPointer() {
 JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
 assertTrue(emptyJsonPointer.matches());
 assertFalse(emptyJsonPointer.mayMatchProperty());
 assertFalse(emptyJsonPointer.mayMatchElement());
 assertNull(emptyJsonPointer.tail());
 assertEquals("", emptyJsonPointer.toString());
 }

 @Test
 public void testJsonPointerCompilation() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/0/b/1");
 assertTrue(jsonPointer.matches());
 assertTrue(jsonPointer.mayMatchProperty());
 assertTrue(jsonPointer.mayMatchElement());
 assertNull(jsonPointer.tail());
 assertEquals("/a/0/b/1", jsonPointer.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputJsonPointerCompilation() {
 JsonPointer jsonPointer = JsonPointer.compile("a/0/b/1");
 fail("Expected IllegalArgumentException: Invalid input: JSON Pointer expression must start with '/': \"a/0/b/1\"");
 }

 @Test
 public void testJsonPointerTail() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/0/b/1");
 JsonPointer tailJsonPointer = jsonPointer.tail();
 assertTrue(tailJsonPointer.matches());
 assertFalse(tailJsonPointer.mayMatchProperty());
 assertFalse(tailJsonPointer.mayMatchElement());
 assertNull(tailJsonPointer.tail());
 assertEquals("/b/1", tailJsonPointer.toString());
 }

 @Test
 public void testJsonPointerMatchProperty() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/0/b/1");
 String propertyName = "b";
 JsonPointer matchingJsonPointer = jsonPointer.matchProperty(propertyName);
 assertTrue(matchingJsonPointer.matches());
 assertTrue(matchingJsonPointer.mayMatchProperty());
 assertTrue(matchingJsonPointer.mayMatchElement());
 assertNull(matchingJsonPointer.tail());
 assertEquals("/a/0/b", matchingJsonPointer.toString());
 assertNull(jsonPointer.matchProperty("c"));
 }

 @Test
 public void testJsonPointerMatchElement() {
 JsonPointer jsonPointer = JsonPointer.compile("/a/0/b/1");
 int index = 1;
 JsonPointer matchingJsonPointer = jsonPointer.matchElement(index);
 assertTrue(matchingJsonPointer.matches());
 assertTrue(matchingJsonPointer.mayMatchProperty());
 assertTrue(matchingJsonPointer.mayMatchElement());
 assertNull(matchingJsonPointer.tail());
 assertEquals("/a/0/b", matchingJsonPointer.toString());
 assertNull(jsonPointer.matchElement(0));
 }
}