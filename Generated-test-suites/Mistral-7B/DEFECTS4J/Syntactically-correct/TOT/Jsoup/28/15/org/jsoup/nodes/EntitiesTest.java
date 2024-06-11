import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> fullEntities = Entities.full;
 //assertTrue(Entities.isNamedEntity("lt"));
 // assertTrue(Entities.isNamedEntity("amp"));
 // assertTrue(Entities.isNamedEntity("quot"));
 // assertTrue(Entities.isNamedEntity("apos"));
 // assertTrue(Entities.isNamedEntity("gt"));
 // false entities
 assertFalse(Entities.isNamedEntity("nonexistent"));
 // check xhtml entities
 assertTrue(Entities.isNamedEntity(Entities.Entities.xhtml.getMap().get('&'))); // amp
 assertTrue(Entities.isNamedEntity(Entities.Entities.xhtml.getMap().get('&apos;'))); // apos
 assertTrue(Entities.isNamedEntity(Entities.Entities.xhtml.getMap().get('&quot;'))); // quot
 assertTrue(Entities.isNamedEntity(Entities.Entities.xhtml.getMap().get('<'))); // lt
 assertTrue(Entities.isNamedEntity(Entities.Entities.xhtml.getMap().get('>'))); // gt
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> fullEntities = Entities.full;
 // assertTrue(Entities.getCharacterByName("lt") == '<');
 // assertTrue(Entities.getCharacterByName("amp") == '&');
 // assertTrue(Entities.getCharacterByName("quot") == '\"');
 // assertTrue(Entities.getCharacterByName("apos") == '\'');
 // assertTrue(Entities.getCharacterByName("gt") == '>');
 // false entity
 assertNull(Entities.getCharacterByName("nonexistent"));
 }

 @Test
 public void testEscape() {
 Map<Character, String> xhtmlByVal = Entities.Entities.xhtml.getMap();
 Map<Character, String> baseByVal = Entities.Entities.base.getMap();
 Map<Character, String> extendedByVal = Entities.Entities.extended.getMap();
 String testString = "&lt; &amp; &apos; &quot; &gt; &amp;amp; &amp;lt; &amp;gt; &nbsp;";
 Character xhtmlLt = xhtmlByVal.get('<');
 Character xhtmlAmp = xhtmlByVal.get('&');
 Character xhtmlApos = xhtmlByVal.get('\'');
 Character xhtmlQuot = xhtmlByVal.get('"');
 Character baseLt = baseByVal.get('<');
 Character baseAmp = baseByVal.get('&');
 Character baseApos = baseByVal.get('\'');
 Character baseQuot = baseByVal.get('"');
 Character extendedAmp = extendedByVal.get("&amp;");
 Character extendedLt = extendedByVal.get("<");
 Character extendedGt = extendedByVal.get(">");
 Character nbsp = 0x00A0;

 String escapedXhtml = Entities.escape(testString, null, Entities.Entities.xhtml);
 String escapedBase = Entities.escape(testString, null, Entities.Entities.base);
 String escapedExtended = Entities.escape(testString, null, Entities.Entities.extended);

 assertEquals("<&amp;lt; &amp;amp; &amp;apos; &amp;quot; &amp;amp;gt; &amp;amp;amp; &amp;amp;lt; &amp;amp;gt; &nbsp;", escapedXhtml);
 assertEquals("&lt; &amp; &apos; &quot; &gt; &amp;amp; &amp;lt; &amp;gt; &nbsp;", escapedBase);
 assertEquals("&#60; &amp;amp; &#62; &amp;amp; &#39; &amp;quot; &amp;amp; &#62; &amp;amp; &amp;amp; &#60; &amp;amp; &amp;amp; &#62; &nbsp;", escapedExtended);

 assertEquals(xhtmlLt + baseAmp + xhtmlApos + baseQuot + extendedAmp + xhtmlLt + extendedGt + nbsp,
 Entities.unescape(escapedXhtml));
 assertEquals(xhtmlLt + baseAmp + xhtmlApos + baseQuot + baseAmp + baseLt + baseGt + nbsp,
 Entities.unescape(escapedBase));
 assertEquals(xhtmlLt + baseAmp + xhtmlApos + baseQuot + extendedAmp + extendedLt + extendedGt + nbsp,
 Entities.unescape(escapedExtended));
 }

}