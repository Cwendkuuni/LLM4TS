import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("amp"));
 assertTrue(entities.isNamedEntity("apos"));
 assertTrue(entities.isNamedEntity("quot"));
 // Test unknown named entities
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entities
 assertEquals('&', entities.getCharacterByName("amp"));
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('>', entities.getCharacterByName("gt"));
 assertEquals("'", entities.getCharacterByName("apos"));
 assertEquals("\"", entities.getCharacterByName("quot"));
 // Test unknown named entity
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String input = "This is a test <>&'\"";
 String expectedOutput = "This is a test &lt;&gt;&amp;&apos;&quot;";
 String actualOutput = entities.escape(input, outputSettings);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeWithCustomEncoderAndEscapeMode() {
 Entities entities = new Entities();
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 Document.OutputSettings outputSettings = new Document.OutputSettings(encoder, Entities.Entities.EscapeMode.extended);
 String input = "This is a test <>&'\"";
 String expectedOutput = "This is a test &lt;&gt;&amp;&apos;&quot;";
 String actualOutput = entities.escape(input, outputSettings);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "This is a test &lt;&gt;&amp;&apos;&quot;";
 String expectedOutput = "This is a test <>&'\"";
 String actualOutput = entities.unescape(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testUnescapeWithCustomPattern() {
 Entities entities = new Entities();
 String input = "This is a test &lt;&gt;&amp;&apos;&quot;";
 boolean strict = false;
 String expectedOutput = "This is a test <>&'\"";
 String actualOutput = entities.unescape(input, strict);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidInput() {
 Entities entities = new Entities();
 String input = "This is a test &%&";
 String expectedExceptionMessage = "Invalid entity: %";
 String actualOutput = entities.unescape(input);
 fail("Expected exception: " + expectedExceptionMessage);
 }
}