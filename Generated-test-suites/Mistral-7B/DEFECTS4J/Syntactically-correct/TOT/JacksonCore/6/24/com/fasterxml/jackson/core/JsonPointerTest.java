import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertEquals("", emptyPointer.getMatchingProperty());
 assertEquals(-1, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer pointer = JsonPointer.compile("/testProperty");
 assertTrue(pointer.matches());
 assertEquals("testProperty", pointer.getMatchingProperty());
 assertNull(pointer.tail());
 assertEquals("/testProperty", pointer.toString());
 }

 @Test
 public void testPointerWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/list[0]");
 assertFalse(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertEquals(0, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list", pointer.tail().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInput() {
 JsonPointer pointer = JsonPointer.compile("invalidInput");
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testMultipleSegments() {
 JsonPointer pointer = JsonPointer.compile("/list/0/name");
 assertFalse(pointer.matches());
 assertEquals("name", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list/0", pointer.tail().toString());
 }

 @Test
 public void testQuotedSegments() {
 JsonPointer pointer = JsonPointer.compile("/list/~0/name");
 assertFalse(pointer.matches());
 assertEquals("name", pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list/~0", pointer.tail().toString());
 }

 @Test
 public void testMultipleLevels() {
 JsonPointer pointer = JsonPointer.compile("/list/0/items/1");
 assertFalse(pointer.matches());
 assertEquals("items", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list/0/items", pointer.tail().toString());
 }

 @Test
 public void testNestedListWithIndex() {
 JsonPointer pointer = JsonPointer.compile("/list/0/items[1]/name");
 assertFalse(pointer.matches());
 assertEquals("name", pointer.getMatchingProperty());
 assertEquals(1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list/0/items[1]", pointer.tail().toString());
 }

 @Test
 public void testNegativeIndex() {
 JsonPointer pointer = JsonPointer.compile("/list/-1");
 assertFalse(pointer.matches());
 assertEquals(null, pointer.getMatchingProperty());
 assertEquals(-1, pointer.getMatchingIndex());
 assertNotNull(pointer.tail());
 assertEquals("/list", pointer.tail().toString());
 }

 @Test
 public void testInvalidIndex() {
 JsonPointer pointer = JsonPointer.compile("/list/invalidIndex");
 fail("Should have thrown IllegalArgumentException");
 }
}