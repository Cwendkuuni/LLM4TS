import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> map = EscapeMode.extended.getMap();
 // Test known named entities
 for (Map.Entry<Character, String> entry : map.entrySet()) {
 assertTrue(isNamedEntity(entry.getValue()));
 }

 // Test unknown named entities
 assertFalse(isNamedEntity("unknownEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 Character lt = getCharacterByName("lt");
 Character amp = getCharacterByName("amp");
 Character quot = getCharacterByName("quot");
 Character apos = getCharacterByName("apos");

 assertEquals('<', lt);
 assertEquals('&', amp);
 assertEquals('"', quot);
 assertEquals("'", apos);
 }

 @Test
 public void testEscape() {
 String input = "This is a &test& with <special> characters!";
 String expectedOutput = "This is a &amp;test&amp; with &lt;special&gt; characters!";
 String output = escape(input, Document.OutputSettings.xhtml());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 String input = "&lt;p&gt;This is a test &amp; this is a test.&lt;/p&gt;";
 String expectedOutput = "<p>This is a test & this is a test.</p>";
 String output = unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 try {
 EscapeMode.invalidEscapeMode = new EscapeMode(null);
 } catch (NullPointerException e) {
 fail("Expected IllegalArgumentException, but got NullPointerException");
 }

 try {
 EscapeMode.invalidEscapeMode.getMap();
 fail("Expected IllegalArgumentException, but method did not throw.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharacterInMap() {
 Map<Character, String> map = new HashMap<Character, String>();
 map.put((char) 0, "value");
 try {
 EscapeMode invalidEscapeMode = new EscapeMode(map);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMapKeyType() {
 Map<Object, String> invalidMap = new HashMap<Object, String>();
 invalidMap.put(123, "value");
 try {
 EscapeMode invalidEscapeMode = new EscapeMode(invalidMap);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> loadedEntities = loadEntities("entities-base.properties");
 Character apostrophe = loadedEntities.get("apos");
 Character ampersand = loadedEntities.get("amp");
 Character lessThan = loadedEntities.get("lt");
 Character greaterThan = loadedEntities.get("gt");

 assertEquals("'", apostrophe);
 assertEquals("&", ampersand);
 assertEquals("<", lessThan);
 assertEquals(">", greaterThan);
 }
}