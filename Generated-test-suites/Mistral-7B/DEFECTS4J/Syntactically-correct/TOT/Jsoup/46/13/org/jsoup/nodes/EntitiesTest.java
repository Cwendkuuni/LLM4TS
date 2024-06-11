package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full entity set
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("quot"));
 // Test for unknown named entities
 assertEquals(false, Entities.isNamedEntity("x"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test for known named entities in the base entity set
 assertEquals(true, Entities.isBaseNamedEntity("lt"));
 assertEquals(true, Entities.isBaseNamedEntity("amp"));
 // Test for unknown named entities and entities not in the base set
 assertEquals(false, Entities.isBaseNamedEntity("quot"));
 assertEquals(false, Entities.isBaseNamedEntity("x"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities in the full entity set
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('"', Entities.getCharacterByName("quot"));
 // Test for unknown named entities
 assertNull(Entities.getCharacterByName("x"));
 }

 @Test
 public void testEscape() {
 // Test escaping a simple string
 StringBuilder expected = new StringBuilder("&lt;Hello&amp; World&gt;");
 StringBuilder actual = new StringBuilder();
 Entities.escape(actual, "Hello<World>", Document.OutputSettings.base());
 assertEquals(expected.toString(), actual.toString());
 // Test escaping a string with attributes
 StringBuilder expectedAttribute = new StringBuilder("&lt;p class=&quot;myclass&quot;&gt;");
 StringBuilder actualAttribute = new StringBuilder();
 Entities.escape(actualAttribute, "<p class=\"myclass\">", Document.OutputSettings.base(), true, false, false);
 assertEquals(expectedAttribute.toString(), actualAttribute.toString());
 // Test escaping a string with leading and trailing whitespaces
 StringBuilder expectedWhitespace = new StringBuilder("&nbsp;&nbsp; &lt;Hello&amp; World&gt; &nbsp;&nbsp;");
 StringBuilder actualWhitespace = new StringBuilder();
 Entities.escape(actualWhitespace, "   Hello<World>   ", Document.OutputSettings.base(), false, true, true);
 assertEquals(expectedWhitespace.toString(), actualWhitespace.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.escapeMode("invalid");
 }

 @Test
 public void testUnescape() {
 // Test unescaping a simple string
 assertEquals("<Hello&World>", Entities.unescape("&lt;Hello&amp;World&gt;"));
 // Test unescaping a string with attributes
 assertEquals("<p class=\"myclass\">", Entities.unescape("&lt;p class=&quot;myclass&quot;&gt;"));
 // Test unescaping a string with leading and trailing whitespaces
 assertEquals("   <Hello&World>   ", Entities.unescape("&nbsp;&nbsp; &lt;Hello&amp;World&gt; &nbsp;&nbsp;"));
 }

 @Test
 public void testCanEncode() {
 // Test encoding ASCII characters
 assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
 // Test encoding non-ASCII characters using the default encoder
 assertTrue(Entities.canEncode(CoreCharset.utf, 'é', null));
 // Test encoding non-ASCII characters using a fallback encoder
 CharsetEncoder fallbackEncoder = StandardCharsets.UTF_8.newEncoder();
 assertTrue(Entities.canEncode(CoreCharset.fallback, 'é', fallbackEncoder));
 }
}