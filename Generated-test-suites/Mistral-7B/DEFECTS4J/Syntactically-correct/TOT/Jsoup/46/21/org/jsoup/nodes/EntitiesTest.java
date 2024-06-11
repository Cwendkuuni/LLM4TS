package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 EntityTestsHelper.testIsNamedEntity(Entities.class, "lt", true);
 EntityTestsHelper.testIsNamedEntity(Entities.class, "amp", true);
 EntityTestsHelper.testIsNamedEntity(Entities.class, "quot", true);
 EntityTestsHelper.testIsNamedEntity(Entities.class, "gt", true);
 EntityTestsHelper.testIsNamedEntity(Entities.class, "nbsp", true);
 EntityTestsHelper.testIsNamedEntity(Entities.class, "auml", false); // not a named entity
 }

 @Test
 public void testIsBaseNamedEntity() {
 EntityTestsHelper.testIsBaseNamedEntity(Entities.class, "lt", true);
 EntityTestsHelper.testIsBaseNamedEntity(Entities.class, "amp", true);
 EntityTestsHelper.testIsBaseNamedEntity(Entities.class, "quot", true);
 EntityTestsHelper.testIsBaseNamedEntity(Entities.class, "gt", false); // not a base named entity
 EntityTestsHelper.testIsBaseNamedEntity(Entities.class, "nbsp", false); // not a base named entity
 }

 @Test
 public void testGetCharacterByName() {
 EntityTestsHelper.testGetCharacterByName(Entities.class, "lt", '<');
 EntityTestsHelper.testGetCharacterByName(Entities.class, "gt", '>');
 EntityTestsHelper.testGetCharacterByName(Entities.class, "quot", '\'');
 EntityTestsHelper.testGetCharacterByName(Entities.class, "amp", '&');
 EntityTestsHelper.testGetCharacterByName(Entities.class, "nbsp", '\u00A0');
 EntityTestsHelper.testGetCharacterByName(Entities.class, "auml", '\u00C4'); // not a named entity
 }

 @Test
 public void testEscape() {
 EntityTestsHelper.testEscape(Entities.class, "&amp;lt;world&gt;", "&amp;lt;world&gt;", false, false, false);
 EntityTestsHelper.testEscape(Entities.class, "&amp;lt;world&gt;", "&lt;world&gt;", false, true, false);
 EntityTestsHelper.testEscape(Entities.class, "&amp;lt;world&gt;", "&lt;world&gt;", true, true, false);
 EntityTestsHelper.testEscape(Entities.class, "&amp;lt;world&gt;", "&lt;world&gt;", true, true, true);
 }

 @Test
 public void testUnescape() {
 EntityTestsHelper.testUnescape(Entities.class, "&lt;world&gt;", "<world>");
 EntityTestsHelper.testUnescape(Entities.class, "&amp;lt;world&gt;", "&lt;world&gt;");
 EntityTestsHelper.testUnescape(Entities.class, "&lt;world&gt;", "<world>");
 EntityTestsHelper.testUnescape(Entities.class, "&lt;world&gt;", "<world>"); // already unescaped, should not change
 }

 @Test
 public void testCanEncode() {
 EntityTestsHelper.testCanEncode(Entities.class, 'A', true);
 EntityTestsHelper.testCanEncode(Entities.class, 'a', true);
 EntityTestsHelper.testCanEncode(Entities.class, '\u00A0', false); // cannot encode
 EntityTestsHelper.testCanEncode(Entities.class, '\u00FF', false); // cannot encode
 }
}

class EntityTestsHelper {
 static void testIsNamedEntity(Class<?> clazz, String name, boolean expected) {
 boolean result = Entities.isNamedEntity(name);
 assertTrue("Expected " + expected + " for '" + name + "'", result == expected);
 }

 static void testIsBaseNamedEntity(Class<?> clazz, String name, boolean expected) {
 boolean result = Entities.isBaseNamedEntity(name);
 assertTrue("Expected " + expected + " for '" + name + "'", result == expected);
 }

 static void testGetCharacterByName(Class<?> clazz, String name, char expected) {
 Character result = Entities.getCharacterByName(name);
 assertEquals("Expected '" + expected + "' for '" + name + "'", expected, result);
 }

 static void testEscape(Class<?> clazz, String input, String expectedOutput, boolean inAttribute, boolean normalizeWhite, boolean stripLeadingWhite) {
 StringBuilder accumulator = new StringBuilder();
 Entities.escape(accumulator, input, null, inAttribute, normalizeWhite, stripLeadingWhite);
 String output = accumulator.toString();
 assertEquals("Expected '" + expectedOutput + "' for input '" + input + "'", expectedOutput, output);
 }

 static void testUnescape(Class<?> clazz, String input, String expectedOutput) {
 String output = Entities.unescape(input);
 assertEquals("Expected '" + expectedOutput + "' for input '" + input + "'", expectedOutput, output);
 }

 static void testCanEncode(Class<?> clazz, char c, boolean expected) {
 boolean result = Entities.canEncode(Entities.CoreCharset.utf, c, null);
 assertTrue("Expected " + expected + " for char '" + c + "'", result == expected);
 }
}