import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private Map<Character, String> getMap(Entities.EscapeMode escapeMode) {
 return escapeMode.getMap();
 }

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> fullMap = Entities.full;
 for (Map.Entry<String, Character> entry : fullMap.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertTrue(Entities.isNamedEntity(key));
 }
}

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> xhtmlMap = Entities.xhtmlByVal;
 for (Map.Entry<Character, String> entry : xhtmlMap.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertTrue(Entities.isBaseNamedEntity(value));
 }
}

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> fullMap = Entities.full;
 for (Map.Entry<String, Character> entry : fullMap.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertEquals(value, Entities.getCharacterByName(key));
 }
}

 @Test
 public void testEscapeWithDefaultSettings() {
 String input = "&lt; &gt; &amp; &quot; &apos; Ää Öö Åå Ää Öö";
 String expected = "&amp;lt; &amp;gt; &amp;amp; &amp;quot; &amp;apos; &Auml; &Aring; &Ouml; &Ouml; &Auml; &Ouml;";
 StringBuilder actual = new StringBuilder();
 Entities.escape(actual, input, null);
 assertEquals(expected, actual.toString());
}

 @Test
 public void testEscapeWithXhtmlSettings() {
 String input = "&lt; &gt; &amp; &quot; &apos; Ää Öö Åå Ää Öö";
 StringBuilder actual = new StringBuilder();
 Entities.escape(actual, input, new Document.OutputSettings(Entities.EscapeMode.xhtml));
 String expected = "&lt; &gt; &amp; &quot; &apos;";
 assertEquals(expected, actual.toString());
}

 @Test
 public void testUnescape() {
 String input = "&lt; &gt; &amp; &quot; &apos; &Auml; &Aring; &Ouml; &Ouml; &Auml; &Ouml;";
 String expected = "< > & \" ' Å Ä Ö Å Å Å";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);
}

 @Test
 public void testCanEncode() {
 Map<Character, String> fullMap = Entities.full;
 Map<Character, String> utfMap = Entities.CoreCharset.utf.getMap();
 for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
 Character c = entry.getValue();
 assertTrue(Entities.canEncode(Entities.CoreCharset.utf, c, null));
 }
}

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testCanEncodeWithNonUTFCharacter() {
 char c = 0xC2;
 assertFalse(Entities.canEncode(Entities.CoreCharset.utf, c, null));
}

}