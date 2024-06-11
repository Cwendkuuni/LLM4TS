import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

 private static Entities entities;

 @BeforeClass
 public static void setup() {
 entities = new Entities();
 }

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character value = entry.getValue();
 assertTrue(Entities.isNamedEntity(name));
 assertEquals(value, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> base = Entities.baseByVal;
 for (Map.Entry<Character, String> entry : base.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertTrue(Entities.isBaseNamedEntity(value));
 assertEquals(key, Entities.getCharacterByName(value));
 }
 }

 @Test
 public void testIsNotNamedEntity() {
 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity(" "));
 assertFalse(Entities.isNamedEntity("&"));
 }

 @Test
 public void testEscape() {
 StringBuilder accum = new StringBuilder();
 String input = "&lt; &amp; &gt; &quot; < > \" &nbsp; &A0; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;\u00A0";
 Entities.escape(accum, input, null, false, false, false);
 String expected = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &amp;lt; &amp;gt; &quot; &nbsp; &A0; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt; &amp;amp; &amp;gt; &amp;quot; &lt; &gt; \" &nbsp; &#x00A0; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;";
 String expected = "< & > \" \u00A0 \u00A0 \u00A0 \u00A0 \u00A0 \u00A0 \u00A0 \u00A0";
 String output = Entities.unescape(input);
 assertEquals(expected, output);
 }

 @Test
 public void testCanEncode() {
 for (char c = 0; c < 256; c++) {
 if (Character.isSupplementaryCodePoint(c)) continue;
 CharsetEncoder encoder = Character.encodingFor(c);
 assertTrue(Entities.canEncode(Entities.CoreCharset.utf, c, encoder));
 }
 }
}