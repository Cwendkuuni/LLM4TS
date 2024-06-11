package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class EntitiesTest {

 private Entities entities;

 @Before
 public void setUp() {
 entities = new Entities();
 }

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertTrue(Entities.isNamedEntity(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> base = Entities.baseByVal;
 for (Map.Entry<Character, String> entry : base.entrySet()) {
 Character character = entry.getKey();
 String name = entry.getValue();
 assertTrue(Entities.isBaseNamedEntity(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 StringBuilder accum = new StringBuilder();
 String input = "&lt;foo&gt;<bar&amp;&#x26;quo\"t&gt;";
 Document.OutputSettings out = new Document.OutputSettings();
 Entities.escape(accum, input, out, false, false, false);
 String output = accum.toString();
 assertEquals("&lt;foo&gt;&amp;lt;bar&amp;amp;&#x26;amp;&#x22;quot;&gt;", output);
 }

 @Test
 public void testUnescape() {
 String input = "&lt;foo&gt;&amp;lt;bar&amp;amp;&#x26;amp;&#x22;quot;&gt;";
 String output = Entities.unescape(input);
 assertEquals("<foo>&<bar>&amp;quot;", output);
 }
}