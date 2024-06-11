package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

 public class EntitiesTest {

 private static final Map<String, Character> full = new HashMap<>();
 private static final Map<Character, String> xhtmlByVal = new HashMap<>();
 private static final Map<Character, String> base = new HashMap<>();
 private static final Map<Character, String> baseByVal = new HashMap<>();
 private static final Map<Character, String> fullByVal = new HashMap<>();
 private static final Pattern unescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);?");
 private static final Pattern strictUnescapePattern = Pattern.compile("&(#(x|X)?([0-9a-fA-F]+)|[a-zA-Z]+\\d*);");

 static {
 full.put("quot", '\u00022');
 full.put("amp", '\u00026');
 full.put("apos", '\u00027');
 full.put("lt", '\u0003C');
 full.put("gt", '\u0003E');
 // other entities not shown for brevity
 }

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getKey()));
 }
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
 }
 }

 @Test
 public void testIsNamedEntityBaseSet() {
 for (Character c : Entities.EscapeMode.base.getMap().keySet()) {
 assertTrue(Entities.isNamedEntity(Entities.getCharacterByName(Character.toString(c)).toString()));
 }
 }

 @Test
 public void testEscape() {
 String input = "&lt; &amp; '&apos; &quot; &nbsp; &iexcl; &cent; &pound; &yen; &euro; &copy; &reg; &trade;";
 String expected = "&amp;lt;&amp;amp;&#039;&quot; &nbsp; &#161; &#160; &#163; &#164; &#169; &#174;";
 String output = Entities.escape(input, null, Entities.EscapeMode.base);
 assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt;&amp;amp;&#039;&quot; &nbsp; &#161; &#160; &#163; &#164; &#169; &#174;";
 String expected = "&lt; &amp; '&quot; &nbsp; ! &cent; £";
 String output = Entities.unescape(input);
 assertEquals(expected, output);
 }

 @Test
 public void testUnescapeStrict() {
 String input = "&amp;lt;&amp;amp;&#039;&quot; &nbsp; &#161; &#160; &#163; &#164; &#169; &#174; &amp;lt;&amp;amp;";
 String expected = "&lt;&amp;lt;";
 String output = Entities.unescape(input, true);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeNonEncodingString() {
 String input = "abc";
 Entities.unescape(input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeNullString() {
 Entities.unescape(null);
 }

 @Test
 public void testXhtmlEntities() {
 for (Object[] entity : Entities.xhtmlArray) {
 Character c = Character.valueOf((char) ((Integer) entity[1]).intValue());
 String name = (String) entity[0];
 assertEquals(name, Entities.getCharacterByName(name.toLowerCase()).toString());
 assertEquals(c, Entities.getCharacterByName(name));
 assertTrue(Entities.isNamedEntity(name));
 }
 }

 @Test
 public void testLoadEntities() {
 Properties properties = new Properties();
 try {
 properties.load(Entities.class.getResourceAsStream("/entities-full.properties"));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 Map<String, Character> entities = new HashMap<>();
 for (Map.Entry<Object, Object> entry : properties.entrySet()) {
 Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
 String key = (String) entry.getKey();
 entities.put(key, value);
 }
 Map<Character, String> characterEntities = Entities.toCharacterKey(entities);
 for (Map.Entry<Character, String> entry : characterEntities.entrySet()) {
 assertEquals(entry.getKey(), Entities.getCharacterByName(entry.getValue()));
 }
 }

 }