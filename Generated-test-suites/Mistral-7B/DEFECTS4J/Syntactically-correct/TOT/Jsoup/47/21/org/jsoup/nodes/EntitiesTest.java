import org.jsoup.Nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();

 // Test true cases
 Assert.assertTrue(entities.isNamedEntity("lt"));
 Assert.assertTrue(entities.isNamedEntity("amp"));
 Assert.assertTrue(entities.isNamedEntity("quot"));

 // Test false cases
 Assert.assertFalse(entities.isNamedEntity("gt")); // not in base set
 Assert.assertFalse(entities.isNamedEntity("abc")); // not a named entity
 }

 @Test
 public void testIsBaseNamedEntity() {
 Entities entities = new Entities();

 // Test true cases (base set)
 Assert.assertTrue(entities.isBaseNamedEntity("lt"));
 Assert.assertTrue(entities.isBaseNamedEntity("amp"));
 Assert.assertTrue(entities.isBaseNamedEntity("quot"));
 Assert.assertTrue(entities.isBaseNamedEntity("nbsp")); // test whitespace entity

 // Test false cases (not in base set)
 Assert.assertFalse(entities.isBaseNamedEntity("gt"));
 Assert.assertFalse(entities.isBaseNamedEntity("abc"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();

 // Test known entities
 Assert.assertEquals('<', entities.getCharacterByName("lt"));
 Assert.assertEquals('&', entities.getCharacterByName("amp"));
 Assert.assertEquals('"', entities.getCharacterByName("quot"));

 // Test unknown entities
 Assert.assertNull(entities.getCharacterByName("gt"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 Document.OutputSettings out = new Document.OutputSettings();
 out.setEscapeMode(Entities.EscapeMode.base);
 StringBuilder accum = new StringBuilder();

 // Test simple characters
 accum.append(entities.escape("Hello World!", out));
 Assert.assertEquals("Hello World!", accum.toString());

 accum.setLength(0);
 accum.append(entities.escape("&", out));
 Assert.assertEquals("&amp;", accum.toString());

 // Test whitespace
 accum.setLength(0);
 accum.append(entities.escape(" Hello World! ", out));
 Assert.assertEquals(" Hello World! ", accum.toString());

 // Test special characters
 accum.setLength(0);
 accum.append(entities.escape("&lt;Hello World&gt;", out));
 Assert.assertEquals("&lt;Hello World&gt;", accum.toString());
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();

 // Test simple entities
 String str = entities.unescape("&amp;");
 Assert.assertEquals("&", str);

 str = entities.unescape("&lt;");
 Assert.assertEquals("<", str);

 str = entities.unescape("&gt;");
 Assert.assertEquals(">", str);

 str = entities.unescape("&quot;");
 Assert.assertEquals("\"", str);

 // Test hex entities
 str = entities.unescape("&#x00022;");
 Assert.assertEquals("\"", str);

 // Test illegal entity
 str = entities.unescape("&#x00023;"); // does not exist in standard entities
 Assert.assertEquals("&#x00023;", str);
 }
}