import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities in base set
 assertEquals('&', entities.getCharacterByName("amp"));
 assertFalse(entities.isNamedEntity("amp;")); // entity with semicolon is not a named entity
 // Test known named entities in extended set
 assertEquals('\u00A9', entities.getCharacterByName("copyright"));
 // Test unknown named entities
 assertNull(entities.getCharacterByName("unknown"));
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testEscapeAndUnescape() {
 Entities entities = new Entities();
 // Test escape and unescape with base mode
 String input = "&amp; &lt; &gt; &apos; &quot;";
 String output = entities.escape(input, null, Entities.EscapeMode.base);
 String unescapedOutput = entities.unescape(output);
 assertEquals(input, unescapedOutput);

 // Test escape and unescape with extended mode
 input = "&copy; &trade; &euro;";
 output = entities.escape(input, null, Entities.EscapeMode.extended);
 unescapedOutput = entities.unescape(output);
 assertEquals(input, unescapedOutput);
 }

 @Test(expected = NullPointerException.class)
 public void testNullStringInput() {
 Entities entities = new Entities();
 entities.escape(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Entities entities = new Entities();
 try {
 entities.EscapeMode.valueOf("invalid");
 } catch (IllegalArgumentException e) {
 }
 }
}