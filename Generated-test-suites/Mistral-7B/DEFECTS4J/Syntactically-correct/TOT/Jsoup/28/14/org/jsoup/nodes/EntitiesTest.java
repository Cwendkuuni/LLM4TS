import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test known named entities in the full set
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("quot"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));

 // Test empty string
 assertFalse(Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities in the full set
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals("'", Entities.getCharacterByName("apos"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 String string = "&lt;&gt;&amp;&apos;&quot;";
 Document.OutputSettings out = new Document.OutputSettings();
 Character expected[] = { '<', '>', '&', '\'' , '"' };
 String escaped = Entities.escape(string, out.encoder(), Entities.Entities.extended);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i].toString(), String.valueOf(escaped.charAt(i)));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscape_MalformedInput() {
 String string = "&lt<gt";
 Document.OutputSettings out = new Document.OutputSettings();
 Entities.escape(string, out.encoder(), Entities.Entities.extended);
 }

 @Test
 public void testUnescape() {
 String string = "&lt;&gt;&amp;&apos;&quot;";
 String unescaped = Entities.unescape(string);
 String expected = "<>&";
 assertEquals(expected, unescaped);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescape_MalformedInput() {
 String string = "&lt<gt";
 String unescaped = Entities.unescape(string);
 }

 @Test
 public void testIsNamedEntityWithXHTML() {
 // Test known named entities in the xhtml set
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("gt"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));

 // Test empty string
 assertFalse(Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByNameWithXHTML() {
 // Test known named entities in the xhtml set
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals("'", Entities.getCharacterByName("apos"));
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('>', Entities.getCharacterByName("gt"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }
}