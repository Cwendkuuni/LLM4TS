package org.jsoup.nodes;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class EntitiesTest {

 private static Map<Character, String> full;
 private static Map<Character, String> xhtmlByVal;
 private static Map<Character, String> base;
 private static Map<Character, String> baseByVal;
 private static Map<Character, String> fullByVal;

 @BeforeClass
 public static void setup() {
 Entities.init(); // Initialize Entities class
 full = Entities.getEntities("entities-full.properties");
 xhtmlByVal = Entities.XHTML_ENTITIES;
 base = Entities.getEntities("entities-base.properties");
 baseByVal = Entities.toCharacterKey(base);
 fullByVal = Entities.toCharacterKey(full);
 }

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<Character, String> entry : fullByVal.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getValue()));
 }

 for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getValue()));
 }

 for (Character character : baseByVal.keySet()) {
 assertTrue(Entities.isBaseNamedEntity(Character.toString(character)));
 }

 assertFalse(Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<Character, String> entry : fullByVal.entrySet()) {
 Character character = entry.getKey();
 String name = entry.getValue();
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 StringBuilder stringBuilder = new StringBuilder();
 String input = "&lt;&gt;&quot;&amp;&nbsp;<script>alert('XSS')</script>";

 Entities.escape(stringBuilder, input, null, false, false, false);

 String expectedOutput = "&amp;lt;&amp;gt;&amp;quot;&amp;amp;&amp;#x00A0;<script>&amp;gt;alert(&#x27;XSS&#x27;)&lt;/script>";

 assertEquals(expectedOutput, stringBuilder.toString());
 }

 @Test
 public void testIsBaseNamedEntity() {
 for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
 assertTrue(Entities.isBaseNamedEntity(Character.toString(entry.getKey())));
 }
 }
}