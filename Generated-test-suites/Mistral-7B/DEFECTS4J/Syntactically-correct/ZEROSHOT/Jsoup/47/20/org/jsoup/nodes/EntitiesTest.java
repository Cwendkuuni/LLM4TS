package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

import java.util.Map;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> fullEntities = Entities.full;
 Map<Character, String> baseEntities = Entities.base;

 // Test known named entities
 assertTrue(isNamedEntity("lt"));
 assertTrue(isNamedEntity("gt"));
 assertTrue(isNamedEntity("amp"));
 assertTrue(isNamedEntity("quot"));

 // Test unknown named entities
 assertFalse(isNamedEntity("unknow"));

 // Test base named entities
 assertTrue(isBaseNamedEntity("amp"));
 assertFalse(isBaseNamedEntity("gt"));
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> fullEntities = Entities.full;

 // Test known named entities
 assertEquals('<', getCharacterByName("lt"));
 assertEquals('>', getCharacterByName("gt"));
 assertEquals('&', getCharacterByName("amp"));
 assertEquals('"', getCharacterByName("quot"));

 // Test unknown named entities
 assertNull(getCharacterByName("unknow"));
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&lt;p&gt;Hello &amp; World! &apos;Quotation Mark&apos; &quot;Double Quotation Mark&quot;";

 // Test escaping with default settings
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("&amp;lt;p&gt;Hello &amp;amp; &amp;apos;Quotation Mark&apos; &amp;quot;Double Quotation Mark&quot;", accumulator.toString());

 // Test escaping in attribute value
 StringBuilder accumulatorAttribute = new StringBuilder();
 Entities.escape(accumulatorAttribute, "\"&<>", Document.OutputSettings.xhtml(), true, false, false);
 assertEquals("&quot;&amp;amp;&lt;&gt;", accumulatorAttribute.toString());
 }

 @Test
 public void testUnescape() {
 String inputEscaped = "&amp;lt;p&gt;Hello &amp;amp; &amp;apos;Quotation Mark&apos; &amp;quot;Double Quotation Mark&quot;";

 // Test unescaping with default settings
 assertEquals("<p>Hello &  'Quotation Mark' \"Double Quotation Mark\"", Entities.unescape(inputEscaped));

 // Test unescaping in strict mode
 assertEquals("<p>Hello &amp; &apos;Quotation Mark&apos; \"Double Quotation Mark\"", Entities.unescape(inputEscaped, true));
 }
}