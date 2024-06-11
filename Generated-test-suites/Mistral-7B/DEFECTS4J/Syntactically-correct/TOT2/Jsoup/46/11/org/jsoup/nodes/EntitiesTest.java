import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test basic functionality
 assertEquals(true, Entities.isNamedEntity("lt"));
 // Test edge cases
 assertFalse(Entities.isNamedEntity("lt1")); // non-existent entity
 assertFalse(Entities.isNamedEntity("LT")); // case-sensitive
 // Test error scenarios
 assertNull(Entities.isNamedEntity(null)); // null input
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test basic functionality
 assertEquals(true, Entities.isBaseNamedEntity("amp"));
 // Test edge cases
 assertFalse(Entities.isBaseNamedEntity("amp1")); // non-existent entity
 assertFalse(Entities.isBaseNamedEntity("AMP")); // case-sensitive
 // Test error scenarios
 assertNull(Entities.isBaseNamedEntity(null)); // null input
 }

 @Test
 public void testGetCharacterByName() {
 // Test basic functionality
 assertEquals('<', Entities.getCharacterByName("lt"));
 // Test edge cases
 assertNull(Entities.getCharacterByName("lt1")); // non-existent entity
 // Test error scenarios
 assertThrows(IllegalArgumentException.class, () -> Entities.getCharacterByName(null)); // null input
 }

 @Test
 public void testEscapeMethodBasic() {
 StringBuilder accumulator = new StringBuilder();
 String input = "Hello & World < Tag >";
 Entities.escape(accumulator, input, null, false, false, false);
 String expectedResult = "Hello &amp; World &lt; Tag &gt;";
 assertEquals(expectedResult, accumulator.toString());
 }

 @Test
 public void testEscapeMethodEdgeCases() {
 StringBuilder accumulator = new StringBuilder();
 String input = "Hello & World < Tag &amp; Attribute = &quot;Quotation Mark&quot; >";
 Entities.escape(accumulator, input, null, false, false, false);
 String expectedResult = "Hello &amp;amp; World &amp;lt; Tag &amp;amp;amp;amp; Attribute = &amp;quot;Quotation Mark&amp;quot; &amp;gt;";
 assertEquals(expectedResult, accumulator.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodNullInput() {
 StringBuilder accumulator = new StringBuilder();
 Entities.escape(accumulator, null, null, false, false, false);
 }

 @Test
 public void testUnescapeMethodBasic() {
 String input = "Hello &amp; World &lt; Tag &gt;";
 String expectedResult = "Hello & World < Tag >";
 assertEquals(expectedResult, Entities.unescape(input, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodNullInput() {
 String input = null;
 String expectedResult = null;
 assertEquals(expectedResult, Entities.unescape(input, false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodStrictInput() {
 String input = "Hello &amp; World &lt; Tag &gt;";
 String expectedResult = null;
 assertEquals(expectedResult, Entities.unescape(input, true));
 }
}