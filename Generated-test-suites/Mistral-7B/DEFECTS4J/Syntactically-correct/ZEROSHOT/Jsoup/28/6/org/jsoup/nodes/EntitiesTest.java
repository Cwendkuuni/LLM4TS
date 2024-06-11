package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

 private static final Map<String, Character> full = new HashMap<String, Character>() {{
 put("quot", '\u00022');
 put("amp", '\u00026');
 put("apos", '\u00027');
 put("lt", '\u0003C');
 put("gt", '\u0003E');
 // Add other entities here if needed
 }};

 private static final Map<Character, String> xhtmlByVal = new HashMap<Character, String>() {{
 put('\u0003C', "lt");
 put('\u0003E', "gt");
 put('\u00022', "quot");
 put('\u00026', "amp");
 put('\u00027', "apos");
 // Add other entities here if needed
 }};

 private static final Map<Character, String> base = new HashMap<Character, String>() {{
 put('\u0003C', "lt");
 put('\u0003E', "gt");
 put('\u00022', "quot");
 put('\u00026', "amp");
 put('\u00027', "apos");
 // Add other entities here if needed
 }};

 private static final Map<Character, String> baseByVal = toCharacterKey(base);
 private static final Map<Character, String> fullByVal = toCharacterKey(full);
 private static final Pattern unescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);?");
 private static final Pattern strictUnescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);");

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getKey()));
 }

 for (Character c : base.keySet()) {
 assertTrue(Entities.isNamedEntity(Character.toString(c)));
 }

 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity("abc"));
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 Character character = Entities.getCharacterByName(entry.getKey());
 assertNotNull(character);
 assertEquals(entry.getValue(), character);
 }

 for (Character c : base.keySet()) {
 Character character = Entities.getCharacterByName(Character.toString(c));
 assertNotNull(character);
 assertEquals(c, character);
 }

 assertNull(Entities.getCharacterByName(""));
 assertNull(Entities.getCharacterByName("abc"));
 }

 @Test
 public void testEscape() {
 String string = "&lt;&amp;&apos;&gt;&#174;&#x60;";
 StringBuilder expected = new StringBuilder("&#x0003C;&#x00026;&#x00027;&#x0003E;&#174;&#x60;");
 String actual = Entities.escape(string, null, Entities.EscapeMode.extended);
 assertEquals(expected.toString(), actual);
 }

 @Test
 public void testUnescape() {
 String string = "&#x0003C;&#x0003E;&#174;&#x60;&amp;&lt;&gt;";
 String expected = "&lt;&amp;&gt;&#174;&#x60;";
 String actual = Entities.unescape(string);
 assertEquals(expected, actual);
 }

 @Test
 public void testUnescapeWithStrict() {
 String string = "&#x0003C;&#x0003E;&#174;&#x60;&amp;&lt;&gt;";
 String expected = "&lt;>";
 String actual = Entities.unescape(string, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNamedEntityWithXhtmlMode() {
 for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getValue()));
 }

 for (Character c : base.keySet()) {
 assertFalse(Entities.isNamedEntity(Character.toString(c)));
 }

 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity("abc"));
 }

 @Test(expected = IllegalStateException.class)
 public void testLoadEntitiesWithMissingResource() {
 Entities.loadEntities("non-existent.properties");
 }

 @Test
 public void testToCharacterKey() {
 Map<String, Character> stringMap = new HashMap<String, Character>() {{
 put("quot", '\u00022');
 put("amp", '\u00026');
 put("apos", '\u00027');
 put("lt", '\u0003C');
 put("gt", '\u0003E');
 }};

 Map<Character, String> characterMap = Entities.toCharacterKey(stringMap);

 assertEquals(5, characterMap.size());
 assertEquals("amp", characterMap.get('\u00026'));
 assertEquals("apos", characterMap.get('\u00027'));
 assertEquals("gt", characterMap.get('\u0003E'));
 assertEquals("lt", characterMap.get('\u0003C'));
 assertEquals("quot", characterMap.get('\u00022'));
 }

}