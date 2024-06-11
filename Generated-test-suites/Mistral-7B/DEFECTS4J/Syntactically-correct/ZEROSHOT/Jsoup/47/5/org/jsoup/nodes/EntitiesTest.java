package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> fullEntities = Entities.full;

 for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue().toString();

 Assert.assertTrue("Named entity '" + value + "' not found in full entities map", Entities.isNamedEntity(value));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> baseEntities = Entities.base;
 Map<Character, String> fullEntities = Entities.full;

 for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue().toString();

 Assert.assertTrue("Named entity '" + value + "' not found in base entities map", Entities.isBaseNamedEntity(value));

 Assert.assertFalse("Named entity '" + value + "' found in full entities map but not in base entities map", Entities.isNamedEntity(value) && !Entities.isBaseNamedEntity(value));
 }

 for (Character key : fullEntities.keySet()) {
 if (!Entities.isBaseNamedEntity(Character.toString(key))) {
 Assert.assertFalse("Named entity '" + Character.toString(key) + "' found in full entities map but not in base entities map", Entities.isBaseNamedEntity(Character.toString(key)));
 }
 }
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> fullEntities = Entities.full;

 for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue().toString();

 Assert.assertEquals("Named entity '" + value + "' does not return the expected character", key, Entities.getCharacterByName(value));
 }
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String input = "This is a test < & > \" string with &nbsp; and &nbsp; &amp; &gt; &quot;.";

 Entities.escape(accumulator, input, Document.OutputSettings.base(), false, false, false);
 String expected = "This is a test &amp;lt; &amp;amp; &amp;gt; &amp;quot; &nbsp; &nbsp;";

 Assert.assertEquals("escape method does not return the expected value for input: " + input, expected, accumulator.toString());
 }

 @Test
 public void testUnescape() {
 String input = "This is a test &amp;lt; &amp;amp; &amp;gt; &amp;quot; &nbsp; &nbsp;";
 String expected = "This is a test < & & > \"  ";

 String output = Entities.unescape(input);

 Assert.assertEquals("unescape method does not return the expected value for input: " + input, expected, output);
 }
}