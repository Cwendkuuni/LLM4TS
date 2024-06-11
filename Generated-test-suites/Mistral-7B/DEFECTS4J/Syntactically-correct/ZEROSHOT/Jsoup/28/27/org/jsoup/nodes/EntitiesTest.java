package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

 private static final Map<String, Character> full = new HashMap<String, Character>() {{
 put("quot", '\u00022');
 put("amp", '\u00026');
 put("apos", '\u00027');
 put("lt", '\u0003C');
 put("gt", '\u0003E');
 // add more entries if needed
 }};

 private static final Map<Character, String> xhtmlByVal = new HashMap<Character, String>() {{
 put((char) 0x00022, "quot");
 put((char) 0x00026, "amp");
 put((char) 0x00027, "apos");
 put((char) 0x0003C, "lt");
 put((char) 0x0003E, "gt");
 // add more entries if needed
 }};

 private static final Map<Character, String> base = new HashMap<Character, String>() {{
 put("lt", "&lt;");
 put("gt", "&gt;");
 put("amp", "&amp;");
 put("apos", "&apos;");
 put("quot", "&quot;");
 // add more entries if needed
 }};

 private static final Map<Character, String> baseByVal = new HashMap<Character, String>() {{
 put('\u0003C', "&lt;");
 put('\u0003E', "&gt;");
 put('\u00026', "&amp;");
 put('\u00027', "&apos;");
 put('\u00022', "&quot;");
 // add more entries if needed
 }};

 private static final Map<Character, String> fullByVal = new HashMap<Character, String>() {{
 put('\u00022', "quot");
 put('\u00026', "amp");
 put('\u00027', "apos");
 put('\u0003C', "lt");
 put('\u0003E', "gt");
 // add more entries if needed
 }};

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method with valid named entities
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("lt"));
 // Test isNamedEntity method with invalid named entities
 assertFalse(Entities.isNamedEntity("invalidEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method with valid named entities
 assertSame('\u00022', Entities.getCharacterByName("quot"));
 assertSame('\u0003C', Entities.getCharacterByName("lt"));
 // Test getCharacterByName method with invalid named entities
 assertNull(Entities.getCharacterByName("invalidEntity"));
 }

 @Test
 public void testEscapeMethod() {
 // Test escape method with default encoding and base escape mode
 String inputString = "This is a test string &amp; it contains &lt;special&gt; characters.";
 String expectedOutput = "This is a test string &amp; it contains &lt;special&gt; characters.";
 String outputString = Entities.escape(inputString, null, Entities.EscapeMode.base);
 assertEquals(expectedOutput, outputString);

 // Test escape method with custom encoding and extended escape mode
 CharsetEncoder customEncoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
 String outputStringCustomEncoding = Entities.escape(inputString, customEncoder, Entities.EscapeMode.extended);
 String expectedOutputCustomEncoding = "This is a test string &amp;#38; it contains &lt;#60;special&gt;#62; characters.";
 assertEquals(expectedOutputCustomEncoding, outputStringCustomEncoding);
 }

 @Test
 public void testUnescapeMethod() {
 // Test unescape method with a simple string
 String inputStringSimple = "This is a test string &amp; it contains &lt;special&gt; characters.";
 String expectedOutputSimple = "This is a test string it contains <special> characters.";
 String outputStringSimple = Entities.unescape(inputStringSimple);
 assertEquals(expectedOutputSimple, outputStringSimple);

 // Test unescape method with strict mode
 String inputStringStrict = "This is a test string &amp; it contains &lt;special&gt; characters.";
 String expectedOutputStrict = "This is a test string it contains <special> characters.";
 String outputStringStrict = Entities.unescape(inputStringStrict, true);
 assertEquals(expectedOutputStrict, outputStringStrict);

 // Test unescape method with a complex string
 String inputStringComplex = "This is a test string &lt;!DOCTYPE html&gt; &amp; &copy; 2023 MyCompany, Inc.";
 String expectedOutputComplex = "This is a test string <!DOCTYPE html> &amp; &copy; 2023 MyCompany, Inc.";
 String outputStringComplex = Entities.unescape(inputStringComplex);
 assertEquals(expectedOutputComplex, outputStringComplex);
 }

 @Test
 public void testEscapeAndUnescapeMethods() {
 // Test escape and unescape methods in sequence
 String inputString = "This is a test string &lt;special&gt; it contains &amp; characters.";
 String expectedOutput = "This is a test string &lt;special&gt; it contains &amp;amp; characters.";
 String outputString = Entities.escape(inputString, null, Entities.EscapeMode.base);
 String outputStringUnescaped = Entities.unescape(outputString);
 assertEquals(expectedOutput, outputString);
 assertEquals(inputString, outputStringUnescaped);
 }

}