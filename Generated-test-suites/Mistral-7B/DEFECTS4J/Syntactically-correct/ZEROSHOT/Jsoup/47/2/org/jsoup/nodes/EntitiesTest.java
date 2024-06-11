import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method with valid named entities
 assertTrue(isNamedEntity("lt"));
 assertTrue(isNamedEntity("gt"));
 assertTrue(isNamedEntity("amp"));
 assertTrue(isNamedEntity("quot"));

 // Test isNamedEntity method with invalid named entities
 assertFalse(isNamedEntity("invalidEntity"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test isBaseNamedEntity method with valid named entities in base entity set
 assertTrue(isBaseNamedEntity("lt"));
 assertTrue(isBaseNamedEntity("gt"));
 assertTrue(isBaseNamedEntity("amp"));
 assertTrue(isBaseNamedEntity("quot"));

 // Test isBaseNamedEntity method with invalid named entities
 assertFalse(isBaseNamedEntity("invalidEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method with valid named entities
 assertEquals('<', getCharacterByName("lt"));
 assertEquals('>', getCharacterByName("gt"));
 assertEquals('&', getCharacterByName("amp"));
 assertEquals('"', getCharacterByName("quot"));

 // Test getCharacterByName method with invalid named entities
 assertNull(getCharacterByName("invalidEntity"));
 }

 @Test
 public void testEscapeMethod() {
 // Test escape method with different input strings, outputSettings and encoding modes
 String string1 = "This is & a < test > string with &amp; &lt; &gt; and &quot; characters.";
 String string2 = "This is a string with surrogate pair: \uD800\uDC00";
 Document.OutputSettings out1 = Document.OutputSettings.createPrettyPrint();
 Document.OutputSettings out2 = Document.OutputSettings.createPrettyPrint().escapeMode(EscapeMode.xhtml);
 Document.OutputSettings out3 = Document.OutputSettings.createPrettyPrint().encoder(StandardCharsets.UTF_8);

 StringBuilder accum1 = new StringBuilder();
 StringBuilder accum2 = new StringBuilder();
 StringBuilder accum3 = new StringBuilder();

 escape(accum1, string1, out1, false, false, false);
 escape(accum2, string1, out2, false, false, false);
 escape(accum3, string1, out3, false, false, false);

 String escapedString1 = accum1.toString();
 String escapedString2 = accum2.toString();
 String escapedString3 = accum3.toString();

 // Test the proper escaping of different characters
 assertEquals("&amp;This is &amp; a &amp;lt; &amp;gt; and &quot; characters&amp;.", escapedString1);
 assertEquals("&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&nbsp; &amp;amp; &amp;lt; &amp;gt; &amp;quot; &amp;#xA0; &amp;#x200B; &amp;#x200C; This is &amp;#x2032; a &amp;#x201C;string&amp;#x201D; with &amp;#x2033;surrogate pair: &amp;#xD800;&amp;#xDC00;&amp;#x2033;.", escapedString2);
 assertEquals("This is &amp;a&lt; &amp; &gt; and &quot; characters.", escapedString3);

 // Test escape method with empty string
 StringBuilder emptyAccum = new StringBuilder();
 escape(emptyAccum, "", out1, false, false, false);
 assertEquals("", emptyAccum.toString());
 }

 @Test
 public void testUnescapeMethod() {
 // Test unescape method with valid escaped strings
 String escapedString1 = "This is &amp; a &lt; test &gt; string with &amp;lt; &amp;gt; and &quot; characters.";
 String escapedString2 = "&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&nbsp; &amp;amp; &amp;lt; &amp;gt; &amp;quot; &amp;#xA0; &amp;#x200B; &amp;#x200C; This is &amp;#x2032; a &amp;#x201C;string&amp;#x201D; with &amp;#x2033;surrogate pair: &amp;#xD800;&amp;#xDC00;&amp;#x2033;.";
 String escapedString3 = "This is &amp;a&lt; &amp; &gt; and &quot; characters.";

 StringBuilder unescapedAccum1 = new StringBuilder();
 StringBuilder unescapedAccum2 = new StringBuilder();
 StringBuilder unescapedAccum3 = new StringBuilder();

 unescape(unescapedAccum1, escapedString1);
 unescape(unescapedAccum2, escapedString2);
 unescape(unescapedAccum3, escapedString3);

 String unescapedString1 = unescapedAccum1.toString();
 String unescapedString2 = unescapedAccum2.toString();
 String unescapedString3 = unescapedAccum3.toString();

 // Test the proper unescaping of different characters
 assertEquals("This is & a < test > string with &lt; &gt; and &quot; characters.", unescapedString1);
 assertEquals("This is <pre xmlns=\"http://www.w3.org/1998/10/REDACTED\" xml:space=\"preserve\"><xml version=\"1.0\" encoding=\"UTF-8\"></xml> &amp;amp; < & > and &quot; & #xA0; &#x200B; &#x200C; This is & #x2032; a & #x201C;string& #x201D; with & #x2033;surrogate pair: &#xD800;&#xDC00; & #x2033;.", unescapedString2);
 assertEquals("This is a <test> string with &amp;< & > and &quot; characters.", unescapedString3);
 }
}