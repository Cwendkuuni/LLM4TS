import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method for known entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("quot"));

 // Test isNamedEntity method for unknown entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test isBaseNamedEntity method for known entities in base set
 assertTrue(Entities.isBaseNamedEntity("lt"));
 assertTrue(Entities.isBaseNamedEntity("amp"));
 assertTrue(Entities.isBaseNamedEntity("quot"));

 // Test isBaseNamedEntity method for unknown entities
 assertFalse(Entities.isBaseNamedEntity("gt"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method for known entities
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test getCharacterByName method for unknown entities
 assertNull(Entities.getCharacterByName("gt"));
 }

 @Test
 public void testEscapeMethod() {
 // Test escape method with default settings
 String input = "&<>";
 String expectedOutput = "&amp;&lt;&gt;";
 String output = Entities.escape(input, null);
 assertEquals(expectedOutput, output);

 // Test escape method with XHTML settings
 input = "&<>";
 expectedOutput = "&amp;lt;>&gt;";
 String xhtmlOutput = Entities.escape(input, Entities.EscapeMode.xhtml);
 assertEquals(expectedOutput, xhtmlOutput);
 }

 @Test
 public void testUnescapeMethod() {
 // Test unescape method with strict mode
 String input = "&amp;lt;>&gt;&quot;";
 String expectedOutput = "<>\"";
 String strictOutput = Entities.unescape(input, true);
 assertEquals(expectedOutput, strictOutput);

 // Test unescape method with non-strict mode
 input = "&amp;lt;>&gt;&quot;";
 expectedOutput = "<>\"";
 String nonStrictOutput = Entities.unescape(input, false);
 assertEquals(expectedOutput, nonStrictOutput);
 }
}