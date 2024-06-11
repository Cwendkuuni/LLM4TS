import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test known named entities
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("apos"));
 assertEquals(true, Entities.isNamedEntity("quot"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities
 assertEquals(34, Entities.getCharacterByName("quot").charValue());
 assertEquals(38, Entities.getCharacterByName("amp").charValue());
 assertEquals(39, Entities.getCharacterByName("apos").charValue());
 assertEquals(60, Entities.getCharacterByName("lt").charValue());
 assertEquals(62, Entities.getCharacterByName("gt").charValue());

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 // Test basic characters
 assertEquals("&lt;", Entities.escape("<<test>>", null, Entities.Entities.extended));
 assertEquals("&apos;", Entities.escape("'test'", null, Entities.Entities.extended));
 assertEquals("&quot;", Entities.escape("\"test\"", null, Entities.Entities.extended));

 // Test special characters
 assertEquals("&amp;", Entities.escape("&amp;test", null, Entities.Entities.extended));
 assertEquals("&apos;", Entities.escape("&apos;test", null, Entities.Entities.extended));
 assertEquals("&quot;", Entities.escape("&quot;test", null, Entities.Entities.extended));

 // Test invalid characters
 assertEquals("&#65;", Entities.escape("A\u0066est", null, Entities.Entities.extended));
 }

 @Test
 public void testUnescape() {
 // Test basic entities
 assertEquals("<<test>>", Entities.unescape("&lt;test&gt;"));
 assertEquals("'test'", Entities.unescape("&apos;test&apos;"));
 assertEquals("\"test\"", Entities.unescape("&quot;test&quot;"));

 // Test special entities
 assertEquals("&amp;", Entities.unescape("&amp;amp;"));
 assertEquals("&apos;", Entities.unescape("&apos;apos;"));
 assertEquals("&quot;", Entities.unescape("&quot;quot;"));

 // Test invalid entities
 assertEquals("A\u0066est", Entities.unescape("&#65;test"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Entities.escape("test", null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 Entities.escape(null, null, null);
 }
}