import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

 @Test
 public void testEmptyPointer() {
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertTrue(emptyPointer.matches());
 assertNull(emptyPointer.getMatchingProperty());
 assertNull(emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.tail());
 assertEquals("", emptyPointer.toString());
 }

 @Test
 public void testConstructNonEmptySegment() {
 JsonPointer pointer1 = new JsonPointer("/property1", "", null);
 assertTrue(pointer1.matches());
 assertEquals("property1", pointer1.getMatchingProperty());
 assertNull(pointer1.getMatchingIndex());
 assertNull(pointer1.tail());
 assertEquals("/property1", pointer1.toString());
 }

 @Test
 public void testConstructWithNextSegment() {
 String input = "/property1/property2";
 JsonPointer pointer1 = new JsonPointer(input, "property1", null);
 JsonPointer pointer2 = new JsonPointer(input, "property2", pointer1);
 assertEquals(pointer2, pointer1.tail());
 assertFalse(pointer1.matches());
 assertEquals("property1", pointer1.getMatchingProperty());
 assertEquals(-1, pointer1.getMatchingIndex());
 assertEquals("property2", pointer2.getMatchingProperty());
 assertNull(pointer2.getMatchingIndex());
 assertEquals("/property1/property2", pointer1.toString());
 assertEquals("/property2", pointer2.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidInputWithNoSlash() {
 String input = "property1";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test
 public void testCompile() {
 String input = "/property1/property2/0";
 JsonPointer pointer = JsonPointer.compile(input);
 assertEquals(pointer, new JsonPointer("/property1/property2/0", "property2", new JsonPointer("/0", "", null)));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompileWithEmptyInput() {
 String input = "";
 JsonPointer pointer = JsonPointer.compile(input);
 }

 @Test
 public void testMatchProperty() {
 JsonPointer pointer1 = new JsonPointer("/property1", "", null);
 JsonPointer pointer2 = new JsonPointer("/property1/property2", "property2", pointer1);
 JsonPointer matchingPointer = new JsonPointer("/property1/property2", "property2", null);
 assertTrue(pointer2.matchProperty("property2").equals(matchingPointer));
 assertNull(pointer1.matchProperty("property2"));
 }

 @Test
 public void testMatchElement() {
 JsonPointer pointer1 = new JsonPointer("/array/0/0", "", null);
 JsonPointer pointer2 = new JsonPointer("/array/0/1", "", null);
 JsonPointer matchingPointer = new JsonPointer("/array/0/0", "", null);
 assertTrue(pointer1.matchElement(0).equals(matchingPointer));
 assertNull(pointer2.matchElement(0));
 }
}