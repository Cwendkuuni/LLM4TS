package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TagTest {

 private static final Map<String, Tag> tags = new HashMap<>(); // map of known tags

 @Test
 public void testTagCreation() {
 Tag tag = Tag.valueOf("p");
 assertEquals("p", tag.getName());
 assertTrue(tag.isBlock());
 assertTrue(tag.formatAsBlock());
 }

 @Test
 public void testUnknownTagCreation() {
 Tag tag = Tag.valueOf("unknown");
 assertEquals("unknown", tag.getName());
 assertFalse(tag.isBlock());
 assertFalse(tag.formatAsBlock());
 }

 @Test
 public void testTagProperties() {
 Tag pTag = Tag.valueOf("p");
 Tag aTag = Tag.valueOf("a");

 assertEquals("p", pTag.getName());
 assertEquals("a", aTag.getName());

 assertTrue(pTag.isBlock());
 assertFalse(aTag.isBlock());

 assertTrue(pTag.formatAsBlock());
 assertTrue(pTag.canContainBlock());
 assertFalse(aTag.canContainBlock());

 assertFalse(pTag.isEmpty());
 assertFalse(aTag.isEmpty());

 assertFalse(pTag.isSelfClosing());
 assertFalse(aTag.isSelfClosing());

 assertFalse(pTag.preserveWhitespace());
 assertFalse(aTag.preserveWhitespace());

 assertFalse(pTag.isFormListed());
 assertFalse(pTag.isFormSubmittable());

 assertFalse(aTag.isFormListed());
 assertTrue(aTag.isFormSubmittable());
 }

 @Test
 public void testTagValueOfWithSettings() {
 ParseSettings settings = new ParseSettings();
 Tag pTag = Tag.valueOf("p", settings);
 Tag PTag = Tag.valueOf("P", settings);

 assertEquals("p", pTag.getName());
 assertEquals("P", PTag.getName());

 assertTrue(pTag.isBlock());
 assertTrue(PTag.isBlock());
 }

 @Test
 public void testTagIsKnownTag() {
 Tag pTag = Tag.valueOf("p");
 Tag unknownTag = Tag.valueOf("unknown");

 assertTrue(Tag.isKnownTag("p"));
 assertFalse(Tag.isKnownTag("unknown"));
 }

 @Test
 public void testTagPreserveWhitespace() {
 Tag preTag = Tag.valueOf("pre");
 Tag textareaTag = Tag.valueOf("textarea");

 assertTrue(preTag.preserveWhitespace());
 assertTrue(textareaTag.preserveWhitespace());
 }

 @Test
 public void testTagFormListed() {
 Tag buttonTag = Tag.valueOf("button");
 Tag inputTag = Tag.valueOf("input");

 assertFalse(buttonTag.isFormListed());
 assertTrue(inputTag.isFormListed());
 }

 @Test
 public void testTagFormSubmittable() {
 Tag inputTag = Tag.valueOf("input");
 Tag selectTag = Tag.valueOf("select");

 assertTrue(inputTag.isFormSubmittable());
 assertTrue(selectTag.isFormSubmittable());
 }
}