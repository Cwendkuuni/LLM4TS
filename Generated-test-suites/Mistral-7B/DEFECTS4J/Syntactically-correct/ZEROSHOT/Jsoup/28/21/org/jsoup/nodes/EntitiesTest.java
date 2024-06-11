package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

 private static final Map<Character, String> expectedXhtmlMap = new HashMap<Character, String>() {{
 put('&', "amp");
 put('&quot;', "quot");
 put('&apos;', "apos");
 put('&lt;', "lt");
 put('&gt;', "gt");
 }};

 private static final Map<Character, String> expectedBaseMap = new HashMap<Character, String>() {{
 put('&lt;', "lt");
 put('&gt;', "gt");
 put('&amp;', "amp");
 put('&apos;', "apos");
 put('&quot;', "quot");
 }};

 private static final Map<Character, String> expectedFullMap = new HashMap<Character, String>() {{
 putAll(expectedBaseMap);
 // Add the extended entities here if needed
 }};

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<Character, String> entry : expectedFullMap.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getValue()));
 }
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<Character, String> entry : expectedFullMap.entrySet()) {
 assertEquals(entry.getKey(), Entities.getCharacterByName(entry.getValue()));
 }
 }

 @Test
 public void testEscape() {
 String input = "Test < & >";
 String xhtmlOutput = Entities.escape(input, Document.OutputSettings.xhtml(), null);
 String baseOutput = Entities.escape(input, Document.OutputSettings.base(), null);
 String extendedOutput = Entities.escape(input, Document.OutputSettings.extended(), null);

 assertEquals("Test &lt; &amp; &gt;", xhtmlOutput);
 assertEquals("Test &lt; &amp; &gt;", baseOutput);
 assertEquals("Test &#60; &#38; &#62;", extendedOutput);
 }

 @Test
 public void testUnescape() {
 String input = "Test &lt; &amp; &gt;";
 String xhtmlOutput = Entities.unescape(input);
 String baseOutput = Entities.unescape(input, false);
 String extendedOutput = Entities.unescape(input, true);

 assertEquals("Test < & >", xhtmlOutput);
 assertEquals("Test < & >", baseOutput);
 assertEquals("Test < & >", extendedOutput);
 }

 @Test
 public void testXhtmlMap() {
 assertEquals(expectedXhtmlMap, Entities.xhtmlByVal.getMap());
 }

 @Test
 public void testBaseMap() {
 assertEquals(expectedBaseMap, Entities.baseByVal.getMap());
 }

 @Test
 public void testFullMap() {
 assertEquals(expectedFullMap, Entities.fullByVal.getMap());
 }

 @Test
 public void testUnescapePattern() {
 String input = "Test &lt; &amp; &gt; &apos; &quot;";
 String expected = "Test < & > ' ' ";
 String actual = Entities.unescape(input, false).replace("&#x2019;", "'").replace("&#x201C;", " ");
 assertEquals(expected, actual);
 }

 @Test
 public void testStrictUnescapePattern() {
 String input = "Test &lt; &amp; &gt; &apos; &quot;";
 String expected = "Test < & > ' ' ";
 String actual = Entities.unescape(input, true).replace("&#x2019;", "'").replace("&#x201C;", " ");
 assertEquals(expected, actual);
 }

}