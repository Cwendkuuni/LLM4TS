package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> base = Entities.base;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> fullByVal = Entities.fullByVal;

 @Test
 public void testIsNamedEntity() {
 // Testing isNamedEntity method
 Map<Character, String> map = full;
 for (Map.Entry<Character, String> entry : map.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 Assert.assertTrue("Expected 'lt' to be a named entity, but was not.", Entities.isNamedEntity(value));
 Assert.assertEquals("Expected 'lt' to have the character '<<' as its value, but it didn't.", key, Entities.getCharacterByName(value));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Testing isBaseNamedEntity method
 Map<Character, String> map = base;
 for (Map.Entry<Character, String> entry : map.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 Assert.assertTrue("Expected 'amp' to be a base named entity, but was not.", Entities.isBaseNamedEntity(value));
 Assert.assertEquals("Expected 'amp' to have the character '&' as its value, but it didn't.", key, Entities.getCharacterByName(value));
 }
 }

 @Test
 public void testGetCharacterByName() {
 // Testing getCharacterByName method
 Map<String, Character> map = full;
 for (Map.Entry<String, Character> entry : map.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();

 Assert.assertEquals("Expected the name 'quot' to have the character '\"' as its value, but it didn't.", '"', Entities.getCharacterByName(name));
 Assert.assertEquals("Expected the name 'lt' to have the character '<' as its value, but it didn't.", '<', Entities.getCharacterByName("lt"));
 }
 }

 @Test
 public void testEscape() {
 // Testing escape method
 StringBuilder accumulator = new StringBuilder();
 String sourceString = "&lt;hello&gt; world &amp;quot; &apos; &apos; &lt;!DOCTYPE html&gt;";

 Entities.escape(accumulator, sourceString, null, false, false, false);

 String expectedResult = "&amp;lt;hello&amp;gt; &amp;amp;quot; &apos; &apos; &amp;lt;!DOCTYPE html&amp;gt;";
 String result = accumulator.toString();

 Assert.assertEquals("Expected the escaped string to be '&amp;lt;hello&amp;gt; &amp;amp;quot; &apos; &apos; &amp;lt;!DOCTYPE html&amp;gt;', but it was: " + result, expectedResult, result);
 }

 @Test
 public void testUnescape() {
 // Testing unescape method
 StringBuilder accumulator = new StringBuilder();
 String sourceString = "&amp;lt;hello&amp;gt; &amp;amp;quot; &apos; &apos; &amp;lt;!DOCTYPE html&amp;gt;";

 String escapedString = Entities.escape(accumulator, sourceString, null, false, false, false);
 String unescapedString = Entities.unescape(escapedString, false);

 String expectedResult = "&lt;hello&gt; \" ' ' &lt;!DOCTYPE html&gt;";
 String result = unescapedString;

 Assert.assertEquals("Expected the unescaped string to be '<hello>', but it was: " + result, "&lt;hello&gt; ", result);
 }

 @Test
 public void testCanEncode() {
 // Testing canEncode method
 CoreCharset coreCharset = CoreCharset.utf;
 CharsetEncoder fallback = null;

 for (Character character = (char) 0; character <= 0x7F; character++) {
 boolean expectedResult = character < 0x80;
 boolean result = Entities.canEncode(coreCharset, character, fallback);

 Assert.assertEquals("Expected 'canEncode' to return 'true' for ASCII characters with code points less than 0x80, but it returned 'false':", expectedResult, result);
 }
 }
}