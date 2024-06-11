import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer pointer = JsonPointer.EMPTY;
 assertEquals("", pointer.toString());
 assertEquals(0, pointer.hashCode());
 assertNull(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertNull(pointer.getMatchingIndex());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testSimplePointer() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo", pointer.toString());
 assertEquals(1, pointer.hashCode());
 assertFalse(pointer.matches());
 assertEquals("foo", pointer.getMatchingProperty());
 assertFalse(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testPointerWithProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/bar", pointer.toString());
 assertEquals(2, pointer.hashCode());
 assertFalse(pointer.matches());
 assertEquals("bar", pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchProperty());
 assertFalse(pointer.mayMatchElement());
 assertNull(pointer.tail());
 }

 @Test
 public void testPointerWithIndex() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals("/foo/0", pointer.toString());
 assertEquals(3, pointer.hashCode());
 assertFalse(pointer.matches());
 assertNull(pointer.getMatchingProperty());
 assertTrue(pointer.mayMatchElement());
 assertFalse(pointer.mayMatchProperty());
 assertNull(pointer.tail());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 String input = "~foo";
 JsonPointer pointer = JsonPointer.compile(input);
 // this line should throw an IllegalArgumentException
 }

 @Test
 public void testMatchingPointer() {
 String input = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(input);
 String json = "{\"foo\": [1, 2, 3], \"bar\": [4, 5, 6]}";
 JsonParser parser = new JsonFactory().createParser(json);
 treeNode node = parser.readValueAsTree();
 JsonPointer matchingPointer = JsonPointer.compile("/foo/bar/0");
 TreeNode result = node.at(matchingPointer);
 assertEquals(6, result.asInt());
 }

 @Test
 public void testMatchingProperty() {
 String input = "/foo/bar";
 JsonPointer pointer = JsonPointer.compile(input);
 String json = "{\"foo\": {\"bar\": 1}, \"qux\": 2}";
 JsonParser parser = new JsonFactory().createParser(json);
 treeNode node = parser.readValueAsTree();
 JsonPointer matchingPointer = JsonPointer.compile("/foo/bar");
 TreeNode result = node.at(matchingPointer);
 assertEquals(1, result.asInt());
 }

 @Test
 public void testMatchingElement() {
 String input = "/foo/0";
 JsonPointer pointer = JsonPointer.compile(input);
 String json = "[1, 2, 3]";
 JsonParser parser = new JsonFactory().createParser(json);
 treeNode node = parser.readValueAsTree();
 JsonPointer matchingPointer = JsonPointer.compile("/foo/0");
 TreeNode result = node.at(matchingPointer);
 assertEquals(1, result.asInt());
 }

 @Test
 public void testTailPointer() {
 String input = "/foo/bar/0";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer tailPointer = pointer.tail();
 assertEquals("/bar/0", tailPointer.toString());
 }

 @Test
 public void testMatchesTrue() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer matchingPointer = JsonPointer.compile("/foo");
 assertTrue(pointer.matches());
 assertTrue(pointer.matches(matchingPointer));
 }

 @Test
 public void testMatchesFalse() {
 String input = "/foo";
 JsonPointer pointer = JsonPointer.compile(input);
 JsonPointer notMatchingPointer = JsonPointer.compile("/bar");
 assertFalse(pointer.matches());
 assertFalse(pointer.matches(notMatchingPointer));
 }
}