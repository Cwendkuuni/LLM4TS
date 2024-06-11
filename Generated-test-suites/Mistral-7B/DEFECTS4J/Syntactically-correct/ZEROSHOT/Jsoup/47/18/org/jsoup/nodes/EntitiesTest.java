package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

public class EntitiesTest {

 private Map<Character, String> getMap(Entities.EscapeMode mode) {
 return mode.getMap();
 }

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> map = getMap(Entities.EscapeMode.full);
 // Test some known named entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("gt"));
 assertTrue(Entities.isNamedEntity("quot"));
 // Test some non-named entities
 assertFalse(Entities.isNamedEntity("a"));
 assertFalse(Entities.isNamedEntity("z"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> map = getMap(Entities.EscapeMode.base);
 // Test some known named entities in the base set
 assertTrue(Entities.isBaseNamedEntity("amp"));
 assertTrue(Entities.isBaseNamedEntity("lt"));
 assertTrue(Entities.isBaseNamedEntity("gt"));
 // Test some non-named entities
 assertFalse(Entities.isBaseNamedEntity("quot"));
 assertFalse(Entities.isBaseNamedEntity("z"));
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> map = getMap(Entities.EscapeMode.full);
 assertEquals(Character.forDigit(0, 16), Entities.getCharacterByName("x0"));
 assertEquals(Character.forDigit(32, 16), Entities.getCharacterByName("x20"));
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('>', Entities.getCharacterByName("gt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('"', Entities.getCharacterByName("quot"));
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String string = "&lt;foo&quot;bar&amp;&#123;";
 Entities.escape(accumulator, string, null, false, false, false);
 assertEquals("<foo&quot;bar&amp;&#123;", accumulator.toString());
 }

 @Test
 public void testEscapeWithAttribute() {
 StringBuilder accumulator = new StringBuilder();
 String string = "&lt;foo attribute=\"&amp;&#123;\"&gt;";
 Entities.escape(accumulator, string, null, true, false, false);
 assertEquals("<foo attribute=\"&amp;&#123;\">", accumulator.toString());
 }

 @Test
 public void testUnescape() {
 String string = "&lt;foo&quot;bar&amp;&#123;";
 assertEquals("<foo\"bar&\"#123;", Entities.unescape(string, false));
 assertEquals("<foo\"bar&\"#123;>", Entities.unescape(string, true));
 }

 @Test
 public void testCanEncode() {
 Character c = Character.forDigit(0, 16);
 CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
 assertTrue(Entities.canEncode(Entities.CoreCharset.utf, c, encoder));
 assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, c, encoder));
 }
}