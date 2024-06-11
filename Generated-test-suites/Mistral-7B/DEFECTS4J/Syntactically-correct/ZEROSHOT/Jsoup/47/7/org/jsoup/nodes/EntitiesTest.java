package org.jsoup.nodes;

import org.junit.Assert; import org.junit.Test; import org.jsoup.helper.StringUtil; import static org.junit.Assert.assertEquals; import static org.junit.Assert.assertFalse; import static org.junit.Assert.assertTrue;

import java.io.IOException; import java.nio.charset.StandardCharsets; import java.util.Arrays; import java.util.HashMap; import java.util.Map; import java.util.Properties;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> fullEntities = Entities.full;
 for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertTrue(Entities.isNamedEntity(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
 for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
 Character character = entry.getKey();
 String name = entry.getValue();
 assertTrue(Entities.isBaseNamedEntity(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testIsNotNamedEntity() {
 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity(" "));
 assertFalse(Entities.isNamedEntity("&"));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> fullEntities = Entities.full;
 for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String input = "This is a <test> in &quot;quotes&quot; with <ampersand> and <entity for &lt; 60: lt > and &gt; 62: gt and &apos; for 39: apostrophe";
 String output = Entities.escape(input, null);
 String expected = "This is a &lt;test&gt; in &amp;quot;quotes&amp;#34; with &amp;amp; and &amp;#x26;lt; 60: lt &amp;gt; 62: gt and &amp;#x26;apos; for 39: apostrophe";
 assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 String input = "This is a &lt;test&gt; in &amp;quot;quotes&amp;#34; with &amp;amp; and &amp;#x26;lt; 60: lt &amp;gt; 62: gt and &amp;#x26;apos; for 39: apostrophe";
 String output = Entities.unescape(input, false);
 String expected = "This is a <test> in \"quotes\" with ampersand and < 60: lt > and > 62: gt and ' for 39: apostrophe";
 assertEquals(expected, output);
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 for (char c = 0; c <= 0x7F; c++) {
 if (Character.isISOControl(c)) continue; // skip control chars
 if (!Entities.canEncode(coreCharset, c, encoder)) {
 String message = "Character " + Integer.toHexString(Character.toIntExact(c)) + " cannot be encoded by " + coreCharset.name() + " encoding";
 throw new AssertionError(message);
 }
 }
 }

 @Test
 public void testIsNotBaseNamedEntity() {
 assertFalse(Entities.isBaseNamedEntity(""));
 assertFalse(Entities.isBaseNamedEntity(" "));
 assertFalse(Entities.isBaseNamedEntity("&"));
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadEntitiesFailure() {
 Map<String, Character> entities = Entities.loadEntities("nonexistent.properties");
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 Map<String, Character> expected = Entities.base;
 assertEquals(expected, entities);
 }
}