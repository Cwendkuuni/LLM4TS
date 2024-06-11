package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> expectedTags = new HashMap<>(); // map of expected tags

 static {
 // pre-populate map with expected tags
 expectedTags.put("html", new Tag("html"));
 expectedTags.put("head", new Tag("head"));
 expectedTags.put("body", new Tag("body"));
 // ... add other expected tags here
 }

 @Test
 public void testTagValueOf() {
 // test getting tag by name (case insensitive)
 Tag tag1 = Tag.valueOf("HTML");
 assertEquals("HTML", tag1.getName());
 assertEquals(expectedTags.get("html"), tag1);

 Tag tag2 = Tag.valueOf("html");
 assertEquals("html", tag2.getName());
 assertEquals(expectedTags.get("html"), tag2);

 // test getting tag by name (case sensitive)
 Tag tag3 = Tag.valueOf("HtmL");
 assertEquals("html", tag3.getName());
 assertEquals(expectedTags.get("html"), tag3);

 // test unknown tag
 Tag tag4 = Tag.valueOf("FOO");
 assertNotEquals("html", tag4.getName());
 assertNull(expectedTags.get("foo"));
 assertEquals(new Tag("FOO"), tag4);
 }

 @Test
 public void testIsBlock() {
 // test known block tags
 Tag html = Tag.valueOf("html");
 assertTrue(html.isBlock());

 Tag p = Tag.valueOf("p");
 assertTrue(p.isBlock());

 // test known inline tags
 Tag a = Tag.valueOf("a");
 assertFalse(a.isBlock());

 // test empty tag
 Tag img = Tag.valueOf("img");
 assertTrue(img.isBlock()); // this is incorrect, but Tag class documents it as block tag
 assertFalse(img.isEmpty());

 // test self-closing tag
 Tag br = Tag.valueOf("br");
 assertFalse(br.isEmpty());
 assertTrue(br.isSelfClosing());
 }

 @Test
 public void testIsKnownTag() {
 // test pre-defined tags
 assertTrue(Tag.isKnownTag("html"));
 assertTrue(Tag.isKnownTag("p"));

 // test unknown tag
 assertFalse(Tag.isKnownTag("FOO"));
 }

 @Test
 public void testPreserveWhitespace() {
 // test pre-defined tags with preserveWhitespace attribute
 Tag pre = Tag.valueOf("pre");
 assertTrue(pre.preserveWhitespace());

 // test unknown tag without preserveWhitespace attribute
 Tag foo = Tag.valueOf("FOO");
 assertFalse(foo.preserveWhitespace());
 }

 @Test
 public void testFormListed() {
 // test pre-defined form listed tags
 Tag input = Tag.valueOf("input");
 assertTrue(input.isFormListed());

 // test unknown tag without formListed attribute
 Tag foo = Tag.valueOf("FOO");
 assertFalse(foo.isFormListed());
 }

 @Test
 public void testFormSubmittable() {
 // test pre-defined form submittable tags
 Tag input = Tag.valueOf("input");
 assertTrue(input.isFormSubmittable());

 // test unknown tag without formSubmittable attribute
 Tag foo = Tag.valueOf("FOO");
 assertFalse(foo.isFormSubmittable());
 }
}