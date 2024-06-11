import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full entity set
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("apos"));
 assertEquals(true, Entities.isNamedEntity("quot"));

 // Test for unknown named entities
 assertEquals(false, Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities in the full entity set
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('\'', Entities.getCharacterByName("apos"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test for unknown named entities
 assertEquals(null, Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 // Test for escaping a simple string
 assertEquals("&lt;Hello&amp;World&gt;", Entities.escape("<Hello&World>", null, Entities.Entities.extended));

 // Test for escaping a string with special characters
 assertEquals("&lt;img src=&quot;image.jpg&quot; alt=&amp;#x26;img&gt;", Entities.escape("<img src=\"image.jpg\" alt=\"&img;\">", null, Entities.Entities.extended));
 }

 @Test
 public void testUnescape() {
 // Test for unescaping a simple string
 assertEquals("<Hello&World>", Entities.unescape("&lt;Hello&amp;World&gt;"));

 // Test for unescaping a string with special characters
 assertEquals("<img src=\"image.jpg\" alt=\"&img;\">", Entities.unescape("&lt;img src=&quot;image.jpg&quot; alt=&amp;#x26;img&gt;"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullInput() {
 Entities.escape(null, null, Entities.Entities.extended);
 }

 @Test(expected = NullPointerException.class)
 public void testGetCharacterByNameWithNullName() {
 Entities.getCharacterByName(null);
 }
}