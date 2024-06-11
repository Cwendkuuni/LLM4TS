package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

public class EntitiesTest {

 // Testing isNamedEntity method
 @Test
 public void testIsNamedEntity() {
  Assert.assertTrue(Entities.isNamedEntity("lt"));
  Assert.assertTrue(Entities.isNamedEntity("amp"));
  Assert.assertTrue(Entities.isNamedEntity("apos"));
  Assert.assertTrue(Entities.isNamedEntity("quot"));
  Assert.assertFalse(Entities.isNamedEntity("abc")); // random string, not a named entity
 }

 // Testing getCharacterByName method with valid inputs
 @Test
 public void testGetCharacterByNameValid() {
  Character lt = Entities.getCharacterByName("lt");
  Character amp = Entities.getCharacterByName("amp");
  Character apos = Entities.getCharacterByName("apos");
  Character quot = Entities.getCharacterByName("quot");

  Assert.assertEquals('<', lt);
  Assert.assertEquals('&', amp);
  Assert.assertEquals('\'', apos);
  Assert.assertEquals('"', quot);
 }

 // Testing getCharacterByName method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameInvalid() {
  Entities.getCharacterByName("randomString");
 }

 // Testing escape method with various inputs
 @Test
 public void testEscape() {
  Document.OutputSettings outputSettings = new Document.OutputSettings();
  outputSettings.setEncoder(StandardCharsets.UTF_8.newEncoder());
  outputSettings.setEscapeMode(Entities.EscapeMode.extended);

  String inputString = "&lt;Hello&amp; World&gt;! &quot; &apos;";
  String expectedOutput = "&amp;lt;Hello&amp;amp;amp;amp;amp;lt;AMPERSAND&amp;gt;! &quot; &amp;apos;";
  String outputString = Entities.escape(inputString, outputSettings);

  Assert.assertEquals(expectedOutput, outputString);
 }

 // Testing unescape method with various inputs
 @Test
 public void testUnescape() {
  String inputString = "&amp;lt;Hello&amp;amp;amp;amp;amp;lt;AMPERSAND&amp;gt;! &quot; &amp;apos;";
  String expectedOutput = "&lt;Hello&amp;lt;AMPERSAND&gt;! \" '";
  String outputString = Entities.unescape(inputString);

  Assert.assertEquals(expectedOutput, outputString);
 }

 // Testing unescape method with strict boolean set to true
 @Test
 public void testUnescapeWithStrict() {
  String inputString = "&amp;lt;Hello&amp;amp;amp;amp;amp;lt;AMPERSAND&amp;gt;! &quot; &amp;apos;";
  String expectedOutput = "&lt;Hello&lt;AMPERSAND&gt;! \" '";
  String outputString = Entities.unescape(inputString, true);

  Assert.assertEquals(expectedOutput, outputString);
 }

 // Testing isNamedEntity method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testIsNamedEntityInvalid() {
  Entities.isNamedEntity(null);
 }

 // Testing getCharacterByName method with null input
 @Test(expected = NullPointerException.class)
 public void testGetCharacterByNameNull() {
  Entities.getCharacterByName(null);
 }
}