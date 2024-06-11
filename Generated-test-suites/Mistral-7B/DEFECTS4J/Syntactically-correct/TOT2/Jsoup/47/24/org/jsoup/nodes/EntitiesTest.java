import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 private static final String IS_NAMED_ENTITY = "isNamedEntity";
 private static final String IS_BASE_NAMED_ENTITY = "isBaseNamedEntity";
 private static final String GET_CHARACTER_BY_NAME = "getCharacterByName";
 private static final String ESCAPE_TEST = "escapeTest";
 private static final String UNESCAPE_TEST = "unescapeTest";

 @Test
 public void testIsNamedEntity() {
 String namedEntity = "lt";
 assertTrue(Entities.isNamedEntity(namedEntity));
 }

 @Test
 public void testIsBaseNamedEntity() {
 String namedEntity = "amp";
 assertFalse(Entities.isBaseNamedEntity(namedEntity));
 }

 @Test
 public void testGetCharacterByName() {
 String namedEntity = "lt";
 Character result = Entities.getCharacterByName(namedEntity);
 assertEquals('<', result.charValue());
 }

 @Test
 public void testEscape() {
 String input = "&lt;<&gt; &quot; &amp; &apos; &nbsp;";
 String expectedOutput = "&amp;lt;&gt; &amp;lt;&gt; &amp;quot; &amp;amp; &amp;apos; &amp;#x200b;";
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, input, null, false, false, false);
 assertEquals(expectedOutput, actualOutput.toString());
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt;&gt; &amp;lt;&gt; &amp;quot; &amp;amp; &amp;apos; &amp;#x200b;";
 String expectedOutput = "&lt;&gt; < &quot; &amp; &apos; ";
 String result = Entities.unescape(input);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIsNamedEntityEdgeCases() {
 // Check for empty string
 assertFalse(Entities.isNamedEntity(""));
 // Check for null
 assertFalse(Entities.isNamedEntity(null));
 // Check for non-String input
 assertFalse(Entities.isNamedEntity((Object) new Object()));
 // Check for whitespace
 assertFalse(Entities.isNamedEntity(" "));
 // Check for a symbol
 assertFalse(Entities.isNamedEntity("#"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsNamedEntityWithNull() {
 Entities.isNamedEntity(null);
 }

 @Test
 public void testGetCharacterByNameEdgeCases() {
 // Check for empty string
 assertNull(Entities.getCharacterByName(""));
 // Check for null
 assertNull(Entities.getCharacterByName(null));
 // Check for non-String input
 assertNull(Entities.getCharacterByName((Object) new Object()));
 // Check for unknown entity
 assertNull(Entities.getCharacterByName("x"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameWithNull() {
 Entities.getCharacterByName(null);
 }
}