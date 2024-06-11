import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entity
 assertTrue(entities.isNamedEntity("lt"));
 // Test unknown named entity
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entity
 assertEquals('<', entities.getCharacterByName("lt"));
 // Test unknown named entity
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 // Test simple string
 assertEquals("&lt;", entities.escape("<<test>>", null, Entities.Entities.Extended));
 // Test empty string
 assertEquals("", entities.escape("", null, Entities.Entities.Extended));
 // Test null input
 assertNull(entities.escape(null, null, Entities.Entities.Extended));
 // Test input with already escaped characters
 assertEquals("&amp;lt;", entities.escape("&lt;<test>", null, Entities.Entities.Extended));
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 // Test simple string
 assertEquals("<<test>>", entities.unescape("&lt;test&gt;"));
 // Test empty string
 assertEquals("", entities.unescape(""));
 // Test null input
 assertNull(entities.unescape(null));
 // Test string with unescaped characters
 assertEquals("&lt;test&gt;", entities.unescape("&amp;lt;test&amp;gt;"));
 // Test string with invalid escape sequence
 assertEquals("&test;", entities.unescape("&test;"));
 }

 @Test
 public void testLoadEntities() {
 Entities entities = new Entities();
 try {
 Properties properties = new Properties();
 InputStream in = Entities.class.getResourceAsStream("/entities-custom.properties");
 properties.load(in);
 in.close();
 Map<String, Character> customEntities = entities.loadEntities("/entities-custom.properties");
 // Test known named entity
 Character entity = customEntities.get("lt");
 assertEquals('<', entity.charValue());
 } catch (IOException e) {
 fail("Failed to load custom entities file: " + e.getMessage());
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingEntitiesFile() {
 Entities entities = new Entities();
 entities.loadEntities("/notfound.properties");
 }
}