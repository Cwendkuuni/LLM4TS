package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> expectedTags = new HashMap<>();
 static {
 // Pre-populate expected map with known tags
 expectedTags.put("html", new Tag("html"));
 expectedTags.put("head", new Tag("head"));
 // ... add other expected tags here ...
 }

 @Test
 public void testValueOf() {
 // Test known tags
 Tag knownTag = Tag.valueOf("html");
 assertEquals("html", knownTag.getName());
 assertEquals(expectedTags.get("html"), knownTag);

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isKnownTag());
 assertEquals("unknown", unknownTag.getName());
 // Check equality with known tag
 Tag knownUnknownTag = new Tag("unknown");
 assertEquals(unknownTag, knownUnknownTag);
 assertNotEquals(knownTag, unknownTag);
 }

 @Test
 public void testIsBlock() {
 // Test known block tags
 assertTrue(Tag.valueOf("html").isBlock());
 assertTrue(Tag.valueOf("p").isBlock());

 // Test known inline tags
 assertFalse(Tag.valueOf("a").isBlock());
 assertFalse(Tag.valueOf("img").isBlock());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isBlock());
 }

 @Test
 public void testFormatAsBlock() {
 // Test known tags with formatAsBlock = true
 assertTrue(Tag.valueOf("p").formatAsBlock());

 // Test known tags with formatAsBlock = false
 assertFalse(Tag.valueOf("a").formatAsBlock());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.formatAsBlock());
 }

 @Test
 public void testCanContainInline() {
 // Test known block tags that can contain inline elements
 assertFalse(Tag.valueOf("p").isData());
 assertTrue(Tag.valueOf("div").isData());

 // Test known inline tags that cannot contain inline elements
 assertFalse(Tag.valueOf("img").canContainInline());
 assertTrue(Tag.valueOf("span").canContainInline());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertTrue(unknownTag.canContainInline());
 }

 @Test
 public void testIsEmpty() {
 // Test known empty tags
 assertTrue(Tag.valueOf("img").isEmpty());
 assertTrue(Tag.valueOf("br").isEmpty());

 // Test known non-empty tags
 assertFalse(Tag.valueOf("p").isEmpty());
 assertFalse(Tag.valueOf("div").isEmpty());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isEmpty());
 }

 @Test
 public void testIsSelfClosing() {
 // Test known self-closing tags
 assertTrue(Tag.valueOf("img").isSelfClosing());
 assertFalse(Tag.valueOf("p").isSelfClosing());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // Test known tags
 assertTrue(Tag.valueOf("html").isKnownTag());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isKnownTag());
 }

 @Test
 public void testPreserveWhitespace() {
 // Test known tags with preserveWhitespace = true
 assertTrue(Tag.valueOf("pre").preserveWhitespace());

 // Test known tags with preserveWhitespace = false
 assertFalse(Tag.valueOf("p").preserveWhitespace());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.preserveWhitespace());
 }

 @Test
 public void testFormListed() {
 // Test known form-listed tags
 assertTrue(Tag.valueOf("input").isFormListed());

 // Test known non-form-listed tags
 assertFalse(Tag.valueOf("p").isFormListed());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isFormListed());
 }

 @Test
 public void testFormSubmittable() {
 // Test known form-submittable tags
 assertTrue(Tag.valueOf("input").isFormSubmittable());

 // Test known non-form-submittable tags
 assertFalse(Tag.valueOf("p").isFormSubmittable());

 // Test unknown tag
 Tag unknownTag = Tag.valueOf("unknown");
 assertFalse(unknownTag.isFormSubmittable());
 }
}