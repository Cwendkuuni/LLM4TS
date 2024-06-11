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
 assertEquals(0, emptyPointer.getMatchingIndex());
 assertNull(emptyPointer.getMatchingProperty());
 assertFalse(emptyPointer.matches());
 }

 @Test
 public void testSimplePointer() {
 JsonPointer simplePointer = JsonPointer.compile("/foo");
 assertEquals("/foo", simplePointer.toString());
 assertEquals(0, simplePointer.getMatchingIndex());
 assertNull(simplePointer.getMatchingProperty());
 assertFalse(simplePointer.matches());
 }

 @Test
 public void testPropertyPointer() {
 JsonPointer propertyPointer = JsonPointer.compile("/foo/bar");
 assertEquals("/foo/bar", propertyPointer.toString());
 assertEquals("bar", propertyPointer.getMatchingProperty());
 assertFalse(propertyPointer.matches());
 }

 @Test
 public void testElementPointer() {
 JsonPointer elementPointer = JsonPointer.compile("/foo/0");
 assertEquals("/foo/0", elementPointer.toString());
 assertEquals(0, elementPointer.getMatchingIndex());
 assertNull(elementPointer.getMatchingProperty());
 assertFalse(elementPointer.matches());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPointer() {
 JsonPointer invalidPointer = JsonPointer.compile("~foo");
 }

 @Test
 public void testMatchingProperty() {
 JsonPointer rootPointer = JsonPointer.compile("/foo/bar");
 JsonPointer matchingProperty = new JsonPointer("/foo/bar", "bar", null);
 assertSame(matchingProperty, rootPointer.matchProperty("bar"));
 }

 @Test
 public void testMatchingElement() {
 JsonPointer rootPointer = JsonPointer.compile("/foo/0");
 assertSame(rootPointer, rootPointer.matchElement(0));
 }

 @Test
 public void testTailingPointer() {
 JsonPointer rootPointer = JsonPointer.compile("/foo/bar");
 JsonPointer tailingPointer = rootPointer.tail();
 assertEquals("/foo", tailingPointer.toString());
 }

 @Test
 public void testNestedPointer() {
 JsonPointer rootPointer = JsonPointer.compile("/foo/bar/baz");
 JsonPointer propertyPointer = rootPointer.matchProperty("bar");
 JsonPointer elementPointer = propertyPointer.matchElement(0);
 assertSame(elementPointer, rootPointer.matchElement(2));
 }

 @Test
 public void testMatches() {
 JsonPointer rootPointer = JsonPointer.compile("/foo/bar/baz");
 JsonPointer propertyPointer = JsonPointer.compile("/foo/bar");
 JsonPointer elementPointer = JsonPointer.compile("/foo/bar/0");
 JsonPointer emptyPointer = JsonPointer.EMPTY;
 assertEquals(true, rootPointer.matches());
 assertEquals(true, propertyPointer.matches());
 assertEquals(true, elementPointer.matches());
 assertEquals(false, emptyPointer.matches());
 }
}