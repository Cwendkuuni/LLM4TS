package org.jsoup.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class EntitiesTest {

 private static final Map<Character, String> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> base = Entities.base;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> fullByVal = Entities.fullByVal;

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method with valid named entities
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 assertTrue("Named entity '" + entry.getKey() + "' not recognized as named entity.", Entities.isNamedEntity(entry.getKey().toString()));
 }

 // Test isNamedEntity method with invalid named entities
 assertFalse("'abc' is a named entity.", Entities.isNamedEntity("abc"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test isBaseNamedEntity method with valid named entities in base entity set
 for (Map.Entry<Character, String> entry : xhtmlByVal.entrySet()) {
 assertTrue("Named entity '" + entry.getKey() + "' not recognized as base named entity.", Entities.isBaseNamedEntity(entry.getKey().toString()));
 }

 // Test isBaseNamedEntity method with invalid named entities
 assertFalse("'abc' is a base named entity.", Entities.isBaseNamedEntity("abc"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method with valid named entities
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 Character character = Entities.getCharacterByName(entry.getKey().toString());
 assertEquals("Named entity '" + entry.getKey() + "' does not have the expected character value.", entry.getValue(), character);
 }
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&lt; &gt; &amp; &quot; < > \" ' &nbsp; &copy;";

 Entities.escape(accumulator, input, null, false, false, false);
 String expectedOutput = "&amp;lt; &amp;gt; &amp;amp; &amp;quot; &amp;lt; &amp;gt; &amp;quot; &amp;apos; &amp;nbsp; &amp;copy;";
 assertEquals("Unexpected output when escaping string.", expectedOutput, accumulator.toString());
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt; &amp;gt; &amp;amp; &amp;quot; &lt; &gt; &amp;quot; &apos; &nbsp; &copy;";
 String expectedOutput = "&lt; &gt; &amp; &quot; < > \" ' &nbsp; &copy;";
 String output = Entities.unescape(input);
 assertEquals("Unexpected output when unescaping string.", expectedOutput, output);
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 CharsetEncoder encoder = null; // Not used in this test
 Map<Character, String> encodedChars = new HashMap<Character, String>();
 Map<Character, Character> decodedChars = new HashMap<Character, Character>();

 // Test canEncode method with basic ASCII characters
 for (char c = 0; c < 128; c++) {
 if (Entities.canEncode(coreCharset, c, encoder)) {
 encodedChars.put(c, String.valueOf(c));
 decodedChars.put(c, c);
 }
 }

 // Test canEncode method with some other characters
 for (char c : new char[]{'&', '<', '>', '"', '&nbsp;'}) {
 assertFalse("Character '" + c + "' can be encoded with the default charset.", Entities.canEncode(coreCharset, c, encoder));
 }

 // Test canEncode method with special characters that require specific charsets to encode
 Map<Character, String> specialChars = new HashMap<Character, String>();
 specialChars.put('\u00A0', "NUL");
 specialChars.put('\u00AD', "SOH");
 specialChars.put('\u00E5', "AGrave");
 specialChars.put('\u0304', "CombiningGraveAccent");
 specialChars.put('\u20AC', "EURO_SIGN");

 for (Map.Entry<Character, String> entry : specialChars.entrySet()) {
 assertFalse("Character '" + entry.getKey() + "' can be encoded with the default charset.", Entities.canEncode(coreCharset, entry.getKey(), encoder));

 CoreCharset charset = CoreCharset.byName(entry.getValue());
 assertTrue("Character '" + entry.getKey() + "' cannot be encoded with charset '" + entry.getValue() + "'.", Entities.canEncode(charset, entry.getKey(), encoder));
 }
 }
}