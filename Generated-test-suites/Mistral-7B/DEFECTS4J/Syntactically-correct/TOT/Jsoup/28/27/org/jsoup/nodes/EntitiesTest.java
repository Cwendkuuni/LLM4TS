import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();

 // Test known named entities
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("amp"));
 assertTrue(entities.isNamedEntity("quot"));
 assertTrue(entities.isNamedEntity("apos"));
 assertTrue(entities.isNamedEntity("gt"));

 // Test unknown named entities
 assertFalse(entities.isNamedEntity("unknown"));

 // Test empty string
 assertFalse(entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();

 // Test known named entities
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('&', entities.getCharacterByName("amp"));
 assertEquals('\"', entities.getCharacterByName("quot"));
 assertEquals("'", entities.getCharacterByName("apos"));
 assertEquals('>', entities.getCharacterByName("gt"));

 // Test unknown named entities
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 String input = "&lt;Hello&amp; World&gt;";
 Document.OutputSettings out = new Document.OutputSettings(CharsetEncoder.forUnicode(), Entities.EscapeMode.extended);
 String expectedOutput = "&lt;&amp;lt;Hello&amp;amp;amp;amp;amp;lt;World&amp;gt;";
 String output = entities.escape(input, out);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "&lt;&amp;lt;Hello&amp;amp;amp;amp;amp;lt;World&amp;gt;";
 String expectedOutput = "<<HelloWorld>";
 String output = entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullString() {
 Entities entities = new Entities();
 String input = null;
 entities.unescape(input);
 }

 @Test
 public void testIsNamedEntityWithEmptyString() {
 Entities entities = new Entities();
 String input = "";
 assertFalse(entities.isNamedEntity(input));
 }
}