package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;

 // Test known named entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("quot"));

 // Test non-named entities
 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity("asdf"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> base = Entities.base;
 Map<Character, String> baseByVal = Entities.baseByVal;

 // Test known base named entities
 assertTrue(Entities.isBaseNamedEntity("amp"));
 assertTrue(Entities.isBaseNamedEntity("lt"));
 assertTrue(Entities.isBaseNamedEntity("quot"));

 // Test non-base named entities
 assertFalse(Entities.isBaseNamedEntity(""));
 assertFalse(Entities.isBaseNamedEntity("asdf"));

 // Test entities not present in base set
 assertFalse(Entities.isBaseNamedEntity("gt"));
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> full = Entities.full;

 // Test known named entities
 assertEquals(Character.forInt(0x003C), Entities.getCharacterByName("lt"));
 assertEquals(Character.forInt(0x0026), Entities.getCharacterByName("amp"));
 assertEquals(Character.forInt(0x0022), Entities.getCharacterByName("quot"));

 // Test non-named entities
 assertNull(Entities.getCharacterByName(""));
 assertNull(Entities.getCharacterByName("asdf"));
 }

 @Test
 public void testEscape() {
 Map<Character, String> mapXhtml = Entities.EscapeMode.xhtml.getMap();
 Map<Character, String> mapBase = Entities.EscapeMode.base.getMap();
 Map<Character, String> mapExtended = Entities.EscapeMode.extended.getMap();
 Document.OutputSettings outputSettingsXhtml = new Document.OutputSettings(Entities.EscapeMode.xhtml);
 Document.OutputSettings outputSettingsBase = new Document.OutputSettings(Entities.EscapeMode.base);
 Document.OutputSettings outputSettingsExtended = new Document.OutputSettings(Entities.EscapeMode.extended);

 String input = "&amp;lt;img src=\";alt=\"&quot;&quot;\">&gt;";

 // Test escape method with different output settings
 StringBuilder outputXhtml = new StringBuilder();
 Entities.escape(outputXhtml, input, outputSettingsXhtml, false, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputXhtml.toString());

 StringBuilder outputBase = new StringBuilder();
 Entities.escape(outputBase, input, outputSettingsBase, false, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputBase.toString());

 StringBuilder outputExtended = new StringBuilder();
 Entities.escape(outputExtended, input, outputSettingsExtended, false, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputExtended.toString());

 // Test escape method with different flags
 StringBuilder outputXhtmlWithAttributes = new StringBuilder();
 Entities.escape(outputXhtmlWithAttributes, input, outputSettingsXhtml, true, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputXhtmlWithAttributes.toString());

 StringBuilder outputBaseWithAttributes = new StringBuilder();
 Entities.escape(outputBaseWithAttributes, input, outputSettingsBase, true, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputBaseWithAttributes.toString());

 StringBuilder outputExtendedWithAttributes = new StringBuilder();
 Entities.escape(outputExtendedWithAttributes, input, outputSettingsExtended, true, false, false);
 assertEquals("&amp;lt;img src=&quot;&quot;&gt;", outputExtendedWithAttributes.toString());

 StringBuilder outputXhtmlWithWhiteSpace = new StringBuilder();
 Entities.escape(outputXhtmlWithWhiteSpace, input, outputSettingsXhtml, false, true, true);
 assertEquals("&amp;lt;img src=&quot;&nbsp; &quot;&gt;", outputXhtmlWithWhiteSpace.toString());

 StringBuilder outputBaseWithWhiteSpace = new StringBuilder();
 Entities.escape(outputBaseWithWhiteSpace, input, outputSettingsBase, false, true, true);
 assertEquals("&amp;lt;img src=&quot;&nbsp; &quot;&gt;", outputBaseWithWhiteSpace.toString());

 StringBuilder outputExtendedWithWhiteSpace = new StringBuilder();
 Entities.escape(outputExtendedWithWhiteSpace, input, outputSettingsExtended, false, true, true);
 assertEquals("&amp;lt;img src=&quot;&nbsp; &quot;&gt;", outputExtendedWithWhiteSpace.toString());
 }

 @Test
 public void testUnescape() {
 Map<Character, String> mapXhtml = Entities.EscapeMode.xhtml.getMap();
 Map<Character, String> mapBase = Entities.EscapeMode.base.getMap();
 Map<Character, String> mapExtended = Entities.EscapeMode.extended.getMap();

 String inputEscapedXhtml = "&amp;lt;img src=&quot;&nbsp; &quot;&gt;";
 String inputEscapedBase = "&lt;img src=&quot; &quot;&gt;";
 String inputEscapedExtended = "&#x003C;img src=&#x0022;&#xA0;&#x0022;&#x003E;";

 StringBuilder outputXhtml = new StringBuilder();
 Entities.unescape(outputXhtml, inputEscapedXhtml, false);
 assertEquals("<img src=\" \">", outputXhtml.toString());

 StringBuilder outputBase = new StringBuilder();
 Entities.unescape(outputBase, inputEscapedBase, false);
 assertEquals("<img src=\" \">", outputBase.toString());

 StringBuilder outputExtended = new StringBuilder();
 Entities.unescape(outputExtended, inputEscapedExtended, false);
 assertEquals("<img src=\" \">", outputExtended.toString());

 StringBuilder outputXhtmlWithTrailingSemicolon = new StringBuilder();
 Entities.unescape(outputXhtmlWithTrailingSemicolon, inputEscapedXhtml, true);
 assertEquals("<img src=\" \">", outputXhtmlWithTrailingSemicolon.toString());
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();

 // Test valid characters
 assertTrue(Entities.canEncode(coreCharset, 'A', encoder));
 assertTrue(Entities.canEncode(coreCharset, 'a', encoder));
 assertTrue(Entities.canEncode(coreCharset, '0', encoder));

 // Test invalid characters
 assertFalse(Entities.canEncode(coreCharset, '\uD800', encoder)); // High Surrogate
 assertFalse(Entities.canEncode(coreCharset, '\uDC00', encoder)); // Low Surrogate
 assertFalse(Entities.canEncode(coreCharset, '\uD834', encoder)); // High Surrogate, Private Use Area
 assertFalse(Entities.canEncode(coreCharset, '\uD835', encoder)); // Low Surrogate, Private Use Area
 }
}