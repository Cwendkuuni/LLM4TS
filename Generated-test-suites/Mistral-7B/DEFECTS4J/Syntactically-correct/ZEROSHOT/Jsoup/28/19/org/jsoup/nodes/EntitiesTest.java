package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class EntitiesTest {

 private static Map<String, Character> getEntities(String fileName) {
 Map<String, Character> entities = new HashMap<String, Character>();
 try (InputStream in = Entities.class.getResourceAsStream(fileName);
 Properties props = new Properties()) {
 props.load(in);
 for (Object obj : props.entrySet()) {
 Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) obj;
 Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
 entities.put(String.valueOf(entry.getKey()), value);
 }
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 return entities;
 }

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> fullEntities = getEntities("entities-full.properties");
 Map<String, Character> xhtmlEntities = new HashMap<String, Character>();
 xhtmlEntities.put("quot", '\u00022');
 xhtmlEntities.put("amp", '\u00026');
 xhtmlEntities.put("apos", '\u00027');
 xhtmlEntities.put("lt", '\u0003C');
 xhtmlEntities.put("gt", '\u0003E');

 for (String namedEntity : fullEntities.keySet()) {
 assertTrue(Entities.isNamedEntity(namedEntity));
 }

 for (String namedEntity : xhtmlEntities.keySet()) {
 assertTrue(Entities.isNamedEntity(namedEntity));
 }

 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity(null));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> fullEntities = getEntities("entities-full.properties");

 for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
 Character namedEntity = entry.getValue();
 String name = entry.getKey();

 assertEquals(namedEntity, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 Map<String, Character> fullEntities = getEntities("entities-full.properties");
 Map<Character, String> xhtmlEntities = Entities.xhtml().getMap();

 String stringToEscape = "&quot;<>&apos;";

 assertEquals("&quot;&lt;&amp;&apos;", Entities.escape(stringToEscape, null, Entities.EscapeMode.extended));

 assertEquals("&quot;<amp>&apos;", Entities.escape(stringToEscape, null, Entities.EscapeMode.base));

 assertEquals("&quot;<&apos;", Entities.escape(stringToEscape, null, Entities.EscapeMode.xhtml));

 StringBuilder stringBuilder = new StringBuilder();
 for (Character c : stringToEscape.toCharArray()) {
 if (fullEntities.containsKey(Character.toString(c))) {
 stringBuilder.append(Entities.escape(Character.toString(c), null, Entities.EscapeMode.extended));
 } else {
 stringBuilder.append(c);
 }
 }

 assertEquals(stringBuilder.toString(), Entities.escape(stringToEscape, null, Entities.EscapeMode.extended));
 }

 @Test
 public void testUnescape() {
 String escapedString = "&quot;<amp>&apos;";

 assertEquals("\"<&amp;\"", Entities.unescape(escapedString));
 }

 @Test
 public void testUnescapeWithTrailingSemicolon() {
 String escapedString = "&quot;<amp>&apos;";

 assertEquals("\"<&amp;\", \"<amp>\"" +
 "&apos;\"", Entities.unescape(escapedString, true));
 }

 @Test
 public void testUnescapeWithInvalidEntity() {
 String escapedString = "&invalidEntity;";

 assertNull(Entities.unescape(escapedString));
 }

 @Test
 public void testUnescapeWithInvalidNamedEntity() {
 String escapedString = "&invalidNamedEntity;";

 assertNull(Entities.unescape(escapedString));
 }

 @Test
 public void testUnescapeWithMixedEntities() {
 String escapedString = "&quot;<amp>&apos; &lt; &amp;amp; &amp;#x1F601; &amp;#x1F602;";

 String expected = "\"<&amp; \"< &amp;amp; &#x1F601; &#x1F602;";
 assertEquals(expected, Entities.unescape(escapedString));
 }

 @Test
 public void testEscapeModeXhtml() {
 Map<Character, String> xhtmlEntities = Entities.xhtml().getMap();
 String stringToEscape = "<>&\"";

 assertEquals("&lt;&amp;&quot;", Entities.escape(stringToEscape, null, Entities.EscapeMode.xhtml));
 }

 @Test
 public void testEscapeModeBase() {
 Map<Character, String> baseEntities = Entities.base().getMap();
 String stringToEscape = "<>&\"";

 assertEquals("&lt;amp;quot;", Entities.escape(stringToEscape, null, Entities.EscapeMode.base));
 }

 @Test
 public void testEscapeModeExtended() {
 Map<Character, String> extendedEntities = Entities.extended().getMap();
 String stringToEscape = "<>&\"";

 assertEquals("&lt;amp;&quot;", Entities.escape(stringToEscape, null, Entities.EscapeMode.extended));
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = getEntities("entities-base.properties");
 assertEquals(113, entities.size());
 }
}