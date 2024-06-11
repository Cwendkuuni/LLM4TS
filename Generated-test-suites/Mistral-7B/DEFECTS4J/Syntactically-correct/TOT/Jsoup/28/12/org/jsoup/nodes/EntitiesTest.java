import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities
 Assert.assertTrue(entities.isNamedEntity("lt"));
 Assert.assertTrue(entities.isNamedEntity("amp"));
 Assert.assertTrue(entities.isNamedEntity("apos"));
 Assert.assertTrue(entities.isNamedEntity("quot"));
 // Test unknown named entity
 Assert.assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entities
 Assert.assertEquals('<', entities.getCharacterByName("lt"));
 Assert.assertEquals('&', entities.getCharacterByName("amp"));
 Assert.assertEquals('\'', entities.getCharacterByName("apos"));
 Assert.assertEquals('"', entities.getCharacterByName("quot"));
 // Test unknown named entity
 Assert.assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscapeAndUnescape() {
 Entities entities = new Entities();
 // Test simple string
 String input = "Hello World!";
 String output = entities.escape(input, Entities.Document.OutputSettings.base(), Entities.EscapeMode.base);
 String unescaped = entities.unescape(output);
 Assert.assertEquals(input, unescaped);

 // Test named entities
 input = "Hello &lt;world&gt;!";
 output = entities.escape(input, Entities.Document.OutputSettings.base(), Entities.EscapeMode.base);
 unescaped = entities.unescape(output);
 Assert.assertEquals("Hello <world>!", unescaped);

 // Test numeric entities
 input = "Hello &#10;world!";
 output = entities.escape(input, Entities.Document.OutputSettings.base(), Entities.EscapeMode.base);
 unescaped = entities.unescape(output);
 Assert.assertEquals("Hello \nworld!", unescaped);

 // Test invalid entities
 input = "Hello &notdefined;world!";
 output = entities.escape(input, Entities.Document.OutputSettings.base(), Entities.EscapeMode.base);
 unescaped = entities.unescape(output);
 Assert.assertEquals("Hello &notdefined;world!", output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Entities entities = new Entities();
 entities.escape("", null, new Entities.EscapeMode("invalid"));
 }
}