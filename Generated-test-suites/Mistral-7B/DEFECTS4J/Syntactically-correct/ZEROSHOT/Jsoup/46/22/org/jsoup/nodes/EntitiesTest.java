package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.Test;

public class EntitiesTest {

 private static final String ENITIES_FILE = "entities-test.properties";
 private static final Map<String, Character> baseEntities = loadEntities(ENITIES_FILE);

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertTrue("'{}' should be a named entity", Entities.isNamedEntity(key));
 }

 String notNamedEntity = "\u0000";
 assertFalse("'{}' should not be a named entity", Entities.isNamedEntity(notNamedEntity));
 }

 @Test
 public void testIsBaseNamedEntity() {
 for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertTrue("'{}' should be a base named entity", Entities.isBaseNamedEntity(key));
 }

 String notBaseNamedEntity = "\u0001";
 assertFalse("'{}' should not be a base named entity", Entities.isBaseNamedEntity(notBaseNamedEntity));
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 Character result = Entities.getCharacterByName(key);
 assertEquals("'{}' should map to '{}'", value, result);
 }

 String keyThatDoesNotExist = "\u0002";
 Character resultThatDoesNotExist = Entities.getCharacterByName(keyThatDoesNotExist);
 assertNull("Should return null for unknown entity", resultThatDoesNotExist);
 }

 @Test
 public void testIsNamedEntityWithEmptyString() {
 String emptyString = "";
 assertFalse("Empty string should not be a named entity", Entities.isNamedEntity(emptyString));
 }

 @Test
 public void testIsNamedEntityWithNullString() {
 String nullString = null;
 assertFalse("Null string should not be a named entity", Entities.isNamedEntity(nullString));
 }

 @Test
 public void testEscape() {
 String input = "&lt;p&gt;Hello &amp; World!&quot;";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEscapeMode(Entities.EscapeMode.base);
 String expectedOutput = "&amp;lt;p&gt;Hello &amp;amp; &amp;quot;World!&amp;quot;";
 String result = Entities.escape(input, outputSettings);
 assertEquals("'{}' should be escaped to '{}'", expectedOutput, result);
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt;p&gt;Hello &amp;amp; &amp;quot;World!&amp;quot;";
 String result = Entities.unescape(input);
 assertEquals("'{}' should be unescaped to '{}'", "<p>Hello & amp; \"World!\"", result);
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.ascii;
 char c = '€';
 assertTrue("'{}' should be encodable", Entities.canEncode(coreCharset, c, null));

 CoreCharset coreCharsetUtf = CoreCharset.utf;
 char cUtf = '\u03B4';
 assertTrue("'{}' should be encodable", Entities.canEncode(coreCharsetUtf, cUtf, null));
 }

 @Test
 public void testCanEncodeWithInvalidCharset() {
 CoreCharset coreCharsetInvalid = new CoreCharset() {
 @Override
 public String getName() {
 return "invalid";
 }
 };
 char cInvalid = '€';
 assertFalse("'{}' should not be encodable", Entities.canEncode(coreCharsetInvalid, cInvalid, null));
 }

 @Test
 public void testEscapeWithXHTMLOutput() {
 Document.OutputSettings outputSettingsXHTML = new Document.OutputSettings();
 outputSettingsXHTML.setEscapeMode(Entities.EscapeMode.xhtml);
 StringBuilder accumulator = new StringBuilder(100);

 Entities.escape(accumulator, "&lt;p&gt;Hello &amp; World!&quot;", outputSettingsXHTML, false, false, false);
 String expectedOutputXHTML = "&lt;p&gt;Hello &amp;amp; &apos;World!&apos;";
 String resultXHTML = accumulator.toString();
 assertEquals("'{}' should be escaped to '{}' with XHTML output", expectedOutputXHTML, resultXHTML);
 }

 @Test
 public void testEscapeWithInvalidOutputSettings() {
 Document.OutputSettings outputSettingsInvalid = new Document.OutputSettings();
 outputSettingsInvalid.setEscapeMode(null);
 StringBuilder accumulator = new StringBuilder(100);
 String input = "&lt;p&gt;Hello &amp; World!&quot;";

 try {
 Entities.escape(accumulator, input, outputSettingsInvalid, false, false, false);
 fail("Should throw an exception for null output settings");
 } catch (NullPointerException e) {
 // expected
 }

 try {
 Entities.escape(accumulator, input, outputSettingsInvalid, false, false, false);
 fail("Should throw an exception for unknown escape mode");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = loadEntities(ENITIES_FILE);
 assertEquals("Number of entities should be '{}'", 66, entities.size());
 }

 @Test
 public void testToCharacterKey() {
 Map<Character, String> entities = new HashMap<>();
 entities.put("quot", '\u00022');
 entities.put("amp", '\u00026');
 entities.put("lt", '\u0003C');
 entities.put("gt", '\u0003E');
 Map<Character, String> result = Entities.toCharacterKey(entities);
 assertEquals("Size of maps should be equal", entities.size(), result.size());

 for (Map.Entry<Character, String> entry : entities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 Character resultKey = result.get(key);
 assertEquals("'{}' should map to '{}'", value, resultKey);
 }
 }

 private Map<String, Character> loadEntities(String filename) {
 Properties properties = new Properties();
 Map<String, Character> entities = new HashMap<String, Character>();
 try {
 InputStream in = Entities.class.getResourceAsStream(filename);
 properties.load(in);
 in.close();
 } catch (IOException e) {
 throw new RuntimeException("Error loading entities resource: " + e.getMessage());
 }

 for (Map.Entry<Object, Object> entry : properties.entrySet()) {
 String key = (String) entry.getKey();
 Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
 entities.put(key, value);
 }
 return entities;
 }

}